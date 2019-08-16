package com.zc.erpext.service;

import java.util.List;
import java.util.Map;

import com.zc.erpext.entity.SysUser;
import com.zc.erpext.entity.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
	List<User> getUser();

    List<User>  bindWx();

    List<User> getWxConfig();

    List<User> getWxUserIdByCode();

    //List<User> getCookieUserId();

    SysUser getById(String account);

    String getUserNoByOpenId(String wx_open_id);

    void updateWXid(SysUser sysUser);

    int getIsPrivilegeByPrivilegeNo(Map map);

}
