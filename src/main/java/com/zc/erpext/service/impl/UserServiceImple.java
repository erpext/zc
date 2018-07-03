package com.zc.erpext.service.impl;

import com.zc.erpext.dao.UserDao;
import com.zc.erpext.entity.SysUser;
import com.zc.erpext.entity.User;
import com.zc.erpext.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "UserService")
public class UserServiceImple implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public List<User> getUser() {
        return userDao.getUser();
    }

    @Override
    public List<User> bindWx() {
        return null;
    }

    /**
     * 用于查询用户做校验
     *
     * @param account
     * @return
     */
    @Override
    public SysUser getById(String account) {
        return userDao.getById(account);
    }

    /**
     * 更新用户信息微信信息
     *
     * @param account
     * @return
     */
    @Override
    public void updateWXid(SysUser sysUser) {
        userDao.updateWXid(sysUser);
    }

}