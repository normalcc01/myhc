package com.example.myhc.service.order.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myhc.domain.order.SalesOrder;
import com.example.myhc.domain.product.ProductInfo;
import com.example.myhc.dto.PurchasePriceDTO;
import com.example.myhc.dto.SalesOrderDTO;
import com.example.myhc.mapper.order.SalesOrderMapper;
import com.example.myhc.service.inventory.BillDetailService;
import com.example.myhc.service.inventory.ProductInventoryService;
import com.example.myhc.service.order.PurchaseOrderService;
import com.example.myhc.service.order.SalesOrderService;
import com.example.myhc.service.product.ProductInfoService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 销售订单(出库)服务Impl
 *
 *
 */
@Log4j2
@Service
public class SalesOrderServiceImpl extends ServiceImpl<SalesOrderMapper, SalesOrder> implements SalesOrderService {

    /**
     * rel产品库存服务
     */
    @Resource
    private ProductInventoryService productInventoryService;

    /**
     * 产品信息服务
     */
    @Resource
    private ProductInfoService productInfoService;


    /**
     * 购买订单服务
     */
    @Resource
    private PurchaseOrderService purchaseOrderService;

    /**
     * 公司账户，账单 服务
     */
    @Resource
    private BillDetailService billDetailService;

    @Override
    public IPage<SalesOrderDTO> listSalesOrderPage(Page<SalesOrder> page, Wrapper<SalesOrder> wrapper) {
        Page<SalesOrder> orderPage = this.page(page, wrapper);
        return orderPage.convert(salesOrder -> {
            SalesOrderDTO dto = new SalesOrderDTO(salesOrder);
            ProductInfo productInfo = productInfoService.getProductInfoByUuid(salesOrder.getProductUuid());
            //查询每笔订单产品的进价
//            PurchasePriceDTO purchaseInfo = purchaseOrderService.getPurchaseInfo(productInfo.getUuid());
//            dto.setPaid(purchaseInfo.getPaid());
//            dto.setAmount(dto.getPaid() * salesOrder.getNumber());
            return dto.toSalesOrderDTO(productInfo);
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sales(SalesOrder salesOrder) {
        log.info("销售订单, 订单uuid[{}]", salesOrder.getUuid());

        salesOrder.setUuid("S"+ IdWorker.getMillisecond());
        this.save(salesOrder);

        //削减库存
        productInventoryService.down(salesOrder);
        log.info("订单出库完成");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void unSales(Long id, String mark) {
        SalesOrder salesOrder = this.getById(id);

        if(salesOrder == null){
            log.warn("订单id[{}]不存在，撤销失败", id);
            throw new NullPointerException("订单不存在，撤销失败!");
        }
        String salesOrderUuid = salesOrder.getUuid();
        log.info("销售订单撤销, 订单uuid[{}]", salesOrderUuid);


        //库存清算
        log.info("销售订单撤销，库存清算");
        productInventoryService.up(salesOrder.toPurchaseOrder());

//        log.info("执行公司账户金额清退操作");
//        inventoryDetailService.accountChange(salesOrderUuid, deliveryUuid, salesOrder.getSaleTotalPrice(), AmountTypeEnum.INCOME, "订单["+salesOrderUuid+"]撤回退款");
//        log.info("公司账户变动完成！");

        UpdateWrapper<SalesOrder> update = new UpdateWrapper<>();
        update.set("remark", mark).eq("uuid", salesOrderUuid);
        this.update(update);
        log.info("记录撤回原因[{}]", mark);

        this.removeById(id);
    }

    @Override
    public SalesOrder getSalesOrderByUuid(String uuid) {
        QueryWrapper<SalesOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("uuid", uuid);
        return this.getOne(wrapper);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void del(Long id) {
        SalesOrder salesOrder = this.getById(id);
        log.info("删除销售订单, 订单uuid[{}]", salesOrder.getUuid());

        //撤销库存
        productInventoryService.undo(salesOrder.getProductUuid(), salesOrder.getUuid());
        log.info("库存已撤销");

        this.removeById(salesOrder.getId());
        log.warn("删除销售订单完成");
    }
}
