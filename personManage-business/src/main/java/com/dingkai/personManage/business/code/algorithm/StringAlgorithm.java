package com.dingkai.personManage.business.code.algorithm;

import java.util.*;

/**
 * @Author dingkai
 * @Date 2021/6/11 17:29
 * 字符串相关
 */
public class StringAlgorithm {

    /**
     * 给定字符串，去除相邻字符相同的元素   例如："abbcdeef"  返回"acdf"
     * ----演变   数字字符串，去除相邻字符相加为10的元素
     * 利用栈数据结构，依次前后判断
     */
    public static String m1(String str) {
        if (str != null && str.length() > 0) {
            char[] chars = str.toCharArray();
            Stack<Character> stack = new Stack<>();
            for (char s : chars) {
                if (stack.isEmpty()) {
                    //栈为空，直接添加
                    stack.push(s);
                } else {
                    //栈不为空，和栈顶元素进行比对，相同的话，就弹出栈顶元素
                    if (stack.peek().equals(s)) {
                        stack.pop();
                    } else {
                        //不相同，就压栈
                        stack.push(s);
                    }
                }
            }
            //栈内元素转为字符串
            StringBuilder sb = new StringBuilder();
            for (Character c : stack) {
                sb.append(c);
            }
            return sb.toString();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(m1("abbcdeef"));
    }

}
