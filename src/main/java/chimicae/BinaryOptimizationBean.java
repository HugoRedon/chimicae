package chimicae;

import hugo.productions.google.ChartType;
import hugo.productions.google.GoogleColumn;
import hugo.productions.google.GoogleColumnType;
import hugo.productions.google.GoogleDataTable;
import hugo.productions.google.GoogleGraphInfo;
import hugo.productions.google.GoogleOptions;
import hugo.productions.google.GoogleRow;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import termo.component.Compound;
import termo.data.ExperimentalDataBinary;
import termo.data.ExperimentalDataBinaryList;
import termo.matter.Heterogeneous;
import termo.matter.HeterogeneousMixture;
import termo.matter.HeterogeneousSubstance;
import termo.matter.Homogeneous;
import termo.matter.Mixture;
import termo.matter.Substance;
import termo.matter.builder.HeterogeneousMixtureBuilder;
import termo.optimization.Parameters_Error;
import termo.optimization.errorfunctions.ErrorFunction;
import termo.optimization.errorfunctions.TemperatureMixtureErrorData;

@Named("binaryOptimizationBean")
@SessionScoped
public class BinaryOptimizationBean implements Serializable,PropertyChangeListener {


	private static final long serialVersionUID = 1555348924467092561L;
	@Inject HomogeneousBean homogeneousBean;
	
