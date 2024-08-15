package org.autumframework.threading;

import java.lang.reflect.Method;

public class ScheduleExecutor implements Runnable{
    private final Object targetObject;
    private final Method targetMethod;
    private final long period;

    public ScheduleExecutor(Object object, Method methodName, long period) {
        this.targetObject = object;
        this.period = period;
        this.targetMethod = methodName;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(period);
                targetMethod.invoke(targetObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
