package com.example.myhc.query.bill;

import com.example.myhc.domain.inventory.BillDetail;
import com.example.myhc.query.QueryComponent;
import com.example.myhc.query.annotation.AdvancedQuery;
import com.example.myhc.query.enums.QueryMethodEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 账单详细查询
 *
 *
 *
 */
@Getter
@Setter
@ToString
public class BillDetailQuery implements QueryComponent<BillDetail> {

    /**
     * 订单Uuid
     */
    public String orderUuid;

    /**
     * 金额类型
     */
    public String amountType;

    /**
     * 记账时间
     */
    @AdvancedQuery(method = QueryMethodEnum.BETWEEN)
    public String billTime;
}
