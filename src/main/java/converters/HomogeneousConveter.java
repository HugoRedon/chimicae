package converters;

import java.io.Serializable;
import java.util.Iterator;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import termo.component.Compound;
import termo.eos.alpha.Alpha;
import termo.matter.Homogeneous;
import termo.matter.Mixture;
import termo.matter.Substance;
import chimicae.HomogeneousBean;

@Named("homogeneousConveter")
@RequestScoped
public class HomogeneousConveter implements Serializable,Converter {
	private static final long serialVersionUID = -4056368963558426519L;
	
	@Inject HomogeneousBean homogeneousBean;
	

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		
		for(Homogeneous homogeneous: homogeneousBean.getHomogeneousList()){
			if(homogeneousId(homogeneous).equals(arg2)){
				return homogeneous;
			}
		}
		return null;
	}

	public String homogeneousId(Homogeneous homogeneous){
		StringBuilder sb = new StringBuilder();
		if(homogeneous instanceof Mixture){
			Mixture mix = (Mixture) homogeneous;
			sb.append("Mezcla, ");
			sb.append("[");
			
			Iterator<Substance> iterator = mix.getPureSubstances().iterator();
			while(iterator.hasNext()){
				Substance sub =iterator.next();
				Compound com = sub.getComponent();
				Alpha alpha = sub.getAlpha();
				sb.append("Substancia:{Compuesto:"+ com.getName()+",Alfa"+alpha.getName()+"}");
				if(iterator.hasNext()){
					sb.append(",");
				}
			}
			sb.append("],");
			sb.append("Regla de mezclado:" + mix.getMixingRule().getName());
			
		}else{
			Substance sub = (Substance)homogeneous;
			Compound com = sub.getComponent();
			Alpha alpha = sub.getAlpha();
			sb.append("Substancia:{Compuesto:"+ com.getName()+",Alfa"+alpha.getName()+"}");
			sb.append(",");
		}
		sb.append("fase:" + homogeneous.getPhase());
		return sb.toString();
	}
	
	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		// TODO Auto-generated method stub
		return homogeneousId((Homogeneous)arg2);
	}

}
