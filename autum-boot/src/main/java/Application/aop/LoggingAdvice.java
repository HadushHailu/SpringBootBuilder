package Application.aop;

import org.autumframework.annotation.After;
import org.autumframework.annotation.Around;
import org.autumframework.annotation.Aspect;
import org.autumframework.annotation.Before;

import java.lang.reflect.Method;

@Aspect
public class LoggingAdvice {

    @Before(pointCut = "CustomerService.testingAop")
    public void traceBeforeMethod() {
        System.out.println("2");
        System.out.println("AOP set name method");
    }

    @Around(pointCut = "ProductService.addProduct")
    public Object traceBeforeMethodProduct(Object target, Method method, Object[] args) {
        System.out.println("~~~~~~~~~~ ProductService logging advice -- BEFORE");
        Object object = null;
        try {
            object = method.invoke(target,args);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("~~~~~~~~~~ ProductService logging advice -- AFTER");
        return object;
    }

    @After(pointCut = "CustomerService.testingAop")
    public void traceAfterMethod() {
        System.out.println("4");
        System.out.println("AOP set name method:4");
    }

}
