package chimicae;

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
import termo.optimization.errorfunctions.ErrorFunction;

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
	
	public ExperimentalDataBinaryList getBinaryExperimentalListFromFileTxy(
			String filePath,Compound methanol,Compound water) throws IOException{
		InputStream file = this.getClass().getResourceAsStream(filePath);
		//Path path = Paths.get(filePath);
		
		
		List<String> lines =new ArrayList<>();//Files.readAllLines(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(file));
		String linew =null;
		while ((linew = br.readLine()) != null) {
			lines.add(linew);
		}         
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
		// TODO Auto-generated method stub
		update();
	}

	public HeterogeneousMixture getSelectedMix() {
		return selectedMix;
	}

	public void setSelectedMix(HeterogeneousMixture selectedMix) {
		this.selectedMix = selectedMix;
	}
}
