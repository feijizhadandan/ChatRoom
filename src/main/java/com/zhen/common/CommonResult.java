package com.zhen.common;

import lombok.Data;

@Data
public class CommonResult {

    private boolean success;
    private String msg;
    private Object data;

    public CommonResult(boolean success, String msg, Object data) {
        this.success = success;
        this.msg = msg;
        this.data = data;
    }
}
