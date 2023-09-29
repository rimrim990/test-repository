package learn.java.testrepository.jpa.lock;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;

@Entity
public class VersionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Version
    private Integer version;

    public Long getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }

    public VersionEntity(final String title) {
        this.title = title;
    }

    public VersionEntity() {
    }

    public void updateTitle(final String title) {
        this.title = title;
    }
}
