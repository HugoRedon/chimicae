package cp;

import termo.Constants;
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
		double minimumTempStep = 5;
		Double minN = (temperature-referenceTemperature)/minimumTempStep;
		Integer minNInt = ((Double)Math.ceil(minN)).intValue();
			
		
		Integer n = (minNInt > 50)?minNInt:50;
		double result=0;
		double tempStep = (temperature-referenceTemperature)/n.doubleValue();
		
		for(Integer i =0;i< n; i++){
			double ti = referenceTemperature + i.doubleValue()*tempStep;
			double tid = referenceTemperature + (i.doubleValue()+1)* tempStep;
			double cpi = cp(ti);
			double cpid = cp(tid);
			result +=tempStep*(cpid + cpi)/2;
		}
		return result;
	}
	public double cpOverTIntegral(double referenceTemperature,double temperature){
		double minimumTempStep = 5;
		Double minN = (temperature-referenceTemperature)/minimumTempStep;
		Integer minNInt = ((Double)Math.ceil(minN)).intValue();
			
		
		Integer n = (minNInt > 50)?minNInt:50;
		double result = 0;
		double tempStep = (temperature-referenceTemperature)/n.doubleValue();
		for(Integer i =0;i<n;i++){
			double ti = referenceTemperature + i.doubleValue()*tempStep;
			double tid = referenceTemperature + (i.doubleValue()+1)* tempStep;
			double cpi = cp(ti)/ti;
			double cpid = cp(tid)/tid;
			result += tempStep*(cpid+cpi)/2;
		}
		return result;
	}
	

	@Override
	public double idealGasEntropy(double temperature,
			double referenceTemperature, double pressure,
			double referencePressure, double entropyReference) {
		double result =0;
		
		double cpOverTIntegral = cpOverTIntegral(referenceTemperature, temperature);
		
		result = entropyReference+ cpOverTIntegral - Constants.R * Math.log(pressure/referencePressure);
		
		
		return result;
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
