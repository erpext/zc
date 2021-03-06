package com.zc.erpext.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zc.erpext.entity.KcTbcpkchzs;
import com.zc.erpext.entity.KcTbcpykLines;
import com.zc.erpext.service.MoveWareHouseService;
import com.zc.erpext.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by msi on 2018/5/31.
 * author jiangmin
 * <p>
 * 移库 -  移出
 */
@CrossOrigin(origins = "*", maxAge = 3600,methods = {RequestMethod.GET,RequestMethod.POST})
@RestController
@RequestMapping(value = "erpext/moveout")
public class MoveOutController {

    private static final Log logger = LogFactory.getLog(MoveOutController.class);

    private ObjectMapper mapper = new ObjectMapper();

    {
        mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
    }

    @Autowired
    private MoveWareHouseService moveWareHouseService;

    @Autowired
    private UserController userController;

    @RequestMapping(value = "/testGetCookie",method = RequestMethod.GET)
    public  String testGetCookie(HttpServletRequest request){
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

        Map result = new HashMap();
        if(wxUserId != null && wxUserId.length() != 0) {
            result.put("result", "OK");
            result.put("wxUserId", wxUserId);
        }else
        {
            result.put("result", "NG");
            result.put("wxUserId", "");
        }
        String json = null;
        try {
            json = mapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {

        }
        logger.info(json);
        return json;
    }

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
    @Transactional
    //事务处理
    String moveout(@RequestBody String requestBody, HttpServletRequest request) throws IOException {
        try {
            System.out.println("***********************************************" );
            System.out.println("********Move Out start*************************" );
            System.out.println("***********************************************" );
            //String wxUserId = userService.getWxUserIdFromCookie(request);

            Map<String, Object> requestMap = mapper.readValue(requestBody, Map.class);
            List<Map> cpjhlist = new ArrayList<>(); //卷号
            String ckdd_yc; //移出仓库
            String ckdd_yr; //移入仓库
            String wxUserId;   //
            String currentPathname;
            //获取参数值
            cpjhlist = (ArrayList<Map>) requestMap.get("cpjhlist"); //卷号
            ckdd_yc = (String) requestMap.get("ckdd_yc");
            ckdd_yr = (String) requestMap.get("ckdd_yr");
            wxUserId = (String) requestMap.get("currentLoginUser");
            currentPathname = (String) requestMap.get("currentPathname");

            System.out.println("********ckdd_yc=" + ckdd_yc);
            System.out.println("********ckdd_yr=" + ckdd_yr);
            System.out.println("********wxUserId=" + wxUserId);

            if (cpjhlist.size() == 0 || ("").equals(ckdd_yc) || ("").equals(ckdd_yr)) {
                //返回 NG
                //{"result":"NG","ngData":[{"msg":"卷号没有找到","code":"002"}]}
                Map result = new HashMap();
                result.put("result", "NG");
                List list = new ArrayList();
                Map ngData = new HashMap();
                ngData.put("code", "003");
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

            // 校验用户wxUserId是否正确
            String userNo = getUserNoByOpenId(wxUserId);
            if (null == userNo || ("").equals(userNo)) {
                Map result = new HashMap();
                result.put("result", "NG");
                List list = new ArrayList();
                Map ngData = new HashMap();
                ngData.put("code", "006");
                ngData.put("msg", "微信帐号"+wxUserId+"与ERP帐号未绑定!");
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
            //判断用户是否有保存权限

            int isPrivilege = userController.getIsPrivilege(wxUserId,currentPathname);
            if (isPrivilege <= 0){
                Map result = new HashMap();
                result.put("result", "NG");
                List list = new ArrayList();
                Map ngData = new HashMap();
                ngData.put("code", "011");
                ngData.put("msg", "未授权！");
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

/*
            if (cpjhlist.size() > 0)
//                for (int i = 0; i < cpjh.size(); i++) {
//                    moveWareHouseService.checkJh(cpjh.get(i).toString());
//                }


            //性能问题先注释
            {
//                int aa = moveWareHouseService.checkJh(cpjhlist);
                Map cMap = new HashMap();
                cMap.put("cpjhs",cpjhlist);
                cMap.put("ckdd_yc",ckdd_yc);
                int aa = moveWareHouseService.checkJhM(cMap);

                System.out.println("查询到条数：" + aa);
                if (aa > 0) { */

            //开始处理业务
                    /*
                        先获取DB参数，生成主单编号
                        新增主表    移库主表：kc_tbCpyks
                        循环新增子表  移库明细表：kc_tbCpyk_lines
                        循环更新库存中库位   库存表：kc_tbCpkchzs
                     */
            String ykno = getMaxYkNo();

            // 插入主表的参数
            Map iMap = new HashMap();
            iMap.put("ykno", ykno);
            iMap.put("ckdd_yc", ckdd_yc);
            iMap.put("ckdd_yr", ckdd_yr);
            iMap.put("open_id", userNo);
            iMap.put("confirm_flag", '0');
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

                /*
                    1、插入移库明细表
                    2、查询仓库是否跨大区
                    3、插入运费主表
                    4、插入运费明细表
                    5、
                 */

                if (null != kcTbcpkchzs) {
                    //插入明细表
                    KcTbcpykLines kcTbcpykLines = new KcTbcpykLines();

                    kcTbcpykLines.setMaster_id(master_id);
                    kcTbcpykLines.setCkqy_old(ckdd_yc);
                    kcTbcpykLines.setCkqy(ckdd_yr);
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
                    kcTbcpkchzs.setCkdd(ckdd_yr);
                    kcTbcpkchzs.setCkqy("移库中");
                    kcTbcpkchzs.setUpdate_user_no(userNo);
                    moveWareHouseService.updateCkdd(kcTbcpkchzs);


                } else {
                    result.put("result", "NG");
                    Map ngData = new HashMap();
                    ngData.put("code", "0004");
                    ngData.put("msg", "[" + cpjhlist.get(i) + "]" + " 卷号没有找到!");
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


/*
                // 性能问题先注释
                } else {
                    //返回 NG
                    //{"result":"NG","ngData":[{"msg":"卷号没有找到","code":"002"}]}
                    Map result = new HashMap();
                    result.put("result", "NG");
                    List list = new ArrayList();
                    Map ngData = new HashMap();
                    ngData.put("code", "002");
                    ngData.put("msg", "卷号没有找到!");
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
        }*/


        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return "";

    }

    private String getUserNoByOpenId(String open_id) {
        return moveWareHouseService.getUserNoByOpenId(open_id);
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

}
