package com.example.myhc.service.order;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.myhc.domain.order.SalesOrder;
import com.example.myhc.dto.SalesOrderDTO;

import java.util.List;

/**
 * 销售订单(出库)Service
 *
 *
 *
 */
public interface SalesOrderService extends IService<SalesOrder> {

    /**
     * 销售订单列表页面
     *
     * @param page    页面
     * @param wrapper 包装器
     * @return {@link IPage<SalesOrder>}
     */
    IPage<SalesOrderDTO> listSalesOrderPage(Page<SalesOrder> page, Wrapper<SalesOrder> wrapper);

    /**
     * 销售
     *
     * @param salesOrder   销售订单

     */
    void sales(SalesOrder salesOrder);

    /**
     * 销售撤销
     * 删除
     *
     * @param id   销售订单ID
     * @param mark 撤出原因
     */
    void unSales(Long id, String mark);

    /**
     * 通过uuid获取订单信息
     *
     * @param uuid uuid 订单号
     * @return {@link SalesOrder}
     */
    SalesOrder getSalesOrderByUuid(String uuid);


    /**
     * 删除
     *
     * @param id id
     */
    void del(Long id);
}
