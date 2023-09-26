package learn.java.testrepository.spring.aop;

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
}
