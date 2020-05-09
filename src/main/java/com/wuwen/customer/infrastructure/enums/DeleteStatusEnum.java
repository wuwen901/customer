package com.wuwen.customer.infrastructure.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wuwen
 */
@AllArgsConstructor
public enum DeleteStatusEnum {

    /**
     * 0 正常
     */
    DELETE(0,"正常"),

    /**
     * 1 删除
     */
    NORMAL(1,"删除");

    @Setter
    @Getter
    private Integer code;

    @Setter
    @Getter
    private String desc;
}
