package com.wugy.java.jvm.gc_algorithm;

/**
 * wugy on 2017/10/23 13:13
 */
public class FinalizeEscapeGC {

    private static FinalizeEscapeGC saveHook = null;

    public void isAlive() {
        System.out.println("yes, i am still alive :)");
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("finalize method executed!");
        FinalizeEscapeGC.saveHook = this;
    }

    public static void main(String[] args) throws Exception {
        saveHook = new FinalizeEscapeGC();
        saveHook = null;
        System.gc();

        Thread.sleep(500);
        if (saveHook != null) {
            saveHook.isAlive();
        } else {
            System.out.println("no, i am dead :(");
        }

        saveHook = null;
        System.gc();

        Thread.sleep(500);
        if (saveHook != null) {
            saveHook.isAlive();
        } else {
            System.out.println("no, i am dead :(");
        }
    }
}
