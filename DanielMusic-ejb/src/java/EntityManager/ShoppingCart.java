package EntityManager;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

@Entity
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne(mappedBy = "shoppingCart", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private Account account;
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private Set<Album> listOfAlbums;
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private Set<Music> listOfMusics;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Set<Album> getListOfAlbums() {
        return listOfAlbums;
    }

    public void setListOfAlbums(Set<Album> listOfAlbums) {
        this.listOfAlbums = (Set<Album>) listOfAlbums;
    }

    public Set<Music> getListOfMusics() {
        return listOfMusics;
    }

    public void setListOfMusics(Set<Music> listOfMusics) {
        this.listOfMusics = (Set<Music>) listOfMusics;
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
        if (!(object instanceof ShoppingCart)) {
            return false;
        }
        ShoppingCart other = (ShoppingCart) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EntityManager.ShoppingCart[ id=" + id + " ]";
    }

}
