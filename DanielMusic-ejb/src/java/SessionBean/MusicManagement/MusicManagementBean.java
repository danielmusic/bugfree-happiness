package SessionBean.MusicManagement;

import EntityManager.ReturnHelper;
import com.paypal.svcs.services.AdaptivePaymentsService;
import com.paypal.svcs.types.ap.PayRequest;
import com.paypal.svcs.types.ap.PayResponse;
import com.paypal.svcs.types.ap.Receiver;
import com.paypal.svcs.types.ap.ReceiverList;
import com.paypal.svcs.types.common.RequestEnvelope;
import it.sauronsoftware.jave.AudioAttributes;
import java.io.File;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;

@Stateless
public class MusicManagementBean implements MusicManagementBeanLocal {

    @Override
    public ReturnHelper encodeToMP3(File sourceFileName, File targetFileName) {
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            Encoder encoder = new Encoder();
            AudioAttributes aa = new AudioAttributes();
            aa.setCodec("libmp3lame");
            aa.setBitRate(new Integer(320000));
            aa.setChannels(new Integer(2));
            aa.setSamplingRate(new Integer(44100));
            EncodingAttributes ea = new EncodingAttributes();
            ea.setFormat("mp3");
            ea.setAudioAttributes(aa);
            encoder.encode(sourceFileName, targetFileName, ea);
            result.setResult(true);
            result.setResultDescription("mp3 file encoded successfully.");
            return result;
        } catch (EncoderException ex) {
            System.out.println("encodeWavToMP3(): Error, " + ex.getMessage());
            result.setResultDescription("Error in converting the provided wav file to mp3. Please try again.");
        } catch (Exception ex) {
            System.out.println("encodeWavToMP3(): Unknown error.");
            result.setResultDescription("Error in converting the provided wav file to mp3. Please try again.");
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public void testAdaptivePayment() {
        try {
        PayRequest payRequest = new PayRequest();

        List<Receiver> receivers = new ArrayList<Receiver>();
//Artist
        Receiver secondaryReceiver = new Receiver();
        secondaryReceiver.setAmount(1.00);
        secondaryReceiver.setEmail("platfo_1255170694_biz@gmail.com");
        receivers.add(secondaryReceiver);

//Daniel
        Receiver primaryReceiver = new Receiver();
        primaryReceiver.setAmount(2.00);
        primaryReceiver.setEmail("platfo_1255612361_per@gmail.com");
        primaryReceiver.setPrimary(true);
        receivers.add(primaryReceiver);

        ReceiverList receiverList = new ReceiverList(receivers);
        payRequest.setReceiverList(receiverList);

        RequestEnvelope requestEnvelope = new RequestEnvelope("en_US");
        payRequest.setRequestEnvelope(requestEnvelope);
        payRequest.setActionType("PAY");
        payRequest.setCancelUrl("https://devtools-paypal.com/guide/ap_chained_payment?cancel=true");
        payRequest.setReturnUrl("https://devtools-paypal.com/guide/ap_chained_payment?success=true");
        payRequest.setCurrencyCode("USD");
        payRequest.setIpnNotificationUrl("http://replaceIpnUrl.com");

        Map<String, String> sdkConfig = new HashMap<String, String>();
        sdkConfig.put("mode", "sandbox");
        sdkConfig.put("acct1.UserName", "jb-us-seller_api1.paypal.com");
        sdkConfig.put("acct1.Password", "WX4WTU3S8MY44S7F");
        sdkConfig.put("acct1.Signature", "AFcWxV21C7fd0v3bYYYRCpSSRl31A7yDhhsPUU2XhtMoZXsWHFxu-RWy");
        sdkConfig.put("acct1.AppId", "APP-80W284485P519543T");

        AdaptivePaymentsService adaptivePaymentsService = new AdaptivePaymentsService(sdkConfig);
        PayResponse payResponse = adaptivePaymentsService.pay(payRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
