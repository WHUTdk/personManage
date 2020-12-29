package com.dingkai.personManage.business.code.task;

/**
 * @Author dingkai
 * @Date 2020/9/7 22:32
 */
public class DeadLockTest {


    private static final Object a = new Object();
    private static final Object b = new Object();

    public static void main(String[] args) throws InterruptedException {
        //死锁模拟
        new Thread(() -> {
            synchronized (a) {
                try {
                    System.out.println("线程1获取锁a");
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (b) {
                    System.out.println("线程1获取锁a和b");
                }
            }
        }).start();

        Thread.sleep(1000);
        new Thread(() -> {
            synchronized (b) {
                System.out.println("线程2获取锁b");
                synchronized (a) {
                    System.out.println("线程2获取锁a和b");
                }
            }
        }).start();
    }


}
