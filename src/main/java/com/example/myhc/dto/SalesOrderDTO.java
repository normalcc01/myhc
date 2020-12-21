package com.example.myhc.dto;

import cn.hutool.core.bean.BeanUtil;
import com.example.myhc.domain.order.SalesOrder;
import com.example.myhc.domain.product.ProductInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 销售订单DTO
 *
 *
 *
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class SalesOrderDTO extends SalesOrder implements Serializable {

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 供应商名称
     */
    private String supplierName;
    private String supplierUuid;

    /**
     * 种类
     */
    private String typeName;


    //从进货单获取数据
    /**
     * 销售单价
     */
//    private Double Paid;

    /**
     * 付款额
     */
//    private Double Amount;


    public SalesOrderDTO(SalesOrder salesOrder){
        BeanUtil.copyProperties(salesOrder, this);
    }

    public SalesOrderDTO toSalesOrderDTO(ProductInfo productInfo){
        this.productName = productInfo.getProductName();

        this.supplierName = productInfo.getSupplierName();
        this.supplierUuid = productInfo.getSupplierUuid();
        this.typeName = productInfo.getTypeName();
        return this;
    }
}
