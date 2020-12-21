package com.example.myhc.service.product;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.myhc.domain.product.ProductBill;
import com.example.myhc.domain.order.PurchaseOrder;
import com.example.myhc.domain.order.SalesOrder;

import java.util.List;

/**
 * ProductBill Service
 *
 *
 */
public interface ProductBillService extends IService<ProductBill> {

    /**
     * ProductBill page
     *
     * @param page    页面
     * @param wrapper 包装器
     * @return {@link IPage<ProductBill>}
     */
    IPage<ProductBill> pageProductBill(Page<ProductBill> page, Wrapper<ProductBill> wrapper);

    /**
     * 购买(订单入库)
     *
     * @param productUuid   产品Uuid
     * @param purchaseOrder 采购订单
     */
    void purchase(String productUuid, PurchaseOrder purchaseOrder);

    /**
     * 订单产品到达
     *
     * @param id ProductBill Id
     */
    void arrive(Long id);

    /**
     * 销售(订单出库)
     *
     * @param productUuid  产品Uuid
     * @param salesOrder   销售订单
     */
    void sales(String productUuid, SalesOrder salesOrder);

    /**
     * 得到产品账单信息
     *
     * @param id id
     * @return {@link ProductBill}
     */
    ProductBill getProductBillInfo(Long id);

    /**
     * 返款-订单返款标记
     * 标记该笔订单已返款
     *
     * @param uuid 订单号
     * @param purchaseOrder 返款信息
     */
    void rebates(String uuid, PurchaseOrder purchaseOrder);


    /**
     * 撤回操作
     *
     * @param id id
     * @param mark 撤回原因
     */
    void unPurchase(Long id, String mark);

    /**
     * 删除操作
     *
     * @param id id
     */
    void del(Long id);
}
