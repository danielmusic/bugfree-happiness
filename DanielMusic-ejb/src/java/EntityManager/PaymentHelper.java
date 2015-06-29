package EntityManager;

public class PaymentHelper {

    private String artistOrBandPaypalEmail;
    private Double totalPaymentAmount;

    public PaymentHelper() {
        this.totalPaymentAmount = 0.0;
    }

    public PaymentHelper(String artistOrBandPaypalEmail) {
        this.artistOrBandPaypalEmail = artistOrBandPaypalEmail;
        this.totalPaymentAmount = 0.0;
    }

    public String getArtistOrBandPaypalEmail() {
        return artistOrBandPaypalEmail;
    }

    public void setArtistOrBandPaypalEmail(String artistOrBandPaypalEmail) {
        this.artistOrBandPaypalEmail = artistOrBandPaypalEmail;
    }

    public Double getTotalPaymentAmount() {
        return totalPaymentAmount;
    }

    public void setTotalPaymentAmount(Double totalPaymentAmount) {
        this.totalPaymentAmount = totalPaymentAmount;
    }
}
