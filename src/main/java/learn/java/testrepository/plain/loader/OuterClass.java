package learn.java.testrepository.plain.loader;

public class OuterClass {

    static final String TEST01 = "I'm TEST01";

    static
    {
        System.out.println("OuterClass.정적 이니셜라이저, TEST01 = " + TEST01);
    }

    public static void main(String[] args) {
        System.out.println("OuterClass.main");
        System.out.println(Inner.obj);
    }

    static class Parent {
        static {
            System.out.println("OuterClass.Parent.정적 이니셜라이저");
        }
    }

    static class Inner extends Parent
    {
        public static String TEST02 = "I'm TEST02";
        public static final String TEST03 = "I'm TEST03";
        public static final Object obj = new Object();

        static
        {
            System.out.println("OuterClass.Inner.정적 이니셜라이저");
        }

        public static String info()
        {
            return "I'm a method in Inner";
        }
    }
}
