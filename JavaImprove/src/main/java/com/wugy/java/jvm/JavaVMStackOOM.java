package com.wugy.java.jvm;

/**
 * VM args:-Xss2M
 * <p>
 * wugy on 2017/10/18 10:39
 */
public class JavaVMStackOOM {

    private void dontStop() {
        while (true) {
        }
    }

    private void stackLeakByThread() {
        while (true) {
            Thread thread = new Thread(() -> {
                dontStop();
            });
            thread.start();
        }
    }

    public static void main(String[] args) {
        JavaVMStackOOM oom = new JavaVMStackOOM();
        oom.stackLeakByThread();
    }
}
