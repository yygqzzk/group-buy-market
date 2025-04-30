package xyz.yygqzzk.types.design.framework.link.model1;

/**
 * @author zzk
 * @version 1.0
 * @description 接口
 * @since 2025/4/29
 */
public interface ILogicLink<T, D, R> extends ILogicChainArmory<T, D, R> {

    R apply(T requestParameter, D dynamicContext) throws Exception;
}




