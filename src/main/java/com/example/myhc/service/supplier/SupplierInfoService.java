package com.example.myhc.service.supplier;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.myhc.domain.supplier.SupplierInfo;
import com.example.myhc.service.CommonComponentService;

import java.util.List;

/**
 * 供应商及企业信息Service
 *
 *
 *
 */
public interface SupplierInfoService extends CommonComponentService<SupplierInfo> {

    /**
     * 供应商信息列表
     *
     * @param supplierInfo 供应商信息
     * @return {@link List<SupplierInfo>}
     */
    List<SupplierInfo> listSupplierInfo(SupplierInfo supplierInfo);

    /**
     * 供应商信息页
     *
     * @param page         页面
     * @param supplierInfo 供应商信息
     * @return {@link IPage<SupplierInfo>}
     */
    IPage<SupplierInfo> pageSupplierInfo(IPage<SupplierInfo> page, SupplierInfo supplierInfo);

    /**
     * 得到供应商的信息
     *
     * @param id id
     * @return {@link SupplierInfo}
     */
    SupplierInfo getSupplierInfo(Long id);

    /**
     * 被Uuid供应商信息
     *
     * @param uuid uuid
     * @return {@link SupplierInfo}
     */
    SupplierInfo getSupplierInfoByUuid(String uuid);

    /**
     * 保存供应商信息
     *
     * @param supplierInfo 供应商信息
     */
    void saveSupplierInfo(SupplierInfo supplierInfo);

    /**
     * 修改供应商信息
     *
     * @param supplierInfo 供应商信息
     * @param id           id
     */
    void editSupplierInfo(Long id, SupplierInfo supplierInfo);

    /**
     * 删除供应商信息
     *
     * @param id id
     */
    void removeSupplierInfo(Long id);
}
