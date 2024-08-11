package org.autumframework.execute;

import java.lang.reflect.Method;

public class FixedRateExecutor implements Runnable {
    private static FixedRateExecutor instance;
    private Object object;
    private Method method;
    private long period;

    // Private constructor prevents instantiation from other classes
    private FixedRateExecutor(Object object, Method method, long period) {
        this.object = object;
        this.method = method;
        this.period = period;
    }

    // Public method to provide access to the singleton instance
    public static synchronized FixedRateExecutor getInstance(Object object, Method method, long period) {
        if (instance == null) {
            instance = new FixedRateExecutor(object, method, period);
        }
        return instance;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(period);
                method.invoke(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

