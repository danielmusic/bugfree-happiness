package SessionBean.ClientManagement;

import EntityManager.Member;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class ClientManagementBean implements ClientManagementBeanLocal {

    @PersistenceContext(unitName = "DanielMusic-ejbPU")
    private EntityManager em;



    public void persist(Object object) {
        em.persist(object);
    }

}
