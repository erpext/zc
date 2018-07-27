package com.zc.erpext.util;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.lang.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result implements Serializable {

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    private Object data;

    private Error error;

}
