package converters;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import termo.component.Compound;
import termo.matter.Heterogeneous;
import termo.matter.HeterogeneousMixture;
import termo.matter.HeterogeneousSubstance;
import chimicae.SaturationBean;

@Named("heterogeneousConverter")
@RequestScoped
public class HeterogeneousConverter implements Converter {
	
	@Inject SaturationBean saturationBean;
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		for(Heterogeneous h: saturationBean.getHeterogeneousList()){
			if(arg2.equals(id(h))){
				return h;
			}
		}
		return null;
	}
	public String id(Heterogeneous heterogeneous){
		StringBuilder sb = new StringBuilder();
		if(heterogeneous instanceof HeterogeneousSubstance){
			HeterogeneousSubstance hs = (HeterogeneousSubstance)heterogeneous;
			sb.append("Substancia, "+hs.getComponent().getName());
			
		}else{
			HeterogeneousMixture hm = (HeterogeneousMixture)heterogeneous;
			String comps = "";
			for(Compound com: hm.getComponents()){
				comps += com.getName() + " ";
			}
			sb.append("Mezcla,"+comps);
		}
		return sb.toString();
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		
		return id((Heterogeneous)arg2);
	}

}
