package com.ruoyi.common.utils;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class ReflectionUpdateUtil {
    public static <T> void updateNull(BaseMapper<T> baseMapper, T entity, Map<String, Object> updateFields)  {
        try{


        Class<?> clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();

        LambdaUpdateWrapper<T> wrapper = new LambdaUpdateWrapper<>();

        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();

            if (updateFields.containsKey(fieldName)) {
                Object fieldValue = updateFields.get(fieldName);
                field.set(entity, fieldValue);
            }

            Object value = field.get(entity);

            if (value == null) {
//                wrapper.setSql(); TODO: 拼sql
            }
        }

        baseMapper.update(entity, wrapper);
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }
    }
}
