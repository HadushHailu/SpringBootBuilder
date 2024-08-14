package org.autumframework.aspect;

import org.autumframework.annotation.After;
import org.autumframework.annotation.Before;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class ApplicationAspect implements InvocationHandler {

    private final Object              target;
    private final Map<Method,Object> aspectObject;

    public ApplicationAspect(Object target, Map<Method,Object> aspectObject) {
        this.target = target;
        this.aspectObject = aspectObject;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(aspectObject != null){
            aspectObject.forEach((k,v)->{

                try {
                    if(k.isAnnotationPresent(Before.class)) {
                        k.setAccessible(true);
                        k.invoke(v);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        Object result =method.invoke(target, args);

        if(aspectObject != null){
            aspectObject.forEach((k,v)->{

                try {
                    if(k.isAnnotationPresent(After.class)) {
                        k.setAccessible(true);
                        k.invoke(v);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        return result;
    }
}
