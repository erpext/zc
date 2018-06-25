package com.zc.erpext.service.impl;

import com.zc.erpext.dao.WareHouseDao;
import com.zc.erpext.entity.WareHouse;
import com.zc.erpext.service.WareHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by msi on 2018/6/14.
 */
@Service(value = "WareHouseService")
public class WareHouseServiceImpl implements WareHouseService{

    @Autowired
    WareHouseDao wareHouseDao;

    @Override
    public List<WareHouse> getWareHouse() {
        return wareHouseDao.getWareHouse();
    }

    @Override
    public List<String> getWareHouseDetail(String ckdd) {
        return wareHouseDao.getWareHouseDetail(ckdd);
    }

}
