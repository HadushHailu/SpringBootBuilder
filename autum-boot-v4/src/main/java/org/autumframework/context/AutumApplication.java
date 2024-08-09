package org.autumframework.context;

import Application.Application;
import org.autumframework.annotation.Autowired;
import org.autumframework.annotation.Qualifier;
import org.autumframework.annotation.Scheduled;
import org.autumframework.annotation.Service;
import org.reflections.Reflections;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class AutumApplication {
    private static List<Object> serviceObject = new ArrayList<>();
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

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
            for (int j = 0; j < serviceObject.size(); j++) {
                Object theServiceClass = serviceObject.get(j);

                //Constructors
                for (Constructor<?> constructor : theServiceClass.getClass().getDeclaredConstructors()) {
                    System.out.println("Constructor: "+constructor.getName() + " "+constructor.getParameterTypes().length);
                    if (constructor.isAnnotationPresent(Autowired.class)) {
                        // Get parameter types of the constructor
                        Class<?>[] parameterTypes = constructor.getParameterTypes();
                        Object[] parameterInstances = new Object[parameterTypes.length];

                        // For each parameter, find the corresponding bean
                        for (int i = 0; i < parameterTypes.length; i++) {
                            parameterInstances[i] = this.getServiceBeanOfType(parameterTypes[i]);
                            System.out.println(parameterInstances[i]);
                        }

                        // Make the constructor accessible and create a new instance
                        constructor.setAccessible(true);
                        try {
                            System.out.println("creating new instance");
                            Object newInstance = constructor.newInstance(parameterInstances);
                            serviceObject.set(j, newInstance);
                            theServiceClass = newInstance;
                        } catch (Exception e) {
                            e.printStackTrace(); // Handle exceptions appropriately
                        }
                    }
                }

                //Fields
                for (Field field : theServiceClass.getClass().getDeclaredFields()) {
                    Annotation[] annotations=field.getAnnotations();
                    if (field.isAnnotationPresent(Autowired.class)) {
                        Class<?> theFieldType = field.getType();

                        String qualifierValue = "";

                        for(Annotation ant: annotations){
                            if(ant.annotationType().getSimpleName().equals(Qualifier.class.getSimpleName())){
                                qualifierValue = ((Qualifier)ant).value();
                            }
                        }
                        if(!qualifierValue.isEmpty()){
                            Object instance = this.getQualifierBeanOfType(theFieldType, qualifierValue);
                            field.setAccessible(true);
                            field.set(theServiceClass, instance);
                        }else{
                            Object instance = this.getServiceBeanOfType(theFieldType);
                            field.setAccessible(true);
                            field.set(theServiceClass, instance);
                        }
                    }
                }

                //Methods
                for(Method method: theServiceClass.getClass().getDeclaredMethods()){
                    if(method.isAnnotationPresent(Autowired.class)){
                        // Get parameter types of the method
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        Object[] parameterInstances = new Object[parameterTypes.length];
                        // For each parameter, find the corresponding bean
                        for (int i = 0; i < parameterTypes.length; i++) {
                            parameterInstances[i] = this.getServiceBeanOfType(parameterTypes[i]);
                        }
                        // Make the method accessible and invoke it with the parameters
                        method.setAccessible(true);
                        method.invoke(theServiceClass, parameterInstances);
                    }

                    final Object theFinalObject = theServiceClass;
                    if (method.isAnnotationPresent(Scheduled.class)) {
                        Scheduled scheduledAnnotation = method.getAnnotation(Scheduled.class);
                        long fixedRate = scheduledAnnotation.fixedRate();
                        scheduler.scheduleAtFixedRate(() -> {
                            try {
                                method.invoke(theFinalObject);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }, 0, fixedRate, TimeUnit.MILLISECONDS);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getQualifierBeanOfType(Class<?> interfaceClass, String qualifierValue){
        Object service = null;
        try {
            for (Object theClass : serviceObject) {
                Class<?>[] interfaces = theClass.getClass().getInterfaces();
                for (Class<?> theInterface : interfaces) {
                    if (theInterface.getName().contentEquals(interfaceClass.getName()) && theClass.getClass().getSimpleName().equals(qualifierValue)){
                            service = theClass;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return service;
    }
    public Object getServiceBeanOfType(Class<?> interfaceClass){
        System.out.println(interfaceClass);
        Object service = null;
        boolean matchingInterfaceFound = false;
        try {
            for (Object theClass : serviceObject) {
                Class<?>[] interfaces = theClass.getClass().getInterfaces();
                for (Class<?> theInterface : interfaces) {
                    if (theInterface.getName().contentEquals(interfaceClass.getName())){
                        System.out.println(theInterface.getName());
                        if(matchingInterfaceFound){
                            throw new RuntimeException("Multiple interface of " + interfaceClass.getName() + " found");
                        }
                        else{
                            service = theClass;
                            matchingInterfaceFound = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return service;
    }

    public void run(Class<? extends Runnable> clazz, String... args) {
        try {
            // Create an instance of the Runnable class
            Runnable runnableInstance = clazz.getDeclaredConstructor().newInstance();

            // Perform dependency injection for the Runnable instance
            this.performDependencyInjection();

            // Inject dependencies into the Runnable instance
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    field.setAccessible(true);
                    Object bean = this.getServiceBeanOfType(field.getType());
                    field.set(runnableInstance, bean);
                }
            }

            // Start a new thread with the Runnable instance
            Thread thread = new Thread(runnableInstance);
            thread.start();
            thread.join(); // Wait for the thread to complete
        } catch (Exception e) {
            e.printStackTrace();
        }
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
