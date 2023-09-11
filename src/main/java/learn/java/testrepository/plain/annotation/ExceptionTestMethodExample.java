package learn.java.testrepository.plain.annotation;

public class ExceptionTestMethodExample {

    @ExceptionTestMethod(RuntimeException.class)
    public static void method1() {
        throw new RuntimeException("method1 throws runtimeException");
    }

    @ExceptionTestMethod(IllegalStateException.class)
    public void method2() {
    }

    @ExceptionTestMethod(ArrayStoreException.class)
    public static void method3() {
    }

    @ExceptionTestMethod(IllegalArgumentException.class)
    public static void method4() {
        throw new UnsupportedOperationException();
    }
}
