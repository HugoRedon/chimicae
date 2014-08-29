/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hugo.productions.google;

/**
 *
 * @author Hugo
 */
public class GoogleStringCell extends GoogleCell {
    private String v;
    GoogleStringCell(String v){
        this.v = v;
    }

    /**
     * @return the v
     */
    public String getV() {
        return v;
    }

    /**
     * @param v the v to set
     */
    public void setV(String v) {
        this.v = v;
    }
    
}
