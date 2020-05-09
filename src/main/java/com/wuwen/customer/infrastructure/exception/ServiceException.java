package com.wuwen.customer.infrastructure.exception;

import com.wuwen.customer.infrastructure.enums.ErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wuwen
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceException extends RuntimeException{
    private Integer code;
    private String errMsg;

    public ServiceException(ErrorEnum error) {
        super(error.getErrMsg());
        this.code = error.getCode();
        this.errMsg = error.getErrMsg();
    }

    public ServiceException(Integer code, String errMsg) {
        super(errMsg);
        this.code = code;
        this.errMsg = errMsg;
    }
}
