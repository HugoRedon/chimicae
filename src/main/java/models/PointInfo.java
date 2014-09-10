package models;

public 
class PointInfo{
	Double temperature;
	Double pressure;
	Double enthalpy;
	Double entropy;
	Double gibbs;
	Double molarVolume;
	
	
	public Double getTemperature() {
		return temperature;
	}
	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}
	public Double getPressure() {
		return pressure;
	}
	public void setPressure(Double pressure) {
		this.pressure = pressure;
	}
	public Double getEnthalpy() {
		return enthalpy;
	}
	public void setEnthalpy(Double enthalpy) {
		this.enthalpy = enthalpy;
	}
	public Double getEntropy() {
		return entropy;
	}
	public void setEntropy(Double entropy) {
		this.entropy = entropy;
	}
	public Double getGibbs() {
		return gibbs;
	}
	public void setGibbs(Double gibbs) {
		this.gibbs = gibbs;
	}
	public Double getMolarVolume() {
		return molarVolume;
	}
	public void setMolarVolume(Double molarVolume) {
		this.molarVolume = molarVolume;
	}
}
