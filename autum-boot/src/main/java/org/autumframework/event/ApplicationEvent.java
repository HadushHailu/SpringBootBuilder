package org.autumframework.event;

import org.autumframework.annotation.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Service
public class ApplicationEvent {

    private static Map<Object, Map<Object, Method>> events = new HashMap<>();

    public static void setEvents(Map<Object, Map<Object, Method>> events) {
        ApplicationEvent.events = events;
    }

    public void publishEvent(Object object){

        Map<Object,Method> methods = events.get(object.getClass().getName());
        if(methods != null) {
            methods.forEach((k,v)->{
                v.setAccessible(true);
                try {
                    v.invoke(k,object);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            });
        }

    }
}
