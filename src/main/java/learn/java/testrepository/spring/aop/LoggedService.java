package learn.java.testrepository.spring.aop;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

@Service
public class LoggedService {

    @Logging
    public String sayHi() {
        System.out.println("hi");
        return "hi";
    }

    @Logging
    public String throwRuntimeEx() {
        System.out.println("throw RuntimeException");
        throw new RuntimeException();
    }

    @Logging
    public final String finalMethod() {
        return "final";
    }

    @Logging
    protected String protectedMethod() {
        return "protected";
    }

    @Logging
    String defaultMethod() {
        return "default";
    }

    @Logging
    String privateMethod() {
        return "private";
    }

    public void selfInvocation() {
        // 1 - target object
        // 2 - advice
        ((LoggedService) AopContext.currentProxy()).sayHi();
    }
}
