package com.example.myhc.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 基础领域
 *
 *
 */
@Getter
@Setter
public class BaseDomain<T extends Model<?>> extends Model<T> {

    private static final long serialVersionUID = -7509106532443410432L;

    @TableId(value="id")
    private Long id;

    @TableField
    private String uuid;

    /**
     * 是否删除
     */
    @TableLogic
    @TableField
    private Boolean yn;

    /**
     * 创造时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 排序
     */
    @TableField
    private Integer sort;

}
