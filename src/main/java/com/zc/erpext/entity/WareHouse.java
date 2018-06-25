package com.zc.erpext.entity;

import java.io.Serializable;

/**
 * Created by msi on 2018/6/14.
 */
public class WareHouse implements Serializable {


    public String getCkdd_no() {
        return ckdd_no;
    }

    public void setCkdd_no(String ckdd_no) {
        this.ckdd_no = ckdd_no;
    }

    public String getCkdd_name() {
        return ckdd_name;
    }

    public void setCkdd_name(String ckdd_name) {
        this.ckdd_name = ckdd_name;
    }

    String ckdd_no;
    String ckdd_name;

}
