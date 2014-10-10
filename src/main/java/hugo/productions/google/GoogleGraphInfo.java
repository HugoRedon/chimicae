package hugo.productions.google;

import java.util.List;

/**
 *
 * @author Hugo
 */
public class GoogleGraphInfo {
    private GoogleDataTable data= new GoogleDataTable();
    private GoogleOptions options;
    
    
    public void addListPoint(List<Point> lp,int index,GoogleOptionSerie optionSerie){
    	options.addSerie(index-1, optionSerie);
    	
    	for (Point point: lp){
	           double fraction = point.getX();
	           double pressure = point.getY(); 
	           Number[] columns = new Number[index+2];
	           columns[0] = fraction;
	           columns[index] = pressure;
     	   
	           data.addRow( new GoogleRow(columns));
	           	           
	       }
    }

    /**
     * @return the data
     */
    public GoogleDataTable getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(GoogleDataTable data) {
        this.data = data;
    }

    /**
     * @return the options
     */
    public GoogleOptions getOptions() {
        return options;
    }

    /**
     * @param options the options to set
     */
    public void setOptions(GoogleOptions options) {
        this.options = options;
    }
}
