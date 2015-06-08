package EntityManager;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Music implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer trackNumber;
    private String name;
    private String artistName;
    private Long numDownloaded;
    private Double price;
    private String fileLocation;
    private Boolean isDeleted;
    @ManyToMany(mappedBy = "listOfMusics", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<Genre> listOfGenres;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Album album;

    public Music() {
        isDeleted = false;
        numDownloaded = 0L;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public List<Genre> getListOfGenres() {
        return listOfGenres;
    }

    public void setListOfGenres(List<Genre> listOfGenres) {
        this.listOfGenres = listOfGenres;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(Integer trackNumber) {
        this.trackNumber = trackNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public Long getNumDownloaded() {
        return numDownloaded;
    }

    public void setNumDownloaded(Long numDownloaded) {
        this.numDownloaded = numDownloaded;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
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
        if (!(object instanceof Music)) {
            return false;
        }
        Music other = (Music) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EntityManager.Music[ id=" + id + " ]";
    }

}
