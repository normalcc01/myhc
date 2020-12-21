package com.example.myhc.service.inventory;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.myhc.domain.inventory.BillDetail;
import com.example.myhc.enums.AmountTypeEnum;

import java.util.List;

public interface BillDetailService extends IService<BillDetail> {
    /**
     * 获取 公司账户账单 列表页面
     *
     * @param page    页面
     * @param wrapper 包装器
     * @return {@link IPage < BillDetail >}
     */
    IPage<BillDetail> listInventoryDetailPage(Page<BillDetail> page, Wrapper<BillDetail> wrapper);

    /**
     * 添加
     *
     * @param billDetail 交货清单细节
     */
    void addDetail(BillDetail billDetail);

    /**
     * 资金变更
     * 采购/手续费
     *
     * @param orderUuid   订单Uuid
     * @param amount      金额
     * @param amountType 资金类型
     * @param mark 变更备注
     */
    void accountChange(String orderUuid, Double amount, AmountTypeEnum amountType, String mark);

    /**
     * 撤销账户变化
     * (删除记录)
     *
     * @param orderUuid   订单Uuid
     * @param amountType  指定 金额类型
     */
    void undoAccountChange(String orderUuid,  AmountTypeEnum amountType);

}
