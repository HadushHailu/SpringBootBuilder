package Application.domain;

import org.autumframework.annotation.Service;

@Service
public class Counter implements ICounter{
    private int counterValue=0;

    public void reset() {
        counterValue=0;
    }

    public int increment(){
        return ++counterValue;
    }

    public int decrement(){
        return --counterValue;
    }
}
