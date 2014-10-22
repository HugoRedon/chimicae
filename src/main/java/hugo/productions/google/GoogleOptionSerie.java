package hugo.productions.google;

/**
 *
 * @author Hugo
 */
public class GoogleOptionSerie {
    private int lineWidth;
    private int pointSize;

    public static GoogleOptionSerie SCATTER  = new GoogleOptionSerie(7,0);
    public static GoogleOptionSerie FUNCTION  = new GoogleOptionSerie(0,2);
    
    public GoogleOptionSerie(){
        
    }

    public GoogleOptionSerie(int pointSize, int lineWidth) {
        this.pointSize = pointSize;
        this.lineWidth = lineWidth;
    }
    
    
    
    
    /**
     * @return the pointSize
     */
    public int getPointSize() {
        return pointSize;
    }

    /**
     * @param pointSize the pointSize to set
     */
    public void setPointSize(int pointSize) {
        this.pointSize = pointSize;
    }

    /**
     * @return the lineWidth
     */
    public int getLineWidth() {
        return lineWidth;
    }

    /**
     * @param lineWidth the lineWidth to set
     */
    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }
}
