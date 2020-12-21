package com.example.myhc.dto;

import lombok.ToString;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * 下拉选项返回值模型
 *
 *
 */
@ToString
public class SelectItemVO implements Serializable {

    /**
     * 文本 展示信息
     */
    public String text;

    /**
     * 值
     */
    public Object val;

    public SelectItemVO(SelectItem item){
        this.text = item.getText();
        this.val = item.getVal();
    }

    /**
     * 构建列表
     *
     * @param itemList 项目列表
     * @return {@link List<SelectItemVO>}
     */
    public static List<SelectItemVO> buildList(List<? extends SelectItem> itemList){
        List<SelectItemVO> list = new LinkedList<>();

        for (SelectItem selectItem : itemList) {
            list.add(new SelectItemVO(selectItem));
        }
        return list;
    }
}