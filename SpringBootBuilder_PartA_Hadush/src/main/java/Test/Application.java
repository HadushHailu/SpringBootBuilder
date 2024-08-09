package Test;

import org.autumframework.context.FWContext;

public class Application {
    public static void main(String[] args) {
        FWContext fWContext = new FWContext("Test");
        fWContext.start();
    }
}
