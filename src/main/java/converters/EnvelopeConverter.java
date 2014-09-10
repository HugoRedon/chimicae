package converters;

import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import models.Envelope;
import chimicae.PhaseEnvelopeBean;
import java.io.Serializable;

@Named("envelopeConverter")
@SessionScoped
public class EnvelopeConverter implements Serializable, Converter{

	private static final long serialVersionUID = 2103072654725064753L;
	@Inject PhaseEnvelopeBean phaseEnvelopeBean;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		for(Envelope env: phaseEnvelopeBean.getEnvelopes()){
			if(env.getName().equals(arg2)){
				return env;
			}
				
				
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		return ((Envelope)arg2).getName();
	}

}
