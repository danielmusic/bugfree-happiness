
package EntityManager;

import java.util.List;

public class SearchHelper {
    private List<Album> listOfAlbums;
    private List<Artist> listOfArtists;
    private List<Music> featuredMusics;
    private List<Music> listOfMusics;

    public List<Album> getListOfAlbums() {
        return listOfAlbums;
    }

    public void setListOfAlbums(List<Album> listOfAlbums) {
        this.listOfAlbums = listOfAlbums;
    }

    public List<Music> getFeaturedMusics() {
        return featuredMusics;
    }

    public void setFeaturedMusics(List<Music> featuredMusics) {
        this.featuredMusics = featuredMusics;
    }

    public List<Artist> getListOfArtists() {
        return listOfArtists;
    }

    public void setListOfArtists(List<Artist> listOfArtists) {
        this.listOfArtists = listOfArtists;
    }

    public List<Music> getListOfMusics() {
        return listOfMusics;
    }

    public void setListOfMusics(List<Music> listOfMusics) {
        this.listOfMusics = listOfMusics;
    }
    
    
}
