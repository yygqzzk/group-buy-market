package xyz.yygqzzk.types.design.framework.link.model2.chain;

import xyz.yygqzzk.types.design.framework.link.model2.handler.ILogicHandler;

/**
 * @author zzk
 * @version 1.0
 * @description
 * @since 2025/4/30
 */
public class BusinessLinkedList<T, D, R> extends LinkedList<ILogicHandler<T, D, R>> implements ILogicHandler<T, D, R>{
    public BusinessLinkedList(String name) {
        super(name);
    }

    @Override
    public R apply(T requestParameter, D dynamicContext) throws Exception {
        Node<ILogicHandler<T, D, R>> current = this.first;

        do {
            ILogicHandler<T, D, R> item = current.item;
            R apply = item.apply(requestParameter, dynamicContext);
            if(null != apply)
                return apply;
            current = current.next;
        } while(null != current);

        return null;
    }
}




