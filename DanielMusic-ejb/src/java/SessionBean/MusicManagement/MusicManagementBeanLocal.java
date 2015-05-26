package SessionBean.MusicManagement;

import EntityManager.ReturnHelper;
import java.io.File;
import javax.ejb.Local;

@Local
public interface MusicManagementBeanLocal {
    public ReturnHelper encodeToMP3(File sourceFileName, File targetFileName); 
}