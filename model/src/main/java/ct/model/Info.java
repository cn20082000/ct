package ct.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "tblInfo")
public class Info implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "createAt")
    private LocalDateTime createAt;

    @Column(name = "version")
    private String version;

    public Info() {
        super();
    }

    public Info(Long id, String name, LocalDateTime createAt, String version) {
        super();
        this.id = id;
        this.name = name;
        this.createAt = createAt;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Info clone() {
        return new Info(id, name, createAt, version);
    }
}
