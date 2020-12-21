package com.example.myhc.dto;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 返回对象
 *
 *
 *
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message implements Serializable {
    private static final long serialVersionUID = -2042618546543630713L;

    public static final Message SUCCESS = Message.builder().code(ResultEnum.SUCCESS).msg(ResultEnum.SUCCESS.getMsg()).build();
    public static final Message FAIL = Message.builder().code(ResultEnum.FAIL).msg(ResultEnum.FAIL.getMsg()).build();

    /**
     * 消息信息
     */
    private String msg;
    /**
     * 请求状态，表示请求成功或者失败
     */
    private String code;
    /**
     * 返回数据集合
     */
    private Object data;

    /**
     * 总记录数
     */
    private long count = 0;

    /////////////////////构造方法/////////////////////

    private Message() {}

    @Builder
    private Message(String msg, ResultEnum code, Object data, long count) {
        this.msg = msg;
        this.code = code.getCode();
        this.data = data;
        this.count = count;
    }

    ////////////////////静态方法//////////////////////////
    /* 有数据返回 */

    public static Message page(IPage<?> page){
        return Message.builder()
                .code(ResultEnum.SUCCESS).msg(ResultEnum.SUCCESS.getMsg())
                .data(page.getRecords()).count(page.getTotal()).build();
    }

    public static <T>Message data(T data){
        MessageBuilder builder = Message.builder();
        if(data instanceof List){
            builder.count(((List) data).size());
        }else{
            builder.count(1);
        }

        return builder.code(ResultEnum.SUCCESS).msg(ResultEnum.SUCCESS.getMsg())
                .data(data).build();
    }

    public static Message count(long count){
        return Message.builder()
                .code(ResultEnum.SUCCESS).msg(ResultEnum.SUCCESS.getMsg())
                .count(count).build();
    }

    /* 无数据返回 */

    @JsonIgnore
    public boolean isSuccess(){
        return ResultEnum.SUCCESS.getCode().equals(this.code);
    }

    public static Message successMsg(String msg){
        return Message.builder().code(ResultEnum.SUCCESS).msg(msg).build();
    }

    public static Message failMsg(String msg){
        return Message.builder().code(ResultEnum.FAIL).msg(msg).build();
    }

    /* 动态方法 */

    /**
     * 动态追加返回键-值
     *
     * @param key   关键
     * @param value 价值
     * @return {@link Message}
     */
    public Message put(String key, Object value){
        Object data = this.data;
        boolean isBean = BeanUtil.isBean(data.getClass());
        if(isBean){
            Map<String, Object> map = BeanUtil.beanToMap(data);
            map.put(key, value);
            this.data = map;
        }else if(data instanceof Map){
            Map<String, Object> map = (Map<String, Object>)data;
            map.put(key, value);
            this.data = map;
        }
        return this;
    }
}