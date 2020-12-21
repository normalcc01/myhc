package com.example.myhc.service.order;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.myhc.domain.order.PurchaseOrder;
import com.example.myhc.dto.PurchasePriceDTO;

import java.util.Map;

/**
 * 采购订单(入库)Service
 *
 *
 */
public interface PurchaseOrderService extends IService<PurchaseOrder> {

    /**
     * 购买
     *
     * @param purchaseOrder 采购订单
     * @return {@link String} 采购订单uuid
     */
    String purchase(PurchaseOrder purchaseOrder);

    /**
     * 账单退回
     * 删除
     *
     * @param purchaseOrderUuid 采购订单
     * @return {@link PurchaseOrder} 对象快照
     */
    PurchaseOrder unPurchase(String purchaseOrderUuid);

    /**
     * 订单产品到达
     *
     * @param purchaseOder 订单号
     */
    void arrive(String purchaseOder);

    /**
     * 获取订单信息
     *
     * @param id id
     * @return {@link PurchaseOrder}
     */
    PurchaseOrder getPurchaseOrderInfo(Long id);

    /**
     * 通过订单号获取订单信息
     *
     * @param uuid uuid 订单号
     * @return {@link PurchaseOrder}
     */
    PurchaseOrder getPurchaseOrderByUuid(String uuid);

    /**
     * 编辑订单返款信息（只变更返款部分）
     * 通过订单号确认
     *
     * @param orderUuid 订单号
     * @param purchaseOrder 订单 返款部分
     */
    void rebates(String orderUuid, PurchaseOrder purchaseOrder);

    /**
     * 得到购买(进价)信息
     * (通过产品uuid和配送公司uuid确定唯一性)
     *
     * @param productUuid  产品Uuid
     * @return {@link PurchasePriceDTO}
     */
    PurchasePriceDTO getPurchaseInfo(String productUuid);

    /**
     * 删除 采购订单
     *
     * @param purchaseOrderUuid uuid
     */
    void del(String purchaseOrderUuid);
}
