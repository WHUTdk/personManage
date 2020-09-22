package com.dingkai.personManage;

/**
 * @Author dingkai
 * @Date 2020/9/19 11:31
 */
public class QaxTest {


    public static void main(String[] args) {
        int x=-2;
        int y=-3;
        int result=1;
        if(x>0&&y>0){
            for (int i = 0; i < y; i++) {
                result =result*x;
            }
        }else if(x>0&&y<0){
            y=Math.abs(y);
            for (int i = 0; i < y; i++) {
                result =result*x;
            }
            result=1/result;
        }else if(x<0&&y>0){
            for (int i = 0; i < y; i++) {
                result =result*x;
            }
        }else {
            //x<0&&y<0
            y=Math.abs(y);
            for (int i = 0; i < y; i++) {
                result =result*x;
            }
            System.out.println(1.0/result);
            result=1/result;
        }

        System.out.println(result);
    }
}
