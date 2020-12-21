package com.example.myhc.service.inventory.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myhc.domain.order.Order;
import com.example.myhc.domain.order.PurchaseOrder;
import com.example.myhc.domain.order.SalesOrder;
import com.example.myhc.dto.InventoryRegisterDTO;
import com.example.myhc.enums.InventoryTypeEnum;
import com.example.myhc.service.inventory.InventoryRegisterService;
import com.example.myhc.service.order.PurchaseOrderService;
import com.example.myhc.service.order.SalesOrderService;
import com.example.myhc.service.product.ProductInfoService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import com.example.myhc.mapper.inventory.InventoryRegisterMapper;
import com.example.myhc.domain.inventory.InventoryRegister;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class InventoryRegisterServiceImpl extends ServiceImpl<InventoryRegisterMapper, InventoryRegister> implements InventoryRegisterService {
    /**
     * 订单服务
     */
    @Resource
    private PurchaseOrderService purchaseOrderService;

    /**
     * 销售订单服务
     */
    @Resource
    private SalesOrderService salesOrderService;

    /**
     * 产品信息服务
     */
    @Resource
    private ProductInfoService productInfoService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addDetail(String ProductInventoryUuid, Order order) {
        InventoryRegister inventoryRegister = new InventoryRegister();
        inventoryRegister.setUuid("pid_"+ IdWorker.get32UUID());
        inventoryRegister.setInventoryUuid(ProductInventoryUuid);
        InventoryTypeEnum orderInventoryType = order.getOrderInventoryType();
        Integer number = order.getNumber();
        if(orderInventoryType.getType() < 0){
            number = -number;
        }
        inventoryRegister.setNumber(number);
        inventoryRegister.setOrderUuid(order.getOrderUuid());
        inventoryRegister.setTotalPrice(order.getTotalPrice());
        inventoryRegister.setInventoryType(orderInventoryType);
        this.save(inventoryRegister);
    }

    @Override
    public IPage<InventoryRegisterDTO> listInventoryRegisterPage(Page<InventoryRegister> page, Wrapper<InventoryRegister> wrapper) {
        Page<InventoryRegister> inventoryDetailPage = this.page(page, wrapper);
        return inventoryDetailPage.convert(this::convertDTO);
    }

    @Override
    public InventoryRegister undo(String inventoryUuid, String orderNumber) {
        log.info("删除产品仓库记录关系，订单号[{}], 仓库uuid：[{}]", orderNumber, inventoryUuid);
        QueryWrapper<InventoryRegister> wrapper = new QueryWrapper<>();
        wrapper.eq("order_uuid", orderNumber).eq("inventory_uuid", inventoryUuid);
        InventoryRegister inventoryRegister = this.getOne(wrapper);

        if(inventoryRegister == null){
            throw new NullPointerException("无库存记录，删除失败");
        }

        this.removeById(inventoryRegister.getId());
        return inventoryRegister;
    }


    private InventoryRegisterDTO convertDTO(InventoryRegister inventoryRegister){
        InventoryRegisterDTO dto = new InventoryRegisterDTO(inventoryRegister);

        InventoryTypeEnum inventoryType = inventoryRegister.getInventoryType();
        String orderUuid = inventoryRegister.getOrderUuid();

        switch (inventoryType){
            case INCOME:
                //进货订单
                PurchaseOrder purchaseOrder = purchaseOrderService.getPurchaseOrderByUuid(orderUuid);
                BeanUtil.copyProperties(purchaseOrder, dto);
                dto.setPaymentAmount(-dto.getPaymentAmount());
                break;
            case SPENDING:
                //销售订单
                SalesOrder salesOrder = salesOrderService.getSalesOrderByUuid(orderUuid);
//                    BeanUtil.copyProperties(salesOrder, dto);
                dto = dto.toSalesOrder(salesOrder);
//                    dto.setNumber(inventoryDetail.getNumber());
                break;
            default:
        }
        dto.setId(inventoryRegister.getId());
        return dto;
    }
}
