package learn.java.testrepository.plain.annotation;

public class TestMethodExample {

    @TestMethod
    public static void method1() {
        throw new RuntimeException("method1 throws runtimeException");
    }

    @TestMethod
    public void method2() {
    }

    @TestMethod
    public static void method3() {
    }

    public void method4() {
    }
}
