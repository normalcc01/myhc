package com.example.myhc.domain.inventory;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.myhc.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * 产品库存表 ProductInventory
 *
 *
 */
@Getter
@Setter
@TableName("product_inventory")
public class ProductInventory extends BaseDomain<ProductInventory> {
    /**
     * 产品uuid
     */
    @TableField("product_uuid")
    private String productUuid;

    /**
     * 库存数量
     */
    private Integer number;

    public ProductInventory() {
    }

    public ProductInventory(String productUuid, Integer number) {
        this.productUuid = productUuid;
        this.number = number;
    }
}