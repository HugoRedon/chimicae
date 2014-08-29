

package hugo.productions.google;

/**
 *
 * @author Hugo
 */
public class GoogleOptionLegend {
    String position ;

    public GoogleOptionLegend() {
    }

    
    public GoogleOptionLegend(GooglePosition position) {
        this.position = position.toString();
    }

    
    public String getPosition() {
        return position;
    }

    public void setPosition(GooglePosition position) {
        this.position = position.toString();
    }
    
}
