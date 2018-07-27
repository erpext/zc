package com.zc.erpext.service;

import java.util.List;

import com.zc.erpext.entity.SysUser;
import com.zc.erpext.entity.User;

public interface UserService {
	List<User> getUser();

    List<User> bindWx();

    SysUser getById(String account);

    String getUserNoByOpenId(String wx_open_id);

    void updateWXid(SysUser sysUser);
}
