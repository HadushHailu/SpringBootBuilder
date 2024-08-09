package org.autumframework.boot;

import Application.service.CustomerService;

import java.lang.reflect.Method;
import java.util.Arrays;

public class GetMethodsOfAClass {
    public static void main(String[] args) {
        Class<?> serviceOne = CustomerService.class;
        Method[] methods = serviceOne.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println(method.getName());
            System.out.println(method.getAnnotatedReturnType());
            System.out.println(method.isDefault());
        }
    }
}
