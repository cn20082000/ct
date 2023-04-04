package ct.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tblFile")
public class File implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "info_id")
    private Info info;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @Column(name = "url")
    private String url;

    public File() {
        super();
    }

    public File(Long id, Info info, Folder folder, String url) {
        super();
        this.id = id;
        this.info = info;
        this.folder = folder;
        this.url = url;
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

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public File clone() {
        return new File(id, info, folder, url);
    }

    @Override
    public String toString() {
        return info.getName();
    }
}
