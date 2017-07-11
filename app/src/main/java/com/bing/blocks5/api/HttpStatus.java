package com.zjonline.blocks5.api;

import com.zjonline.blocks5.Constants;

public class HttpStatus {
    private int status_code;
    private String message;

    public HttpStatus(int status_code, String message) {
        this.status_code = status_code;
        this.message = message;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * API是否请求失败
     * @return 失败返回true, 成功返回false
     */
    public boolean isSuccessful() {
        return Constants.CustomHttpCode.HTTP_SUCCESS  == status_code;
    }

}
