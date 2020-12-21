package com.example.myhc.service.supplier.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myhc.domain.supplier.SupplierInfo;
import com.example.myhc.dto.SelectItemVO;
import com.example.myhc.mapper.supplier.SupplierInfoMapper;
import com.example.myhc.service.supplier.SupplierInfoService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 供应商信息服务Impl
 *
 *
 *
 */
@Log4j2
@Service
public class SupplierInfoServiceImpl extends ServiceImpl<SupplierInfoMapper, SupplierInfo> implements SupplierInfoService {

    private void conditionFilter(SupplierInfo supplierInfo, QueryWrapper<SupplierInfo> wrapper){
        if(StrUtil.isNotBlank(supplierInfo.getFullName())){
            wrapper.like("full_name", supplierInfo.getFullName());
        }
    }

    @Override
    public List<SupplierInfo> listSupplierInfo(SupplierInfo supplierInfo) {
        QueryWrapper<SupplierInfo> wrapper = new QueryWrapper<>();
        conditionFilter(supplierInfo, wrapper);
        return this.list(wrapper);
    }

    @Override
    public IPage<SupplierInfo> pageSupplierInfo(IPage<SupplierInfo> page, SupplierInfo supplierInfo) {
        QueryWrapper<SupplierInfo> wrapper = new QueryWrapper<>();
        conditionFilter(supplierInfo, wrapper);
        return this.page(page, wrapper);
    }

    @Override
    public SupplierInfo getSupplierInfo(Long id) {
        return this.getById(id);
    }

    @Override
    public SupplierInfo getSupplierInfoByUuid(String uuid) {
        QueryWrapper<SupplierInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("uuid", uuid);
        return this.getOne(wrapper);
    }

    @Override
    public void saveSupplierInfo(SupplierInfo supplierInfo) {
        log.info("保存供应商信息, [{}]", supplierInfo);

        supplierInfo.setUuid("sup_" + IdWorker.get32UUID());
        this.save(supplierInfo);
    }

    @Override
    public void editSupplierInfo(Long id, SupplierInfo supplierInfo) {
        log.info("修改供应商信息, [{}]", supplierInfo);
        supplierInfo.setId(id);
        this.updateById(supplierInfo);
    }

    @Override
    public void removeSupplierInfo(Long id) {
        log.warn("删除供应商/生产企业信息, id=[{}]", id);
        this.removeById(id);
    }

    @Override
    public List<SelectItemVO> selectItem(QueryWrapper<SupplierInfo> wrapper) {
        wrapper.select("uuid", "full_name");
        List<SupplierInfo> supplierInfos = this.list(wrapper);
        return SelectItemVO.buildList(supplierInfos);
    }
}
