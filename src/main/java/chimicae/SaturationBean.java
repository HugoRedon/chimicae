package chimicae;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import excel.SaturationPressureReport;
import termo.component.Compound;
import termo.matter.Heterogeneous;
import termo.matter.HeterogeneousMixture;
import termo.matter.HeterogeneousSubstance;
import termo.matter.Homogeneous;
import termo.matter.Mixture;
import termo.matter.Substance;
import termo.matter.builder.HeterogeneousMixtureBuilder;

@Named("saturationBean")
@SessionScoped
public class SaturationBean implements Serializable,PropertyChangeListener	 {
	private static final long serialVersionUID = -716210662824991410L;
	@Inject HomogeneousBean homogeneousBean;
	
	
	List<Heterogeneous> heterogeneousList= new ArrayList<>();
	Heterogeneous selectedHeterogeneous ;
	
	Integer iterations;
	
	public void download(SaturationPressureReport spr) throws IOException {
	    FacesContext fc = FacesContext.getCurrentInstance();
	    ExternalContext ec = fc.getExternalContext();

	    ec.responseReset(); 
	   ec.setResponseContentType("text/csv");
	    String fileName = "saturationPressureReport.xlsx";
	    ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\""); 

	    OutputStream output = ec.getResponseOutputStream();
	    
    	spr.createReport((HeterogeneousMixture)selectedHeterogeneous).write(output);
	    
		  
	    output.close();
	    fc.responseComplete(); // Important! Otherwise JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.
	}
		

	
	public void saturationPressure(){
		if(selectedHeterogeneous instanceof HeterogeneousSubstance){
			iterations=((HeterogeneousSubstance) selectedHeterogeneous).bubblePressure();
		}else{
			iterations=((HeterogeneousMixture)selectedHeterogeneous).bubblePressure();
		}
			
	}
	public void saturationPressureReport()throws IOException{
		if(selectedHeterogeneous instanceof HeterogeneousSubstance){
			iterations=((HeterogeneousSubstance) selectedHeterogeneous).bubblePressure();
		}else{
			SaturationPressureReport spr =new SaturationPressureReport();
			if(selectedHeterogeneous instanceof HeterogeneousMixture){
				download(spr);
			}
		}
		
	}
	
	
	@PostConstruct
	public void SaturationBeanConstructor() {
		homogeneousBean.addPropertyChangelistener(this);
		update();
		selectedHeterogeneous = heterogeneousList.get(0);
	}
	
	public void update(){
		List<Homogeneous> homogeneousList = homogeneousBean.getHomogeneousList();
		heterogeneousList.clear();
		System.out.println("size homogeneous"  + homogeneousList.size());
		for(Homogeneous h: homogeneousList){
			if(h instanceof Substance){				
				HeterogeneousSubstance hSub = new HeterogeneousSubstance(
								h.getCubicEquationOfState(), 
								((Substance) h).getAlpha(),
								((Substance) h).getComponent());
				heterogeneousList.add(hSub);
			}else{
				HeterogeneousMixture hm = new HeterogeneousMixtureBuilder().fromMixture((Mixture)h);
				heterogeneousList.add(hm);
			}
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		update();
	}
	public List<Heterogeneous> getHeterogeneousList() {
		return heterogeneousList;
	}

	public void setHeterogeneousList(List<Heterogeneous> heterogeneousList) {
		this.heterogeneousList = heterogeneousList;
	}
	public Heterogeneous getSelectedHeterogeneous() {
		return selectedHeterogeneous;
	}

	public void setSelectedHeterogeneous(Heterogeneous selectedHeterogeneous) {
		this.selectedHeterogeneous = selectedHeterogeneous;
	}


	public Integer getIterations() {
		return iterations;
	}


	public void setIterations(Integer iterations) {
		this.iterations = iterations;
	}
}