package xyz.yygqzzk.types.design.framework.tree;

/**
 * @author zzk
 * @version 1.0
 * @since 2025/4/27
 * @description 策略映射器
 */
public interface StrategyMapper<T, D, R> {
    StrategyHandler<T, D, R> get(T requestParameter, D dynamicParameter) throws Exception;
}




