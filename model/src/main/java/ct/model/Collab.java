package ct.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tblCollab")
public class Collab implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User toUser;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project fromProject;

    @Column(name = "status")
    private Boolean accept;

    public Collab() {
        super();
    }

    public Collab(Long id, User toUser, Project fromProject, boolean isAccept) {
        super();
        this.id = id;
        this.toUser = toUser;
        this.fromProject = fromProject;
        this.accept = isAccept;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public Project getFromProject() {
        return fromProject;
    }

    public void setFromProject(Project fromProject) {
        this.fromProject = fromProject;
    }

    public boolean isAccept() {
        return accept;
    }

    public void setAccept(boolean isAccept) {
        this.accept = isAccept;
    }

    public Collab clone() {
        return new Collab(id, toUser, fromProject, accept);
    }
}
