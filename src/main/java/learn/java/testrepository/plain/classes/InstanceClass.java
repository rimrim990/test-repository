package learn.java.testrepository.plain.classes;

public class InstanceClass {

    private final static boolean staticVar = true;

    private final boolean instanceVar = true;

    private boolean instanceMethod() {
        InnerClass inner = new InnerClass();
        return true;
    }

    private static boolean staticMethod() {
        // InnerClass inner = new InnerClass();
        return true;
    }

    // 클래스의 멤버로 중첩 클래스 생성
    class InnerClass {

        private static boolean staticInnerVar = true;

        private boolean instanceInnerVar = true;

        public static boolean staticInnerMethod() {
            return true;
        }

        public boolean instanceInnerMethod() {
            return true;
        }

        public boolean getOuterInstanceVar() {
            return instanceVar;
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

        public boolean callOuterInstanceMethod() {
            return instanceMethod();
        }

        public boolean callOuterStaticMethod() {
            return staticMethod();
        }
    }
}
