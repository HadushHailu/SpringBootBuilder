package org.autumframework.boot;

import Application.service.CustomerService;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class GetAnnotations {
    public static void main(String[] args) {
        Class<?> serviceOneClass = CustomerService.class;
        Annotation[] annotations=serviceOneClass.getAnnotations();
        for (Annotation annotation : annotations){
            System.out.println("Class with name "+serviceOneClass.getName()+" has annotation: "+annotation);
        }

        Field[] fields = serviceOneClass.getDeclaredFields();
        for (Field field : fields) {
            Annotation[] fieldAnnotations=field.getAnnotations();
            for (Annotation annotation : fieldAnnotations){
                System.out.println("Field with name "+field.getName()+" has annotation: "+annotation);
            }
        }
        Method[] methods = serviceOneClass.getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] methodAnnotations=method.getAnnotations();
            for (Annotation annotation : methodAnnotations){
                System.out.println("Method with name "+method.getName()+" has annotation: "+annotation);
            }
        }
    }
}
