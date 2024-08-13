package org.autumframework.event;

import org.autumframework.annotation.Service;
import org.autumframework.customClass.CustomMethod;
import org.autumframework.threading.AsyncExecutor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Service
public class ApplicationEvent {

    private static Map<Object, Map<Object, CustomMethod>> events = new HashMap<>();


    public static void setEvents(Map<Object, Map<Object, CustomMethod>> events) {
        ApplicationEvent.events = events;
    }

    public void publishEvent(Object object){

        Map<Object,CustomMethod> methods = events.get(object.getClass().getName());
        if(methods != null) {
            methods.forEach((k,v)->{
                System.out.println("[=============++++] "+k.getClass()+" isAsync="+v.isAsync());
                if(v.isAsync()){
                    System.out.println("[========+++++] Application event started");
                    new Thread(new AsyncExecutor(v.getMethod(), k, object)).start();
                    System.out.println("[========+++++] Application event finished");
                }else{
                    this.serialExecutor(v.getMethod(), k, object);
                }

            });
        }

    }

    public void serialExecutor(Method method, Object object, Object param){
        method.setAccessible(true);
        try {
            method.invoke(object,param);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
