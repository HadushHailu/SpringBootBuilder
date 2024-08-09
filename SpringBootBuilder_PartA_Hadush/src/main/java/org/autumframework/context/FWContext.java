package org.autumframework.context;

import org.autumframework.annotation.*;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class FWContext {
    private static List<Object> testObjectMap = new ArrayList<>();
    private static List<Object> serviceObjectMap = new ArrayList<>();

    private void performDI() {
        try {
            for (Object theTestClass : testObjectMap) {
                for (Field field : theTestClass.getClass().getDeclaredFields()) {
                    if (field.isAnnotationPresent(Inject.class)) {
                        System.out.println(field);
                        System.out.println(field.getName());
                        System.out.println(field.getType());
                        Class<?> theFieldType = field.getType();
                        Object instance = this.getServiceBeanOftype(theFieldType);
                        field.setAccessible(true);
                        field.set(theTestClass, instance);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getServiceBeanOftype(Class interfaceClass){
        Object service = null;
        try {
            for (Object theClass : serviceObjectMap) {
                Class<?>[] interfaces = theClass.getClass().getInterfaces();
                System.out.println(Arrays.stream(Arrays.stream(interfaces).toArray()).toList());
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

    public FWContext(String prefix) {
        try {
            Reflections reflections = new Reflections(prefix);
            Set<Class<?>> testTypes = reflections.getTypesAnnotatedWith(TestClass.class);
            for (Class<?> implementationClass : testTypes) {
                testObjectMap.add((Object) implementationClass.getDeclaredConstructor().newInstance());
            }

            Reflections reflections1 = new Reflections("Application");
            Set<Class<?>> serviceTypes = reflections1.getTypesAnnotatedWith(Service.class);
            for (Class<?> implementationClass : serviceTypes) {
                serviceObjectMap.add((Object) implementationClass.getDeclaredConstructor().newInstance());
            }

            this.performDI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            for (Object theTestClass : testObjectMap) {
                List<Method> beforeMethod = new ArrayList<>();

                for (Method method : theTestClass.getClass().getDeclaredMethods()) {
                    if (method.isAnnotationPresent(Before.class)) {
                        beforeMethod.add(method);
                    }
                }

                for (Method method : theTestClass.getClass().getDeclaredMethods()) {

                    if (method.isAnnotationPresent(Test.class)) {
                       if(!beforeMethod.isEmpty()){
                           for(Method method1: beforeMethod)
                               method1.invoke(theTestClass);
                       }
                        method.invoke(theTestClass);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
