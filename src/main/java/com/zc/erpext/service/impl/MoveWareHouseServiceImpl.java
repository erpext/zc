package com.zc.erpext.service.impl;

import com.zc.erpext.dao.MoveWareHouseDao;
import com.zc.erpext.entity.KcTbcpkchzs;
import com.zc.erpext.entity.KcTbcpykLines;
import com.zc.erpext.service.MoveWareHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by msi on 2018/5/31.
 */
@Service(value = "MoveWareHouseService")
public class MoveWareHouseServiceImpl implements MoveWareHouseService {

    @Autowired
    private MoveWareHouseDao moveWareHouseDao;

    @Override
    public int checkJh(String string) {
        return 0;
    }

    @Override
    public int checkJh(List<String> cpjh) {
        return moveWareHouseDao.checkJh(cpjh);
    }

    @Override
    public int checkJhM(Map cMap) {
        return moveWareHouseDao.checkJhM(cMap);
    }

    @Override
    public String getMaxykno() {
        return moveWareHouseDao.getMaxykno();
    }

    @Override
    public void insertCpyks(Map iMap) {
        moveWareHouseDao.insertCpyks(iMap);
    }

    @Override
    public String getDbDate() {
        return moveWareHouseDao.getDbDate();
    }

    @Override
    public KcTbcpkchzs checkJhM2(Map cMap) {
        return moveWareHouseDao.getKcXxByCpjh(cMap);
    }

    @Override
    public void updateCkdd(KcTbcpkchzs kcTbcpkchzs) {
        moveWareHouseDao.updateCkdd(kcTbcpkchzs);
    }

    @Override
    public void insertCpykLines(KcTbcpykLines kcTbcpykLines) {
        moveWareHouseDao.insertCpykLines(kcTbcpykLines);
    }

    @Override
    public long getYkdIdByYkno(String ykno) {
        return moveWareHouseDao.getYkdIdByYkno(ykno);
    }

    @Override
    public String getUserNoByOpenId(String open_id) {
        return moveWareHouseDao.getUserNoByOpenId(open_id);
    }
/*
    @Override
    public List<MoveWareHouseController> getUser() {
        return moveWareHouseDao.getUser();
    }*/

}
