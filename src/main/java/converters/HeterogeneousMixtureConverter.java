package converters;

import java.util.Iterator;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import termo.component.Compound;
import termo.eos.alpha.Alpha;
import termo.matter.HeterogeneousMixture;
import termo.matter.Mixture;
import termo.matter.Substance;
import chimicae.BinaryOptimizationBean;

@Named("hmc")
@RequestScoped
public class HeterogeneousMixtureConverter implements Converter{
	@Inject BinaryOptimizationBean binaryOptimizationBean;
	public HeterogeneousMixtureConverter() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		for(HeterogeneousMixture hm: binaryOptimizationBean.getHmList()){
			if(id(hm).equals(arg2)){
				return hm;
			}
		}
		return null;
	}
	
	String id(HeterogeneousMixture mix){
		StringBuilder sb = new StringBuilder();
		sb.append("Mezcla, ");
		sb.append("[");
		
		Iterator<Substance> iterator = mix.getLiquid().getPureSubstances().iterator();
		while(iterator.hasNext()){
			Substance sub =iterator.next();
			Compound com = sub.getComponent();
			
			sb.append("Compuesto:"+ com.getName());
			if(iterator.hasNext()){
				sb.append(",");
			}
		}
		sb.append("],");
		sb.append("Regla de mezclado:" + mix.getMixingRule().getName());
		return sb.toString();
	}
	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		// TODO Auto-generated method stub
		return id((HeterogeneousMixture)arg2);
	}

}
