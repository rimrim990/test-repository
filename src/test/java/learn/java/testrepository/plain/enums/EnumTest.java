package learn.java.testrepository.plain.enums;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("자바 열거 클래스 학습 테스트")
class EnumTest {

    @Test
    @DisplayName("외부에서 열거 클래스의 생성자에 접근 불가능하다")
    void enum_constructor() {
        // Operation 은 abstract 이며 인스턴스화 할 수 없습니다
        // Operation operation = new Operation("^");
    }

    @Test
    @DisplayName("열거 클래스의 각 상수 필드들은 싱글턴이다")
    void enum_singleton() {
        // given
        final Operation firstPlus = Operation.PLUS;
        final Operation secondPlus = Operation.PLUS;

        // when & then
        assertThat(firstPlus).isEqualTo(secondPlus);
    }

    @Test
    @DisplayName("열거 클래스의 toString 은 필드 이름을 출력한다")
    void enum_toString() {
        // when & then
        assertThat(Operation.PLUS.toString()).isEqualTo("PLUS");
    }

    @Test
    @DisplayName("열거 클래스를 흉내낸 OperationEnum 클래스 테스트")
    void enum_mimic() {
        // given
        final OperationEnum firstPlus = OperationEnum.PLUS;
        final OperationEnum secondPlus = OperationEnum.PLUS;

        // when & then
        assertThat(firstPlus).isEqualTo(secondPlus);
    }
}