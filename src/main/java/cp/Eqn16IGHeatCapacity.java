package cp;

import termo.cp.CpEquation;

public class Eqn16IGHeatCapacity implements CpEquation {
	
	
	private Double A;
	private Double B;
	private Double C;
	private Double D;
	private Double E;
	

	@Override
	public double cp(double T) {
		return A + Math.exp(B / T + C + D * T + E * Math.pow( T , 2));
	}

	@Override
	public double Enthalpy(double temperature, double referenceTemperature,
			double enthalpyReference) {

		return enthalpyReference + cpIntegral(referenceTemperature, temperature);
	}
	
	public double cpIntegral(double referenceTemperature,double temperature){
		Integer n = 50;
		double result=0;
		double tempStep = (temperature-referenceTemperature)/n.doubleValue();
		for(Integer i =0;i< n; i++){
			double ti = referenceTemperature + i.doubleValue();
			double tid = referenceTemperature + i.doubleValue()* tempStep;
			double cpi = cp(ti);
			double cpid = cp(tid);
			result +=tempStep*(cpid + cpi)/2;
		}
		return result;
	}
	

	@Override
	public double idealGasEntropy(double temperature,
			double referenceTemeprature, double pressure,
			double referencePressure, double entropyReference) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getMathEquation() {
		// TODO Auto-generated method stub
		return null;
	}

	public Double getA() {
		return A;
	}

	public void setA(Double a) {
		A = a;
	}

	public Double getB() {
		return B;
	}

	public void setB(Double b) {
		B = b;
	}

	public Double getC() {
		return C;
	}

	public void setC(Double c) {
		C = c;
	}

	public Double getD() {
		return D;
	}

	public void setD(Double d) {
		D = d;
	}

	public Double getE() {
		return E;
	}

	public void setE(Double e) {
		E = e;
	}

}
