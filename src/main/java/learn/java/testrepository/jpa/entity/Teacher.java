package learn.java.testrepository.jpa.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue("T")
public class Teacher extends Person{

    private Integer age;
}
