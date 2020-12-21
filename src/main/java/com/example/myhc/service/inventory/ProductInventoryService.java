package com.example.myhc.service.inventory;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.myhc.domain.inventory.ProductInventory;
import com.example.myhc.domain.order.Order;
import com.example.myhc.dto.ProductInventoryDTO;

import java.util.List;

public interface ProductInventoryService extends IService<ProductInventory> {
    /**
     * 获取产品库存清单
     *
     * @param page    页面
     * @param wrapper 包装器
     * @return {@link IPage <ProductInventoryDTO>}
     */
    IPage<ProductInventoryDTO> listProductInventoryPage(Page<ProductInventory> page, Wrapper<ProductInventory> wrapper);

    /**
     * 增加
     *  @param order  订单

     */
    void up(Order order);

    /**
     * 减少
     *
     * @param order  订单

     */
    void down(Order order);

    /**
     * 撤销库存记录
     *
     * @param productUuid  产品uuid
     * @param orderUuid 订单号
     */
    void undo(String productUuid, String orderUuid);

}
