package learn.java.testrepository.jpa.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@ToString
public class Class {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    //@BatchSize(size=10)
    @OneToMany(mappedBy = "studentClass", cascade = CascadeType.ALL)
    List<Student> students = new ArrayList<>();

    public Class(String name) {
        this.name = name;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public void addStudent(List<Student> students) {
        for (Student student : students) {
            this.students.add(student);
            student.setStudentClass(this);
        }
    }

    public Class() {
    }
}
