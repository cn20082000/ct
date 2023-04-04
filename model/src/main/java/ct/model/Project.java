package ct.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "tblProject")
public class Project implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "info_id")
    private Info info;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User host;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "folder_id")
    private Folder rootFolder;

    @OneToMany(mappedBy = "fromProject", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Collab> collabs;

    public Project() {
        super();
    }

    public Project(Long id, Info info, User host, Folder rootFolder, List<Collab> collabs) {
        super();
        this.id = id;
        this.info = info;
        this.host = host;
        this.rootFolder = rootFolder;
        this.collabs = collabs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
    }

    public Folder getRootFolder() {
        return rootFolder;
    }

    public void setRootFolder(Folder rootFolder) {
        this.rootFolder = rootFolder;
    }

    public List<Collab> getCollabs() {
        return collabs;
    }

    public void setCollabs(List<Collab> collabs) {
        this.collabs = collabs;
    }

    public Project clone() {
        return new Project(id, info, host, rootFolder, collabs);
    }
}
