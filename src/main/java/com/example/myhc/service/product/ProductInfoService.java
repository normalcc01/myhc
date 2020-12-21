package com.example.myhc.service.product;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.myhc.domain.product.ProductInfo;
import com.example.myhc.service.CommonComponentService;

import java.util.List;

/**
 * 产品信息Service
 *
 *
 */
public interface ProductInfoService extends CommonComponentService<ProductInfo> {

    /**
     * 产品信息列表页面
     *
     * @param page    页面
     * @param wrapper 包装器
     * @return {@link IPage<ProductInfo>}
     */
    IPage<ProductInfo> listProductInfoPage(IPage<ProductInfo> page, Wrapper<ProductInfo> wrapper);

    /**
     * 列出产品名称
     * 用于选择列表
     *
     * @param fullName 全名
     * @return {@link List <ProductInfo>} 返回uuid及
     */
    List<ProductInfo> listProductName(String fullName);

    /**
     * 获取产品信息
     *
     * @param id id
     * @return {@link ProductInfo}
     */
    ProductInfo getProductInfo(Long id);

    /**
     * 通过Uuid获取产品信息
     *
     * @param uuid uuid
     * @return {@link ProductInfo}
     */
    ProductInfo getProductInfoByUuid(String uuid);

    /**
     * 通过Uuid获取产品名称
     *
     * @param uuid uuid
     * @return {@link ProductInfo}
     */
    String getProductNameByUuid(String uuid);

    /**
     * 保存产品信息
     *
     * @param productInfo 产品信息
     */
    void saveProductInfo(ProductInfo productInfo);

    /**
     * 编辑产品信息
     *
     * @param productInfo 产品信息
     */
    void editProductInfo(ProductInfo productInfo);

    /**
     * 删除
     *
     * @param id id
     */
    void remove(Long id);


}
