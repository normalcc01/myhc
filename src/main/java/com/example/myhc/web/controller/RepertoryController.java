package com.example.myhc.web.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.myhc.domain.inventory.InventoryRegister;
import com.example.myhc.domain.inventory.ProductInventory;
import com.example.myhc.service.inventory.InventoryRegisterService;
import com.example.myhc.service.inventory.ProductInventoryService;
import com.example.myhc.dto.Message;
import com.example.myhc.dto.ProductInventoryDTO;
import com.example.myhc.dto.InventoryRegisterDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

/**
 * 库存 控制器
 *
 *
 */
@RestController
@RequestMapping("/repertory")
public class RepertoryController {

    @Resource
    private ProductInventoryService productInventoryService;

    @Resource
    private InventoryRegisterService inventoryRegisterService;

    @RequestMapping("/listData")
    public Message listData(Page<ProductInventory> page, ProductInventory productInventory){
        String productUuid = productInventory.getProductUuid();
        QueryWrapper<ProductInventory> wrapper = new QueryWrapper<>();
        if(StrUtil.isNotBlank(productUuid)){
            wrapper.eq("product_uuid", productUuid);
        }

        IPage<ProductInventoryDTO> productInventoryPage = null;
        try {
            productInventoryPage = productInventoryService.listProductInventoryPage(page, wrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Message.page(productInventoryPage);
    }

    /**
     * 库存产品详情（流水）
     * @param uuid relProductInventory uuid
     * @return {@link Message}
     */
    @RequestMapping("/detail")
    public Message productInventoryDetail(Page<InventoryRegister> page, String uuid, @RequestParam(value = "type", required = false) String inventoryType){
        QueryWrapper<InventoryRegister> wrapper = new QueryWrapper<>();
        wrapper.eq("inventory_uuid", uuid);
        if(StrUtil.isNotBlank(inventoryType)){
            wrapper.eq("inventory_type", inventoryType);
        }
        IPage<InventoryRegisterDTO> inventoryDetailPage = inventoryRegisterService.listInventoryRegisterPage(page, wrapper);
        return Message.page(inventoryDetailPage);
    }

}
