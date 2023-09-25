package learn.java.testrepository.plain.classes;

public class LocalClass {

    // 아우터 클래스 필드
    private final static boolean staticVar = true;

    private final boolean instanceVar = true;

    // 아우터 클래스 메서드
    private boolean instanceMethod() {
        return true;
    }

    private static boolean staticMethod() {
        return true;
    }

    // 메서드의 로컬 변수로 로컬 중첩 클래스 생성
    public void method() {

        class InnerClass {
            private static boolean staticInnerVar = true;


            private boolean instanceInnerVar = true;

            // 중첩 클래스 내부 변수 접근

            public static boolean staticInnerMethod() {
                return true;
            }

            public boolean instanceInnerMethod() {
                return true;
            }

            public boolean getInnerInstanceVar() {
                return instanceInnerVar;
            }

            public boolean getInnerStaticVar() {
                return staticInnerVar;
            }

            // 아우터 클래스 멤버 접근
            public boolean getOuterInstanceVar() {
                return instanceVar;
            }

            public boolean getOuterStaticVar() {
                return staticVar;
            }

            public boolean callOuterInstanceMethod() {
                return instanceMethod();
            }

            public boolean callOuterStaticMethod() {
                return staticMethod();
            }
        }
    }
}
