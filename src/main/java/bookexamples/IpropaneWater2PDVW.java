package bookexamples;

import java.util.HashSet;
import java.util.Set;

import termo.binaryParameter.ActivityModelBinaryParameter;
import termo.component.Compound;
import termo.eos.Cubic;
import termo.eos.EquationsOfState;
import termo.eos.alpha.Alphas;
import termo.eos.mixingRule.MixingRule;
import termo.eos.mixingRule.TwoParameterVanDerWaals;
import termo.matter.HeterogeneousMixture;
import chimicae.AvailableCompounds;

public class IpropaneWater2PDVW  {

//	public IpropaneWater2PDVW(AvailableCompounds availableCompounds) {
//		super(availableCompounds,files(
//				"/data/2propanolWater/2propanolWater_353_liquid.txt",
//				"/data/2propanolWater/2propanolWater_353_vapor.txt"));
//	}
//
//	
//	public void createCompoundsAndMixture() {
//		referenceCompound = availableCompounds.getCompoundByExactName("isopropanol");
//		referenceCompound.setK_StryjekAndVera(0.23264);
//		
//		nonReferenceCompound = availableCompounds.getCompoundByExactName("water");
//		nonReferenceCompound.setK_StryjekAndVera(-0.06635);
//		
//		Set<Compound> components = new HashSet<>();
//		
//		components.add(referenceCompound);
//		components.add(nonReferenceCompound);
//		
//		Cubic equationOfState = EquationsOfState .pengRobinson();
//		MixingRule mr = new TwoParameterVanDerWaals();
//		
//		ActivityModelBinaryParameter k = new ActivityModelBinaryParameter();
//					
//		k.getTwoParameterVanDerWaals().setValue(referenceCompound, nonReferenceCompound, 0.0953);
//		k.getTwoParameterVanDerWaals().setValue(nonReferenceCompound, referenceCompound, 0.0249);
//		
//		hm = new HeterogeneousMixture(equationOfState,
//				Alphas.getStryjekAndVeraExpression(),
//				mr, 
//				components, k);
//		
//		hm.setTemperature(353);
//			
//		
//	}

//	public void calculateVaporLine(GoogleDataTable table){
//		 
//	       
//	       double minX = 0;//getMinX(vaporLine);
//	       double maxX =0.9;// getMaxX(vaporLine);
//	       
//	       double maxY = getMaxY(vaporLine);
//	       Integer n = 50;
//	       
//	       double xStep = (maxX - minX)/n.doubleValue();
//	       for(Integer i =0;i < n; i++ ){
//	    	   double vaporMolarFraction = minX  + i.doubleValue() * xStep;
//	    	   
//	    	   Map<String,Double>vaporFractions = hm.getLiquid().getFractions();
//	    	   double pressureEstimate = hm.getPressure();
//	    	   
//	    	   hm.setZFraction(referenceCompound, vaporMolarFraction);
//	    	   hm.setZFraction(nonReferenceCompound, 1-vaporMolarFraction);
//	    	   
//	    	   if(i==0){
//	    		   hm.dewPressure();
//	    	   }else{
//	    		   hm.dewPressure(pressureEstimate,vaporFractions);
//	    	   }
//	    	   Double pressure = hm.getPressure();
//	    	   if(!pressure.isNaN()){
//	    		   table.addRow(new GoogleRow(vaporMolarFraction,null,null,null,pressure));
//	    	   }else{
//	    		   pressure = pressureEstimate;
//	    		   for(IterationInfo ii:hm.getCalculationReport()){
//	    			   System.out.println("pressure: " + ii.getPressure() + " pressure_: "+ ii.getPressure_() 
//	    					   + " temperature: " + ii.getTemperature() + " newPressure : " + ii.getNewPressure() 
//	    					   + " error: " +ii.getError() + " error_: " + ii.getError_());
//	    		   }
//	    	   }
//	       }
//	       
//	       
//	       
//	       
//	}
//	
//	public void calculateLiquidLine(GoogleDataTable table){
//		double minX =0;// getMinX(liquidLine);
//	       double maxX = getMaxX(liquidLine);
//	       
//	       Integer n = 120;
//	       double xStep = (maxX - minX)/n.doubleValue();
//	       for(Integer i =n;i > 0; i-- ){
//	    	   double liquidMolarFraction = minX  + i.doubleValue() * xStep;
//	    	   
//	    	   Map<String,Double>vaporFractions = hm.getVapor().getFractions();
//	    	   double pressureEstimate = hm.getPressure();
//	    	   
//	    	   hm.setZFraction(referenceCompound, liquidMolarFraction);
//	    	   hm.setZFraction(nonReferenceCompound, 1-liquidMolarFraction);
//	    	   
//	    	   if(i==0){
//	    		   hm.bubblePressure();
//	    	   }else{
//	    		   hm.bubblePressure(pressureEstimate,vaporFractions);
//	    	   }
//	    	   Double pressure = hm.getPressure();
//	    	   if(!pressure.isNaN()){
//	    		   table.addRow(new GoogleRow(liquidMolarFraction,null,pressure));
//	    	   }else{
//	    		   pressure = pressureEstimate;
//	    		   for(IterationInfo ii:hm.getCalculationReport()){
//	    			   System.out.println("pressure: " + ii.getPressure() + " pressure_: "+ ii.getPressure_() 
//	    					   + " temperature: " + ii.getTemperature() + " newPressure : " + ii.getNewPressure() 
//	    					   + " error: " +ii.getError() + " error_: " + ii.getError_());
//	    		   }
//	    	   }
//	       }
//	}



}
