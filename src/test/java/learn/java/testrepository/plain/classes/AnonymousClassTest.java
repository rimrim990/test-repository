package learn.java.testrepository.plain.classes;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("익명 객체 학습 테스트")
public class AnonymousClassTest {

    @Test
    @DisplayName("클래스를 상속한 익명 객체 선언이 가능하다")
    void anonymousClassInstance() {
        // when
        OuterClass anonymous = new OuterClass() {
            @Override
            public boolean getValue() {
                return true;
            }
        };

        // then
        assertThat(anonymous.getValue()).isTrue();
    }

    @Test
    @DisplayName("익명 자식 객체는 부모 타입으로 선언되기 때문에 내부에 정의된 멤버는 외부에서 접근 불가능하다")
    void anonymousClassInstance_variableScope() {
        // when
        OuterClass anonymous = new OuterClass() {
            @Override
            public boolean getValue() {
                return true;
            }

            public boolean getAnonymousValue() {
                return true;
            }
        };

        // then
        assertThat(anonymous.getValue()).isTrue();
        // assertThat(anonymous.getAnonymousValue()).isTrue();
    }

    @Test
    @DisplayName("인터페이스를 상속한 익명 객체 선언이 가능하다")
    void anonymousInterfaceInstance() {
        // when
        OuterInterface anonymous = new OuterInterface() {
            @Override
            public boolean getValue() {
                return true;
            }
        };

        // then
        assertThat(anonymous.getValue()).isTrue();
    }

    @Test
    @DisplayName("익명 자식 객체는 인터페이스 타입으로 선언되기 때문에 내부에 정의된 멤버는 외부에서 접근 불가능하다")
    void anonymousInterfaceInstance_variableScope() {
        // when
        OuterClass anonymous = new OuterClass() {
            @Override
            public boolean getValue() {
                return true;
            }

            public boolean getAnonymousValue() {
                return true;
            }
        };

        // then
        assertThat(anonymous.getValue()).isTrue();
        // assertThat(anonymous.getAnonymousValue()).isTrue();
    }

    private class OuterClass {
        public boolean getValue() {
            return false;
        }
    }

    private interface OuterInterface {
        boolean getValue();
    }
}
