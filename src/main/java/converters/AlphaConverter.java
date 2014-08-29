package converters;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import termo.eos.alpha.Alpha;
import utils.EqProUtils;

@Named("alphaConverter")
@ApplicationScoped
public class AlphaConverter implements Converter{
    
    @Inject EqProUtils eqProUtils;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for(Alpha alpha: eqProUtils.getAlphaList()){
            if(alpha.getName() != null && alpha.getName().equals(value)){
                return alpha;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value.toString();
    }

}
