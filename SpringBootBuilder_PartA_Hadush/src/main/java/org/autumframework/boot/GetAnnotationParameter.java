package org.autumframework.boot;

import Application.service.CustomerService;
import org.autumframework.annotation.Service;

import java.lang.annotation.Annotation;

public class GetAnnotationParameter {
    public static void main(String[] args) {
        Class<?> serviceOneClass = CustomerService.class;
        Annotation[] annotations=serviceOneClass.getAnnotations();
        for (Annotation annotation : annotations){
            System.out.println("Class with name "+serviceOneClass.getName()+" has annotation: "+annotation);
            System.out.println("This annotation: "+annotation+" has a 'name' parameter with value: "+((Service)annotation).name());
        }
    }
}
