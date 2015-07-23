/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EntityManager;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Account account;
    private String nonMemberEmail;
    private Double totalPaymentAmount;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCompleted;
    private Boolean paymentCompleted;
    @OneToMany
    private List<Music> musicPurchased;
    private List<Double> musicPrices;
    @OneToMany
    private List<Album> albumPurchased;
    private List<Double> albumPrices;
    private String UUID;

    public Payment() {
    }

    public Payment(Double paymentAmount, String UUID) {
        this.dateCreated = new Date();
        this.paymentCompleted=false;
        this.totalPaymentAmount = paymentAmount;
        this.UUID = UUID;
    }

    public Long getId() {
        return id;
    }

    public List<Double> getMusicPrices() {
        return musicPrices;
    }

    public void setMusicPrices(List<Double> musicPrices) {
        this.musicPrices = musicPrices;
    }

    public List<Double> getAlbumPrices() {
        return albumPrices;
    }

    public void setAlbumPrices(List<Double> albumPrices) {
        this.albumPrices = albumPrices;
    }

    public String getNonMemberEmail() {
        return nonMemberEmail;
    }

    public void setNonMemberEmail(String nonMemberEmail) {
        this.nonMemberEmail = nonMemberEmail;
    }

    public Date getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(Date dateCompleted) {
        this.dateCompleted = dateCompleted;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public List<Album> getAlbumPurchased() {
        return albumPurchased;
    }

    public void setAlbumPurchased(List<Album> albumPurchased) {
        this.albumPurchased = albumPurchased;
    }

    public List<Music> getMusicPurchased() {
        return musicPurchased;
    }

    public void setMusicPurchased(List<Music> musicPurchased) {
        this.musicPurchased = musicPurchased;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Double getTotalPaymentAmount() {
        return totalPaymentAmount;
    }

    public void setTotalPaymentAmount(Double totalPaymentAmount) {
        this.totalPaymentAmount = totalPaymentAmount;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Boolean getPaymentCompleted() {
        return paymentCompleted;
    }

    public void setPaymentCompleted(Boolean paymentCompleted) {
        this.paymentCompleted = paymentCompleted;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
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
        if (!(object instanceof Payment)) {
            return false;
        }
        Payment other = (Payment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EntityManager.Payment[ id=" + id + " ]";
    }
    
}
