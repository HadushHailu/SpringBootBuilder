package Application;

import org.autumframework.annotation.After;
import org.autumframework.annotation.Aspect;
import org.autumframework.annotation.Before;

@Aspect
public class AopClass {

    @Before(pointCut = "CustomerService.testingAop")
    public void traceBeforeMethod() {
        System.out.println("2");
        System.out.println("AOP set name method");
    }

    @After(pointCut = "CustomerService.testingAop")
    public void traceAfterMethod() {
        System.out.println("4");
        System.out.println("AOP set name method:4");
    }

}
