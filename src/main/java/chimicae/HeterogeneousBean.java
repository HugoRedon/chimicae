package chimicae;

import hugo.productions.google.ChartType;
import hugo.productions.google.GoogleColumn;
import hugo.productions.google.GoogleColumnType;
import hugo.productions.google.GoogleDataTable;
import hugo.productions.google.GoogleGraphInfo;
import hugo.productions.google.GoogleOptions;
import hugo.productions.google.GoogleRow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import termo.component.Compound;
import termo.data.ExperimentalDataList;
import termo.matter.HeterogeneousSubstance;
import termo.matter.Homogeneous;
import termo.matter.Substance;
import termo.optimization.ErrorData;
import termo.optimization.Parameters_Error;
import termo.optimization.errorfunctions.ErrorFunction;

@Named("heterogeneousBean")
@SessionScoped
public class HeterogeneousBean implements Serializable {
	private static final long serialVersionUID = -6997182885598930870L;
	
	@Inject HomogeneousBean homogeneousBean;
	private HeterogeneousSubstance selectedHeterogeneousSubstance;
	
	@PostConstruct
	public void init(){
		System.out.println("HeterogeneousBean init----------------");
		selectedHeterogeneousSubstance = getHeterogeneousSubstanceList().get(0);
	}
	
	public void resetParametersToZero(){
		for(ParameterViewModel pvm : getParametersList()){
			pvm.setValue(0);
		}
	}
	
	public List<HeterogeneousSubstance>getHeterogeneousSubstanceList(){
		List<HeterogeneousSubstance> result = new ArrayList<>();
		for(Homogeneous homogeneous: homogeneousBean.getHomogeneousList()){
			if(homogeneous instanceof Substance){
				
				Compound com = ((Substance) homogeneous).getComponent();
				HeterogeneousSubstance hSub = new HeterogeneousSubstance(
								homogeneous.getCubicEquationOfState(), 
								((Substance) homogeneous).getAlpha(),
								((Substance) homogeneous).getComponent());
				result.add(hSub);
				ExperimentalDataList list = com.getExperimentalLists().iterator().next();
				hSub.getErrorFunction().setExperimental(list.getList());
			}
		}
		return result;
	}

	public HeterogeneousSubstance getSelectedHeterogeneousSubstance() {
		System.out.println(selectedHeterogeneousSubstance.getComponent().getName());
		return selectedHeterogeneousSubstance;
	}

	public void setSelectedHeterogeneousSubstance(
			HeterogeneousSubstance selectedHeterogeneousSubstance) {
		System.out.println(selectedHeterogeneousSubstance.getComponent().getName());
		this.selectedHeterogeneousSubstance = selectedHeterogeneousSubstance;
	}
    public List<ParameterViewModel> getParametersList(){
        List<ParameterViewModel> result = new ArrayList();

        ErrorFunction errorFunction = selectedHeterogeneousSubstance.getErrorFunction();
        int numberOfParams = errorFunction.numberOfParameters();
        
        for(int i = 0 ; i < numberOfParams; i++){
            ParameterViewModel parame = new ParameterViewModel(
            		i,
            		selectedHeterogeneousSubstance.
            			getErrorFunction().getOptimizer(),
            			selectedHeterogeneousSubstance.getAlpha());
            result.add(parame);
        }
        
        return result;
    }
	
	
    private boolean logarithm;
	@Inject HeterogeneousBean heterogeneousBean;
	
	
	public void optimizeSelected(){
		selectedHeterogeneousSubstance.getErrorFunction().minimize();
	}
	
	 public GoogleGraphInfo dataTable(){
	        
	        GoogleDataTable table = new GoogleDataTable( );
	        
	        String log = "";
	        log = isLogarithm()? "LN(":log;
	        
	        String log_ = "";
	        log_ = isLogarithm()? ")":log_;
	        
	        
	        table.addColumns(
	                new GoogleColumn("temp", log+ "Temperatura [K]" + log_, GoogleColumnType.number),
	                new GoogleColumn("Pexp", log+"Presión experimental [Pa]" + log_, GoogleColumnType.number),
	                new GoogleColumn("Pcalc", log + "Presión calculada [Pa]" + log_, GoogleColumnType.number)
	                );
	  
	        for (ErrorData row: selectedHeterogeneousSubstance.getErrorFunction().getErrorForEachExperimentalData()){
	            double expP = row.getExperimentalPressure();
	            double temperature = row.getTemperature();
	            double calcP = row.getCalculatedPressure();
	            
	            expP = isLogarithm()? Math.log(expP):expP;
	            temperature = isLogarithm()? Math.log(temperature):temperature;
	            calcP = isLogarithm()? Math.log(calcP):calcP;
	            
	            
	            if(Double.isNaN(calcP)){
	                table.addRow(new GoogleRow(temperature, expP));
	            }else{
	                table.addRow(new GoogleRow(temperature, expP,calcP));
	            }
	        }
	        GoogleGraphInfo pressureComparison = new GoogleGraphInfo();
	        pressureComparison.setData(table);
	         pressureComparison.setOptions(GoogleOptions.googleOptions(
	               "Presión experimental vs presión calculada", 
	               "Temperatura [K]", 
	               "Presión[Pa]",ChartType.scatterFunction));
	        return pressureComparison;
	    }
	       
