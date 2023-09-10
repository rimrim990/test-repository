package learn.java.testrepository.jpa.id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class IdentityMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
