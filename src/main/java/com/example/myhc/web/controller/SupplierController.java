package com.example.myhc.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.myhc.domain.supplier.SupplierInfo;
import com.example.myhc.dto.Message;
import com.example.myhc.service.supplier.SupplierInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/supplier")
public class SupplierController {

    @Resource
    private SupplierInfoService supplierInfoService;

    @GetMapping("/select")
    public Message selectSupplierItem(){
        QueryWrapper<SupplierInfo> wrapper = new QueryWrapper<>();
        /*if(type != 0){
            wrapper.and(w -> w.eq("type", SupplierInfo.NORMAL).or().eq("type", type));
        }else {
            wrapper.eq("type", SupplierInfo.NORMAL);
        }*/
        return Message.data(supplierInfoService.selectItem(wrapper));
    }

    @GetMapping("/listSupplier")
    public Message listSupplier(SupplierInfo supplierInfo){
        List<SupplierInfo> supplierInfos = supplierInfoService.listSupplierInfo(supplierInfo);

        return Message.data(supplierInfos);
    }

    @RequestMapping("/listData")
    public Message listSupplierData(Page<SupplierInfo> page, SupplierInfo supplierInfo){
        IPage<SupplierInfo> supplierInfoPage = supplierInfoService.pageSupplierInfo(page, supplierInfo);
        return Message.page(supplierInfoPage);
    }

    @GetMapping("/info")
    public Message getInfo(Long id){
        SupplierInfo supplierInfo = supplierInfoService.getSupplierInfo(id);
        return Message.data(supplierInfo);
    }
    @PostMapping({"/save", "/edit"})
    public Message saveOrEdit(SupplierInfo supplierInfo){
        if(supplierInfo.getId() == null){
            supplierInfoService.saveSupplierInfo(supplierInfo);
        }else {
            supplierInfoService.editSupplierInfo(supplierInfo.getId(), supplierInfo);
        }
        return Message.SUCCESS;
    }

    @RequestMapping("/del")
    public Message del(Long id){
        supplierInfoService.removeSupplierInfo(id);
        return Message.SUCCESS;
    }
}
