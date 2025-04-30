package xyz.yygqzzk.types.design.framework.link.model2.chain;

import java.util.Objects;

/**
 * @author zzk
 * @version 1.0
 * @description 多实例责任链类
 * @since 2025/4/30
 */
public class LinkedList<E> implements ILink<E>{

    /* 责任链名称 */
    private final String name;

    transient int size = 0;

    transient Node<E> first;

    transient Node<E> last;

    public LinkedList(String name) {
        this.name = name;
    }

    /* 头插法插入链表 */
    void linkFirst(E e) {
        /* 确保头节点不会被误操作改变 */
        final Node<E> f = first;
        final Node<E> newNode = new Node<>(e, null, f);
        first = newNode;
        if(f == null) {
            last = newNode;
        } else {
            f.prev = newNode;
        }
        ++ size;
    }

    /* 尾插法插入链表 */
    void linkLast(E e) {
        final Node<E> l = last;
        final Node<E> newNode = new Node<>(e,l,null);
        last = newNode;
        if(l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
        ++ size;

    }

    /* 删除链表节点 */
    E unlink(Node<E> x) {
        final E element = x.item;
        final Node<E> prev = x.prev;
        final Node<E> next = x.next;

        if(prev == null) {
            first = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if(next == null) {
            last = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

        x.item = null;
        -- size;

        return element;
    }


    @Override
    public boolean add(E e) {
        linkLast(e);
        return true;
    }

    @Override
    public boolean addFirst(E e) {
        linkFirst(e);
        return true;
    }

    @Override
    public boolean addLast(E e) {
        linkLast(e);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        for (Node<E> x = first; x != null; x = x.next) {
            if (Objects.equals(o, x.item)) {
                unlink(x);
                return true;
            }
        }

        return true;
    }

    @Override
    public E get(int index) {
        return node(index).item;
    }

    Node<E> node(int index) {
        if (index < (size >> 1)) {
            /* 在链表前半部分 */
            Node<E> x = first;
            for (int i = 0; i < index; i++)
                x = x.next;
            return x;
        } else {
            /* 在链表后半部分 */
            Node<E> x = last;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
            return x;
        }

    }


    @Override
    public void printLinkList() {
        if (this.size == 0) {
            System.out.println("链表为空");
        } else {
            Node<E> temp = first;
            System.out.print("目前的列表，头节点：" + first.item + " 尾节点：" + last.item + " 整体：");
            while (temp != null) {
                System.out.print(temp.item + "，");
                temp = temp.next;
            }
            System.out.println();
        }
    }

    protected static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        public Node(E item, Node<E> prev, Node<E> next) {
            this.item = item;
            this.next = next;
            this.prev = prev;
        }


    }

    public String getName() {
        return name;
    }

}




