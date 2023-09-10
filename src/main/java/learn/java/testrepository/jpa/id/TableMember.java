package learn.java.testrepository.jpa.id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TableMember {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
}
