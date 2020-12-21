package com.example.myhc.web.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.myhc.domain.product.ProductInfo;
import com.example.myhc.dto.Message;
import com.example.myhc.query.AdvancedQueryComponent;
import com.example.myhc.query.enums.product.ProductInfoQuery;
import com.example.myhc.service.product.ProductInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 产品控制器
 *
 */
@RestController
@RequestMapping("/product")
public class ProductController {
    @Resource
    private ProductInfoService productInfoService;

    @RequestMapping("/listData")
    public Message listData(Page<ProductInfo> page, ProductInfoQuery productInfoQuery){
        AdvancedQueryComponent<ProductInfo> component = new AdvancedQueryComponent<>();
        QueryWrapper<ProductInfo> wrapper = component.buildQueryWrapper(productInfoQuery, ProductInfo.class);
        IPage<ProductInfo> productInfoPage = productInfoService.listProductInfoPage(page, wrapper);
        return Message.page(productInfoPage);
    }

    @GetMapping("/select")
    public Message selectItem(){
        return Message.data(productInfoService.selectItem(new QueryWrapper<>()));
    }

    @RequestMapping("/list")
    public Message list(@RequestParam(value = "keyword", required = false) String name){
        List<ProductInfo> productInfos = productInfoService.listProductName(name);
        return Message.data(productInfos);
    }

    @PostMapping("/save")
    public Message save(ProductInfo productInfo){
        productInfoService.saveProductInfo(productInfo);
        return Message.SUCCESS;
    }

    @PostMapping("/edit")
    public Message edit(Long id, ProductInfo productInfo){
        productInfo.setId(id);
        productInfoService.editProductInfo(productInfo);
        return Message.SUCCESS;
    }

    @RequestMapping("/del")
    public Message remove(Long id){
        productInfoService.remove(id);
        return Message.SUCCESS;
    }

    @GetMapping("/info")
    public Message info(@RequestParam(value = "id", required = false) Long id, @RequestParam(value = "uuid", required = false) String uuid){
        ProductInfo productInfo = null;
        if(id != null){
            productInfo = productInfoService.getProductInfo(id);
        }
        if(StrUtil.isNotBlank(uuid)){
            productInfo = productInfoService.getProductInfoByUuid(uuid);
        }
        return Message.data(productInfo);
    }
}
