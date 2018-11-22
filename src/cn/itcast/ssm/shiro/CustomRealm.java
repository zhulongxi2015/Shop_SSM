package cn.itcast.ssm.shiro;

import cn.itcast.ssm.po.ActiveUser;
import cn.itcast.ssm.po.SysPermission;
import cn.itcast.ssm.po.SysUser;
import cn.itcast.ssm.service.SysService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class CustomRealm extends AuthorizingRealm {
    @Autowired
    private SysService sysService;
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String userCode=(String)authenticationToken.getPrincipal();
        SysUser sysUser =sysService.findSysUserByUserCode(userCode);
        if(sysUser==null)return null;

        String password=sysUser.getPassword();
        ActiveUser activeUser=new ActiveUser();
        activeUser.setUserid(sysUser.getId());
        activeUser.setUsername(sysUser.getUsername());
        activeUser.setUsercode(sysUser.getUsercode());

        String salt=sysUser.getSalt();

        try{
            activeUser.setMenus(sysService.findMenuListByUserId(sysUser.getId()));
        }catch(Exception e){
            e.printStackTrace();
        }

        SimpleAuthenticationInfo simpleAuthenticationInfo=new SimpleAuthenticationInfo(activeUser,password, ByteSource.Util.bytes(salt),"shiro");
        return simpleAuthenticationInfo;
    }
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) throws AuthorizationException {
        ActiveUser activeUser=(ActiveUser)principalCollection.getPrimaryPrincipal();
        List<String> sysPermissionList=new ArrayList<>();
        List<SysPermission> sysPermissions=null;
        try{
             sysPermissions = sysService.findPermissionListByUserId(activeUser.getUserid());
        }catch (Exception ex){
            ex.printStackTrace();
        }
       for(SysPermission sysPermission:sysPermissions){
           sysPermissionList.add(sysPermission.getPercode());
       }
        SimpleAuthorizationInfo simpleAuthorizationInfo=new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addStringPermissions(sysPermissionList);
        return simpleAuthorizationInfo;
    }

}
