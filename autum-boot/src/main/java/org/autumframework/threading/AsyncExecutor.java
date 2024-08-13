package org.autumframework.threading;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AsyncExecutor implements Runnable{
    private final Method method;
    private final Object object;
    private final Object param;

    public AsyncExecutor(Method method, Object object, Object param) {
       this.method = method;
       this.object = object;
       this.param = param;
    }

    @Override
    public void run() {
        method.setAccessible(true);
        try {
            method.invoke(object,param);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
