package com.wuwen.customer.infrastructure.util;

import com.wuwen.customer.infrastructure.enums.ErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author wuwen
 */
@Data
@AllArgsConstructor
public class RpcResult<T> {
    private Integer code;
    private String errMsg;
    private T data;

    public RpcResult() {

    }

    public static <T> RpcResult<T> buildSuccess(T t) {
        RpcResult<T> result = new RpcResult<>();
        result.code = ErrorEnum.SUCCESS.getCode();
        result.data = t;

        return result;
    }

    public static <T> RpcResult<T> buildFail(Integer code, String errMsg, T t) {
        RpcResult<T> result = new RpcResult<>();
        result.code = code;
        result.errMsg = errMsg;
        result.data = t;

        return result;
    }

    public static <T> RpcResult<T> buildFail(Integer code, String errMsg) {
        RpcResult<T> result = new RpcResult<>();
        result.code = code;
        result.errMsg = errMsg;

        return result;
    }

}
