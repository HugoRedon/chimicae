package hugo.productions.google;

/**
 *
 * @author Hugo
 */
public class GoogleNumberCell extends GoogleCell {
    private Number v;
    public GoogleNumberCell(Number v){
        this.v = v;
    }

    /**
     * @return the v
     */
    public Number getV() {
        return v;
    }

    /**
     * @param v the v to set
     */
    public void setV(Number v) {
        this.v = v;
    }
}
