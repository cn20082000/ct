package ct.rmi.dao;

import ct.model.User;
import ct.rmi.base.BaseCrud;
import ct.rmi.base.BaseDAO;
import ct.rmi.util.Security;
import ct.rmi.util.exception.DatabaseFailedException;
import org.hibernate.ObjectNotFoundException;

import java.util.List;

public class UserDAO extends BaseDAO implements BaseCrud<User> {
    public UserDAO() {
        super();
    }

    @Override
    public User create(User user) throws DatabaseFailedException {
        try {
            session.getTransaction().begin();

            user.setPassword(Security.hashPassword(user.getPassword()));
            Long id = (Long) session.save(user);

            session.getTransaction().commit();
            return get(id);
        } catch (Exception e) {
//            e.printStackTrace();
            throw new DatabaseFailedException(e.getLocalizedMessage());
        }
    }

    @Override
    public List<User> getAll() {
        List<User> list = session.createQuery("FROM User").list();
        return list;
    }

    @Override
    public User get(Long id) {
        return session.get(User.class, id);
    }

    @Override
    public void update(User user) throws DatabaseFailedException {
        try {
            session.getTransaction().begin();

            User u = session.get(User.class, user.getId());
            if (u == null) {
                throw new ObjectNotFoundException(user.getId(), User.class.getName());
            }
            u.setName(user.getName());
            u.setUsername(user.getUsername());
            u.setPassword(Security.hashPassword(user.getPassword()));
            session.update(u);

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

            User u = session.get(User.class, id);
            if (u == null) {
                throw new ObjectNotFoundException(id, User.class.getName());
            }
            session.delete(u);

            session.getTransaction().commit();
        } catch (Exception e) {
//            e.printStackTrace();
            throw new DatabaseFailedException(e.getLocalizedMessage());
        }
    }

    // Custom function
    public User signIn(User user) throws DatabaseFailedException {
        try {
            String hql = "FROM User u WHERE u.username = :username AND u.password = :password";
            User u = session
                    .createQuery(hql, User.class)
                    .setParameter("username", user.getUsername())
                    .setParameter("password", Security.hashPassword(user.getPassword()))
                    .uniqueResult();
            if (u == null) {
                throw new ObjectNotFoundException(user.getUsername(), User.class.getName());
            }
            return u;
        } catch (Exception e) {
//            e.printStackTrace();
            throw new DatabaseFailedException(e.getLocalizedMessage());
        }
    }

    public List<User> getByKey(String key) throws DatabaseFailedException {
        try {
            String hql = "FROM User u WHERE u.username LIKE :username";
            List<User> result = session
                    .createQuery(hql, User.class)
                    .setParameter("username", "%" + key + "%")
                    .list();
            return result;
        } catch (Exception e) {
//            e.printStackTrace();
            throw new DatabaseFailedException(e.getLocalizedMessage());
        }
    }
}
