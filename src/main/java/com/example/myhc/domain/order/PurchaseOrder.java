package com.example.myhc.domain.order;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.example.myhc.domain.BaseDomain;
import com.example.myhc.enums.InventoryTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 采购订单(入库)
 *
 *
 */
@Getter
@Setter
@TableName("purchase_order")
@ToString(callSuper = true)
public class PurchaseOrder extends BaseDomain<PurchaseOrder> implements Order{

    /**
     * 采购产品Uuid
     */
    @TableField("product_uuid")
    private String productUuid;

    /**
     * 采购名称
     */
    @TableField("purchase_name")
    private String purchaseName;

    /**
     * 订单时间
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    @TableField("order_time")
    private Date orderTime;

    /**
     * 数量
     */
    @TableField
    private Integer number;

    /**
     * 采购价格
     */
    @TableField
    private Double price;

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
     * 到货时间
     */
    @TableField("arrive_time")
    private Date arriveTime;

    /**
     * 返款的人或单位
     */
    @TableField("refund_people")
    private String refundPeople;

    /**
     * 返款时间
     */
    @TableField("return_time")
    private Date returnTime;

    /**
     * 备注
     */
    @TableField
    private String remark;

    @Override
    public InventoryTypeEnum getOrderInventoryType() {
        return InventoryTypeEnum.INCOME;
    }

    @Override
    public String getOrderUuid() {
        return this.getUuid();
    }

    @Override
    public Double getTotalPrice() {
        return this.getPaymentAmount();
    }

    /**
     * 转换为销售订单
     *
     * 目前只转换部分字段
     *
     * 对应参照 {@link SalesOrder#toPurchaseOrder()}
     * @return {@link SalesOrder}
     */
    public SalesOrder toSalesOrder(){
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setUuid(this.getUuid());
        salesOrder.setProductUuid(this.productUuid);
        salesOrder.setNumber(this.number);
        salesOrder.setSaleTotalPrice(this.paymentAmount);
        return salesOrder;
    }
}
