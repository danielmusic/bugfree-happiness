package EntityManager;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class ExploreHelper implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private Genre genre;
    private Artist artist;
    private Music featuredMusic;

    public ExploreHelper() {
        artist = null;
        featuredMusic = null;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artists) {
        this.artist = artists;
    }

    public Music getFeaturedMusic() {
        return featuredMusic;
    }

    public void setFeaturedMusic(Music featuredMusic) {
        this.featuredMusic = featuredMusic;
    }
}
