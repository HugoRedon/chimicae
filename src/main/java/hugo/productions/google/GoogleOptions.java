package hugo.productions.google;

import com.google.gson.Gson;
import hugo.productions.google.ChartType;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Hugo
 */
public class GoogleOptions {
    private String title;
    private String curveType;
    private GoogleOptionLegend legend;
    Map<Integer,GoogleOptionSerie> series = new HashMap();
    
    private GoogleChartArea chartArea;
    
    private GoogleAxis vAxis;
    private GoogleAxis hAxis;
    
    	public static GoogleOptions googleOptions(String title,String hAxis,String vAxis,GooglePosition position){
    	      GoogleOptions options = new GoogleOptions();
    	        options.setTitle(title);
    	        options.setCurveType(CurveType.function);
    	        options.setLegend(new GoogleOptionLegend(position));
    	        options.setvAxis(new GoogleAxis(vAxis));
    	        options.sethAxis(new GoogleAxis(hAxis));
    	        return options;
    	}
     public static GoogleOptions googleOptions(String title, String hAxis, String vAxis,ChartType chartType){
        GoogleOptions options = new GoogleOptions();
        options.setTitle(title);
        options.setCurveType(CurveType.function);
        options.setLegend(new GoogleOptionLegend(GooglePosition.bottom));
        options.setvAxis(new GoogleAxis(vAxis));
        options.sethAxis(new GoogleAxis(hAxis));
        
        GoogleOptionSerie function = new GoogleOptionSerie(0, 3);
        GoogleOptionSerie scatter = new GoogleOptionSerie(7, 0);
        if(chartType.equals(ChartType.scatterFunction)){
            options.addSerie(0, scatter);
            options.addSerie(1, function);
        }else if(chartType.equals(ChartType.scatter)){
            options.addSerie(0, scatter);
        }else if(chartType.equals(ChartType.multipleScatter)){
            options.addSerie(0, scatter);
            options.addSerie(1, scatter);
            options.addSerie(2, scatter);
            options.addSerie(3, scatter);
            options.addSerie(4, scatter);
        }else if(chartType.equals(ChartType.function)){
            options.addSerie(0, function);
        }else if(chartType.equals(ChartType.functionScatter)){
            options.addSerie(0,function);
            options.addSerie(1, scatter);
        }else if(chartType.equals(ChartType.scatterFunctionScatterFunction)){
            options.addSerie(0, scatter);
            options.addSerie(1, function);
            options.addSerie(2, scatter);
            options.addSerie(3, function);
        }else if (chartType.equals(ChartType.multipleFunction)){
        	int n = 20;
        	for(int i = 0; i <n; i++){
        		options.addSerie(i, new GoogleOptionSerie(0, 2));
        	}
        }
                

        return options;
       
   }
    

    public void setSeries(Map<Integer,GoogleOptionSerie> series) {
        this.series = series;
    }

    public Map<Integer,GoogleOptionSerie> getSeries() {
        return series;
    }
    
    public void addSerie(Integer index, GoogleOptionSerie serie){
        series.put(index,serie);
    }
    
    
    public void addSeries(GoogleOptionSerie... series){
        
        for(int i=0 ; i < series.length; i++){
            this.series.put(i, series[i]);
        }
        
    }
    
    public String toJson(){
        return new Gson().toJson(this);
    }
            

    
    
    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the curveType
     */
    public String getCurveType() {
        return curveType;
    }

    /**
     * @param curveType the curveType to set
     */
    public void setCurveType(CurveType curveType) {
        this.curveType = curveType.toString();
    }

    /**
     * @return the legend
     */
    public GoogleOptionLegend getLegend() {
        return legend;
    }

    /**
     * @param legend the legend to set
     */
    public void setLegend(GoogleOptionLegend legend) {
        this.legend = legend;
    }

    /**
     * @return the vAxis
     */
    public GoogleAxis getvAxis() {
        return vAxis;
    }

    /**
     * @param vAxis the vAxis to set
     */
    public void setvAxis(GoogleAxis vAxis) {
        this.vAxis = vAxis;
    }

    /**
     * @return the hAxis
     */
    public GoogleAxis gethAxis() {
        return hAxis;
    }

    /**
     * @param hAxis the hAxis to set
     */
    public void sethAxis(GoogleAxis hAxis) {
        this.hAxis = hAxis;
    }
	public GoogleChartArea getChartArea() {
		return chartArea;
	}
	public void setChartArea(GoogleChartArea chartArea) {
		this.chartArea = chartArea;
	}
    
    
    
}
