package com.example.myhc.service.inventory;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.myhc.domain.inventory.InventoryRegister;
import com.example.myhc.domain.order.Order;
import com.example.myhc.dto.InventoryRegisterDTO;

import java.util.List;

public interface InventoryRegisterService extends IService<InventoryRegister> {
    /**
     * 添加产品库存关联表明细
     *
     * @param relProductInventoryUuid rel产品库存Uuid
     * @param order                   订单
     */
    void addDetail(String relProductInventoryUuid, Order order);

    /**
     * 产品库存详细信息页面列表
     *
     * @param page 页面
     * @return {@link IPage <InventoryRegisterDTO>}
     */
    IPage<InventoryRegisterDTO> listInventoryRegisterPage(Page<InventoryRegister> page, Wrapper<InventoryRegister> wrapper);

    /**
     * 撤销(删除库存变更记录)
     *
     * @param inventoryUuid rel产品库存Uuid (这个产品对应的仓库记录)
     * @param orderNumber  订单号
     * @return {@link InventoryRegister} 返回这条记录快照
     */
    InventoryRegister undo(String inventoryUuid, String orderNumber);

}
