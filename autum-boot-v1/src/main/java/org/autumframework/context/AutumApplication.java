package org.autumframework.context;

import Application.Application;
import org.autumframework.annotation.Autowired;
import org.autumframework.annotation.Service;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class AutumApplication {
    private static List<Object> serviceObject = new ArrayList<>();

    public AutumApplication(String prefix){
        //Add all classes with @Service annotation
        try {
            Reflections reflections = new Reflections(prefix);
            Set<Class<?>> ImplServiceObjectType = reflections.getTypesAnnotatedWith(Service.class);
            for (Class<?> implementationClass : ImplServiceObjectType) {
                serviceObject.add((Object) implementationClass.getDeclaredConstructor().newInstance());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.performDependencyInjection();
    }

    private void performDependencyInjection(){
        try {
            for (Object theServiceClass : serviceObject) {
                for (Field field : theServiceClass.getClass().getDeclaredFields()) {
                    if (field.isAnnotationPresent(Autowired.class)) {
                        Class<?> theFieldType = field.getType();
                        Object instance = this.getServiceBeanOfType(theFieldType);
                        field.setAccessible(true);
                        field.set(theServiceClass, instance);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getServiceBeanOfType(Class<?> interfaceClass){
        Object service = null;
        try {
            for (Object theClass : serviceObject) {
                Class<?>[] interfaces = theClass.getClass().getInterfaces();
                for (Class<?> theInterface : interfaces) {
                    if (theInterface.getName().contentEquals(interfaceClass.getName()))
                        service = theClass;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return service;
    }

    public void run(){
    }

    public Object getContext(Class<?> var){
        Object retObject = null;
        for(Object object: serviceObject){
            if(object.getClass().equals(var)){
                retObject = object;
            }
        }
        return retObject;
    }
}
