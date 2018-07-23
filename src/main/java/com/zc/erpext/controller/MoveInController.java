package com.zc.erpext.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zc.erpext.entity.KcTbcpkchzs;
import com.zc.erpext.entity.KcTbcpykLines;
import com.zc.erpext.service.MoveWareHouseService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiangmin on 2018/5/31.
 * author jiangmin
 * <p>
 * 移库 -  库内
 */
@RestController
@RequestMapping(value = "movein")
public class MoveInController {

    private static final Log logger = LogFactory.getLog(MoveInController.class);

    private ObjectMapper mapper = new ObjectMapper();

    {
        mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
    }

    @Autowired
    private MoveWareHouseService moveWareHouseService;

/*
    //后台处理
    @Autowired
    private MoveInWareHouseService moveInWareHouseService;*/

    /**
     * 移库接口
     *
     * @param requestBody
     * @param request
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    public
    @ResponseBody
    String create(@RequestBody String requestBody, HttpServletRequest request) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> requestMap = mapper.readValue(requestBody, Map.class);

            List<Map> cpjhlist = new ArrayList<>(); //卷号
            String ckdd_yc; //移出仓库
            String ckqy; //仓库区域
            String open_id;

            //获取参数值
            cpjhlist = (ArrayList<Map>) requestMap.get("cpjhlist"); //卷号
            ckdd_yc = (String) requestMap.get("ckdd_yc");
            ckqy = (String) requestMap.get("ckqy");
            open_id = (String) requestMap.get("open_id");

            if (cpjhlist.size() == 0 || ("").equals(ckdd_yc) || ("").equals(open_id) || ("").equals(ckqy)) {
                //返回 NG
                //{"result":"NG","ngData":[{"msg":"卷号没有找到","code":"002"}]}
                Map result = new HashMap();
                result.put("result", "NG");
                List list = new ArrayList();
                Map ngData = new HashMap();
                ngData.put("code", "0003");
                ngData.put("msg", "参数不正确!");
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
            // 校验用户openid是否正确
            String userNo = getUserNoByOpenId(open_id);
            if (null == userNo || ("").equals(userNo)) {
                Map result = new HashMap();
                result.put("result", "NG");
                List list = new ArrayList();
                Map ngData = new HashMap();
                ngData.put("code", "0005");
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

            /*
            获取到参数后
            循环校验卷号
            如果全部不合格，直接返回
            如果有合格：
                先获取DB参数，生成主单编号
                新增主表
                循环新增子表
                循环更新库存中库位
             */
            String ykno = getMaxYkNo();
            // 插入主表的参数
            Map iMap = new HashMap();
            iMap.put("ykno", ykno);
            iMap.put("ckdd_yc", ckdd_yc);
            iMap.put("ckdd_yr", ckdd_yc);
            iMap.put("open_id", userNo);
            iMap.put("confirm_flag", '1');
            moveWareHouseService.insertCpyks(iMap);

            long master_id = moveWareHouseService.getYkdIdByYkno(ykno);

            Map result = new HashMap();
            result.put("result", "OK");
            List reList = new ArrayList();
            for (int i = 0; i < cpjhlist.size(); i++) {
                // 用卷号校验移出仓库库存是否存在
                Map cMap = new HashMap();
                cMap.put("cpjh", (String) ((Map) cpjhlist.get(i)).get("cpjh"));
                cMap.put("ckdd_yc", ckdd_yc);
                KcTbcpkchzs kcTbcpkchzs = moveWareHouseService.checkJhM2(cMap);
                if (null != kcTbcpkchzs) {
                    KcTbcpykLines kcTbcpykLines = new KcTbcpykLines();
                    kcTbcpykLines.setMaster_id(master_id);
                    kcTbcpykLines.setCkqy_old(ckdd_yc);
                    kcTbcpykLines.setCkqy(ckqy);
                    kcTbcpykLines.setFramework_no(kcTbcpkchzs.getFramework_no());
                    kcTbcpykLines.setCppm_no(kcTbcpkchzs.getCppm_no());
                    kcTbcpykLines.setCpgg(kcTbcpkchzs.getCpgg());
                    kcTbcpykLines.setQuality_level(kcTbcpkchzs.getQuality_level());
                    kcTbcpykLines.setCpgg_add(kcTbcpkchzs.getCpgg_add());
                    kcTbcpykLines.setCpjh(kcTbcpkchzs.getCpjh());
                    kcTbcpykLines.setWeight(kcTbcpkchzs.getWeight());
                    kcTbcpykLines.setCpgg_ply(kcTbcpkchzs.getCpgg_ply());
                    kcTbcpykLines.setCpgg_width(kcTbcpkchzs.getCpgg_width());
                    kcTbcpykLines.setCpgg_length(kcTbcpkchzs.getCpgg_length());
                    kcTbcpykLines.setCpgg_number(kcTbcpkchzs.getCpgg_number());
                    kcTbcpykLines.setProd_company(kcTbcpkchzs.getProd_company());
                    kcTbcpykLines.setCompany_id(kcTbcpkchzs.getCompany_id());

                    kcTbcpykLines.setCreate_user_no(userNo);

                    moveWareHouseService.insertCpykLines(kcTbcpykLines);

                    //更新库存中库位
                    kcTbcpkchzs.setCkdd(ckdd_yc);
                    kcTbcpkchzs.setCkqy(ckqy);
                    kcTbcpkchzs.setUpdate_user_no(userNo);
                    moveWareHouseService.updateCkdd(kcTbcpkchzs);

                } else {
                    result.put("result", "NG");
                    Map ngData = new HashMap();
                    ngData.put("code", "0004");
                    ngData.put("msg", cpjhlist.get(i) + "卷号没有找到!");
                    reList.add(ngData);
                }

            }

            //返回结果
            result.put("ngData", reList);
            String json = null;
            try {
                json = mapper.writeValueAsString(result);
            } catch (JsonProcessingException e) {

            }
            logger.info(json);
            return json;


        } catch (Exception e) {

        }
        return "";

    }

    private String getMaxYkNo() {
        //获取移库主表当天的最大移库编号：
        //select right(max(yk_no),4) from kc_tbCpyks where yk_no like 'ZCYK'+right(CONVERT (nvarchar(8),GETDATE(),112),6)+'%'
        String ykno = moveWareHouseService.getMaxykno();
        System.out.println("获取移库主表当天的最大移库编号：" + ykno);
        if (null == ykno) {
            String dateStr = moveWareHouseService.getDbDate();
            dateStr = dateStr.substring(2, 8);
            ykno = "ZCYK" + dateStr + "0001";
//            ykno = "ZCYK1805050001";
        } else {
            String qz = ykno.substring(0, ykno.length() - 5);
            String old_srt_ykno = ykno.substring(ykno.length() - 5, ykno.length());
            int old_ykno = Integer.parseInt(old_srt_ykno);
            int new_ykno = old_ykno + 1;
            String str = String.format("%04d", new_ykno);
            ykno = qz + str;
        }
        System.out.println("新生成的移库主表编号：" + ykno);
        return ykno;
    }

    private String getUserNoByOpenId(String open_id) {
        return moveWareHouseService.getUserNoByOpenId(open_id);
    }

}
