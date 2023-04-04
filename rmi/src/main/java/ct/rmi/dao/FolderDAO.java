package ct.rmi.dao;

import ct.model.Folder;
import ct.rmi.base.BaseCrud;
import ct.rmi.base.BaseDAO;
import ct.rmi.util.exception.DatabaseFailedException;

import java.util.List;

public class FolderDAO extends BaseDAO implements BaseCrud<Folder> {
    @Override
    public Folder create(Folder folder) throws DatabaseFailedException {
        return null;
    }

    @Override
    public List<Folder> getAll() {
        return null;
    }

    @Override
    public Folder get(Long id) {
        return session.get(Folder.class, id);
    }

    @Override
    public void update(Folder folder) throws DatabaseFailedException {

    }

    @Override
    public void delete(Long id) throws DatabaseFailedException {

    }
}
