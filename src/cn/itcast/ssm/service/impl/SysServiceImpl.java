package cn.itcast.ssm.service.impl;

import cn.itcast.ssm.exception.CustomException;
import cn.itcast.ssm.mapper.SysPermissionMapperCustom;
import cn.itcast.ssm.mapper.SysUserMapper;
import cn.itcast.ssm.po.ActiveUser;
import cn.itcast.ssm.po.SysPermission;
import cn.itcast.ssm.po.SysUser;
import cn.itcast.ssm.po.SysUserExample;
import cn.itcast.ssm.service.SysService;
import cn.itcast.ssm.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class SysServiceImpl implements SysService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysPermissionMapperCustom sysPermissionMapperCustom;
    @Override
    public ActiveUser authenticat(String userCode, String password) throws Exception {
        SysUser sysUser=findSysUserByUserCode(userCode);
        if(sysUser==null){
            throw  new CustomException("用户账号不存在！");
        }
        String password_db=sysUser.getPassword();
        String password_input_md5= new MD5().getMD5ofStr(password);
        if(!password_input_md5.equalsIgnoreCase(password_db)){
            throw  new CustomException("用户名或密码错误！");
        }
        //得到用户id
        String userid = sysUser.getId();
        //根据用户id查询菜单
        List<SysPermission> menus =this.findMenuListByUserId(userid);

        //根据用户id查询权限url
        List<SysPermission> permissions = this.findPermissionListByUserId(userid);

        //认证通过，返回用户身份信息
        ActiveUser activeUser = new ActiveUser();
        activeUser.setUserid(sysUser.getId());
        activeUser.setUsercode(userCode);
        activeUser.setUsername(sysUser.getUsername());//用户名称

        //放入权限范围的菜单和url
        activeUser.setMenus(menus);
        activeUser.setPermissions(permissions);
        return activeUser;
    }
    public SysUser findSysUserByUserCode(String userCode){
        SysUserExample  sysUserExample =new SysUserExample();
        SysUserExample.Criteria criteria=sysUserExample.createCriteria();
        criteria.andUsercodeEqualTo(userCode);
        List<SysUser> list=sysUserMapper.selectByExample(sysUserExample);
        if(list!=null&& list.size()==1){
            return list.get(0);
        }

        return null;
    }

    @Override
    public List<SysPermission> findMenuListByUserId(String userId) throws Exception {
        return sysPermissionMapperCustom.findMenuListByUserId(userId);
    }

    @Override
    public List<SysPermission> findPermissionListByUserId(String userId) throws Exception {
        return sysPermissionMapperCustom.findPermissionListByUserId(userId);
    }

}
