package com.example.myhc.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.myhc.domain.product.ProductBill;
import com.example.myhc.domain.order.PurchaseOrder;
import com.example.myhc.domain.order.SalesOrder;
import com.example.myhc.dto.Message;
import com.example.myhc.query.AdvancedQueryComponent;
import com.example.myhc.query.enums.product.ProductBillQuery;
import com.example.myhc.service.product.ProductBillService;
import com.example.myhc.service.order.PurchaseOrderService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

/**
 * 产品账单 控制器
 *
 */
@RestController
@RequestMapping("/productBill")
public class ProductBillController {

    @Resource
    private ProductBillService productBillService;

    @Resource
    private PurchaseOrderService purchaseOrderService;

    @RequestMapping("/listData")
    public Message list(Page<ProductBill> page, ProductBillQuery productQuery){
        AdvancedQueryComponent<ProductBill> component = new AdvancedQueryComponent<>();
        QueryWrapper<ProductBill> wrapper = component.buildQueryWrapper(productQuery, ProductBill.class);
        IPage<ProductBill> res = productBillService.pageProductBill(page, wrapper);
        return Message.page(res);
    }

    /**
     * 保存(采购入库)
     *
     * @param purchaseOrder 采购订单
     * @param productUuid   产品Uuid
     * @return {@link Message}
     */
    @RequestMapping("/purchase")
    public Message save(String productUuid, PurchaseOrder purchaseOrder){
        productBillService.purchase(productUuid, purchaseOrder);
        return Message.SUCCESS;
    }


    /**
     * 订单产品 到达
     *
     */
    @RequestMapping("/arrive")
    public Message arrive(Long id){
        productBillService.arrive(id);
        return Message.SUCCESS;
    }

    /**
     * 销售(订单出库)
     *
     * @param productUuid  产品Uuid
     * @param salesOrder   销售订单
     * @param deliveryUuid 配送公司Uuid（货源）
     * @return {@link Message}
     */
    @RequestMapping("/sales")
    public Message sales(String productUuid, String deliveryUuid, SalesOrder salesOrder){
        productBillService.sales(productUuid,salesOrder);
        return Message.SUCCESS;
    }

    /**
     * 产品订单信息
     *
     * @param id id
     * @return {@link Message}
     */
    @GetMapping("/info")
    public Message info(Long id){
        ProductBill productBillInfo = productBillService.getProductBillInfo(id);
        PurchaseOrder purchaseOrder = purchaseOrderService.getPurchaseOrderByUuid(productBillInfo.getPurchaseOder());
        return Message.data(productBillInfo).put("price", purchaseOrder.getPrice());
    }

    /**
     * 编辑产品账单
     *
     * @param productUuid   产品Uuid
     * @param purchaseOrder 采购订单
     * @return {@link Message}
     */
    @PostMapping("/edit")
    public Message editBill(String productUuid, PurchaseOrder purchaseOrder){

        System.out.println(productUuid);
        System.out.println(purchaseOrder);
        return Message.FAIL;
    }

    /**
     * 撤回
     */
    @GetMapping("/undo")
    public Message toWithdraw(Long id, String mark){
        productBillService.unPurchase(id, mark);
        return Message.SUCCESS;
    }

}
