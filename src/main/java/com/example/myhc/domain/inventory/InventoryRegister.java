package com.example.myhc.domain.inventory;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.myhc.domain.BaseDomain;
import com.example.myhc.enums.InventoryTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 库存登记表 InventoryRegister
 *
 */
@Getter
@Setter
@TableName("inventory_register")
public class InventoryRegister extends BaseDomain<InventoryRegister> {
    /**
     * 订单号（入库订单号 出库订单号 ）
     */
    @TableField("order_uuid")
    private String orderUuid;

    /**
     * 产品库存表uuid
     */
    @TableField("inventory_uuid")
    private String inventoryUuid;

    /**
     * 库存类型
     */
    @TableField("inventory_type")
    private InventoryTypeEnum inventoryType;

    /**
     * 变更数量 （正负表示）
     */
    @TableField("number")
    private Integer number;

    /**
     * 总价格
     */
    @TableField("total_price")
    private Double totalPrice;
}
