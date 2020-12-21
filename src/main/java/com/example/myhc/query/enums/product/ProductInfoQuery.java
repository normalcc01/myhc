package com.example.myhc.query.enums.product;

import com.example.myhc.domain.product.ProductInfo;
import com.example.myhc.query.QueryComponent;
import com.example.myhc.query.enums.QueryMethodEnum;
import com.example.myhc.query.annotation.AdvancedQuery;
import lombok.Getter;
import lombok.Setter;

/**
 * 产品信息查询
 *
 *
 *
 */
@Getter
@Setter
public class ProductInfoQuery implements QueryComponent<ProductInfo> {

    @AdvancedQuery(method = QueryMethodEnum.LIKE)
    public String productName;

    /**
     * 供应商Uuid
     */
    public String supplierUuid;


    /**
     * 种类
     */
    @AdvancedQuery(method = QueryMethodEnum.LIKE)
    public String typeName;


}
