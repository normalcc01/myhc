package com.example.myhc.dto;

import com.example.myhc.domain.product.ProductInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 产品库存 DTO
 *
 *
 *
 */
@Getter
@Setter
@ToString
public class ProductInventoryDTO implements Serializable {

    private String uuid;


    /**
     * 库存数量
     */
    private Integer number;

    //采购信息

    /**
     * 采购单价
     */
    private Double Paid;

    /**
     * 含税总价格
     */
    private Double TotalPrice;


    //产品信息

    /**
     * 产品
     */
    private String productUuid;
    private String productName;

    /**
     * 供应商名称
     */
    private String supplierName;



    /**
     * 规格
     */
    private String typeName;


    /**
     * 建立产品信息
     *
     */
    public void buildProductInfo(ProductInfo productInfo){
        if(productInfo != null){
            this.productName = productInfo.getProductName();
            this.supplierName = productInfo.getSupplierName();
            this.typeName = productInfo.getTypeName();
        }else {
            this.productName = "";
            this.supplierName = "";
            this.typeName = "";
        }

    }
}
