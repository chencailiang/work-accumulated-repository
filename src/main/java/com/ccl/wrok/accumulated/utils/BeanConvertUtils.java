package com.ccl.wrok.accumulated.utils;

import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * @Description BeanUtils封装
 * @Author Liu Xu
 * @Date 2020/11/6
 * @Version 2.0
 */
public class BeanConvertUtils extends BeanUtils {
    @FunctionalInterface
    public interface CallBack<S, T> {
        /**
         * 回调方法
         *
         * @param s 源对象
         * @param t 目标对象
         */
        void callBack(S s, T t);
    }

    /**
     * 转换list 对象
     *
     * @param sources        源对象
     * @param targetSupplier 目标对象供应方
     * @param callBack       回调方法
     * @param <S>            源对象类型
     * @param <T>            目标对象类型
     * @Return 转换对象
     */
    public static <S, T> List<T> convertListTo(List<S> sources, Supplier<T> targetSupplier, CallBack<S, T> callBack) {
        if (null == sources || null == targetSupplier) {
            return Collections.emptyList();
        }

        List<T> list = Lists.newArrayListWithCapacity(sources.size());
        for (S source : sources) {
            T target = targetSupplier.get();
            copyProperties(source, target);
            if (callBack != null) {
                callBack.callBack(source, target);
            }
            list.add(target);
        }
        return list;
    }

    /**
     * 转换list 对象
     *
     * @param sources        源对象
     * @param targetSupplier 目标对象供应方
     * @param <S>            源对象类型
     * @param <T>            目标对象类型
     */
    public static <S, T> List<T> convertListTo(List<S> sources, Supplier<T> targetSupplier) {
        return convertListTo(sources, targetSupplier, null);
    }

    /**
     * 转换对象
     *
     * @param source         源对象
     * @param targetSupplier 目标对象供应方
     * @param <S>            源对象类型
     * @param <T>            目标对象类型
     * @return 目标对象
     */
    public static <S, T> T convertTo(S source, Supplier<T> targetSupplier) {
        return convertTo(source, targetSupplier, null);
    }

    /**
     * 转换对象
     *
     * @param source         源对象
     * @param targetSupplier 目标对象供应方
     * @param callBack       回调方法
     * @param <S>            源对象类型
     * @param <T>            目标对象类型
     * @return 目标对象
     */
    public static <S, T> T convertTo(S source, Supplier<T> targetSupplier, CallBack<S, T> callBack) {
        if (null == source || null == targetSupplier) {
            return null;
        }

        T target = targetSupplier.get();
        copyProperties(source, target);
        if (callBack != null) {
            callBack.callBack(source, target);
        }
        return target;
    }
}
