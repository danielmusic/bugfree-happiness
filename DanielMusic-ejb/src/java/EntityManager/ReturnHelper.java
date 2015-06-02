package EntityManager;

import java.io.Serializable;

public class ReturnHelper implements Serializable {
    
    private Boolean result;
    private String description;
    private Long ID;

    public Boolean getResult() {
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

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }
    
}
