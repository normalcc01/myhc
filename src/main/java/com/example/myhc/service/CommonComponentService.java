package com.example.myhc.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.myhc.dto.SelectItemVO;

import java.util.List;

/**
 * 常见的组件服务
 *
 *
 *
 */
public interface CommonComponentService<T> extends IService<T> {

    /**
     * 选择项
     * 组装返回下拉选项列表值
     *
     * @param wrapper 包装器
     * @return {@link List<SelectItemVO>}
     */
    List<SelectItemVO> selectItem(QueryWrapper<T> wrapper);
}

