package learn.java.testrepository.jpa.id;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("스프링 데이터 JPA 기본키 생성 전략 테스트")
class IdMemberTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @DisplayName("Identity 방식 기본키 생성 전략")
    void identity_pk() {
        // given
        IdentityMember member = new IdentityMember();

        // when & then
        entityManager.persist(member);
    }

    @Test
    @DisplayName("Identity 전략은 INSERT 쿼리가 수행되는 즉시 날린다")
    void identity_pk_query() {
        // given
        IdentityMember member = new IdentityMember();

        // when
        System.out.println("----INSERT----");
        entityManager.persist(member);

        System.out.println("----SELECT----");
        IdentityMember findMember = entityManager.find(IdentityMember.class, member.getId());
    }

    @Test
    @DisplayName("Sequence 방식 기본키 생성 전략")
    void sequence_pk() {
        // given
        SequenceMember member = new SequenceMember();

        // when
        entityManager.persist(member);
        entityManager.flush();
    }

    @Test
    @DisplayName("Sequence 방식 기본키 생성 전략은 시퀀스를 50개씩 미리 가져온다")
    void sequence_pk_allocate() {
        // given
        SequenceMember firstMember = new SequenceMember();
        SequenceMember secondMember = new SequenceMember();
        SequenceMember thirdMember = new SequenceMember();
        SequenceMember fourthMember = new SequenceMember();

        // when
        entityManager.persist(firstMember);
        System.out.println("----FIRST PERSISTED-----");

        entityManager.persist(secondMember);
        Integer nextValue = getNextValue();
        System.out.println("----SECOND PERSISTED-----");

        entityManager.persist(thirdMember);
        System.out.println("----THIRD PERSISTED-----");

        entityManager.persist(fourthMember);
        System.out.println("----FOURTH PERSISTED-----");

        entityManager.flush();

        // then
        assertThat(nextValue).isEqualTo(101);
    }

    @Test
    @DisplayName("Table 방식 기본키 생성 전략")
    void table_pk() {
        // given
        TableMember member = new TableMember();

        // when
        entityManager.persist(member);
        entityManager.flush();
    }

    @Test
    @DisplayName("Sequence 방식 기본키 생성 전략은 시퀀스를 50개씩 미리 가져온다")
    void table_pk_allocate() {
        // given
        TableMember firstMember = new TableMember();
        TableMember secondMember = new TableMember();
        TableMember thirdMember = new TableMember();
        TableMember fourthMember = new TableMember();

        // when
        entityManager.persist(firstMember);
        System.out.println("----FIRST PERSISTED-----");

        entityManager.persist(secondMember);
        System.out.println("----SECOND PERSISTED-----");

        entityManager.persist(thirdMember);
        System.out.println("----THIRD PERSISTED-----");

        entityManager.persist(fourthMember);
        System.out.println("----FOURTH PERSISTED-----");

        entityManager.flush();
    }

    @Test
    @DisplayName("UUID AUTO 키 생성전략")
    void uuid_auto() {
        // given
        UUIDMember member = new UUIDMember();

        // when
        entityManager.persist(member);
        entityManager.flush();

        System.out.println(member.getId());
    }

    private Integer getNextValue() {
        return (Integer) entityManager.createNativeQuery(
                "select next value for sequence_member_seq", Integer.class)
            .getSingleResult();
    }

    private Integer getNextTableValue() {
        return (Integer) entityManager.createNativeQuery(
                "select tbl.next_val from hibernate_sequences tbl where tbl.sequence_name = :tableName", Integer.class)
            .setParameter("tableName", "default")
            .getSingleResult();
    }
}