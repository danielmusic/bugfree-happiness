package SessionBean.MusicManagement;

import EntityManager.ReturnHelper;
import it.sauronsoftware.jave.AudioAttributes;
import java.io.File;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
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
            System.out.println("encodeWavToMP3(): Error, "+ex.getMessage());
            result.setResultDescription("Error in converting the provided wav file to mp3. Please try again.");
        } catch (Exception ex) {
            System.out.println("encodeWavToMP3(): Unknown error.");
            result.setResultDescription("Error in converting the provided wav file to mp3. Please try again.");
            ex.printStackTrace();
        }
        return result;
    }

}
