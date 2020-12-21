package com.example.myhc.domain.product;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.myhc.domain.BaseDomain;
import com.example.myhc.domain.order.PurchaseOrder;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 产品账单
 *
 * 用于总的展示 逐条展示
 *
 *
 */
@Getter
@Setter
@TableName("product_bill")
public class ProductBill extends BaseDomain<ProductBill> {

    /**
     * 订单号
     */
    @TableField("purchase_order")
    private String purchaseOder;

    /**
     * 采购名称
     */
    @TableField("purchase_name")
    private String purchaseName;

    /**
     * 产品名称
     */
    @TableField("product_name")
    private String productName;
    @TableField("product_uuid")
    private String productUuid;

    /**
     * 供应商名称
     */
    @TableField("supplier_name")
    private String supplierName;
    @TableField("supplier_uuid")
    private String supplierUuid;

    /**
     * 种类
     */
//    @TableField
//    private String typeName;

    /**
     * 销售价格
     */
    @TableField("sale_price")
    private Double salePrice;

    /**
     * 采购价格
     */
    @TableField("cost_price")
    private Double costPrice;

    /**
     * 订单时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @TableField("order_time")
    private Date orderTime;

    /**
     * 数量
     */
    @TableField
    private Integer number;

    /**
     * 总金额
     */
    @TableField("payment_amount")
    private Double paymentAmount;

    /**
     * 是否到达
     */
    @TableField
    private Boolean arrive;

    /**
     * 入库时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @TableField("arrive_time")
    private Date arriveTime;

    /**
     * 是否返款
     */
    @TableField
    private Boolean rebates;

    /**
     * 返款时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @TableField("rebates_time")
    private Date rebatesTime;

    /**
     * 备注
     */
    private String remark;

    public ProductBill() {
    }

    public ProductBill(PurchaseOrder purchaseOrder, ProductInfo productInfo){

        this.purchaseName = purchaseOrder.getPurchaseName();

        this.productName = productInfo.getProductName();
        this.productUuid = productInfo.getUuid();

        this.supplierName = productInfo.getSupplierName();
        this.supplierUuid = productInfo.getSupplierUuid();

//        this.typeName = productInfo.getTypeName();

        this.salePrice = productInfo.getSalePrice();
        this.costPrice = productInfo.getCostPrice();

        this.orderTime = purchaseOrder.getOrderTime();
        this.number = purchaseOrder.getNumber();
        this.paymentAmount = purchaseOrder.getPaymentAmount();
        this.arrive = purchaseOrder.getArrive();
        if(this.arrive != null && this.arrive){
            this.arriveTime = new Date();
            if(purchaseOrder.getArriveTime() != null){
                this.arriveTime = purchaseOrder.getArriveTime();
            }
        }

    }

}
