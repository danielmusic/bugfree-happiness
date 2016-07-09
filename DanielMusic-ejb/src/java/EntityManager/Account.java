package EntityManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
    private String password;
    @Lob
    private String name;
    private Boolean isDisabled;
    private Boolean isDeleted;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Music> listOfPurchasedMusics;
    private Boolean forgetPassword;
    private String passwordResetCode;
    @Temporal(TemporalType.TIMESTAMP)
    private Date passwordResetCodeGeneratedDate;
    
    private Boolean emailIsVerified; //Initial registered email
    private String initialEmailVerificationCode;
    @Temporal(TemporalType.TIMESTAMP)
    private Date initialEmailVerificationCodeGeneratedDate;
    
    private String newEmailVerificationCode;
    @Temporal(TemporalType.TIMESTAMP)
    private Date newEmailVerificationCodeGeneratedDate;
    private Boolean newEmailIsVerified; //Subsequent change (will reset to false when the user tries to change email)
    @Lob
    private String imageURL;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    private ShoppingCart shoppingCart;
    @OneToMany(mappedBy = "account", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Payment> paymentRecord;

    public Account() {
        isDisabled = false;
        isDeleted = false;
        emailIsVerified = false;
        forgetPassword = false;
        newEmailIsVerified = false;
        paymentRecord = new ArrayList();
    }

    public Boolean getForgetPassword() {
        return forgetPassword;
    }

    public void setForgetPassword(Boolean forgetPassword) {
        this.forgetPassword = forgetPassword;
    }

    public String getPasswordResetCode() {
        return passwordResetCode;
    }

    public void setPasswordResetCode(String passwordResetCode) {
        this.passwordResetCode = passwordResetCode;
        this.passwordResetCodeGeneratedDate = new Date();
    }

    public Date getPasswordResetCodeGeneratedDate() {
        return passwordResetCodeGeneratedDate;
    }

    public String getInitialEmailVerificationCode() {
        return initialEmailVerificationCode;
    }

    public void setInitialEmailVerificationCode(String initialEmailVerificationCode) {
        this.initialEmailVerificationCode = initialEmailVerificationCode;
        this.initialEmailVerificationCodeGeneratedDate = new Date();
    }

    public Date getInitialEmailVerificationCodeGeneratedDate() {
        return initialEmailVerificationCodeGeneratedDate;
    }

    public void setInitialEmailVerificationCodeGeneratedDate(Date initialEmailVerificationCodeGeneratedDate) {
        this.initialEmailVerificationCodeGeneratedDate = initialEmailVerificationCodeGeneratedDate;
    }

    public void setPasswordResetCodeGeneratedDate(Date passwordResetCodeGeneratedDate) {
        this.passwordResetCodeGeneratedDate = passwordResetCodeGeneratedDate;
    }

    public Date getNewEmailVerificationCodeGeneratedDate() {
        return newEmailVerificationCodeGeneratedDate;
    }

    public void setNewEmailVerificationCodeGeneratedDate(Date newEmailVerificationCodeGeneratedDate) {
        this.newEmailVerificationCodeGeneratedDate = newEmailVerificationCodeGeneratedDate;
        this.newEmailVerificationCodeGeneratedDate = new Date();
    }

    public List<Payment> getPaymentRecord() {
        return paymentRecord;
    }

    public void setPaymentRecord(List<Payment> paymentRecord) {
        this.paymentRecord = paymentRecord;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
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

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getNewEmailVerificationCode() {
        return newEmailVerificationCode;
    }

    public void setNewEmailVerificationCode(String newEmailVerificationCode) {
        this.newEmailVerificationCode = newEmailVerificationCode;
        this.newEmailVerificationCodeGeneratedDate = new Date();
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
