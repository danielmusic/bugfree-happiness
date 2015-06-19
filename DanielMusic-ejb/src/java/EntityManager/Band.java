package EntityManager;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Band extends Account implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer isApproved; //{new: 0, pending: -2, approve: 1, not approve: -1}
    @OneToMany(mappedBy = "band", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<Album> listOfAlbums;

    @Temporal(TemporalType.DATE)
    private Date dateFormed;
    @ManyToOne
    private Genre genre;
    @Lob
    private String members;
    @Lob
    private String biography;
    @Lob
    private String influences;
    private String contactEmail;
    private String paypalEmail;
    @Lob
    private String facebookURL;
    @Lob
    private String instagramURL;
    @Lob
    private String twitterURL;

    public Band() {
        isApproved = 0;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public Date getDateFormed() {
        return dateFormed;
    }

    public void setDateFormed(Date dateFormed) {
        this.dateFormed = dateFormed;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getInfluences() {
        return influences;
    }

    public void setInfluences(String influences) {
        this.influences = influences;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getPaypalEmail() {
        return paypalEmail;
    }

    public void setPaypalEmail(String paypalEmail) {
        this.paypalEmail = paypalEmail;
    }

    public String getFacebookURL() {
        return facebookURL;
    }

    public void setFacebookURL(String facebookURL) {
        this.facebookURL = facebookURL;
    }

    public String getInstagramURL() {
        return instagramURL;
    }

    public void setInstagramURL(String instagramURL) {
        this.instagramURL = instagramURL;
    }

    public String getTwitterURL() {
        return twitterURL;
    }

    public void setTwitterURL(String twitterURL) {
        this.twitterURL = twitterURL;
    }

    public List<Album> getListOfAlbums() {
        return listOfAlbums;
    }

    public void setListOfAlbums(List<Album> listOfAlbums) {
        this.listOfAlbums = listOfAlbums;
    }

    /**
     * {@inheritDoc}
     * <p>
     * newly created: 0 pending: -2 approve: 1 not approved: -1
     * </p>
     */
    public Integer getIsApproved() {
        return isApproved;
    }

    /**
     * {@inheritDoc}
     * <p>
     * newly created: 0 pending: -2 approve: 1 not approved: -1
     * </p>
     *
     * {
     *
     * @param isApproved Integer for the status.}
     */
    public void setIsApproved(Integer isApproved) {
        this.isApproved = isApproved;
    }

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
        if (!(object instanceof Band)) {
            return false;
        }
        Band other = (Band) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EntityManager.Band[ id=" + id + " ]";
    }

}
