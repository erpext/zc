package com.zc.erpext.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zc.erpext.entity.ConstParam;
import com.zc.erpext.entity.SysUser;
import com.zc.erpext.entity.User;
import com.zc.erpext.service.UserService;
import com.zc.erpext.util.Error;
import com.zc.erpext.util.Result;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600, methods = {RequestMethod.GET,RequestMethod.POST})
@RestController
@RequestMapping(value = "erpext/user")
public class UserController {

    private static final Log logger = LogFactory.getLog(UserController.class);

    @Autowired
    private ConstParam constParam;

    @Value("${constparam.appid}")
    private String appId;
    @Value("${constparam.secret}")
    private String secret;
    @Value("${constparam.agentid}")
    private String agentId;

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

    @RequestMapping(value = "/getCookieUserId",method = RequestMethod.GET)
    public String getCookieUserId(HttpServletRequest request, @RequestParam("currentPathname") String currentPathname){
        System.out.println("***********************************************");
        System.out.println("********currentPathname: " + currentPathname);
        System.out.println("***********************************************");
        String wxUserId = getWxUserIdFromCookie(request);

        Map result = new HashMap();
        if(wxUserId != null && wxUserId.length() != 0) {
            //判断权限
            int isPrivilege = getIsPrivilege(wxUserId,currentPathname);
            if (isPrivilege > 0){
                result.put("result", "OK");
                result.put("wxUserId", wxUserId);
            }else{
                result.put("result", "NP");
                result.put("wxUserId", "未授权此功能!");
            }
        }else
        {
           result.put("result", "NG");
           result.put("wxUserId", "Cookie取不到用户信息!");
        }
        String json = null;
        try {
            json = mapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {

        }
        logger.info(json);
        return json;
    }

    //判断用户是否有保存权限
    public int getIsPrivilege(String wxUserId, String currentPathname) {
        int isPrivilege = 0;
        currentPathname = currentPathname.toLowerCase();
        if (currentPathname.indexOf("index") >= 0 ){
            isPrivilege = 1;
        }
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("wxUserId", wxUserId);
        if (currentPathname.indexOf("outstore") >= 0 ){
            map.put("resourceNo", "KCCP23100");
            map.put("operationNo", "save");
            isPrivilege = userService.getIsPrivilegeByPrivilegeNo(map); //移库-移出
        }
        if (currentPathname.indexOf("instore") >= 0 ){
            map.put("resourceNo", "KCCP24100");
            map.put("operationNo", "save");
            isPrivilege = userService.getIsPrivilegeByPrivilegeNo(map); //移库-库内
        }
        if (currentPathname.indexOf("updateflag") >= 0 ){
            map.put("resourceNo", "KCCP16100");
            map.put("operationNo", "save");
            isPrivilege = userService.getIsPrivilegeByPrivilegeNo(map);  //更新产品销售标志
        }
        System.out.println("***********************************************");
        System.out.println("********getIsPrivilege.isPrivilege: " + isPrivilege);
        System.out.println("***********************************************");
        return isPrivilege;
    }

    public String getWxUserIdFromCookie(HttpServletRequest request) {
        //HttpServletRequest 装请求信息类
        //HttpServletRespionse 装相应信息的类
        //Cookie cookie=new Cookie("sessionId","CookieTestInfo");
        String wxUserId=null;
        System.out.println("***********************************************" );
        System.out.println("********getWxUserIdFromCookie******************" );
        System.out.println("***********************************************" );
        Cookie[] cookies =  request.getCookies();
        if(cookies != null){
            System.out.println("********Cookie is not null*********************" );
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("wxUserId")){
                    wxUserId = cookie.getValue();
                    System.out.println("********wxUserId (UserController) 1=" + wxUserId);
                    //return wxUserId;
                }
            }
        }else {
            System.out.println("***********************************************" );
            System.out.println("********Cookie is null*************************" );
            System.out.println("***********************************************" );
        }
        System.out.println("********wxUserId (UserController) 2:" + wxUserId);
        return wxUserId;
    }

    @RequestMapping(value = "/bindWx")
    public String bindWx(@RequestBody String requestBody, HttpServletRequest request) {

        try {
//            Map<String, Object> mapType = JSON.parseObject(requestBody,Map.class);

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
                ngData.put("msg", "微信帐号未映射ERP帐号!");
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

    //@CrossOrigin(origins = "*", maxAge = 3600)    //改为全局配置
    @RequestMapping(value = "/getWxConfig", method = RequestMethod.GET)
    public String getWxConfig(@RequestParam("pageName") String pageName) {
        //1、获取AccessToken
        String accessToken = getAccessToken(appId,secret);

        //2、获取Ticket
        String jsapi_ticket = getTicket(accessToken);

        //3、时间戳和随机字符串
        String noncestr = UUID.randomUUID().toString().replace("-", "").substring(0, 16);//随机字符串
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);//时间戳
        System.out.println("******accessToken:"+accessToken+"\n******jsapi_ticket:"+jsapi_ticket+"\n******时间戳："+timestamp+"\n******随机字符串："+noncestr);

        //4、获取url
        String url="";
        url = "http://" + pageName;
        System.out.println("******url:"+url);
        /*根据JSSDK上面的规则进行计算，这里比较简单，我就手动写啦
        String[] ArrTmp = {"jsapi_ticket","timestamp","nonce","url"};
        Arrays.sort(ArrTmp);
        StringBuffer sf = new StringBuffer();
        for(int i=0;i<ArrTmp.length;i++){
            sf.append(ArrTmp[i]);
        }
        */

        //5、将参数排序并拼接字符串
        String str = "jsapi_ticket="+jsapi_ticket+"&noncestr="+noncestr+"&timestamp="+timestamp+"&url="+url;
        //6、将字符串进行sha1加密
        String signature =SHA1(str);
        System.out.println("******签名参数："+str+"\n签名："+signature);

        Map result = new HashMap();
        result.put("result", "OK");
        List list = new ArrayList();
        Map ngData = new HashMap();

        System.out.println("********getWxConfig.appId: " + appId);
        System.out.println("*******getWxConfig.secret: " + secret);
        System.out.println("******getWxConfig.agentId: " + agentId);

        ngData.put("appId", appId);
        ngData.put("secret", secret);
        ngData.put("agentId", agentId);
        ngData.put("accessToken", accessToken);
        ngData.put("jsapi_ticket", jsapi_ticket);
        ngData.put("noncestr", noncestr);
        ngData.put("timestamp", timestamp);
        ngData.put("signature", signature);
        ngData.put("url domain", url);

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

    @RequestMapping(value = "/getWxUserIdByCode", method = RequestMethod.GET)
    public String getWxUserIdByCode(HttpServletResponse response,@RequestParam("code") String code) {
        String accessToken = getAccessToken(appId,secret);  //获取AccessToken
        String wxUserId = "";
        String url = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token="+accessToken+"&code="+code;
        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
            http.connect();
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            String message = new String(jsonBytes, "UTF-8");
            JSONObject demoJson = JSONObject.fromObject(message);
            System.out.println("USER INFO JSON字符串："+demoJson);
            if(demoJson.has("UserId")){
                wxUserId = demoJson.getString("UserId");
                System.out.println("USER ID：" + wxUserId);
                Cookie cookie=new Cookie("wxUserId",wxUserId);
                response.addCookie(cookie);
            }else
            {
                wxUserId = "NG_UnauthorizedUser";
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String json = null;
        try {
            json = mapper.writeValueAsString(wxUserId);
        } catch (JsonProcessingException e) {
            return null;
        }
        logger.info(json);
        return json;
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

    /**
    getAccessToken
     */
    public static String getAccessToken(String myAppId, String mySecret) {
        String access_token = "";
        System.out.println("********getAccessToken.appId: " + myAppId);
        System.out.println("*******getAccessToken.secret: " + mySecret);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + myAppId +"&corpsecret=" + mySecret;
        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
            http.connect();
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            String message = new String(jsonBytes, "UTF-8");
            JSONObject demoJson = JSONObject.fromObject(message);
            System.out.println("JSON字符串："+demoJson);
            access_token = demoJson.getString("access_token");
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return access_token;
    }

    /**
    获取jsapi_ticket
     */
    public static String getTicket(String access_token) {
        String ticket = null;
        String url = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?access_token="+ access_token;//这个url链接和参数不能变
        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
            http.connect();
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            String message = new String(jsonBytes, "UTF-8");
            JSONObject demoJson = JSONObject.fromObject(message);
            System.out.println("JSON字符串："+demoJson);
            ticket = demoJson.getString("ticket");
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ticket;
    }

    public static String SHA1(String decript) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获得指定数目的UUID
     * @param number int 需要获得的UUID数量
     * @return String[] UUID数组
     */
    public static String[] getUUID(int number){
        if(number < 1){
            return null;
        }
        String[] retArray = new String[number];
        for(int i=0;i<number;i++){
            retArray[i] = getUUID();
        }
        return retArray;
    }

    /**
     * 获得一个UUID
     * @return String UUID
     */
    public static String getUUID(){
        String uuid = UUID.randomUUID().toString();
        //去掉“-”符号
        return uuid.replaceAll("-", "");
    }



}
