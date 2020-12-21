package com.example.myhc.dto;

import java.io.Serializable;

/**
 * 返回常量枚举
 *
 **/
public enum ResultEnum implements Serializable {

    /**
     * 返回成功
     */
    SUCCESS("0", "操作成功"),
    /**
     * 返回失败
     */
    FAIL("1", "操作失败");

    private String code;
    private String msg;

    ResultEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
