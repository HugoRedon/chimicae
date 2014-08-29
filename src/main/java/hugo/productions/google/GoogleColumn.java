package hugo.productions.google;

/**
 *
 * @author Hugo
 */
public class GoogleColumn {
    private String id;
    private String label;
    private String type;
    public GoogleColumn(String id , String label, GoogleColumnType type){
        this.id = id;
        this.label = label;
        this.type = type.toString();
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(GoogleColumnType type) {
        this.type = type.toString();
    }
    
}
