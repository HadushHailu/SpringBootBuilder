package Application.aop;

import org.autumframework.annotation.After;
import org.autumframework.annotation.Around;
import org.autumframework.annotation.Aspect;
import org.autumframework.annotation.Before;

import java.lang.reflect.Method;

@Aspect
public class LoggingAdvice {

    @Before(pointCut = "CustomerService.addCustomer")
    public void traceBeforeMethodAddCustomer() {
        System.out.println("LoggingAdvice: @Before CustomerService.addCustomer");
    }

    @Around(pointCut = "ProductService.addProduct")
    public Object traceAroundMethodAddProduct(Object target, Method method, Object[] args) {
        System.out.println("LoggingAdvice: @Around ProductService.addProduct -- 1");
        Object object = null;
        try {
            object = method.invoke(target,args);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("LoggingAdvice: @Around ProductService.addProduct -- 2");
        return object;
    }

    @After(pointCut = "CustomerService.findCustomer")
    public void traceAfterMethodFindCustomer() {
        System.out.println("LoggingAdvice: @After CustomerService.findCustomer");
    }

}
