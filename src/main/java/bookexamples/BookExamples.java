package bookexamples;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import termo.phase.Phase;
import chimicae.AvailableCompounds;

@Named("bookExamples")
@SessionScoped
public class BookExamples implements Serializable {
	private static final long serialVersionUID = -456711596676697713L;
	
	BookExample methanenpentane;
	BookExample ipropanewater;
	BookExample ipropanewaterWS;
	BookExample methanepentaneHVVL;
	BookExample iPropaneWater2PDVW;
	BookExample methanepentaneMHV1VL;
	
	@Inject AvailableCompounds availableCompounds;
	
	@PostConstruct
	public void init(){
		methanenpentane = new MethanePentane(availableCompounds);
		ipropanewater = new IpropaneWater(availableCompounds);
		ipropanewaterWS = new IPropaneWaterWS(availableCompounds);
		methanepentaneHVVL = new MethanePentaneHVVanLaar(availableCompounds);
		iPropaneWater2PDVW = new IpropaneWater2PDVW(availableCompounds);
		methanepentaneMHV1VL = new MethanePentaneMHV1VL(availableCompounds);
	}
	
	public BookExample getMethanenpentane() {
		return methanenpentane;
	}

	public void setMethanenpentane(BookExample methanenpentane) {
		this.methanenpentane = methanenpentane;
	}

	public BookExample getIpropanewater() {
		return ipropanewater;
	}

	public void setIpropanewater(BookExample ipropanewater) {
		this.ipropanewater = ipropanewater;
	}

	public BookExample getIpropanewaterWS() {
		return ipropanewaterWS;
	}

	public void setIpropanewaterWS(BookExample ipropanewaterWS) {
		this.ipropanewaterWS = ipropanewaterWS;
	}

	public BookExample getMethanepentaneHVVL() {
		return methanepentaneHVVL;
	}

	public void setMethanepentaneHVVL(BookExample methanepentaneHVVL) {
		this.methanepentaneHVVL = methanepentaneHVVL;
	}

	public BookExample getiPropaneWater2PDVW() {
		return iPropaneWater2PDVW;
	}

	public void setiPropaneWater2PDVW(BookExample iPropaneWater2PDVW) {
		this.iPropaneWater2PDVW = iPropaneWater2PDVW;
	}

	public BookExample getMethanepentaneMHV1VL() {
		return methanepentaneMHV1VL;
	}

	public void setMethanepentaneMHV1VL(BookExample methanepentaneMHV1VL) {
		this.methanepentaneMHV1VL = methanepentaneMHV1VL;
	}

	
	
	

	
}



