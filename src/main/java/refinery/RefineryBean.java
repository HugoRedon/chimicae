package refinery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import termo.matter.Heterogeneous;
import termo.matter.HeterogeneousSubstance;
import termo.matter.Homogeneous;
import termo.matter.Mixture;
import termo.matter.Substance;
import termo.matter.builder.HeterogeneousMixtureBuilder;
import chimicae.HomogeneousBean;
import chimicae.SaturationBean;

@Named("refineryBean")
@SessionScoped
public class RefineryBean implements Serializable{

	private static final long serialVersionUID = 3244528474822301499L;		
	@Inject SaturationBean saturationBean;
	
	List<Tank> tanks = new ArrayList<>();
	int equipmentCount=-1;
	
	public String createTank(double temperature,double pressure){
		
		Heterogeneous het = saturationBean.getSelectedHeterogeneous();
		het.setPressure(pressure);
		het.setTemperature(temperature);
		int idTank =++equipmentCount ;
		
		Tank tank = new Tank(idTank,het);
		String rep ="";
		try{
			rep= tank.toJson();
		}catch(Exception e){
			String m = e.getMessage();
			System.out.println(m);
		}
		return rep;
	}

	public int getEquipmentCount() {
		return equipmentCount;
	}

	
	

}
