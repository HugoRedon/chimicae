package converters;

import utils.EqProUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import termo.eos.mixingRule.MixingRule;

/**
 *
 * @author Hugo
 */
@Named("mixingRuleConverter")
@ApplicationScoped
public class MixingRuleConverter implements Converter{

    @Inject EqProUtils eqProUtils;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for(MixingRule mr: eqProUtils.getMixingRules()){
            if(mr.getName() != null && mr.getName().equals(value)){
                return mr;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value.toString();
    }
    
}
