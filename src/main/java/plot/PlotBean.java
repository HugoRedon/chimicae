package plot;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;


@Named("plotBean")
@SessionScoped
public class PlotBean implements Serializable {

	private static final long serialVersionUID = -3851272732029076531L;
	private String controlsPage;
	private String plotPage="webGLPlotTemplate";
	
	private String jsonData="[{x:500,y:500,z:500}]";
	
	private Boolean secondPlot= new Boolean(false);
	private Boolean thirdPlot= new Boolean(false);
	private String thirdJson="[{x:500,y:500,z:500}]";
	
	private String xAxisLabel ="eje X";
	private String yAxisLabel = "eje Y";
	private String zAxisLabel ="eje Z";
	
	
	private Boolean secondLine;
	private Boolean firstPlot=new Boolean(true);
	
	private String secondLineJson;
	
	private Boolean line = new Boolean(false);
	public Boolean getLine() {
		return line;
	}

	public String setControl_Plot_Pages(String controlPage, String plotPage){
		this.controlsPage=controlPage;
		this.plotPage = plotPage;
		return "verticalSplit";
	}
	
	public void setLine(Boolean line) {
		this.line = line;
	}
	private String lineJson="[{x:500,y:500,z:500}]";
	public String getLineJson() {
		return lineJson;
	}

	public void setLineJson(String lineJson) {
		this.lineJson = lineJson;
	}
	private String secondJson="[{x:500,y:500,z:500}]";
	
	public String getSecondJson() {
		return secondJson;
	}

	public void setSecondJson(String secondJson) {
		this.secondJson = secondJson;
	}

	public Boolean getSecondPlot() {
		return secondPlot;
	}

	public void setSecondPlot(Boolean secondPlot) {
		this.secondPlot = secondPlot;
	}

	public String setControls(String controls){
		this.controlsPage = controls;
		return "verticalSplit";
	}
	
	public String getControlsPage() {
		return controlsPage;
	}
	public void setControlsPage(String controlsPage) {
		this.controlsPage = controlsPage;
	}
	public String getJsonData() {
		return jsonData;
	}
	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	public String getPlotPage() {
		return plotPage;
	}

	public void setPlotPage(String plotPage) {
		this.plotPage = plotPage;
	}

	public Boolean getThirdPlot() {
		return thirdPlot;
	}

	public void setThirdPlot(Boolean thirdPlot) {
		this.thirdPlot = thirdPlot;
	}

	public String getThirdJson() {
		return thirdJson;
	}

	public void setThirdJson(String thirdJson) {
		this.thirdJson = thirdJson;
	}

	public String getxAxisLabel() {
		return xAxisLabel;
	}

	public void setxAxisLabel(String xAxisLabel) {
		this.xAxisLabel = xAxisLabel;
	}

	public String getyAxisLabel() {
		return yAxisLabel;
	}

	public void setyAxisLabel(String yAxisLabel) {
		this.yAxisLabel = yAxisLabel;
	}

	public String getzAxisLabel() {
		return zAxisLabel;
	}

	public void setzAxisLabel(String zAxisLabel) {
		this.zAxisLabel = zAxisLabel;
	}

	public Boolean getSecondLine() {
		return secondLine;
	}

	public void setSecondLine(Boolean secondLine) {
		this.secondLine = secondLine;
	}

	public Boolean getFirstPlot() {
		return firstPlot;
	}

	public void setFirstPlot(Boolean firstPlot) {
		this.firstPlot = firstPlot;
	}

	public String getSecondLineJson() {
		return secondLineJson;
	}

	public void setSecondLineJson(String secondLineJson) {
		this.secondLineJson = secondLineJson;
	}
	


}
