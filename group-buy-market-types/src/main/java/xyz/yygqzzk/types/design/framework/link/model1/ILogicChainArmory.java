package xyz.yygqzzk.types.design.framework.link.model1;

/**
 * @author zzk
 * @version 1.0
 * @description 抽象类
 * @since 2025/4/30
 */
public interface ILogicChainArmory<T, D, R> {
    ILogicLink<T, D, R> next();

    ILogicLink<T, D, R> appendNext(ILogicLink<T, D, R> next);
}
