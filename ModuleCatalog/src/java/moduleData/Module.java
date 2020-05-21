package moduleData;

/**
 * A class modelling a Module.
 * @author Manol
 */
public class Module {

    private String code;

    private String name;

    private Boolean isActive;

    public Module(String code, String name) {
        this.code = code;
        this.name = name;
        this.isActive = true;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Returns the module formatted as a string.
     * @return the module formatted as a string.
     */
    @Override
    public String toString() {
        String active;
        if (isActive) {
            active = "active";
        } else {
            active = "discontinued";
        }
        return "name: " + name + "; code: " + code + "; activity: " + active + ";";
    }

}
