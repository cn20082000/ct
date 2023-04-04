package ct.rmi.dao;

import ct.model.File;
import ct.model.Folder;
import ct.rmi.base.BaseCrud;
import ct.rmi.base.BaseDAO;
import ct.rmi.util.exception.DatabaseFailedException;
import org.hibernate.ObjectNotFoundException;

import java.util.List;

public class FileDAO extends BaseDAO implements BaseCrud<File> {
    public FileDAO() {
        super();
    }

    @Override
    public File create(File file) throws DatabaseFailedException {
        try {
            Folder f = new FolderDAO().get(file.getFolder().getId());
            if (f == null) {
                throw new ObjectNotFoundException(file.getFolder().getId(), Folder.class.getName());
            }
            file.setFolder(f);

            session.getTransaction().begin();

            Long id = (Long) session.save(file);

            session.getTransaction().commit();
            return get(id);
        } catch (Exception e) {
//            e.printStackTrace();
            throw new DatabaseFailedException(e.getLocalizedMessage());
        }
    }

    @Override
    public List<File> getAll() {
        List<File> list = session.createQuery("FROM File").list();
        return list;
    }

    @Override
    public File get(Long id) {
        return session.get(File.class, id);
    }

    @Override
    public void update(File file) throws DatabaseFailedException {
        try {
            session.getTransaction().begin();

            File f = session.get(File.class, file.getId());
            if (f == null) {
                throw new ObjectNotFoundException(file.getId(), File.class.getName());
            }
            f.setInfo(file.getInfo());
            f.setFolder(file.getFolder());
            f.setUrl(file.getUrl());
            session.update(f);

            session.getTransaction().commit();
        } catch (Exception e) {
//            e.printStackTrace();
            throw new DatabaseFailedException(e.getLocalizedMessage());
        }
    }

    @Override
    public void delete(Long id) throws DatabaseFailedException {
        try {
            session.getTransaction().begin();

            File f = session.get(File.class, id);
            if (f == null) {
                throw new ObjectNotFoundException(id, File.class.getName());
            }
            session.delete(f);

            session.getTransaction().commit();
        } catch (Exception e) {
//            e.printStackTrace();
            throw new DatabaseFailedException(e.getLocalizedMessage());
        }
    }
}
