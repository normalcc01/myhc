package com.example.myhc.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.myhc.service.product.ProductBillService;
import com.example.myhc.domain.order.PurchaseOrder;
import com.example.myhc.domain.order.SalesOrder;
import com.example.myhc.dto.Message;
import com.example.myhc.dto.SalesOrderDTO;
import com.example.myhc.query.AdvancedQueryComponent;
import com.example.myhc.query.order.SalesOrderQuery;
import com.example.myhc.service.order.PurchaseOrderService;
import com.example.myhc.service.order.SalesOrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 订单控制器
 *
 *
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    /**
     * 产品订单列表服务
     */
    @Resource
    private ProductBillService productBillService;

    /**
     * 采购订单服务
     */
    @Resource
    private PurchaseOrderService purchaseOrderService;

    /**
     * 销售订单服务
     */
    @Resource
    private SalesOrderService salesOrderService;


    /* ---------------purchase---------------- */


    /**
     * 返款
     */
    @RequestMapping("/purchase/rebates")
    public Message rebates(@RequestParam("uuid") String purchaseOrderUuid, PurchaseOrder purchaseOrder){
        purchaseOrderService.rebates(purchaseOrderUuid, purchaseOrder);
        productBillService.rebates(purchaseOrderUuid, purchaseOrder);
        return Message.SUCCESS;
    }

    @GetMapping("/purchase/del")
    public Message purchaseDel(Long id){
        productBillService.del(id);
        return Message.SUCCESS;
    }

    /* -------------sales--------------- */

    /**
     * 销售
     */
    @PostMapping("/sales/add")
    public Message salesOrderAdd(SalesOrder salesOrder){
        salesOrderService.sales(salesOrder);
        return Message.SUCCESS;
    }

    /**
     * 销售记录列表
     */
    @GetMapping("/sales/listData")
    public Message salesListData(Page<SalesOrder> page, SalesOrderQuery salesOrderQuery){
        AdvancedQueryComponent<SalesOrder> component = new AdvancedQueryComponent<>();
        QueryWrapper<SalesOrder> wrapper = component.buildQueryWrapper(salesOrderQuery, SalesOrder.class);
        IPage<SalesOrderDTO> orderPage = salesOrderService.listSalesOrderPage(page, wrapper);
        return Message.page(orderPage);
    }


    /**
     * 撤回
     */
    @GetMapping("/sales/undo")
    public Message toWithdraw(Long id, String mark){
        salesOrderService.unSales(id, mark);
        return Message.SUCCESS;
    }

    @GetMapping("/sales/del")
    public Message salesDel(Long id){
        salesOrderService.del(id);
        return Message.SUCCESS;
    }

    /**
     * 修改销售订单客户姓名
     */
    @PostMapping("/sales/editCName")
    public Message editSalesCustomerName(@RequestParam("u") String orderNumber, @RequestParam("n") String customerName){
        UpdateWrapper<SalesOrder> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("customer_name", customerName).eq("uuid", orderNumber);
        salesOrderService.update(updateWrapper);
        return Message.SUCCESS;
    }

}
