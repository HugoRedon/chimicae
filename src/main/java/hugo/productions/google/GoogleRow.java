package hugo.productions.google;

import java.util.ArrayList;

/**
 *
 * @author Hugo
 */
public class GoogleRow {
    private ArrayList<GoogleCell> c = new ArrayList();
    
    public GoogleRow(Number... numbers){
        for (Number n : numbers){
            c.add(new GoogleNumberCell(n));
        }
    }
    
    public GoogleRow(String... values){
        for (String v : values){
            c.add(new GoogleStringCell(v));
        }
    }
    
    public void addCell(GoogleCell cell){
        getC().add(cell);
    }
    public void addCells(GoogleCell... cells){
        for(GoogleCell cell: cells){
            getC().add(cell);
        }
        
    }

    /**
     * @return the c
     */
    public ArrayList<GoogleCell> getC() {
        return c;
    }

    /**
     * @param c the c to set
     */
    public void setC(ArrayList<GoogleCell> c) {
        this.c = c;
    }
    
}
