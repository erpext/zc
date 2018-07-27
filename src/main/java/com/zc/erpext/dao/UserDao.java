package com.zc.erpext.dao;

import java.util.List;

import com.zc.erpext.entity.SysUser;
import com.zc.erpext.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
	List<User> getUser();

    SysUser getById(String account);

    String getUserNoByOpenId(String wx_open_id);

    void updateWXid(SysUser sysUser);
}
