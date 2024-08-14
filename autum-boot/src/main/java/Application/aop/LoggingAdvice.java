package Application.aop;

import org.autumframework.annotation.After;
import org.autumframework.annotation.Around;
import org.autumframework.annotation.Aspect;
import org.autumframework.annotation.Before;

import java.lang.reflect.Method;

@Aspect
public class LoggingAdvice {

    @Before(pointCut = "CustomerService.addCustomer")
    public void traceBeforeMethod() {
        System.out.println("AOP: @Before CustomerService.addCustomer");
    }

    @Around(pointCut = "ProductService.addProduct")
    public Object traceBeforeMethodProduct(Object target, Method method, Object[] args) {
        System.out.println("AOP: @Around ProductService.addProduct -- 1");
        Object object = null;
        try {
            object = method.invoke(target,args);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("AOP: @Around ProductService.addProduct --2");
        return object;
    }

}
