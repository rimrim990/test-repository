package learn.java.testrepository.jpa.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue("S")
public class Student extends Person{

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private Class studentClass;

    public Student(String name, String location) {
        this.name = name;
        this.setLocation(location);
    }

    public void setStudentClass(Class studentClass) {
        this.studentClass = studentClass;
    }

    public Student() {
    }
}
