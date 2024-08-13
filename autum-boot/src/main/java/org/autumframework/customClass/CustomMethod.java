package org.autumframework.customClass;

import java.lang.reflect.Method;

public class CustomMethod {
    private Method method;
    private boolean isAsync;

    public CustomMethod(Method method, boolean isAsync) {
        this.isAsync = isAsync;
        this.method = method;
    }

    public boolean isAsync() {
        return isAsync;
    }

    public void setAsync(boolean async) {
        isAsync = async;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
