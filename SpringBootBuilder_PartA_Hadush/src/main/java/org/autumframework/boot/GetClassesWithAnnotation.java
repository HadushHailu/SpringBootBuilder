package org.autumframework.boot;

import org.autumframework.annotation.Service;
import org.reflections.Reflections;

import java.util.Set;

public class GetClassesWithAnnotation {
    public static void main(String[] args ){
        Reflections reflections = new Reflections("Application");
        Set<Class<?>> serviceClasses = reflections.getTypesAnnotatedWith(Service.class);
        for (Class<?> serviceClass : serviceClasses) {
            System.out.println(serviceClass.getName());
        }
    }
}
