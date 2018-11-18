package cn.itcast.ssm.service;

import cn.itcast.ssm.po.ActiveUser;
import cn.itcast.ssm.po.SysPermission;
import cn.itcast.ssm.po.SysUser;

import java.util.List;

public interface SysService {
    public ActiveUser authenticat(String userCode,String password) throws Exception;
    public SysUser findSysUserByUserCode(String userCode);
    public List<SysPermission> findMenuListByUserId(String userId)throws  Exception;
    public List<SysPermission> findPermissionListByUserId(String userId)throws  Exception;
}
