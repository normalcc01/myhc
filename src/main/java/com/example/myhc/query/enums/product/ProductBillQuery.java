package com.example.myhc.query.enums.product;

import com.example.myhc.domain.product.ProductBill;
import com.example.myhc.query.QueryComponent;
import com.example.myhc.query.annotation.AdvancedQuery;
import com.example.myhc.query.enums.QueryMethodEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

/**
 * 产品查询
 *
 *
 */
@Log4j2
@Getter
@Setter
@ToString
public class ProductBillQuery implements QueryComponent<ProductBill> {

    /**
     * 产品名称
     */
    public String productUuid;

    /**
     * 供应商名称
     */
    public String supplierUuid;

    /**
     * 种类
     */
    @AdvancedQuery(method = QueryMethodEnum.LIKE)
    public String typeName;

    /**
     * 订单时间
     */
    @AdvancedQuery(method = QueryMethodEnum.BETWEEN)
    public String orderTime;

    /**
     * 入库时间
     */
    @AdvancedQuery(method = QueryMethodEnum.BETWEEN)
    public String arriveTime;

    /**
     * 返款时间
     */
    @AdvancedQuery(method = QueryMethodEnum.BETWEEN)
    public String rebatesTime;


}
