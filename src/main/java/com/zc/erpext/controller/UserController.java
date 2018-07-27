package com.zc.erpext.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zc.erpext.entity.SysUser;
import com.zc.erpext.entity.User;
import com.zc.erpext.service.UserService;
import com.zc.erpext.util.Error;
import com.zc.erpext.util.Result;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "user")
public class UserController {

    private static final Log logger = LogFactory.getLog(UserController.class);

    @Autowired
    private UserService userService;

    private ObjectMapper mapper = new ObjectMapper();

    {
        mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
    }

    @RequestMapping(value = "/listUser")
    public List<User> listUser() {
        return userService.getUser();
    }

    @RequestMapping(value = "/bindWx")
    public String bindWx(@RequestBody String requestBody, HttpServletRequest request) {

        try {
            Map<String, Object> requestMap = mapper.readValue(requestBody, Map.class);
            String account; //账号
            String password; //密码
            String wx_open_id_zc; //wx_open_id_zc

            //获取account参数值
            account = (String) requestMap.get("user_no");
            //获取password参数值 wx_bind_password
            password = (String) requestMap.get("user_password");

            //获取wx_open_id_zc参数值
            wx_open_id_zc = (String) requestMap.get("wx_open_id_zc");

            SysUser user = userService.getById(account);

            String md5Password = MD5(account + password);


            try {
                // 生成一个MD5加密计算摘要
                MessageDigest md = MessageDigest.getInstance("MD5");
                // 计算md5函数
                md.update((account + password).getBytes());
                // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
                // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
//                return new BigInteger(1, md.digest()).toString(16);
                md5Password = new BigInteger(1, md.digest()).toString(16);
            } catch (Exception e) {

            }

            /*
                1、密码加密
                2、密码比对 wx_bind_password
                3、更新openid
             */
            if (user != null) {  // && password
                if (password.equals(user.getWx_bind_password())) {
                    user.setWx_open_id_zc(wx_open_id_zc);
                    userService.updateWXid(user);

                    Map result = new HashMap();
                    result.put("result", "OK");
                    List list = new ArrayList();
                    Map ngData = new HashMap();
                    ngData.put("code", "001");
                    ngData.put("msg", "绑定成功!");
                    list.add(ngData);
                    result.put("ngData", list);

                    String json = null;
                    try {
                        json = mapper.writeValueAsString(result);
                    } catch (JsonProcessingException e) {

                    }
                    logger.info(json);
                    return json;
                } else {
                    Map result = new HashMap();
                    result.put("result", "NG");
                    List list = new ArrayList();
                    Map ngData = new HashMap();
                    ngData.put("code", "003");
                    ngData.put("msg", "密码错误!");
                    list.add(ngData);
                    result.put("ngData", list);

                    String json = null;
                    try {
                        json = mapper.writeValueAsString(result);
                    } catch (JsonProcessingException e) {

                    }
                    logger.info(json);
                    return json;
                }


//                {"result":" NG"," ngData":[ {"code":"001","msg":"06"},{"code":"002","msg":"06"}]}
//                {"result":" OK" }
            } else {

                Map result = new HashMap();
                result.put("result", "NG");
                List list = new ArrayList();
                Map ngData = new HashMap();
                ngData.put("code", "002");
                ngData.put("msg", "账号不存在!");
                list.add(ngData);
                result.put("ngData", list);

                String json = null;
                try {
                    json = mapper.writeValueAsString(result);
                } catch (JsonProcessingException e) {

                }
                logger.info(json);
                return json;

            }


        } catch (IOException e) {

        }
        return "";
    }

    @RequestMapping(value = "/checkBind")
    public String checkBind(@RequestBody String requestBody, HttpServletRequest request) {

        try {
            Map<String, Object> requestMap = mapper.readValue(requestBody, Map.class);
            String wx_open_id_zc; //wx_open_id_zc

            //获取wx_open_id_zc参数值
            wx_open_id_zc = (String) requestMap.get("open_id");

            // 校验用户openid是否正确
            String userNo = userService.getUserNoByOpenId(wx_open_id_zc);
            if (null == userNo || ("").equals(userNo)) {
                Map result = new HashMap();
                result.put("result", "NG");
                List list = new ArrayList();
                Map ngData = new HashMap();
                ngData.put("code", "0006");
                ngData.put("msg", "微信帐号未绑定!");
                list.add(ngData);
                result.put("ngData", list);
                String json = null;
                try {
                    json = mapper.writeValueAsString(result);
                } catch (JsonProcessingException e) {

                }
                logger.info(json);
                return json;
            }else{
                Map result = new HashMap();
                result.put("result", "OK");
                List list = new ArrayList();
                Map ngData = new HashMap();
                ngData.put("code", "007");
                ngData.put("msg", "微信用户合法!");
                list.add(ngData);
                result.put("ngData", list);

                String json = null;
                try {
                    json = mapper.writeValueAsString(result);
                } catch (JsonProcessingException e) {

                }
                logger.info(json);
                return json;
            }

        } catch (IOException e) {

        }
            return "";
    }

    private String reResult(Result r, Error error) {
        String json = null;
        try {
            json = mapper.writeValueAsString(r);
        } catch (JsonProcessingException e) {
            error.setCode(403);
            error.setMessage(e.getMessage());
            r.setError(error);
        }
        logger.info(json);
        return json;
    }

    public String MD5(String input) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = input.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

}
