package xyz.yygqzzk.types.design.framework.link.model2;

import xyz.yygqzzk.types.design.framework.link.model2.chain.BusinessLinkedList;
import xyz.yygqzzk.types.design.framework.link.model2.handler.ILogicHandler;

/**
 * @author zzk
 * @version 1.0
 * @description
 * @since 2025/4/30
 */
public class LinkArmory<T, D, R> {
    private final BusinessLinkedList<T, D, R> logicLink;

    @SafeVarargs
    public LinkArmory(String linkName, ILogicHandler<T, D, R>... logicHandlers) {
        logicLink = new BusinessLinkedList<>(linkName);
        for (ILogicHandler<T, D, R> logicHandler : logicHandlers) {
            logicLink.add(logicHandler);
        }
    }

    public BusinessLinkedList<T, D, R> getLogicLink() {
        return logicLink;
    }
}




