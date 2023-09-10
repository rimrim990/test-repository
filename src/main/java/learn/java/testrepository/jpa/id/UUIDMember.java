package learn.java.testrepository.jpa.id;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Getter;

@Getter
@Entity
public class UUIDMember {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
}
