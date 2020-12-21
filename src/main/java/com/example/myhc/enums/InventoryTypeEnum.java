package com.example.myhc.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 库存类型的枚举
 *
 *
 *
 */
@Getter
public enum InventoryTypeEnum {

    /**
     * 库存类型的枚举
     * 库存数量
     * 操作类型 1:新增  0:不变  -1:减少
     */
    NULL("", "未知类型", 0),
    SPENDING("spending", "减少", -1),
    INCOME("income", "增加", 1);


    @EnumValue
    private String code;
    @JsonValue
    private String title;

    private int type;

    InventoryTypeEnum(String code, String title, int type){
        this.code = code;
        this.title = title;
        this.type = type;
    }

    public static InventoryTypeEnum getInstance(String code){
        InventoryTypeEnum[] values = InventoryTypeEnum.values();
        for (InventoryTypeEnum value : values) {
            if(value.code.equals(code)){
                return value;
            }
        }
        return NULL;
    }
}
