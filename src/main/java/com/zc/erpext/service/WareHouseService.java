package com.zc.erpext.service;

import com.zc.erpext.entity.WareHouse;

import java.util.List;
import java.util.Map;

/**
 * Created by msi on 2018/6/14.
 */
public interface WareHouseService {
    List<WareHouse> getWareHouse();

    List<String> getWareHouseDetail(String ckdd);

    int updateSaleFlag(List<Map> cpjhlist);

    String getWareHouseCpkchzsByCpjh(String cpjh);

    int updateSaleFlagById(Map map);

}
