package ct.rmi.dao;

import ct.model.Collab;
import ct.model.Project;
import ct.model.User;
import ct.rmi.base.BaseCrud;
import ct.rmi.base.BaseDAO;
import ct.rmi.util.exception.DatabaseFailedException;
import org.hibernate.ObjectNotFoundException;

import java.util.List;

public class CollabDAO extends BaseDAO implements BaseCrud<Collab> {
    @Override
    public Collab create(Collab collab) throws DatabaseFailedException {
        try {
            User u = new UserDAO().get(collab.getToUser().getId());
            if (u == null) {
                throw new ObjectNotFoundException(collab.getToUser().getId(), User.class.getName());
            }
            Project p = new ProjectDAO().get(collab.getFromProject().getId());
            if (p == null) {
                throw new ObjectNotFoundException(collab.getFromProject().getId(), Project.class.getName());
            }
            collab.setFromProject(p);
            collab.setToUser(u);
            session.getTransaction().begin();

            Long id = (Long) session.save(collab);

            session.getTransaction().commit();
            return get(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseFailedException(e.getLocalizedMessage());
        }
    }

    @Override
    public List<Collab> getAll() {
        List<Collab> list = session.createQuery("FROM Collab").list();
        return list;
    }

    @Override
    public Collab get(Long id) {
        return session.get(Collab.class, id);
    }

    @Override
    public void update(Collab collab) throws DatabaseFailedException {
        try {
            session.getTransaction().begin();

            Collab c = new CollabDAO().get(collab.getId());
            if (c == null) {
                throw new ObjectNotFoundException(collab.getId(), Collab.class.getName());
            }
            c.setToUser(collab.getToUser());
            c.setFromProject(c.getFromProject());
            c.setAccept(c.isAccept());
            session.update(c);

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

            Collab c = new CollabDAO().get(id);
            if (c == null) {
                throw new ObjectNotFoundException(id, Collab.class.getName());
            }
            session.delete(c);

            session.getTransaction().commit();
        } catch (Exception e) {
//            e.printStackTrace();
            throw new DatabaseFailedException(e.getLocalizedMessage());
        }
    }

    // Custom function
    public void acceptCollab(Long id) throws DatabaseFailedException {
        try {
            session.getTransaction().begin();

            Collab c = new CollabDAO().get(id);
            if (c == null) {
                throw new ObjectNotFoundException(id, Collab.class.getName());
            }
            c.setAccept(true);
            session.update(c);

            session.getTransaction().commit();
        } catch (Exception e) {
//            e.printStackTrace();
            throw new DatabaseFailedException(e.getLocalizedMessage());
        }
    }

    public List<Collab> getPendingByUser(User user) throws DatabaseFailedException {
        try {
            String hql = "FROM Collab c WHERE c.toUser = :user AND c.accept = false";
            User u = new UserDAO().get(user.getId());
            if (u == null) {
                throw new ObjectNotFoundException(user.getId(), User.class.getName());
            }
            List<Collab> list = session
                    .createQuery(hql, Collab.class)
                    .setParameter("user", u)
                    .list();
            return list;
        } catch (Exception e) {
//            e.printStackTrace();
            throw new DatabaseFailedException(e.getLocalizedMessage());
        }
    }

    public Collab get(User user, Project project) throws DatabaseFailedException {
        try {

            String hql = "FROM Collab c WHERE c.toUser = :user AND x.fromProject = :project";
            User u = new UserDAO().get(user.getId());
            if (u == null) {
                throw new ObjectNotFoundException(user.getId(), User.class.getName());
            }
            Project p = new ProjectDAO().get(project.getId());
            if (p == null) {
                throw new ObjectNotFoundException(project.getId(), Project.class.getName());
            }
            Collab collab = session
                    .createQuery(hql, Collab.class)
                    .setParameter("user", u)
                    .setParameter("project", p)
                    .uniqueResult();
            return collab;
        } catch (Exception e) {
//            e.printStackTrace();
            throw new DatabaseFailedException(e.getLocalizedMessage());
        }
    }
}
