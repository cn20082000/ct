package ct.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "tblFolder")
public class Folder implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "info_id")
    private Info info;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder dadFolder;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "dadFolder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Folder> folders;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<File> files;

    public Folder() {
        super();
    }

    public Folder(Long id, Info info, Folder dadFolder, List<Folder> folders, List<File> files) {
        super();
        this.id = id;
        this.info = info;
        this.dadFolder = dadFolder;
        this.folders = folders;
        this.files = files;
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

    public Folder getDadFolder() {
        return dadFolder;
    }

    public void setDadFolder(Folder dadFolder) {
        this.dadFolder = dadFolder;
    }

    public List<Folder> getFolders() {
        return folders;
    }

    public void setFolders(List<Folder> folders) {
        this.folders = folders;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public Folder clone() {
        return new Folder(id, info, dadFolder, folders, files);
    }

    @Override
    public String toString() {
        return info.getName();
    }
}
