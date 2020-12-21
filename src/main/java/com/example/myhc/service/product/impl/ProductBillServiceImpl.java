package com.example.myhc.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myhc.domain.product.ProductBill;
import com.example.myhc.domain.order.PurchaseOrder;
import com.example.myhc.domain.order.SalesOrder;
import com.example.myhc.domain.product.ProductInfo;
import com.example.myhc.enums.AmountTypeEnum;
import com.example.myhc.mapper.product.ProductBillMapper;
import com.example.myhc.service.inventory.BillDetailService;
import com.example.myhc.service.inventory.ProductInventoryService;
import com.example.myhc.service.order.PurchaseOrderService;
import com.example.myhc.service.order.SalesOrderService;
import com.example.myhc.service.product.ProductBillService;
import com.example.myhc.service.product.ProductInfoService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * ProductBill
 *
 *
 */
@Log4j2
@Service
public class ProductBillServiceImpl extends ServiceImpl<ProductBillMapper, ProductBill> implements ProductBillService {

    /**
     * 产品信息维护
     */
    @Resource
    private ProductInfoService productInfoService;

    /**
     * 购买订单
     */
    @Resource
    private PurchaseOrderService purchaseOrderService;

    /**
     * 销售订单服务
     */
    @Resource
    private SalesOrderService salesOrderService;


    /**
     * 公司账户服务
     */
    @Resource
    private BillDetailService billDetailService;

    /**
     * 产品库存服务
     */
    @Resource
    private ProductInventoryService productInventoryService;

    @Override
    public IPage<ProductBill> pageProductBill(Page<ProductBill> page, Wrapper<ProductBill> wrapper) {
        return this.page(page, wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void purchase(String productUuid, PurchaseOrder purchaseOrder) {
        log.info("订单入库, 产品uuid:[{}], 订单uuid:[{}]", productUuid, purchaseOrder.getUuid());
        ProductInfo productInfo = productInfoService.getProductInfoByUuid(productUuid);

        //保存采购订单
        String purchaseUuid = purchaseOrderService.purchase(purchaseOrder);

        //手续费处理
        /*Double poundage = purchaseOrder.getPoundage();
        if(poundage != null && poundage > 0){
            log.info("该笔订单[{}]有手续费[{}]！开始从公司[{}]账户扣除", purchaseUuid, poundage, purchaseOrder.getDeliveryUuid());
            inventoryDetailService.accountChange(purchaseUuid, purchaseOrder.getDeliveryUuid(), poundage, AmountTypeEnum.POUNDAGE, null);
            log.info("手续费扣除完成!");
        }*/

        //保存采购信息
        ProductBill productBill = new ProductBill(purchaseOrder, productInfo);
        productBill.setUuid("pb_"+ IdWorker.get32UUID());
        productBill.setPurchaseOder(purchaseUuid);
        this.save(productBill);
        log.info("保存采购信息完成！");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void arrive(Long id) {
        ProductBill productBill = this.getById(id);
        log.info("标记产品[{}]到货", productBill.getProductUuid());
        productBill.setArrive(true);
        productBill.setArriveTime(new Date());
        this.updateById(productBill);
        log.info("ProductBill修改完成");

        log.info("标记产品采购订单到货,订单号[{}]", productBill.getPurchaseOder());
        purchaseOrderService.arrive(productBill.getPurchaseOder());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sales(String productUuid, SalesOrder salesOrder) {
        log.info("订单出库, 产品uuid:[{}], 订单uuid:[{}]", productUuid,  salesOrder.getUuid());
        salesOrderService.sales(salesOrder);
    }

    @Override
    public ProductBill getProductBillInfo(Long id) {
        return this.getById(id);
    }

    @Override
    public void rebates(String uuid, PurchaseOrder purchaseOrder) {
        UpdateWrapper<ProductBill> wrapper = new UpdateWrapper<>();
        //通过订单号确认并标记
        wrapper.set("rebates", true).set("rebates_time", purchaseOrder.getReturnTime()).eq("purchase_order", uuid);
        this.update(wrapper);
    }



    @Transactional(rollbackFor = Exception.class)
    @Override
    public void unPurchase(Long id, String mark) {
        log.warn("撤回采购订单！");
        ProductBill bill = this.getById(id);

        if(bill != null){
            String purchaseUuid = bill.getPurchaseOder();
            log.warn("撤回订单[{}]操作！", bill.getUuid());
            PurchaseOrder purchaseOrder = purchaseOrderService.unPurchase(purchaseUuid);

            //手续费处理
            /*Double poundage = bill.getPoundage();
            if(poundage != null && poundage > 0){
                log.info("该笔订单[{}]有手续费[{}]！开始执行手续费清退操作", purchaseUuid, poundage);
                inventoryDetailService.accountChange(purchaseUuid, purchaseOrder.getDeliveryUuid(), poundage, AmountTypeEnum.INCOME, null);
                log.info("手续费清退完成!");
            }*/

            UpdateWrapper<ProductBill> update = new UpdateWrapper<>();
            update.set("remark", "用户撤回备注：" + mark).eq("id", id);
            this.update(update);
            log.warn("撤回采购信息，id:[{}];撤回原因：[{}]", id, mark);
            this.removeById(id);
        }
    }

    @Override
    public void del(Long id) {
        ProductBill productBill = this.getById(id);

        if(productBill == null){
            throw new NullPointerException("ProductBill is null");
        }
        String purchaseOder = productBill.getPurchaseOder();

        log.info("删除采购订单[{}]", purchaseOder);
        purchaseOrderService.del(purchaseOder);

        this.removeById(productBill.getId());
    }
}
