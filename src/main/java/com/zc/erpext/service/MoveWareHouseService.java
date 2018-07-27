package com.zc.erpext.service;

import com.zc.erpext.entity.KcTbcpkchzs;
import com.zc.erpext.entity.KcTbcpykLines;

import java.util.List;
import java.util.Map;

/**
 * Created by msi on 2018/5/31.
 */
public interface MoveWareHouseService {

    // 校验卷号
    int checkJh(String string);

    int checkJh(List<String> cpjh);

    int checkJhM(Map cMap);

    String getMaxykno();

    void insertCpyks(Map iMap);

    String getDbDate();

    KcTbcpkchzs checkJhM2(Map cMap);

    void updateCkdd(KcTbcpkchzs kcTbcpkchzs);

    void insertCpykLines(KcTbcpykLines kcTbcpykLines);

    long getYkdIdByYkno(String ykno);

    String getUserNoByOpenId(String open_id);
}
