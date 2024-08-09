package org.autumframework.boot;

import Application.service.CustomerService;

import java.lang.reflect.Field;

public class GetFieldsOfAClass {
    public static void main(String[] args ){
        Class<?> serviceOne = CustomerService.class;
        Field[] fields = serviceOne.getDeclaredFields();
        for (Field field : fields) {
            System.out.println(field.getName());
            System.out.println(field.accessFlags());
            System.out.println(field.getAnnotatedType());
            System.out.println(field.getAnnotations().length);
            System.out.println(field.getType());
        }
    }
}
