package com.example.myhc.domain.order;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.myhc.domain.BaseDomain;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.example.myhc.enums.InventoryTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 销售订单(出库)
 *
 *
 */
@Getter
@Setter
@TableName("sales_order")
public class SalesOrder extends BaseDomain<SalesOrder> implements Order{

    /**
     * 产品Uuid
     */
    @TableField("product_uuid")
    private String productUuid;


    /**
     * 销售时间(出库时间)
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @TableField("sales_time")
    private Date salesTime;

    /**
     * 数量
     */
    @TableField
    private Integer number;

    /**
     * 销售价格
     */
    @TableField("sale_price")
    private Double salePrice;

    /**
     * 销售总价格
     */
    @TableField("sale_total_price")
    private Double saleTotalPrice;

    /**
     * 客户名称
     */
    @TableField("customer_name")
    private String customerName;

    /**
     * 备注
     */
    @TableField
    private String remark;

    @Override
    public InventoryTypeEnum getOrderInventoryType() {
        return InventoryTypeEnum.SPENDING;
    }

    @Override
    public String getOrderUuid() {
        return this.getUuid();
    }

    @Override
    public Double getTotalPrice() {
        return this.getSaleTotalPrice();
    }

    /**
     * 将销售订单对象转换为采购爱订单对象
     *
     * 只转换部分数据
     * 对应参照 {@link PurchaseOrder#toSalesOrder()}
     * @return duix
     */
    public PurchaseOrder toPurchaseOrder(){
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setUuid(this.getUuid());
        purchaseOrder.setProductUuid(this.productUuid);
        purchaseOrder.setNumber(this.number);
        purchaseOrder.setPaymentAmount(this.saleTotalPrice);
        return purchaseOrder;
    }
}
