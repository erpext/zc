package com.zc.erpext.service.impl;

import com.zc.erpext.dao.UserDao;
import com.zc.erpext.entity.SysUser;
import com.zc.erpext.entity.User;
import com.zc.erpext.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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

    @Override
    public List<User> getWxConfig() {
        return null;
    }

    @Override
    public List<User> getWxUserIdByCode() {
        return null;
    }

//    @Override
//    public List<User> getCookieUserId() {
//        return null;
//    }

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
     * 用于校验微信是否已绑定
     *
     * @param wx_open_id
     * @return
     */
    @Override
    public String getUserNoByOpenId(String wx_open_id) {
        return userDao.getUserNoByOpenId(wx_open_id);
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


    /**
     * 用于校验微信是否已绑定
     *
     * @param wxUserId
     * @param resourceNo
     * @param operationNo
     * @return
     */
    @Override
    public int getIsPrivilegeByPrivilegeNo(Map map) {
        return userDao.getIsPrivilegeByPrivilegeNo(map);
    }

}
