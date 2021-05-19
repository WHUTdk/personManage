package com.dingkai.personManage.business.code.algorithm;

import java.util.HashMap;

/**
 * @author dingkai1
 * @desc 实现lru缓存，get set均为O(1)
 * @date 2021/5/19 15:39
 */
public class LruCache {

    /**
     * 双向链表，实现lru策略
     */
    class DoubleLinkedListNode {
        //缓存键值对
        int key, value;
        //上下节点
        DoubleLinkedListNode prev, next;

        public DoubleLinkedListNode(int key, int value) {
            this.key = key;
            this.value = value;
            this.prev = null;
            this.next = null;
        }
    }

    /**
     * hashMap的get为o(1)  双向链表同时存储键值对
     */
    private HashMap<Integer, DoubleLinkedListNode> map;

    /**
     * 容量，map存在元素>=容量时，触发删除操作，删除链表的尾节点
     */
    public int capacity;

    /**
     * 双向链表的头尾节点，每次将操作的数据移到头部
     */
    private DoubleLinkedListNode head, tail;

    public LruCache(int capacity) {
        this.map = new HashMap<>();
        this.capacity = capacity;
        //默认头尾节点元素为-1,热门数据每次插入到在head的下一个，保证能够根据head和tail在o(1)的复杂度下找到热门和不热门元素
        this.head = new DoubleLinkedListNode(-1, -1);
        this.tail = new DoubleLinkedListNode(-1, -1);
    }

    public void set(int key, int value) {
        //先判断是否已存在
        if (map.containsKey(key)) {
            //已存在的话，覆盖value，然后将节点移到头部
            DoubleLinkedListNode node = map.get(key);
            node.value = value;
            //移动节点到头节点
            //先删除节点----在确认节点位置的情况下，链表写操作的复杂度为o(1)
            remove(node);
            //再设置为头节点
            insertHead(node);
        } else {
            //不存在的话
            DoubleLinkedListNode node = new DoubleLinkedListNode(key, value);
            //先判断集合容量
            if (map.size() >= capacity) {
                //元素大小达到最大容量的话，就删除链表结尾的元素
                DoubleLinkedListNode lastNode = tail.prev;
                remove(lastNode);
                //map集合中也删除
                map.remove(lastNode.key);
            }
            //将新元素移到链表的头部
            insertHead(node);
            map.put(key, node);
        }
    }

    public int get(int key) {
        if (!map.containsKey(key)) {
            return -1;
        }
        DoubleLinkedListNode node = map.get(key);
        //先删除，再插入元素到head的下一个
        remove(node);
        insertHead(node);
        return node.value;
    }

    /**
     * 将节点从链表中移除 --- 在确认节点位置的情况下，链表写操作的复杂度为o(1)
     */
    private void remove(DoubleLinkedListNode node) {
        //将该节点的上下节点相互连接即可
        DoubleLinkedListNode prev = node.prev;
        DoubleLinkedListNode next = node.next;
        prev.next = next;
        next.prev = prev;
    }

    /**
     * 将节点移到头部 --- 始终放在head的下一个，复杂度为o(1)
     */
    private void insertHead(DoubleLinkedListNode node) {
        DoubleLinkedListNode next = head.next;
        //将该节点插入head和next中间
        head.next = node;
        node.prev = head;
        node.next = next;
        next.prev = node;
    }


}
