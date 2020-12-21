package com.example.myhc.query.order;

import com.example.myhc.domain.order.SalesOrder;
import com.example.myhc.query.QueryComponent;
import com.example.myhc.query.annotation.AdvancedQuery;
import com.example.myhc.query.enums.QueryMethodEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 销售订单查询
 *
 *
 */
@Getter
@Setter
@ToString
public class SalesOrderQuery implements QueryComponent<SalesOrder> {

    public String productUuid;

    @AdvancedQuery(method = QueryMethodEnum.LIKE)
    public String customerName;

    @AdvancedQuery(method = QueryMethodEnum.BETWEEN)
    public String salesTime;

}
