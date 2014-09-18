package plot;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named("linesPlotsBean")
@SessionScoped
public class LinesPlotsBean implements Serializable {

	private static final long serialVersionUID = 9157108038195662424L;
	
	String info ="Información de la gráfica";
	String liquidLineJson ;
	String vaporLineJson;
	
	String liquidAreaTemperatureLines;
	String vaporAreaTemperatureLines;
	
	
	
	

	public String getVaporAreaTemperatureLines() {
		return vaporAreaTemperatureLines;
	}

	public void setVaporAreaTemperatureLines(String vaporAreaTemperatureLines) {
		this.vaporAreaTemperatureLines = vaporAreaTemperatureLines;
	}

	public String getLiquidAreaTemperatureLines() {
		return liquidAreaTemperatureLines;
	}

	public void setLiquidAreaTemperatureLines(String liquidAreaTemperatureLines) {
		this.liquidAreaTemperatureLines = liquidAreaTemperatureLines;
	}

	public String getVaporLineJson() {
		return vaporLineJson;
	}

	public void setVaporLineJson(String vaporLineJson) {
		this.vaporLineJson = vaporLineJson;
	}

	public String getLiquidLineJson() {
		return liquidLineJson;
	}

	public void setLiquidLineJson(String liquidLineJson) {
		this.liquidLineJson = liquidLineJson;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public LinesPlotsBean() {
		// TODO Auto-generated constructor stub
	}

}
