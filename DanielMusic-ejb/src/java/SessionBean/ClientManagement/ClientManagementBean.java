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

    @Override
    public List<Member> listAllMembers(Boolean isAdmin) {
        System.out.println("listAllMembers() called");
        try {
            Query q;
            if (isAdmin) {
                q = em.createQuery("select m from Member m where m.isDisabled=false");
            } else {
                q = em.createQuery("select m from Member m where m.isDisabled=false and m.emailIsVerified=true");
            }
            List<Member> listOfMembers = q.getResultList();
            System.out.println("listAllMembers() called successfully");
            return listOfMembers;
        } catch (Exception e) {
            System.out.println("Error while calling listAllMembers()");
            e.printStackTrace();
        }
        return null;
    }

    public void persist(Object object) {
        em.persist(object);
    }

}
