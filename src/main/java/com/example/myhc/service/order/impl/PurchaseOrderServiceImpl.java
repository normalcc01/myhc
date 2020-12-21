package com.example.myhc.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myhc.domain.order.PurchaseOrder;
import com.example.myhc.dto.PurchasePriceDTO;
import com.example.myhc.enums.AmountTypeEnum;
import com.example.myhc.mapper.order.PurchaseOrderMapper;

import com.example.myhc.service.inventory.BillDetailService;
import com.example.myhc.service.inventory.ProductInventoryService;
import com.example.myhc.service.order.PurchaseOrderService;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 采购订单(入库)服务Impl
 *
 *
 */
@Log4j2
@Service
public class PurchaseOrderServiceImpl extends ServiceImpl<PurchaseOrderMapper, PurchaseOrder> implements PurchaseOrderService {

    /**
     * 产品库存服务
     */
    @Resource
    private ProductInventoryService productInventoryService;

    /**
     * 公司账户，账单 服务
     */
    @Resource
    private BillDetailService billDetailService;

    @Override
    public String purchase(PurchaseOrder purchaseOrder) {
        log.info("采购订单(入库), [{}]", purchaseOrder);

        //保存采购订单
        //获取一个订单时间戳
        purchaseOrder.setUuid("B"+ IdWorker.getMillisecond());
        this.save(purchaseOrder);
        log.info("保存采购订单完成");
        //采购数量
        Integer number = purchaseOrder.getNumber();
        if(purchaseOrder.getArrive() !=null && purchaseOrder.getArrive()){
            purchaseOrder.setArriveTime(new Date());
            //修改库存
            productInventoryService.up(purchaseOrder);
            log.info("修改库存完成");
        }

        log.info("订单入库完成");
        /*billDetailService.accountChange(purchaseOrder.getUuid(),  purchaseOrder.getPaymentAmount(), AmountTypeEnum.SPENDING, null);
        log.info("公司账户变动完成！");*/
        return purchaseOrder.getUuid();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PurchaseOrder unPurchase(String purchaseOrderUuid) {
        log.warn("采购订单取消(清退入库), 采购订单号：[{}]", purchaseOrderUuid);
        QueryWrapper<PurchaseOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("uuid", purchaseOrderUuid);
        PurchaseOrder purchaseOrder = this.getOne(wrapper);
        if(purchaseOrder == null){
            log.warn("订单[{}]不存在，退回清算失败", purchaseOrderUuid);
            throw new NullPointerException("订单不存在，退回清算失败");
        }

        log.info("订单已到货，执行清退库存操作");
        if(purchaseOrder.getArrive() !=null && purchaseOrder.getArrive()){
            //修改库存
            log.info("订单取消，清算退回库存！");
            productInventoryService.down(purchaseOrder.toSalesOrder());
            log.info("库存退回完成");
        }

//        log.info("执行公司账户金额清退操作");
        /*billDetailService.accountChange(purchaseOrder.getUuid(),  purchaseOrder.getPaymentAmount(), AmountTypeEnum.INCOME, "订单["+purchaseOrderUuid+"]撤回退款");
        log.info("公司账户变动完成！");*/

        log.info("删除订单[{}]", purchaseOrderUuid);
        this.remove(wrapper);
        return purchaseOrder;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void arrive(String purchaseOderUuid) {
        QueryWrapper<PurchaseOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("uuid", purchaseOderUuid);
        PurchaseOrder purchaseOrder = this.getOne(wrapper);

        log.info("将订单号[{}]产品数量加入库存", purchaseOderUuid);
        productInventoryService.up(purchaseOrder);
        log.info("修改库存完成");

        purchaseOrder.setArrive(true);
        purchaseOrder.setArriveTime(new Date());
        this.updateById(purchaseOrder);
        log.info("订单入库完成");
    }

    @Override
    public PurchaseOrder getPurchaseOrderInfo(Long id) {
        return this.getById(id);
    }

    @Override
    public PurchaseOrder getPurchaseOrderByUuid(String uuid) {
        QueryWrapper<PurchaseOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("uuid", uuid);
        return this.getOne(wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void rebates(String orderUuid, PurchaseOrder purchaseOrder) {

        log.info("订单[{}]返款信息变更[{}]", orderUuid, purchaseOrder);

        UpdateWrapper<PurchaseOrder> wrapper = new UpdateWrapper<>();
        wrapper .set("price", purchaseOrder.getPrice())
                .set("payment_amount", purchaseOrder.getPaymentAmount())
                .set("refund_people", purchaseOrder.getRefundPeople())
                .set("return_time", purchaseOrder.getReturnTime()).eq("uuid", orderUuid);

        this.update(wrapper);
        log.info("订单[{}]返款信息变更完成", orderUuid);

    }

    @Deprecated
    @Override
    public PurchasePriceDTO getPurchaseInfo(String productUuid) {
        QueryWrapper<PurchaseOrder> wrapper = new QueryWrapper<>();
        wrapper.select("uuid", "price");
        wrapper.eq("product_uuid", productUuid);
        List<PurchaseOrder> list = this.list(wrapper);
        if(list == null || list.isEmpty()){
            log.error("获取物品价格失败，采购订单不存在, 产品uuid：{}", productUuid);
            throw new NullPointerException("获取物品价格失败，采购订单不存在");
        }
        return new PurchasePriceDTO(list.get(0));
    }

    @Override
    public void del(String purchaseOrderUuid) {
        QueryWrapper<PurchaseOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("uuid", purchaseOrderUuid);
        PurchaseOrder purchaseOrder = this.getOne(wrapper);

        if(purchaseOrder == null){
            log.warn("订单[{}]不存在", purchaseOrderUuid );
            throw new NullPointerException("订单不存在");
        }

        log.info("删除采购订单, [{}]", purchaseOrder);

        if(purchaseOrder.getArrive() !=null && purchaseOrder.getArrive()){
            purchaseOrder.setArriveTime(new Date());
            //撤销库存
            productInventoryService.undo(purchaseOrder.getProductUuid(), purchaseOrder.getUuid());
            log.info("撤销库存完成");
        }


//        log.info("订单购买支出金额退回");
        /*billDetailService.undoAccountChange(purchaseOrder.getUuid(), AmountTypeEnum.SPENDING);
        log.info("公司账户变动完成！");*/

        this.removeById(purchaseOrder.getId());
        log.info("采购订单删除完成");
    }
}
