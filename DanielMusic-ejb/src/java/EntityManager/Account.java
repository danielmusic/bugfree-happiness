package EntityManager;

import java.io.Serializable;
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
    private Boolean isDisabled;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Music> listOfPurchasedMusics;
    @OneToMany(mappedBy = "account", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<TransactionOrder> listOfTransactionOrders;
    private Boolean emailIsVerified; //Initial registered email
    private String verificationCode;
    private Boolean newEmailIsVerified; //Subsequent change (will reset to false when the user tries to change email)
    @Lob
    private String imageURL;

    public Account() {
        isDisabled = false;
        emailIsVerified = false;
        newEmailIsVerified = false;
    }

    public List<Music> getListOfPurchasedMusics() {
        return listOfPurchasedMusics;
    }

    public void setListOfPurchasedMusics(List<Music> listOfPurchasedMusics) {
        this.listOfPurchasedMusics = listOfPurchasedMusics;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public Boolean getEmailIsVerified() {
        return emailIsVerified;
    }

    public void setEmailIsVerified(Boolean emailIsVerified) {
        this.emailIsVerified = emailIsVerified;
    }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    
    public Boolean getNewEmailIsVerified() {
        return newEmailIsVerified;
    }

    public void setNewEmailIsVerified(Boolean newEmailIsVerified) {
        this.newEmailIsVerified = newEmailIsVerified;
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
