package org.autumframework.context;

import Application.dao.IProductDAO;
import Application.dao.ProductDAO;
import Application.service.CustomerService;
import Application.service.ICustomerService;
import Application.service.IProductService;
import org.autumframework.annotation.EventListener;
import org.autumframework.annotation.*;
import org.autumframework.aspect.ApplicationAspect;
import org.autumframework.customClass.CustomMethod;
import org.autumframework.event.ApplicationEvent;
import org.autumframework.loader.PropertyLoader;
import org.autumframework.threading.FixedRateScheduler;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class AutumApplication {
    private static List<Object>        serviceObject = new ArrayList<>();
    private static Map<Object, Map<Object, CustomMethod>> events = new HashMap<>();
    private static Map<Object, Map<Method,Object>> aopMethods = new HashMap<>();
    private static List<Object>        aopClasses = new ArrayList<>();

    public AutumApplication(String prefix){
        //Add all classes with @Service annotation
        try {
            String propertyValue = PropertyLoader.getConfigProperty("profiles", "active");
            Reflections reflections = new Reflections(prefix);
            Set<Class<?>> ImplServiceObjectType = reflections.getTypesAnnotatedWith(Service.class);
            for (Class<?> implementationClass : ImplServiceObjectType) {
                if(implementationClass.isAnnotationPresent(Profile.class)){
                    Profile profileType = implementationClass.getAnnotation(Profile.class);
                    if(propertyValue.equals(profileType.value())){
                        serviceObject.add((Object) implementationClass.getDeclaredConstructor().newInstance());
                    }
                }else{
                    serviceObject.add((Object) implementationClass.getDeclaredConstructor().newInstance());
                }
            }


            ImplServiceObjectType = reflections.getTypesAnnotatedWith(ConfigurationProperties.class);
            for (Class<?> implementationClass : ImplServiceObjectType) {
                ConfigurationProperties configProps=implementationClass.getAnnotation(ConfigurationProperties.class);
                String prefixData=configProps.prefix();
                System.out.println("[****] TEST1: "+prefixData);
                Object instance = implementationClass.getDeclaredConstructor().newInstance();
                serviceObject.add(instance);
                injectConfigurationProperties(instance, prefixData);
            }

            //AOP annotation Class getting
            ImplServiceObjectType = reflections.getTypesAnnotatedWith(Aspect.class);
            for (Class<?> implementationClass : ImplServiceObjectType) {
                aopClasses.add((Object) implementationClass.getDeclaredConstructor().newInstance());
            }
            for (Object aopObj: aopClasses){
                Method[] methods=aopObj.getClass().getDeclaredMethods();
                Map<Method,Object> methodMap=null;

                for(Method method: methods){

                    //Before Class
                    if(method.isAnnotationPresent(Before.class)){
                        Before annotation=method.getAnnotation(Before.class);
                        System.out.println("----aop-------"+annotation.pointCut());
                        methodMap=aopMethods.get(annotation.pointCut());
                        if(methodMap==null){
                            methodMap=new HashMap<>();
                        }
                        methodMap.put(method,aopObj);
                        aopMethods.put(annotation.pointCut(), methodMap);
                    }

                    //After Class
                    else if(method.isAnnotationPresent(After.class)){
                        After annotation=method.getAnnotation(After.class);
                        System.out.println("----aop-------"+annotation.pointCut());
                        methodMap=aopMethods.get(annotation.pointCut());
                        if(methodMap==null){
                            methodMap=new HashMap<>();
                        }
                        methodMap.put(method,aopObj);
                        aopMethods.put(annotation.pointCut(), methodMap);
                    }
                    System.out.println("<<<<<<<@>>>>>>> aopMethods="+aopMethods);
                }
            }

            reflections = new Reflections("org.autumframework");
            ImplServiceObjectType = reflections.getTypesAnnotatedWith(Service.class);
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
                    if (field.isAnnotationPresent(Value.class)) {
                        valueInjection(field,theServiceClass);
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
                        long fixedRate;
                        String schedulerType = "";

                        if(scheduledAnnotation.cron().isEmpty()){
                            fixedRate = scheduledAnnotation.fixedRate();
                            schedulerType = "fixedRate";
                        }else{
                            fixedRate = convertCronToPeriod(scheduledAnnotation.cron());
                            schedulerType = "cron";
                        }
                        System.out.println("SCHEDULER THREADING: type="+schedulerType+" period="+fixedRate);
                        FixedRateScheduler fixedRateScheduler = new FixedRateScheduler(theFinalObject, method, fixedRate);
                        new Thread(fixedRateScheduler).start();
                    }

                    if(method.isAnnotationPresent(EventListener.class)){
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        for (Class<?> paramType : parameterTypes) {

                            Map<Object,CustomMethod> methods= events.get(paramType.getName());
                            if(methods == null){
                                methods= new HashMap<>();
                            }

                            //Does the method have @Async
                            if(method.isAnnotationPresent(Async.class)){
                                methods.put(theServiceClass, new CustomMethod(method, true));
                            }else{
                                methods.put(theServiceClass, new CustomMethod(method, false));
                            }

                            events.put(paramType.getName(), methods);
                        }
                    }

                    // Proxy Calling
                    Map<Method,Object> aopMethodCaching = aopMethods.get(theServiceClass.getClass().getSimpleName() + "." + method.getName());
                    if(aopMethodCaching != null){
                        Object proxy=createProxy(theServiceClass,method);
                        serviceObject.set(j, proxy);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ApplicationEvent event= new ApplicationEvent();
        event.setEvents(events);
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
                if(interfaceClass.getName().equals(theClass.getClass().getName())){
                    service = theClass;
                    return service;
                }
                Class<?>[] interfaces = theClass.getClass().getInterfaces();
                for (Class<?> theInterface : interfaces) {
                    if (theInterface.getName().contentEquals(interfaceClass.getName())){
                        System.out.println("+++++++++++++ Matching Interface="+theInterface.getName()+" corresponding class="+theClass.getClass());
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

            // Inject dependencies into the Runnable instance
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    field.setAccessible(true);
                    Object bean = this.getServiceBeanOfType(field.getType());
                    field.set(runnableInstance, bean);
                }
                if (field.isAnnotationPresent(Value.class)) {
                    valueInjection(field,runnableInstance);
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
    private void injectConfigurationProperties(Object bean, String prefix) throws IllegalAccessException {
        for (Field field : bean.getClass().getDeclaredFields()) {

            String propertyName = null;
            if (field.isAnnotationPresent(Name.class)) {
                Name valueAnnotation = field.getAnnotation(Name.class);
                propertyName = valueAnnotation.value();
            }else {
                propertyName = field.getName();
            }
            System.out.println("[+++++++] Inject Configuration Properites: prefix="+prefix+" propertyName="+propertyName);
            String propertyValue = PropertyLoader.getConfigProperty(prefix, propertyName);

            if (propertyValue != null) {
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                Object convertedValue = convertValue(propertyValue, fieldType);
                field.set(bean, convertedValue);
            }

        }
    }
    private void valueInjection(Field field, Object service) throws IllegalAccessException {
        if (field.isAnnotationPresent(Value.class)) {
            Value valueAnnotation = field.getAnnotation(Value.class);
            String propertyKey = valueAnnotation.value();
            String propertyValue = PropertyLoader.getProperty(propertyKey);

            if (propertyValue != null) {
                field.setAccessible(true);
                Class<?> fieldType = field.getType();

                // Handle conversion if necessary
                Object convertedValue = convertValue(propertyValue, fieldType);

                field.set(service, convertedValue);
            }
        }
    }
    private Object convertValue(String propertyValue, Class<?> targetType) {
        if (targetType == String.class) {
            return propertyValue;
        } else if (targetType == int.class || targetType == Integer.class) {
            return Integer.parseInt(propertyValue);
        } else if (targetType == boolean.class || targetType == Boolean.class) {
            return Boolean.parseBoolean(propertyValue);
        }
        // Add more conversions as needed
        return null;
    }
    public static long convertCronToPeriod(String cron) {
        String[] parts = cron.split(" ");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid cron expression: " + cron);
        }

        int seconds = Integer.parseInt(parts[0]);  // Seconds part (first field)
        int minutes = Integer.parseInt(parts[1]);  // Minutes part (second field)

        // Calculate period in milliseconds
        //System.out.println(" [CRON] seconds="+seconds+" minutes="+minutes);
        return (TimeUnit.MINUTES.toSeconds(minutes) + seconds)*1000;
    }

    //Proxy for AOP
    private Class<?> findInterfaceFromClass(Object theServiceClass){
        System.out.println();
        ICustomerService iCustomerService = new CustomerService();
        return ICustomerService.class;

    }
    private Object createProxy(Object theServiceClass, Method method){
        Map<Method,Object> aopMethodCaching = aopMethods.get(theServiceClass.getClass().getSimpleName() + "." + method.getName());

        Class<?> theInterface = this.findInterfaceFromClass(theServiceClass);
        Object           instance         = this.getServiceBeanOfType(theServiceClass.getClass());
        System.out.println(":-::-: "+instance.getClass() +","+theServiceClass.getClass() );
        Object proxyInstance=  Proxy.newProxyInstance(theInterface.getClassLoader(),
                                                      new Class[]{theInterface},
                                                      new ApplicationAspect(theServiceClass, aopMethodCaching));

        return proxyInstance;
    }

}
