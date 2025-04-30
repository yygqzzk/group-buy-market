package xyz.yygqzzk.types.design.framework.link.model2.chain;

/**
 * @author zzk
 * @version 1.0
 * @description
 * @since 2025/4/30
 */
public interface ILink<E>{

    boolean add(E e);

    boolean addFirst(E e);

    boolean addLast(E e);

    boolean remove(Object o);

    E get(int index);

    void printLinkList();
}
