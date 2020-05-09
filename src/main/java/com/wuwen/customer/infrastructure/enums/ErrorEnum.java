package com.wuwen.customer.infrastructure.enums;

/**
 * @author wuwen
 */
public enum ErrorEnum {
    /**
     * 操作成功
     */
    SUCCESS(0,""),

    /**
     * 参数错误
     */
    PARAM_ERROR(10100,""),

    /**
     * 系统错误
     */
    SYSTEM_ERROR(10101,"");

    private Integer code;
    private String errMsg;

    public Integer getCode() {
        return code;
    }

    public String getErrMsg() {
        return errMsg;
    }

    ErrorEnum(Integer code, String errMsg) {
        this.code = code;
        this.errMsg = errMsg;
    }


}
