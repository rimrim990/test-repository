package learn.java.testrepository.plain.stream;

public class UsingThis {

    public int outerField = 10;

    class Inner {
        int innerField = 20;

        void method(final int a, int b) {
            int c = 3;
            final int d = 4;
            MyFunctionalInterface fi = () -> {
                System.out.println("outerField: " + outerField);
                System.out.println("outerField: " + UsingThis.this.outerField + "\n");
                System.out.println("innerField: " + innerField);
                System.out.println("innerField: " + this.innerField);

                int result = a + b + c + d;
            };
            fi.method();
        }
    }

    public static void main(String[] args) {
        UsingThis usingThis = new UsingThis();
        UsingThis.Inner inner = usingThis.new Inner();
        inner.method(2, 3);
    }
}


