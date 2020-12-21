package com.example.myhc.query;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.StrSpliter;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.myhc.query.annotation.AdvancedQuery;
import com.example.myhc.query.enums.QueryMethodEnum;
import com.example.myhc.util.StrKit;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 高级查询组件
 *
 *
 *
 */
@Log4j2
public class AdvancedQueryComponent<T> {

    /**
     * 建立查询包装
     *
     * @param queryComponent 查询组件
     * @return {@link QueryWrapper<T>}
     */
    public QueryWrapper<T> buildQueryWrapper(QueryComponent<T> queryComponent, Class<T> beanClass) {
        log.info("高级查询，实例自动组装！");
        Class<?> clz = queryComponent.getClass();

        T instance;
        try {
            instance = beanClass.newInstance();
            log.info("实例创建成功!{}", instance.getClass());
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("高级查询[{}]实例自动创建失败:{}", beanClass.getName(), e.getMessage());
            e.printStackTrace();
            throw new NullPointerException("实例自动创建失败");
        }

        //query转bean 自动忽略列表
        List<String> ignorePropertiesList = new LinkedList<>();

        QueryWrapper<T> wrapper = new QueryWrapper<>();
        for (Field field : clz.getFields()) {
            //跳过 static/final标注的字段
            int modifiers = field.getModifiers();
            if(Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers)){
                continue;
            }

            //获取字段数据
            String fieldName = field.getName();
            String fieldValue;
            try {
                Object obj = field.get(queryComponent);
                if(ObjectUtil.isEmpty(obj)){
                    //将空值加入忽略列表
                    ignorePropertiesList.add(fieldName);
                    log.info("查询字段[{}]值为空，已忽略", fieldName);
                    continue;
                }
                fieldValue = obj.toString().trim();
            } catch (IllegalAccessException e) {
                log.error("字段[{}]值获取失败！对象名:{}", fieldName, clz.getName());
                e.printStackTrace();
                continue;
            }

            //判断注解
            AdvancedQuery advancedQuery = field.getAnnotation(AdvancedQuery.class);

            if(advancedQuery != null){
                log.info("高级查询字段[{}]", fieldName);
                //将字段名加入忽略列表
                ignorePropertiesList.add(fieldName);

                //是否有特殊的字段名
                String tableField = advancedQuery.tableField();
                if(StrUtil.isNotBlank(tableField)){
                    fieldName = tableField;
                }

                //判断查询的方法
                QueryMethodEnum method = advancedQuery.method();
                //将驼峰式命名的字符串转换为下划线方式
                String underlineCase = StrKit.toUnderlineCase(fieldName);
                switch (method){
                    case LIKE:
                        wrapper.like(underlineCase, fieldValue);
                        break;
                    case BETWEEN:
                        List<String> items = StrSpliter.split(fieldValue.toString(), QueryComponent.BETWEEN, 2, true, true);
                        if(items.size() != 2){
                            throw new IllegalArgumentException("范围查询参数错误");
                        }
                        wrapper.between(underlineCase, items.get(0), items.get(1));
                        break;
                    default:
                        wrapper.eq(underlineCase, fieldValue);
                }
            }

        }

        //复制 queryComponent 中属性值到 bean
        String[] ignoreProperties = Arrays.copyOf(ignorePropertiesList.toArray(), ignorePropertiesList.size(), String[].class);
        BeanUtil.copyProperties(queryComponent, instance, ignoreProperties);

        log.info("属性合并完毕");
        log.info("已忽略含需特殊处理的字段为：{}", ignorePropertiesList.toString());
        log.info("无需特殊处理的对象为：{}", instance);
        //将不需要处理的条件设置
        wrapper.setEntity(instance);

        log.info("自动组装条件为：{}", wrapper.getCustomSqlSegment());
        return wrapper;
    }

}
