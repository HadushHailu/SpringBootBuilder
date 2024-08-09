package org.autumframework.boot;

import Application.service.CustomerService;

import java.lang.reflect.InvocationTargetException;

public class InstantiateAClass {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException,
            NoSuchMethodException, InvocationTargetException {
        Class<?> serviceOneClass = CustomerService.class;
        CustomerService serviceOneObject = (CustomerService) serviceOneClass.getDeclaredConstructor().newInstance();
        System.out.println(serviceOneObject.getName("Hadush", 23));
    }
}
