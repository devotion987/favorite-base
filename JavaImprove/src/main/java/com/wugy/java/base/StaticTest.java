package com.wugy.java.base;

/**
 * Created by wugy on 2017/6/23 15:20
 */
public class StaticTest {

    // static：静态变量属于类，不属于实例对象的属性，程序加载类字节码时，静态变量就会加载且只加载一次
    private static StaticTest main1 = new StaticTest();

    // 实例变量是对象在创建后通过对象引用才可以使用，本例中创建main2时，由于不断创建对象MainTest，最终导致java.lang.StackOverflowError
    // 通过test对象引用不到静态变量main1
    private StaticTest main2 = new StaticTest();

    public static void main(String[] args) {
        StaticTest test = new StaticTest();
        System.out.println(test.getClass());
    }
}
