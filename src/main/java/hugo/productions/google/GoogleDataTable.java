package hugo.productions.google;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.Gson;

/**
 *
 * @author Hugo
 */
public class GoogleDataTable {
    private ArrayList<GoogleColumn> cols = new ArrayList();
    private ArrayList<GoogleRow> rows = new ArrayList();
    
    public void addColumn(GoogleColumn col){
        this.cols.add(col);
    }
    public void addColumns(GoogleColumn... cols){
        for(GoogleColumn col : cols){
            this.cols.add(col);
        }
    }
   
    
    public void addRow(GoogleRow row){
        this.getRows().add(row);
    }
    public void addRows(GoogleRow... rows){
        for(GoogleRow row: rows){
            this.getRows().add(row);
        }
    }
    public void addRows(Collection<GoogleRow> rows){
        for(GoogleRow row: rows){
            this.getRows().add(row);
        }
    }
    
    
    public String toJson(){
        return new Gson().toJson(this);
    }

    /**
     * @return the cols
     */
    public ArrayList<GoogleColumn> getCols() {
        return cols;
    }

    /**
     * @param cols the cols to set
     */
    public void setCols(ArrayList<GoogleColumn> cols) {
        this.cols = cols;
    }

    /**
     * @return the rows
     */
    public ArrayList<GoogleRow> getRows() {
        return rows;
    }

    /**
     * @param rows the rows to set
     */
    public void setRows(ArrayList<GoogleRow> rows) {
        this.rows = rows;
    }
}
