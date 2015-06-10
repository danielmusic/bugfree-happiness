package EntityManager;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

@Entity
public class Artist extends Account implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer isApproved; //{new: 0, pending: -2, approve: 1, not approve: -1}
    @OneToMany(mappedBy = "artist", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<Album> listOfAlbums;
    @Lob
    private String facebookURL;
    @Lob
    private String instagramURL;
    @Lob
    private String twitterURL;

    public Artist() {
        isApproved = 0;
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
     * newly created: 0 
     * pending: -2
     * approve: 1
     * not approved: -1
     * </p>
     */
    public Integer getIsApproved() {
        return isApproved;
    }

    /**
     * {@inheritDoc}
     * <p>
     * newly created: 0 
     * pending: -2
     * approve: 1
     * not approved: -1
     * </p>
     *
     * {@param isApproved Integer for the status.}
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
        if (!(object instanceof Artist)) {
            return false;
        }
        Artist other = (Artist) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EntityManager.Artist[ id=" + id + " ]";
    }

}
