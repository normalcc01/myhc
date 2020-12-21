package com.example.myhc.dto;

import com.example.myhc.domain.order.PurchaseOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 购买价格DTO
 * 记录购买价格
 *
 *
 */
@Getter
@Setter
@ToString
public class PurchasePriceDTO {


    /**
     * 采购单价
     */
    private Double Paid;


    public PurchasePriceDTO(PurchaseOrder purchaseOrder) {
        this.Paid = purchaseOrder.getPrice();
    }

}
