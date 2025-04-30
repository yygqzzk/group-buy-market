package xyz.yygqzzk.types.design.framework.link.model1;

/**
 * @author zzk
 * @version 1.0
 * @description 单实例责任链抽象类
 * @since 2025/4/30
 */
public abstract class AbstractLogicLink<T, D, R> implements ILogicLink<T, D, R> {

    private ILogicLink<T, D, R> next;


    @Override
    public ILogicLink<T, D, R> next() {
        return next;
    }

    @Override
    public ILogicLink<T, D, R> appendNext(ILogicLink<T, D, R> next) {
        this.next = next;
        return next;
    }


    protected R next(T requestParameter, D dynamicContext) throws Exception {
        return next.apply(requestParameter,dynamicContext);
    }
}




