package com.zc.erpext.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zc.erpext.service.MoveWareHouseService;
import com.zc.erpext.service.WareHouseService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600,methods = {RequestMethod.GET,RequestMethod.POST})
@RestController
@RequestMapping(value = "warehouse")
public class WareHouseController {

    private static final Log logger = LogFactory.getLog(WareHouseController.class);

    @Autowired
    private WareHouseService wareHouseService;

    @Autowired
    private MoveWareHouseService moveWareHouseService;

    private ObjectMapper mapper = new ObjectMapper();

    {
        mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
    }

/*    @RequestMapping(value = "/listWareHouse")
    public List<WareHouse> lisWareHouse() {
        return wareHouseService.getWareHouse();
    }*/

    @RequestMapping(value = "/listWareHouse")
    public String lisWareHouse(@RequestBody String requestBody, HttpServletRequest request) {
        try {
            Map<String, Object> requestMap = mapper.readValue(requestBody, Map.class);
            logger.info("请求参数 ===> " + requestMap);
            String open_id; //wx_open_id_zc
            open_id = (String) requestMap.get("open_id");

            List lst = wareHouseService.getWareHouse();
            if (lst != null && lst.size() > 0) {
                Map result = new HashMap();
                result.put("result", "OK");
                result.put("ngData", lst);
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
                ngData.put("code", "002");
                ngData.put("msg", "查询无数据!");
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

        } catch (Exception e) {

        }
        return "";

    }

    @RequestMapping(value = "/listWareHouseDetail")
    public String listWareHouseDetail(@RequestBody String requestBody, HttpServletRequest request) {

        try {
            Map<String, Object> requestMap = mapper.readValue(requestBody, Map.class);
            logger.info("请求参数 ===> " + requestMap);
            String ckdd; //ckdd
            String open_id; //wx_open_id_zc

            //获取account参数值
            ckdd = (String) requestMap.get("ckdd");

            //获取wx_open_id_zc参数值
            open_id = (String) requestMap.get("open_id");

            List lst = wareHouseService.getWareHouseDetail(ckdd);

            if (lst != null && lst.size() > 0) {

                Map result = new HashMap();
                result.put("result", "OK");
                List list = new ArrayList();
                Map ngData = new HashMap();
                ngData.put("data", lst);
                list.add(ngData);
                result.put("ngData", lst);

                String json = null;
                try {
                    json = mapper.writeValueAsString(result);
                } catch (JsonProcessingException e) {

                }
                logger.info(json);
                return json;

//                {"result":" NG"," ngData":[ {"code":"001","msg":"06"},{"code":"002","msg":"06"}]}
//                {"result":" OK" }
            } else {

                Map result = new HashMap();
                result.put("result", "NG");
                List list = new ArrayList();
                Map ngData = new HashMap();
                ngData.put("code", "002");
                ngData.put("msg", "查询无数据!");
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


        } catch (Exception e) {

        }
        return "";
    }

    @RequestMapping(value = "/updateSaleFlag")
    public String updateSaleFlag(@RequestBody String requestBody, HttpServletRequest request) {

        try {
            Map<String, Object> requestMap = mapper.readValue(requestBody, Map.class);
            logger.info("请求参数 ===> " + requestMap);

            /**
             * 请求参数	参数名称	参数类型	是否必输	说明
             wx_open_id_zc	String	Y	公众号id
             saleflag	String	Y	0或1
             cpjhlist	String 数组	Y	产品卷号数组
             cpjh	String	Y	产品卷号
             */

            String saleflag; //ckdd
            String open_id; //wx_open_id_zc
            List<Map> cpjhlist = new ArrayList<>(); //卷号

            //获取account参数值
            saleflag = (String) requestMap.get("saleflag");

            //获取wx_open_id_zc参数值
            open_id = (String) requestMap.get("open_id");

            // 校验用户openid是否正确
            String userNo = getUserNoByOpenId(open_id);
            if (null == userNo || ("").equals(userNo)) {
                Map result = new HashMap();
                result.put("result", "NG");
                List list = new ArrayList();
                Map ngData = new HashMap();
                ngData.put("code", "005");
                ngData.put("msg", "open id 不正确!");
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

            //获取参数值
            cpjhlist = (ArrayList<Map>) requestMap.get("cpjhlist"); //卷号

            //
            List rsList = new ArrayList(); // 用来存放错误卷号
            int rsNum = 0; //用统计成功数
            if (cpjhlist.size() > 0)
                for (int i = 0; i < cpjhlist.size(); i++) {
                    String cpjh = (String) cpjhlist.get(i).get("cpjh");
                    String id = wareHouseService.getWareHouseCpkchzsByCpjh(cpjh);
                    if (null == id) { // ID 不存在
                        rsList.add(cpjh); //错误信息+1
                    }// 存在
                    else {//更新
                        Map uMap = new HashMap();
                        uMap.put("id", id);
                        uMap.put("saleflag", saleflag);
                        int num = wareHouseService.updateSaleFlagById(uMap); //数据库更新
                        if (num > 0) { // 更新成功
                            rsNum++; //成功数+1
                        } else {
                            rsList.add(cpjh); //错误信息+1
                        }
                    }
                }

//            int num = wareHouseService.updateSaleFlag(cpjhlist);
            if (rsNum == cpjhlist.size()) {

                Map result = new HashMap();
                result.put("result", "OK");
                String json = null;
                try {
                    json = mapper.writeValueAsString(result);
                } catch (JsonProcessingException e) {

                }
                logger.info(json);
                return json;

//                {"result":" NG"," ngData":[ {"code":"001","msg":"06"},{"code":"002","msg":"06"}]}
//                {"result":" OK" }
            } else {

                Map result = new HashMap();
                result.put("result", "NG");
                List list = new ArrayList();
                for (int i = 0; i < rsList.size(); i++) {
                    Map ngData = new HashMap();
                    ngData.put("code", "004");
                    ngData.put("msg", "[" + rsList.get(i) + "]" + " 卷号没有找到!");
                    list.add(ngData);
                }
                result.put("ngData", list);
                String json = null;
                try {
                    json = mapper.writeValueAsString(result);
                } catch (JsonProcessingException e) {

                }
                logger.info(json);
                return json;
            }

        } catch (Exception e) {

        }
        return "";
    }

    private String getUserNoByOpenId(String open_id) {
        return moveWareHouseService.getUserNoByOpenId(open_id);
    }

}
