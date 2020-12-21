package com.example.myhc.service.inventory.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myhc.enums.AmountTypeEnum;
import com.example.myhc.service.inventory.BillDetailService;
import com.example.myhc.service.order.PurchaseOrderService;
import com.example.myhc.service.product.ProductInfoService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.myhc.mapper.inventory.BillDetailMapper;
import com.example.myhc.domain.inventory.BillDetail;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class BillDetailServiceImpl extends ServiceImpl<BillDetailMapper, BillDetail> implements BillDetailService {


    /**
     * 购买订单服务
     */
    @Resource
    private PurchaseOrderService purchaseOrderService;

    /**
     * 产品信息服务
     */
    @Resource
    private ProductInfoService productInfoService;

    @Override
    public IPage<BillDetail> listInventoryDetailPage(Page<BillDetail> page, Wrapper<BillDetail> wrapper) {
        return this.page(page, wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public synchronized void addDetail(BillDetail billDetail) {
        log.info("添加公司账户账单操作");

        billDetail.setUuid("inv_"+ IdWorker.get32UUID());

        //操作金额
        Double amount = billDetail.getAmount();

        //判定记录方式
        AmountTypeEnum amountType = billDetail.getAmountType();
        switch (amountType){
            //收入
//            case INCOME:
                //回款
            //支出
            case SPENDING:
                //提款
                billDetail.setAmount(-amount);
                break;
            default:
                log.warn("未知的金额类型，操作已终止");
                throw new NullPointerException("未知的金额类型");
        }

        billDetail.setBillTime(new Date());
        this.save(billDetail);
        log.info("公司账户账单记录添加完成！");

        log.info("公司账户更新成功");

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void accountChange(String orderUuid, Double amount, AmountTypeEnum amountType, String mark) {
        log.info("公司账户记录！类型：{}[{}]，订单号：{}，变动金额：{}", amountType.getTitle(), amountType.getType() > 0 ? "收入" : "支出", orderUuid, amount);


        BillDetail billDetail = new BillDetail();

        //采购支出
        billDetail.setAmountType(amountType);
        billDetail.setOrderUuid(orderUuid);
        billDetail.setAmount(amount);
        billDetail.setRemark(StrUtil.isBlank(mark) ? "系统自动记录："+amountType.getTitle() : mark);
        this.addDetail(billDetail);
        log.info("系统自动记录账户变更成功");
    }

    @Override
    public void undoAccountChange(String orderUuid, AmountTypeEnum amountType) {

        QueryWrapper<BillDetail> wrapper = new QueryWrapper<>();
        wrapper.eq("order_uuid", orderUuid).eq("amount_type", amountType.getCode());
        BillDetail billDetail = this.getOne(wrapper);

        if(billDetail == null){
            log.warn("订单记录[{}]不存在，删除失败!,类型:[{}]", orderUuid, amountType.getCode());
            throw new NullPointerException("账户记录不存在，删除失败");
        }
        log.info("删除 账户明细， id:{}", billDetail.getId());
        this.removeById(billDetail.getId());
    }


}
