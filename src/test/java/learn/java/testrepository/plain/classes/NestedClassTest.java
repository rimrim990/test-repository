package learn.java.testrepository.plain.classes;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("자바 중첩 클래스 학습 테스트")
class NestedClassTest {

    @Test
    @DisplayName("인스턴스 멤버 클래스는 외부 클래스의 모든 필드에 접근 가능하다")
    void instance_accessAllOuterClassVar() {
        // given
        InstanceClass outer = new InstanceClass();

        // when
        InstanceClass.InnerClass inner = outer.new InnerClass();

        // then
        assertThat(inner.getOuterInstanceVar()).isTrue();
        assertThat(inner.getOuterStaticVar()).isTrue();
        assertThat(inner.callOuterInstanceMethod()).isTrue();
        assertThat(inner.callOuterStaticMethod()).isTrue();
    }

    @Test
    @DisplayName("인스턴스 멤버 클래스는 모든 멤버를 선언할 수 있다")
    void instance_canDeclareAllMember() {
        // given
        InstanceClass outer = new InstanceClass();

        // when
        InstanceClass.InnerClass inner = outer.new InnerClass();

        // then
        assertThat(inner.getInnerInstanceVar()).isTrue();
        assertThat(inner.getInnerStaticVar()).isTrue();
        assertThat(inner.instanceInnerMethod()).isTrue();
        assertThat(InstanceClass.InnerClass.staticInnerMethod()).isTrue();
    }

    @Test
    @DisplayName("정적 멤버 클래스는 외부 클래스의 정적 필드만 접근 가능하다")
    void static_accessOnlyStaticOuterClassVar() {
        // when
        StaticClass.InnerClass inner = new StaticClass.InnerClass();

        // then
        assertThat(inner.getOuterStaticVar()).isTrue();
        assertThat(inner.callOuterStaticMethod()).isTrue();
    }

    @Test
    @DisplayName("정적 멤버 클래스는 모든 멤버를 선언할 수 있다")
    void static_canDeclareAllMember() {
        // when
        StaticClass.InnerClass inner = new StaticClass.InnerClass();

        // then
        assertThat(inner.getInnerInstanceVar()).isTrue();
        assertThat(inner.getInnerStaticVar()).isTrue();
        assertThat(inner.instanceInnerMethod()).isTrue();
        assertThat(StaticClass.InnerClass.staticInnerMethod()).isTrue();
    }

    @Test
    @DisplayName("로컬 클래스는 모든 멤버를 선언할 수 있다")
    void local_canDeclareAllMember() {
        // when
        StaticClass.InnerClass inner = new StaticClass.InnerClass();

        // then
        assertThat(inner.getInnerInstanceVar()).isTrue();
        assertThat(inner.getInnerStaticVar()).isTrue();
        assertThat(inner.instanceInnerMethod()).isTrue();
        assertThat(StaticClass.InnerClass.staticInnerMethod()).isTrue();
    }
}