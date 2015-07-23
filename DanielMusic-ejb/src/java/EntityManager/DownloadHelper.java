/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EntityManager;

import java.util.List;

public class DownloadHelper {
    private List<String> downloadLinks128;
    private List<String> downloadLinks320;
    private List<String> downloadLinksWav;
    private List<Music> purchasedMusics;

    public List<String> getDownloadLinks128() {
        return downloadLinks128;
    }

    public void setDownloadLinks128(List<String> downloadLinks128) {
        this.downloadLinks128 = downloadLinks128;
    }

    public List<Music> getPurchasedMusics() {
        return purchasedMusics;
    }

    public void setPurchasedMusics(List<Music> purchasedMusics) {
        this.purchasedMusics = purchasedMusics;
    }

    public List<String> getDownloadLinks320() {
        return downloadLinks320;
    }

    public void setDownloadLinks320(List<String> downloadLinks320) {
        this.downloadLinks320 = downloadLinks320;
    }

    public List<String> getDownloadLinksWav() {
        return downloadLinksWav;
    }

    public void setDownloadLinksWav(List<String> downloadLinksWav) {
        this.downloadLinksWav = downloadLinksWav;
    }
    
}
