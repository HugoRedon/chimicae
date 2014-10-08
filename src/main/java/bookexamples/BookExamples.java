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
	
	@Inject AvailableCompounds availableCompounds;
	
	@PostConstruct
	public void init(){
		methanenpentane = new MethanePentane(availableCompounds);
		ipropanewater = new IpropaneWater(availableCompounds);
		ipropanewaterWS = new IPropaneWaterWS(availableCompounds);
		methanepentaneHVVL = new MethanePentaneHVVanLaar(availableCompounds);
		iPropaneWater2PDVW = new IpropaneWater2PDVW(availableCompounds);
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

	
	
	

	
}

class ListPoint{
	String label;
	String id ;
	double temperature;
	Phase phase ;
	
	List<Point> list = new ArrayList<>();
	double Temperature ;
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public List<Point> getList() {
		return list;
	}
	public void setList(List<Point> list) {
		this.list = list;
	}
	public double getTemperature() {
		return Temperature;
	}
	public void setTemperature(double temperature) {
		Temperature = temperature;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Phase getPhase() {
		return phase;
	}
	public void setPhase(Phase phase) {
		this.phase = phase;
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