package com.wugy.java.custom;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 简单算法
 * <p>
 * devotion on 2017-03-24 14:51.
 */
public class AlgorithmTest {

    private int[] array = {49, 38, 65, 97, 76, 13, 27, 14, 10};

    /**
     * 插入排序
     */
    @Test
    public void testInsertSort() {
        int i, j, temp, len = array.length;
        for (i = 1; i < len; i++) {
            j = i;
            temp = array[i];
            while (j > 0 && temp < array[j - 1]) {
                array[j] = array[j - 1];
                j--;
            }
            array[j] = temp;
        }
        System.out.println(Arrays.toString(array));
    }

    /**
     * 阿姆斯特朗数问题：153 = 1^3 + 5^3 + 3^3
     * <p>
     * Todo：输出三位数的Armstrong数
     */
    @Test
    public void testArmstrong() {
        int hundredsDigit, tensDigit, unitsDigit; // 百位、十位、个位数字
        System.out.println("100~999内的Armstrong数：");
        for (int i = 100; i <= 999; i++) {
            hundredsDigit = i / 100; // "/"：除法运算：如1/2=0，而不是0.5，而1.0/2=0.5
            tensDigit = (i % 100) / 10; // "%"：取余运算：如13%7=6，左右操作数必须为整数
            unitsDigit = i % 10;
            if (i == (Math.pow(hundredsDigit, 3) + Math.pow(tensDigit, 3) + Math.pow(unitsDigit, 3)))
                System.out.print(i + " ");
        }
        System.out.println();
    }

    /**
     * 约瑟夫环问题：50个人围成一圈报数，报到3的倍数的离开，求最后剩下的那个人原来站的位置
     */
    @Test
    public void testYuesefuLoop() {
        List<Integer> list = new LinkedList<>();
        for (int i = 1; i <= 50; i++)
            list.add(i);
        int index = 0;
        while (list.size() > 0) {
            for (int i = 0; i < (3 - 1); i++) { // 不是3的倍数
                int num = list.remove(0); // 不是3的倍数，第一次开始为1
                list.add(num);
            }
            index = list.remove(0); // 未进入循环，说明是3的倍数，从列表中移除
            System.out.println("end：" + list.size() + "\tindex：" + index);
        }
        // while循环完毕，最后一个人已被移除
        System.out.println("最后剩下的那个人原来站的位置：" + index);
    }
}
