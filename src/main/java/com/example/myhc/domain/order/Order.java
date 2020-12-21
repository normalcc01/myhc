package com.example.myhc.domain.order;

import com.example.myhc.enums.InventoryTypeEnum;

/**
 * 订单
 *
 *
 *
 */
public interface Order {

    /**
     * 获取订单的库存类型
     *
     * @return {@link InventoryTypeEnum}
     */
    InventoryTypeEnum getOrderInventoryType();

    /**
     * 得到产品Uuid
     *
     * @return {@link String}
     */
    String getProductUuid();

    /**
     * 得到数量
     *
     * @return {@link Integer}
     */
    Integer getNumber();

    /**
     * 得到订单号
     *
     * @return {@link String}
     */
    String getOrderUuid();

    /**
     * 得到的总价格
     *
     * @return {@link Double}
     */
    Double getTotalPrice();
}

