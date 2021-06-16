package com.dingkai.personManage.business.code.algorithm;


/**
 * @Author dingkai
 * @Date 2021/6/14 21:40
 * 链表相关
 */
public class LinkedListAlgorithm {

    public static void main(String[] args) {
        ListNode listNode = new ListNode(1);
        ListNode listNode2 = new ListNode(2);
        listNode.next=listNode2;
        ListNode listNode3 = new ListNode(3);
        listNode2.next=listNode3;
        ListNode node = reverseListNode(listNode);
    }

    /**
     * 简单链表
     */
    public static class ListNode {
        int val;
        ListNode next = null;

        ListNode(int val) {
            this.val = val;
        }
    }


    /**
     * 反转链表，传入头节点
     */
    public static ListNode reverseListNode(ListNode head) {
        ListNode pre = null;
        ListNode cur = head;
        ListNode nex = null;
        while (cur != null) {
            /**
             * 临时保存下级节点
             * 断开当前节点，并将当前节点设置为pre
             * 将下级节点设置为当前节点cur
             * 再次遍历
             * cur.next=pre 下级指向之前断开的节点，连接顺序与之前相反
             */
            nex = cur.next;
            cur.next = pre;
            pre = cur;
            cur = nex;
        }
        return pre;
    }


}
