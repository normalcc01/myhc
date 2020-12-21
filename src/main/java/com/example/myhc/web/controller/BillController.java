package com.example.myhc.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.myhc.domain.inventory.BillDetail;
import com.example.myhc.dto.Message;
import com.example.myhc.enums.AmountTypeEnum;
import com.example.myhc.query.AdvancedQueryComponent;
import com.example.myhc.query.bill.BillDetailQuery;
import com.example.myhc.service.inventory.BillDetailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 公司账户（账单）
 *
 * 均以公司为中心进行计算
 *
 */
@RestController
@RequestMapping("/bill")
public class BillController {

    @Resource
    BillDetailService billDetailService;

    @RequestMapping("/listData")
    public Message listData(Page<BillDetail> page, BillDetailQuery query){
        AdvancedQueryComponent<BillDetail> component = new AdvancedQueryComponent<>();
        QueryWrapper<BillDetail> wrapper = component.buildQueryWrapper(query, BillDetail.class);
        IPage<BillDetail> billDetail = billDetailService.listInventoryDetailPage(page, wrapper);
        return Message.page(billDetail);
    }

    /**
     * 资金变更记录
     *
     * @param amountTypeCode 类型代码
     * @param detail         细节
     * @return {@link Message}
     */
    @PostMapping("/moneyChange")
    public Message save(String amountTypeCode, BillDetail detail){

        if(detail.getAmount() == null || detail.getAmount() == 0 ){
            return Message.failMsg("无效的资金变更！");
        }

        //重置类型
        detail.setAmountType(AmountTypeEnum.getInstance(amountTypeCode));
        billDetailService.addDetail(detail);
        return Message.SUCCESS;
    }

}
