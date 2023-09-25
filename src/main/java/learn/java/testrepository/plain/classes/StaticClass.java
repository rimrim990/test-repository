package learn.java.testrepository.plain.classes;

public class StaticClass {

    private final boolean instanceVar = true;
    private final static boolean staticVar = true;

    private boolean instanceMethod() {
        InnerClass inner = new InnerClass();
        return true;
    }

    private static boolean staticMethod() {
        InnerClass inner = new InnerClass();
        return true;
    }

    // 클래스의 정적 멤버로 중첩 클래스 생성
    static class InnerClass {

        private static boolean staticInnerVar = true;

        private boolean instanceInnerVar = true;

        public static boolean staticInnerMethod() {
            return true;
        }

        public boolean instanceInnerMethod() {
            return true;
        }

        public boolean getOuterStaticVar() {
            return staticVar;
        }

        public boolean getInnerInstanceVar() {
            return instanceInnerVar;
        }

        public boolean getInnerStaticVar() {
            return staticInnerVar;
        }

        public boolean callOuterStaticMethod() {
            return staticMethod();
        }
    }
}