	         private Double ifNotANumberOrInfinityNull(Double aNumber){
	            return  (Double.isNaN(aNumber) || Double.isInfinite(aNumber))?null:aNumber;
	        }
	       
	         
	         
	         
        public GoogleGraphInfo errorVsIteration(){
        	
	        GoogleDataTable result = new GoogleDataTable();
	         
	        result.addColumns(
	                new GoogleColumn("iteration", "Iteración", GoogleColumnType.number),
	                new GoogleColumn("error", "error total", GoogleColumnType.number)
	                );
	        
	        int numberOfParameters = selectedHeterogeneousSubstance.getVapor().getAlpha().numberOfParameters();
	        for(Integer i = 0; i < numberOfParameters ; i++){
	           result.addColumn(new GoogleColumn(String.valueOf(i),"Parametro "+ i , GoogleColumnType.number));
	        }
	        
	       
	        boolean clearBecauseOfError = false; //todo corregir codigo
	        for (Parameters_Error row:selectedHeterogeneousSubstance.getErrorFunction().getOptimizer().getConvergenceHistory()){
	            double error = row.getError();
	            int iter = row.getIteration();
	            
	            
	            Number[]  collectionToAdd = new Number[numberOfParameters + 2];
	            //System.out.println("numberofparameters + 2"+(numberOfParameters + 2));
	            collectionToAdd[0] = iter;
	            collectionToAdd[1] = ifNotANumberOrInfinityNull(error);
	            for(int i = 0; i < numberOfParameters;i++){
	                
	                try{
	                    collectionToAdd[i+2] 
	                            = ifNotANumberOrInfinityNull(
	                                    row.getParameters()[i]);

	                }catch(IndexOutOfBoundsException ex){
	                    System.out.println("IndexOutofBoundsException ");
	                    System.out.println("i=" + i );
	                    System.out.println("collectionToAdd.length=" + collectionToAdd.length);
	                    clearBecauseOfError = true;
	                }
	            }
	            
	            result.addRow(new GoogleRow(collectionToAdd));
	            
	        }
	        if(clearBecauseOfError ){
	        	selectedHeterogeneousSubstance.getErrorFunction().getOptimizer().setConvergenceHistory(new ArrayList());
	            
	        }
	         GoogleGraphInfo historyConvergence = new GoogleGraphInfo();
	       historyConvergence.setData(result);
	       historyConvergence.setOptions(GoogleOptions.googleOptions(
	               "Historia de convergencia",
	               "Iteración",
	               "Error, parametros de alpha",
	               ChartType.multipleScatter));
	       
	       
	       return historyConvergence;
	    }

	    
	    
	    public GoogleGraphInfo errorGraphData(){
	    	
	        GoogleDataTable result = new GoogleDataTable();
	        result.addColumns( 
	                new GoogleColumn("temp", "Temperatura [K]", GoogleColumnType.number),
	                new GoogleColumn("error", "Error relativo", GoogleColumnType.number));
	        for (ErrorData row: selectedHeterogeneousSubstance.getErrorFunction().getErrorForEachExperimentalData()){
	            double temperature = row.getTemperature();
	            double error = row.getError();
	            
	            if(Double.isNaN(error)){
	                result.addRow(new GoogleRow(temperature));
	            }else{
	                result.addRow(new GoogleRow(temperature,error));
	            }
	        }

	        GoogleGraphInfo errorGraph = new GoogleGraphInfo();
	        errorGraph.setData(result);
	         errorGraph.setOptions(GoogleOptions.googleOptions(
	               "Error relativo",
	               "Temperatura [K]" ,
	               "Error relativo",
	               ChartType.scatter));
	        return errorGraph;

	        
	    }

		public boolean isLogarithm() {
			return logarithm;
		}

		public void setLogarithm(boolean logarithm) {
			this.logarithm = logarithm;
		}



}