	@PostConstruct
	public void init(){
		homogeneousBean.addPropertyChangelistener(this);
		update();
		selectedMix = getHmList().get(0);
		Iterator<Compound> it= selectedMix.getComponents().iterator();
		Compound methanol = null;
		Compound water = null;
		while(it.hasNext()){
			Compound com = it.next();
			if(com.getName().equals("water")){
				water = com;
			}else if(com.getName().equals("methanol")){
					methanol = com;
			}
		}		
		try{
			ExperimentalDataBinaryList blist = getBinaryExperimentalListFromFileTxy("/data/binary.dat",methanol,water);
			selectedMix.getErrorfunction().setExperimental(blist);
		}catch(Exception e){
			System.out.println("Cant read binary dat");
		}
		
	}
	

	
	public List<String> readFile(String filePath) {
		List<String> lines =new ArrayList<>();
		try{
			InputStream file = this.getClass().getResourceAsStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(file));
			String linew =null;
			while ((linew = br.readLine()) != null) {
				lines.add(linew);
			}
		}catch(IOException e){
			System.out.println("Cant read file " + filePath);
		}
		return lines;
	}
	
	public ExperimentalDataBinaryList getBinaryExperimentalListFromFileTxy(
			String filePath,Compound methanol,Compound water) throws IOException{
				
		List<String> lines = readFile(filePath);
		    
		ExperimentalDataBinaryList blist = new ExperimentalDataBinaryList();
		blist.setName("Ejemplo");
		
		
		double pressure = Double.valueOf(lines.get(0).split("\\s+")[1]);

		List<ExperimentalDataBinary> list = new ArrayList();
		
		for(String line: lines){
			 String[] lineWords = line.split("\\s+");
			 
			 System.out.println(line);
			 
			 try{
				 double temperature =Double.valueOf( lineWords[0]);
				 double x =Double.valueOf( lineWords[1]);
				 double y=Double.valueOf( lineWords[2]);
				 
				 ExperimentalDataBinary data = new ExperimentalDataBinary(temperature, pressure, x, y);
				 list.add(data);
			 }catch(Exception ex){
				 System.out.println("Linea sin datos");
			 }
			 
		}
		blist.setReferenceComponent(methanol);
		blist.setNonReferenceComponent(water);
		blist.setList(list);
		return blist;
	}
	public String id(HeterogeneousMixture mix){
		StringBuilder sb = new StringBuilder();
		sb.append("Mezcla, ");
		sb.append("[");
		
		Iterator<Substance> iterator = mix.getLiquid().getPureSubstances().iterator();
		while(iterator.hasNext()){
			Substance sub =iterator.next();
			Compound com = sub.getComponent();
			
			sb.append( com.getName());
			if(iterator.hasNext()){
				sb.append(",");
			}
		}
		sb.append("],");
		sb.append("Regla de mezclado:" + mix.getMixingRule().getName());
		return sb.toString();
	}
	
	List<HeterogeneousMixture> hmList = new ArrayList<>();
	HeterogeneousMixture selectedMix;
	
	
	public boolean isMixtureWithTwoCompounds(Homogeneous homogeneous){
		if(homogeneous instanceof Substance){
			return false;
		}
		Mixture mix = (Mixture)homogeneous;
		if(mix.getComponents().size() != 2){
			return false;
		}
		return true;
	}

	public void update(){
		hmList.clear();
		for(Homogeneous homogeneous:homogeneousBean.getHomogeneousList()){
			if(isMixtureWithTwoCompounds(homogeneous)){
				HeterogeneousMixture hm = getHeterogeneous((Mixture)homogeneous);
				hmList.add(hm);
			}
		}
	}
	public void resetParametersToZero(){
		for(ParameterViewModel pvm : getParameterList()){
			pvm.setValue(0);
		}
	}
	public List<HeterogeneousMixture> getHmList() {
		return hmList;
	}

	public void setHmList(List<HeterogeneousMixture> hmList) {
		this.hmList = hmList;
	}

	public HeterogeneousMixture getHeterogeneous(Mixture mix){
		HeterogeneousMixture hetMixture =new HeterogeneousMixtureBuilder().fromMixture(mix);
		return hetMixture;
	}
	
	public Heterogeneous getHeterogeneous(Substance substance){
		return new HeterogeneousSubstance(substance.getCubicEquationOfState(),
				substance.getAlpha(),substance.getComponent());
	}
	public List<ParameterViewModel> getParameterList(){
		List<ParameterViewModel> result= new ArrayList<>();

        ErrorFunction errorFunction = selectedMix.getErrorfunction();
        int numberOfParams = errorFunction.numberOfParameters();
        
        for(int i = 0 ; i < numberOfParams; i++){
            ParameterViewModel parame = new ParameterViewModel(
            		i,
            		selectedMix.
            			getErrorfunction().getOptimizer(),
            			selectedMix.getMixingRule());
            result.add(parame);
        }              
		return result;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		update();
	}

	public HeterogeneousMixture getSelectedMix() {
		return selectedMix;
	}

	public void setSelectedMix(HeterogeneousMixture selectedMix) {
		this.selectedMix = selectedMix;
	}
	
	 public GoogleGraphInfo errorGraphData(){
	        GoogleDataTable result = new GoogleDataTable();
	        result.addColumns( 
	                new GoogleColumn("fraction", "Fracción molar (líquido)", GoogleColumnType.number),
	                new GoogleColumn("error", "Error relativo", GoogleColumnType.number));
	        for (TemperatureMixtureErrorData row: selectedMix.getErrorfunction().getErrorForEachExperimentalData()){
	            double liquidFraction = row.getLiquidFraction();
	            double error = row.getRelativeError();
	            
	            if(Double.isNaN(error)){
	                result.addRow(new GoogleRow(liquidFraction));
	            }else{
	                result.addRow(new GoogleRow(liquidFraction,error));
	            }
	        }

	        GoogleGraphInfo errorGraph = new GoogleGraphInfo();
	        errorGraph.setData(result);
	         errorGraph.setOptions(GoogleOptions.googleOptions(
	               "Error relativo",
	               "Fracción molar (líquido)" ,
	               "Error relativo",
	               ChartType.scatter));
	        return errorGraph;

	        
	    }
	 
	    

	      public GoogleGraphInfo errorVsIteration(){
	        GoogleDataTable result = new GoogleDataTable();
	         
	        result.addColumns(
	                new GoogleColumn("iteration", "Iteración", GoogleColumnType.number),
	                new GoogleColumn("error", "error total", GoogleColumnType.number)
	                );
	        
	        int numberOfParameters = selectedMix.getErrorfunction().numberOfParameters();
	        for(Integer i = 0; i < numberOfParameters ; i++){
	           result.addColumn(new GoogleColumn(String.valueOf(i),"Parametro "+ i , GoogleColumnType.number));
	        }
	        
	       
	        boolean clearBecauseOfError = false; //todo corregir codigo
	        for (Parameters_Error row:selectedMix.getErrorfunction().getOptimizer().getConvergenceHistory()){
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
	        	selectedMix.getErrorfunction().getOptimizer().setConvergenceHistory(new ArrayList());
	            
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
	    private Double ifNotANumberOrInfinityNull(Double aNumber){
	        return  (Double.isNaN(aNumber) || Double.isInfinite(aNumber))?null:aNumber;
	    }
	    public GoogleGraphInfo dataTable(){
	         System.out.println("paint");
	        GoogleDataTable table = new GoogleDataTable( );
	        Compound sel = selectedMix.getErrorfunction().getReferenceComponent();
	        table.addColumns(
	            new GoogleColumn("molarFraction", "Fracción molar " + sel , GoogleColumnType.number),
	            new GoogleColumn("Texp", "Temperatura experimental [K] (liquido)" , GoogleColumnType.number),
	            new GoogleColumn("Tcalc", "Temperatura calculada [K] (liquido)" , GoogleColumnType.number),
	            new GoogleColumn("TexpV", "Temperatura experimental [K] (vapor)", GoogleColumnType.number),
	            new GoogleColumn("TcalcV", "Temperatura calculada [K] (vapor)", GoogleColumnType.number)
	        );
	      
	        for (TemperatureMixtureErrorData row: selectedMix.getErrorfunction().getErrorForEachExperimentalData()){
	            double liquidFraction = row.getLiquidFraction();
	            
	            double expT = row.getExperimentalTemperature();            
	            Double calcT = row.getCalculatedTemperature();
	            calcT = (Double.isNaN(calcT)?null: calcT);
	            
	            table.addRow(new GoogleRow(liquidFraction, expT,calcT));
	            
	        }
	        for (TemperatureMixtureErrorData row: selectedMix.getErrorfunction().getErrorForEachExperimentalData()){
	             double expT = row.getExperimentalTemperature();   
	             double expY = row.getExperimentalVaporFraction();
	             table.addRow( new GoogleRow(expY, null,null,expT));
	        }
	        for (TemperatureMixtureErrorData row: selectedMix.getErrorfunction().getErrorForEachExperimentalData()){
	            double expT = row.getExperimentalTemperature();   
	            double expY = row.getExperimentalVaporFraction();
	            Double calcT = row.getCalculatedTemperature();
	            Double calcY = row.getCalculatedVaporFraction();

	            calcT = (Double.isNaN(calcT)?null: calcT);
	            calcY = (Double.isNaN(calcY)? null: calcY);
	            table.addRow( new GoogleRow(calcY,null, null,null,  calcT));
	            
	        }
	             
	        
	        
	        
	        GoogleGraphInfo pressureComparison = new GoogleGraphInfo();
	        pressureComparison.setData(table);
	         pressureComparison.setOptions(GoogleOptions.googleOptions(
	               "Fracción molar vs Temperatura", 
	               "Fracción molar", 
	               "Temperatura[K]",ChartType.scatterFunctionScatterFunction));
	        return pressureComparison;
	    }
	     
}
