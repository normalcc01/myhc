package com.example.myhc.domain.inventory;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.myhc.domain.BaseDomain;
import com.example.myhc.enums.AmountTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@TableName("bill_detail")
public class BillDetail extends BaseDomain<BillDetail> {
    /**
     * 订单号
     */
    @TableField("order_uuid")
    private String orderUuid;

    /**
     * 金额类型（spending支出/income收入/receivable回款）
     */
    @TableField("amount_type")
    private AmountTypeEnum amountType;

    /**
     * 金额(根据amountType标识+/-)
     */
    @TableField
    private Double amount;


    /**
     * 记账时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("bill_time")
    private Date billTime;

    /**
     * 备注
     */
    @TableField
    private String remark;
}
