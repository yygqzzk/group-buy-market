package xyz.yygqzzk.types.design.framework.tree;

/**
 * @description 策略处理器
 * @author zzk
 * @version 1.0
 * @since 2025/4/27
 */
public interface StrategyHandler<T, D, R> {
    StrategyHandler DEFAULT = (T, D) -> null;
    R apply(T requestParameter, D dynamicContext) throws Exception;
}




