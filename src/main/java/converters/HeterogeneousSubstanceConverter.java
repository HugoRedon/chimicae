package converters;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import termo.matter.HeterogeneousSubstance;
import chimicae.HeterogeneousBean;

@Named("hsubConverter")
@RequestScoped
public class HeterogeneousSubstanceConverter implements Converter{

	@Inject HeterogeneousBean heterogeneousBean;
	

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		for(HeterogeneousSubstance hsub: heterogeneousBean.getHeterogeneousSubstanceList()){
			if(arg2.equals(heterogeneousSubstanceId((HeterogeneousSubstance)hsub))){
				return hsub;
			}
		}
		return null;
	}
	
	public String heterogeneousSubstanceId(HeterogeneousSubstance het){
		StringBuilder sb =new StringBuilder();
		
		sb.append("Substancia heterogénea: ");
		sb.append("{ alfa: " + het.getAlpha().getName()+ ",");
		sb.append("compuesto:" + het.getComponent().getName());
		sb.append("}");
		
		return sb.toString();
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		return heterogeneousSubstanceId((HeterogeneousSubstance)arg2);
	}

}
