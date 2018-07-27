package com.zc.erpext.service;

import com.zc.erpext.entity.WareHouse;

import java.util.List;

/**
 * Created by msi on 2018/6/14.
 */
public interface WareHouseService {
    List<WareHouse> getWareHouse();

    List<String> getWareHouseDetail(String ckdd);
}
