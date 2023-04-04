package ct.rmi.base;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

import java.io.File;

public class BaseDAO {
    public static Session session;

    public BaseDAO() {
        if(session == null){
            try {
                session = new Configuration()
                        .configure(new File("src/main/resources/hibernate.cfg.xml"))
                        .buildSessionFactory()
                        .openSession();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
