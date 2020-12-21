package com.example.myhc.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 枚举 -金额类型
 *
 * //@EnumValue 标记数据库存储取值
 * //@JsonValue 标记json返回取值
 *
 */
@Getter
public enum AmountTypeEnum {

    /**
     * 金额类型
     * 操作类型 1:新增  0:不变  -1:减少
     */
    NULL("unknown", "未知类型", 0),
    SPENDING("spending", "支出", -1),
    INCOME("income", "收入", 1),
    RECEIVABLE("receivable", "回款", 1),
    WITHDRAWALS("drawing", "提款", -1);

    @EnumValue
    private String code;
    @JsonValue
    private String title;

    private int type;

    AmountTypeEnum(String code, String title, int type){
        this.code = code;
        this.title = title;
        this.type = type;
    }

    public static AmountTypeEnum getInstance(String code){
        AmountTypeEnum[] values = AmountTypeEnum.values();
        for (AmountTypeEnum value : values) {
            if(value.code.equals(code)){
                return value;
            }
        }
        return NULL;
    }

}
