package converters;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import termo.component.Compound;
import chimicae.AvailableCompounds;

@Named("componentConverter")
@ApplicationScoped
public class ComponentConverter implements Converter{
    @Inject AvailableCompounds availableCompounds;
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return getAsObject(value);
    }
    
    public Compound getAsObject(String value){
        
    //    return componentUtils.findByExactMatchName(value);
         for(Compound termoComponent: availableCompounds.getAvailableCompounds()){
            if(termoComponent.getName().equals(value)){
                return termoComponent;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value.toString();
    }
    
    
}
