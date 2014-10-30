package refinery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import termo.matter.Heterogeneous;
import termo.matter.HeterogeneousMixture;
import termo.matter.HeterogeneousSubstance;
import termo.matter.Mixture;
import termo.matter.builder.HeterogeneousMixtureBuilder;
import chimicae.SaturationBean;

@Named("refineryBean")
@SessionScoped
public class RefineryBean implements Serializable{

	private static final long serialVersionUID = 3244528474822301499L;		
	@Inject SaturationBean saturationBean;
	
	List<Equipment> equipmentList = new ArrayList<>();
	int equipmentCount=-1;
	
	public String createTank(double temperature,double pressure){
		
		Heterogeneous het = saturationBean.getSelectedHeterogeneous();
		het.setPressure(pressure);
		het.setTemperature(temperature);
		int idTank =++equipmentCount ;
		
		Tank tank = new Tank(idTank,het);
		equipmentList.add(tank);
		String rep ="";
		try{
			rep= tank.toJson();
		}catch(Exception e){
			String m = e.getMessage();
			System.out.println(m);
		}
		return rep;
	}
	public String createHeatExchanger(int fromEquipmentId, double newTemperature){
		Tank previousEquipment = (Tank)findEquipmentById(fromEquipmentId);
		if(previousEquipment ==null){
			System.out.println("here is null");
		}
		HeatExchangerEquipment hee = new HeatExchangerEquipment(++equipmentCount, previousEquipment, newTemperature);
		
		return hee.toJson();
		
		
				
	}

	
	public Equipment findEquipmentById(int id){
		Equipment result =null;
		for(Equipment equ: equipmentList){
			if(equ.getId()==id){
				result = equ;
			}
		}
		return result;
	}

	public int getEquipmentCount() {
		return equipmentCount;
	}

	
	

}
