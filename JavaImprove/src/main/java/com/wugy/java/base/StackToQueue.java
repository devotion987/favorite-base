package com.wugy.java.base;

import java.util.Stack;

/**
 * 使用两个栈实现队列
 * <p>
 * wugy 2017-12-22 17:01
 */
public class StackToQueue {

    private Stack<String> stack1 = new Stack<>();
    private Stack<String> stack2 = new Stack<>();

    // 出队
    public String pop() {
        if (size() == 0)
            return null;
        if (stack2.isEmpty()) {
            while (!stack1.isEmpty())
                stack2.push(stack1.pop());
        }
        return stack2.pop();
    }

    // 入队
    public void push(String s) {
        stack1.push(s);
    }

    private int size() {
        return stack1.size() + stack2.size();
    }

}
