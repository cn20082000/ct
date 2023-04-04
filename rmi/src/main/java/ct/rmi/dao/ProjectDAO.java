package ct.rmi.dao;

import ct.model.Project;
import ct.model.User;
import ct.rmi.base.BaseCrud;
import ct.rmi.base.BaseDAO;
import ct.rmi.util.exception.DatabaseFailedException;
import org.hibernate.ObjectNotFoundException;

import java.util.List;

public class ProjectDAO extends BaseDAO implements BaseCrud<Project> {
    public ProjectDAO() {
        super();
    }

    @Override
    public Project create(Project project) throws DatabaseFailedException {
        try {
            User u = new UserDAO().get(project.getHost().getId());
            if (u == null) {
                throw new ObjectNotFoundException(project.getHost().getId(), User.class.getName());
            }
            project.setHost(u);

            session.getTransaction().begin();

            Long id = (Long) session.save(project);

            session.getTransaction().commit();
            return get(id);
        } catch (Exception e) {
//            e.printStackTrace();
            throw new DatabaseFailedException(e.getLocalizedMessage());
        }
    }

    @Override
    public List<Project> getAll() {
        List<Project> list = session.createQuery("FROM Project").list();
        return list;
    }

    @Override
    public Project get(Long id) {
        session.refresh(session.get(Project.class, id));
        return session.get(Project.class, id);
    }

    @Override
    public void update(Project project) throws DatabaseFailedException {
        try {
            session.getTransaction().begin();

            Project p = session.get(Project.class, project.getId());
            if (p == null) {
                throw new ObjectNotFoundException(project.getId(), Project.class.getName());
            }
            p.setHost(project.getHost());
            p.setInfo(project.getInfo());
            p.setRootFolder(project.getRootFolder());
            p.setCollabs(project.getCollabs());
            session.update(p);

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

            Project p = session.get(Project.class, id);
            if (p == null) {
                throw new ObjectNotFoundException(id, Project.class.getName());
            }
            session.delete(p);

            session.getTransaction().commit();
        } catch (Exception e) {
//            e.printStackTrace();
            throw new DatabaseFailedException(e.getLocalizedMessage());
        }
    }

    // Custom function
    public List<Project> getByUser(User user) throws DatabaseFailedException {
        try {
            String hql = "FROM Project p WHERE p.host = :user";
            User u = new UserDAO().get(user.getId());
            if (u == null) {
                throw new ObjectNotFoundException(user.getId(), User.class.getName());
            }
            List<Project> list = session
                    .createQuery(hql, Project.class)
                    .setParameter("user", u)
                    .list();
            return list;
        } catch (Exception e) {
//            e.printStackTrace();
            throw new DatabaseFailedException(e.getLocalizedMessage());
        }
    }

    public List<Project> getCollabByUser(User user) throws DatabaseFailedException {
        try {
            String hql = "SELECT c.fromProject FROM Collab c WHERE c.toUser = :user AND c.accept = true";
            User u = new UserDAO().get(user.getId());
            if (u == null) {
                throw new ObjectNotFoundException(user.getId(), User.class.getName());
            }
            List<Project> list = session
                    .createQuery(hql, Project.class)
                    .setParameter("user", u)
                    .list();
            return list;
        } catch (Exception e) {
//            e.printStackTrace();
            throw new DatabaseFailedException(e.getLocalizedMessage());
        }
    }
}
