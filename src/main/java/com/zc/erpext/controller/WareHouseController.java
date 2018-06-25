package com.zc.erpext.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zc.erpext.service.WareHouseService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "warehouse")
public class WareHouseController {

    private static final Log logger = LogFactory.getLog(WareHouseController.class);

    @Autowired
    private WareHouseService wareHouseService;

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

}
