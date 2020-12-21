package com.example.myhc.dto;

import cn.hutool.core.bean.BeanUtil;
import com.example.myhc.domain.inventory.InventoryRegister;
import com.example.myhc.domain.order.SalesOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 库存详情DTO
 *
 *

 */
@Getter
@Setter
@ToString
public class InventoryRegisterDTO extends InventoryRegister {
    //库存相关字段--来自订单

    /**
     * 单价
     */
    private Double price;

    /**
     * 付款额
     */
    private Double paymentAmount;



    public InventoryRegisterDTO(InventoryRegister inventoryRegister){
        BeanUtil.copyProperties(inventoryRegister, this);
    }

    public InventoryRegisterDTO toSalesOrder(SalesOrder salesOrder){
        if(salesOrder != null){
            this.price = salesOrder.getSalePrice();
            this.paymentAmount = salesOrder.getSaleTotalPrice();
        }
        return this;
    }

}
