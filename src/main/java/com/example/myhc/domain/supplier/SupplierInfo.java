package com.example.myhc.domain.supplier;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.myhc.domain.BaseDomain;
import com.example.myhc.dto.SelectItem;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 供应商及企业信息
 *
 * 维护供应商以及生产企业信息
 *
 *
 */
@Getter
@Setter
@TableName("supplier_info")
@ToString(callSuper = true)
public class SupplierInfo extends BaseDomain<SupplierInfo> implements SelectItem {

    /**
     * 全名
     */
    @TableField("full_name")
    private String fullName;

    /**
     * 备注
     */
    @TableField
    private String remark;

    @Override
    public String getText() {
        return getFullName();
    }

    @Override
    public Object getVal() {
        return getUuid();
    }
}
