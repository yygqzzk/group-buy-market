package xyz.yygqzzk.types.design.framework.link.model2.handler;

/**
 * @author zzk
 * @version 1.0
 * @description
 * @since 2025/4/30
 */
public interface ILogicHandler<T, D, R> {

    default R next(T requestParameter, D dynamicContext) {
        return null;
    }

    R apply(T requestParameter, D dynamicContext) throws Exception;

}




