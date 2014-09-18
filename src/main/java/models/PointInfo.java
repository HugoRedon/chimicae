package models;

public 
class PointInfo{
	double temperature;
	double pressure;
	double enthalpy;
	double entropy;
	double gibbs;
	double molarVolume;
	
	
	public double getTemperature() {
		return temperature;
	}
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	public double getPressure() {
		return pressure;
	}
	public void setPressure(double pressure) {
		this.pressure = pressure;
	}
	public double getEnthalpy() {
		return enthalpy;
	}
	public void setEnthalpy(double enthalpy) {
		this.enthalpy = enthalpy;
	}
	public double getEntropy() {
		return entropy;
	}
	public void setEntropy(double entropy) {
		this.entropy = entropy;
	}
	public double getGibbs() {
		return gibbs;
	}
	public void setGibbs(double gibbs) {
		this.gibbs = gibbs;
	}
	public double getMolarVolume() {
		return molarVolume;
	}
	public void setMolarVolume(double molarVolume) {
		this.molarVolume = molarVolume;
	}
}
