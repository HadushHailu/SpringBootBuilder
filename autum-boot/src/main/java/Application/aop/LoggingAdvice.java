package Application.aop;

import org.autumframework.annotation.After;
import org.autumframework.annotation.Aspect;
import org.autumframework.annotation.Before;

@Aspect
public class LoggingAdvice {

    @Before(pointCut = "CustomerService.testingAop")
    public void traceBeforeMethod() {
        System.out.println("2");
        System.out.println("AOP set name method");
    }

//    @Before(pointCut = "ProductService.addProduct")
//    public void traceBeforeMethodProduct() {
//        System.out.println("~~~~~~~~~~ ProductService logging advice");
//    }

    @After(pointCut = "CustomerService.testingAop")
    public void traceAfterMethod() {
        System.out.println("4");
        System.out.println("AOP set name method:4");
    }

}
