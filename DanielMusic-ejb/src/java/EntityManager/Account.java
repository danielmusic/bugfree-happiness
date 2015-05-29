package EntityManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Account implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Lob
    private String email;
    @Lob
    private String newEmail;
    private String passwordSalt;
    private String passwordHash;
    @Lob
    private String name;
    //private Boolean isArtist;
    //private Boolean isAdmin;
    private Boolean isDisabled;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Music> listOfPurchasedMusics;
    @OneToMany(mappedBy = "account", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<TransactionOrder> listOfTransactionOrders;
//    @OneToOne(mappedBy = "account", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    private Page page;
    private Integer isVerifiedEmail; //status types: 0(not verified), 1(verified) 
//    private Boolean isApproved;

    public Account() {
//        isArtist = false;
//        isAdmin = false;
        isDisabled = false;
        isVerifiedEmail = 0;
//        isApproved = false;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public Integer getIsVerifiedEmail() {
        return isVerifiedEmail;
    }

    public void setIsVerifiedEmail(Integer isVerifiedEmail) {
        this.isVerifiedEmail = isVerifiedEmail;
    }

//    public Boolean getIsApproved() {
//        return isApproved;
//    }
//
//    public void setIsApproved(Boolean isApproved) {
//        this.isApproved = isApproved;
//    }

    public Boolean getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(Boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public List<Music> getListOfMusics() {
        return listOfPurchasedMusics;
    }

    public void setListOfMusics(List<Music> listOfMusics) {
        this.listOfPurchasedMusics = listOfMusics;
    }

    public List<TransactionOrder> getListOfTransactionOrders() {
        return listOfTransactionOrders;
    }

    public void setListOfTransactionOrders(List<TransactionOrder> listOfTransactionOrders) {
        this.listOfTransactionOrders = listOfTransactionOrders;
    }

//    public Page getPage() {
//        return page;
//    }
//
//    public void setPage(Page page) {
//        this.page = page;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public Boolean getIsArtist() {
//        return isArtist;
//    }
//
//    public void setIsArtist(Boolean isArtist) {
//        this.isArtist = isArtist;
//    }
//
//    public Boolean getIsAdmin() {
//        return isAdmin;
//    }
//
//    public void setIsAdmin(Boolean isAdmin) {
//        this.isAdmin = isAdmin;
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EntityManager.Account[ id=" + id + " ]";
    }

}
