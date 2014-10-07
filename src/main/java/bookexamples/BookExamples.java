package bookexamples;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import chimicae.AvailableCompounds;

@Named("bookExamples")
@SessionScoped
public class BookExamples implements Serializable {
	private static final long serialVersionUID = -456711596676697713L;
	
	BookExample methanenpentane;
	BookExample ipropanewater;
	BookExample ipropanewaterWS;
	
	@Inject AvailableCompounds availableCompounds;
	
	@PostConstruct
	public void init(){
		methanenpentane = new MethanePentane(availableCompounds);
		ipropanewater = new IpropaneWater(availableCompounds);
		ipropanewaterWS = new IPropaneWaterWS(availableCompounds);
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

	
	
	

	
}

class Point{
	double x;
	double y;
	
	public Point() {
	}
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
}