package EntityManager;

import java.io.Serializable;

public class ReturnHelper implements Serializable {
    
    private boolean result;
    private String description;

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
