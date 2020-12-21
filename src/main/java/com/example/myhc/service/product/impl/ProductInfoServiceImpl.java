package com.example.myhc.service.product.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myhc.domain.product.ProductInfo;
import com.example.myhc.domain.supplier.SupplierInfo;
import com.example.myhc.dto.SelectItemVO;
import com.example.myhc.mapper.product.ProductInfoMapper;
import com.example.myhc.service.product.ProductInfoService;
import com.example.myhc.service.supplier.SupplierInfoService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
/**
 * 产品信息服务Impl
 *
 *
 */
@Log4j2
@Service
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoMapper,ProductInfo> implements ProductInfoService {

    /**
     * 供应商信息服务
     */
    @Resource
    private SupplierInfoService supplierInfoService;

    @Override
    public IPage<ProductInfo> listProductInfoPage(IPage<ProductInfo> page, Wrapper<ProductInfo> wrapper) {
        return this.page(page, wrapper);
    }

    @Override
    public List<SelectItemVO> selectItem(QueryWrapper<ProductInfo> wrapper) {
        wrapper.select("uuid", "product_name");
        List<ProductInfo> products = this.list(wrapper);
        return SelectItemVO.buildList(products);
    }

    @Override
    public List<ProductInfo> listProductName(String fullName) {
        QueryWrapper<ProductInfo> wrapper = new QueryWrapper<>();
        if(StrUtil.isNotBlank(fullName)){
            wrapper.like("product_name", fullName);
        }
        return this.list(wrapper);
    }

    @Override
    public ProductInfo getProductInfo(Long id) {
        return this.getById(id);
    }

    @Override
    public ProductInfo getProductInfoByUuid(String uuid) {
        QueryWrapper<ProductInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("uuid", uuid);
        return this.getOne(wrapper);
    }

    @Override
    public String getProductNameByUuid(String uuid) {
        QueryWrapper<ProductInfo> wrapper = new QueryWrapper<>();
        wrapper.select("product_name").eq("uuid", uuid);
        ProductInfo productInfo = this.getOne(wrapper);
        if(productInfo != null){
            return productInfo.getProductName();
        }else {
            return "";
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveProductInfo(ProductInfo productInfo) {
        log.info("保存产品信息维护，入参[{}]", productInfo);

        productInfo.setUuid("pro_" + IdWorker.get32UUID());
        SupplierInfo supplier = supplierInfoService.getSupplierInfoByUuid(productInfo.getSupplierUuid());
        productInfo.setSupplierName(supplier.getFullName());
        this.save(productInfo);
    }

    @Override
    public void editProductInfo(ProductInfo productInfo) {
        log.info("修改产品信息维护，入参[{}]", productInfo);

        this.updateById(productInfo);
    }

    @Override
    public void remove(Long id) {
        log.warn("删除产品信息维护，id={}", id);

        this.removeById(id);
    }
}