package com.example.myhc.dto;

/**
 * 选择项 定义接口 定义通用的text val
 *
 *
 */
public interface SelectItem {

    /**
     * 得到文本
     *
     * @return {@link String}
     */
    String getText();

    /**
     * 获取值
     *
     * @return {@link Object}
     */
    Object getVal();
}
