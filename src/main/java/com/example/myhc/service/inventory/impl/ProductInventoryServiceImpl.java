package com.example.myhc.service.inventory.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myhc.domain.inventory.InventoryRegister;
import com.example.myhc.domain.order.Order;
import com.example.myhc.domain.product.ProductInfo;
import com.example.myhc.dto.ProductInventoryDTO;
import com.example.myhc.dto.PurchasePriceDTO;
import com.example.myhc.service.inventory.InventoryRegisterService;
import com.example.myhc.service.inventory.ProductInventoryService;
import com.example.myhc.service.order.PurchaseOrderService;
import com.example.myhc.service.product.ProductInfoService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import com.example.myhc.domain.inventory.ProductInventory;
import com.example.myhc.mapper.inventory.ProductInventoryMapper;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Log4j2
@Service
public class ProductInventoryServiceImpl extends ServiceImpl<ProductInventoryMapper, ProductInventory> implements ProductInventoryService {



    @Resource
    private ProductInfoService productInfoService;

    /**
     * 购买订单服务
     */
    @Resource
    private PurchaseOrderService purchaseOrderService;

    /**
     * 产品库存关联表明细
     */
    @Resource
    private InventoryRegisterService inventoryRegisterService;

    @Override
    public IPage<ProductInventoryDTO> listProductInventoryPage(Page<ProductInventory> page, Wrapper<ProductInventory> wrapper) {
        Page<ProductInventory> productInventoryPage = this.page(page, wrapper);
        return productInventoryPage.convert(this::convert);
    }

    private ProductInventoryDTO convert(ProductInventory productInventory){
        ProductInventoryDTO inventoryDTO = new ProductInventoryDTO();
        BeanUtil.copyProperties(productInventory, inventoryDTO);


        //装载产品信息
        ProductInfo productInfo = productInfoService.getProductInfoByUuid(inventoryDTO.getProductUuid());
        inventoryDTO.buildProductInfo(productInfo);

        if(productInventory.getNumber() > 0){

            //装载采购信息
            PurchasePriceDTO purchaseInfo = purchaseOrderService.getPurchaseInfo(inventoryDTO.getProductUuid());
            try {
                inventoryDTO.setPaid(purchaseInfo.getPaid());

                inventoryDTO.setTotalPrice(inventoryDTO.getPaid() * inventoryDTO.getNumber());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return inventoryDTO;
    }

    /**
     * 增加
     *
     * @param order 采购订单
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void up(Order order) {
        //产品Uuid
        String productUuid = order.getProductUuid();
        //数量
        Integer number = order.getNumber();

        log.info("产品[{}], 增加库存[{}]件", productUuid, number);

        QueryWrapper<ProductInventory> wrapper = new QueryWrapper<>();
        wrapper.eq("product_uuid", productUuid);
        ProductInventory productInventory = this.getOne(wrapper);

        if(productInventory == null){
            log.info("[{}]新增库存[{}]件", productUuid, number);
            productInventory = new ProductInventory(productUuid, number);
            productInventory.setUuid("relPI_"+ IdWorker.get32UUID());
            this.save(productInventory);
        }else {
            Integer storeNumber = productInventory.getNumber();
            storeNumber += number;
            UpdateWrapper<ProductInventory> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("number", storeNumber);
            updateWrapper.eq("id", productInventory.getId());
            log.info("[{}]增加库存[{}]->[{}]", productUuid, productInventory.getNumber(), storeNumber);
            this.update(updateWrapper);
        }

        inventoryRegisterService.addDetail(productInventory.getUuid(), order);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void down(Order order) {
        //产品Uuid
        String productUuid = order.getProductUuid();
        //数量
        Integer number = order.getNumber();
        log.info("产品[{}], 删减库存[{}]件", productUuid, number);

        QueryWrapper<ProductInventory> wrapper = new QueryWrapper<>();
        wrapper.eq("product_uuid", productUuid);
        ProductInventory productInventory = this.getOne(wrapper);

        if(productInventory == null){
            throw new NullPointerException("产品库存为空！无法减少！");
        }

        Integer storeNumber = productInventory.getNumber();
        storeNumber = storeNumber - number;

        if(storeNumber < 0){
            throw new IllegalArgumentException("库存不足！"+storeNumber);
        }

        Long id = productInventory.getId();
//        if(storeNumber == 0){
//            log.warn("库存减少为0，删除记录！记录id：[{}]", id);
//            this.removeById(id);
//        }else {
        log.info("[{}]减少库存[{}]->[{}]", productUuid, productInventory.getNumber(), storeNumber);
        UpdateWrapper<ProductInventory> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("number", storeNumber);
        updateWrapper.eq("id", id);
        this.update(updateWrapper);
        log.info("更新库存成功");
//        }

        inventoryRegisterService.addDetail(productInventory.getUuid(), order);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void undo(String productUuid, String orderUuid) {
        log.info("获取仓库记录，商品uuid：[{}]", productUuid);
        QueryWrapper<ProductInventory> wrapper = new QueryWrapper<>();
        wrapper.eq("product_uuid", productUuid);
        ProductInventory productInventory = this.getOne(wrapper);

        if(productInventory == null){
            log.warn("库存记录获取为空");
            throw new NullPointerException("库存记录获取为空");
        }

        log.info("删除产品库存关系记录");
        InventoryRegister inventoryRegister = inventoryRegisterService.undo(productInventory.getUuid(), orderUuid);
        Integer number = inventoryRegister.getNumber();

        //恢复库存
        Integer currentNumber = productInventory.getNumber();

        currentNumber = currentNumber - number;

        Long id = productInventory.getId();
//        if(currentNumber == 0){
//            log.warn("库存减少为0，删除记录！记录id：[{}]", id);
//            this.removeById(id);
//        }else {
        log.warn("撤销产品库存关系记录，恢复库存[{}]->[{}]", productInventory.getNumber(), currentNumber);
        UpdateWrapper<ProductInventory> update = new UpdateWrapper<>();
        update.set("number", currentNumber).eq("id", id);
        this.update(update);
//        }

    }
}
