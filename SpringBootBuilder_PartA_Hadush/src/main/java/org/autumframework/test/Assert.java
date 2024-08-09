package org.autumframework.test;

public class Assert {
    public static void assertEquals(int x, int y) {
        if (x != y)
            System.out.println("Fail: result = "+x+" but expected "+y);
        else{
            System.out.println("Test succeeded");
        }
    }
}
