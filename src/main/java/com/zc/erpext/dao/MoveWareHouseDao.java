package com.zc.erpext.dao;

import com.zc.erpext.entity.KcTbcpkchzs;
import com.zc.erpext.entity.KcTbcpykLines;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Created by msi on 2018/5/31.
 */
@Mapper
public interface MoveWareHouseDao {

     int checkJh(List<String> list);

     int checkJhM(Map cMap);

     String getMaxykno();

     void insertCpyks(Map iMap);

     String getDbDate();

     KcTbcpkchzs getKcXxByCpjh(Map cMap);

     void updateCkdd(KcTbcpkchzs kcTbcpkchzs);

     void insertCpykLines(KcTbcpykLines kcTbcpykLines);

     long getYkdIdByYkno(String ykno);

     String getUserNoByOpenId(String open_id);
}
