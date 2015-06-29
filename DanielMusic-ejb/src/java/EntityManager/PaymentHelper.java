package EntityManager;

public class PaymentHelper {
    private Artist artist;
    private Double totalPaymentAmount;

    public PaymentHelper() {
        this.totalPaymentAmount = 0.0;
    }

    public PaymentHelper(Artist artist) {
        this.artist = artist;
        this.totalPaymentAmount = 0.0;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Double getTotalPaymentAmount() {
        return totalPaymentAmount;
    }

    public void setTotalPaymentAmount(Double totalPaymentAmount) {
        this.totalPaymentAmount = totalPaymentAmount;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PaymentHelper)) {
            return false;
        }
        PaymentHelper other = (PaymentHelper) object;
        if ((this.artist == null && other.artist != null) || (this.artist != null && !this.artist.equals(other.artist))) {
            return false;
        }
        return true;
    }
    
}
