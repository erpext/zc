package com.zc.erpext.dao;

import com.zc.erpext.entity.WareHouse;
import org.apache.ibatis.annotations.Mapper;

import java.rmi.MarshalledObject;
import java.util.List;
import java.util.Map;

/**
 * Created by msi on 2018/6/14.
 */
@Mapper
public interface WareHouseDao {

    List<WareHouse> getWareHouse();

    List<String> getWareHouseDetail(String ckdd);

    int updateSaleFlag(List<Map> cpjhlist);

    String getWareHouseCpkchzsByCpjh(String cpjh);

    int updateSaleFlagById(Map map);

}
