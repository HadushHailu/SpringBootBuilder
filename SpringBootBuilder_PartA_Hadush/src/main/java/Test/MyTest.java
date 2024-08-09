package Test;

import Application.domain.Counter;
import Application.domain.ICounter;
import org.autumframework.annotation.Before;
import org.autumframework.annotation.Inject;
import org.autumframework.annotation.Test;
import org.autumframework.annotation.TestClass;
import  static org.autumframework.test.Assert.*;

@TestClass
public class MyTest {
    @Inject
    ICounter counter;

    @Before
    public void init(){
        counter.reset();
    }
    @Test
    public void testIncrement(){
       assertEquals(counter.increment(), 1);
       assertEquals(counter.increment(), 2);
    }

    @Test
    public void testDecrement(){
        assertEquals(counter.decrement(), -1);
        assertEquals(counter.decrement(), -2);
    }
}
