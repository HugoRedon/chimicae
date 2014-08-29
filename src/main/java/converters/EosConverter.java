package converters;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import termo.eos.Cubic;
import utils.EqProUtils;

@Named("eosConverter")
@ApplicationScoped
public class EosConverter implements Converter{

	@Inject EqProUtils eqProUtils;
	
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for(Cubic eos: eqProUtils.getEos()){
            if(eos.getName()!= null && eos.getName().equals(value)){
                return eos;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value.toString();
    }
    
}
