package com.example.myhc.domain.product;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.myhc.domain.BaseDomain;
import com.example.myhc.dto.SelectItem;
import lombok.Getter;
import lombok.Setter;

/**
 * 产品信息
 *
 * 用于维护商品信息
 *
 *
 */
@Getter
@Setter
@TableName("product_info")
public class ProductInfo extends BaseDomain<ProductInfo> implements SelectItem {
    /**
     * 产品名称
     */
    @TableField("product_name")
    private String productName;

    /**
     * 种类
     */
    @TableField("type_name")
    private String typeName;

    /**
     * 供应商名称
     */
    @TableField("supplier_name")
    private String supplierName;
    private String supplierUuid;

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
     * 备注
     */
    private String remark;

    @Override
    public String getText() {
        return this.getProductName();
    }

    @Override
    public Object getVal() {
        return this.getUuid();
    }
}
