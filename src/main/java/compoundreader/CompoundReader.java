package compoundreader;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cp.Eqn16IGHeatCapacity;
import termo.component.Compound;
import termo.data.ExperimentalData;
import termo.data.ExperimentalDataList;
import termo.equations.Eqn101VaporPressure;
import termo.equations.Eqn10VaporPressure;
import termo.equations.EqnVaporPressure;


/**
 *
 * @author Hugo Redon Rivera
 */
public class CompoundReader {
    private List<Compound> components = new ArrayList();
    
    
    public Compound getCompoundByExactName(String name){
    	if(components.size()==0){
    		read();
    	}
		for(Compound compound: components){
			if(compound.getName().equals(name.toLowerCase())){
				System.out.println("Compound found" + name);
				return compound;
			}
		}
    	return null;
    }

     public void read(){
        try {
            components.clear();
            InputStream file = this.getClass().getResourceAsStream("/data/chemsep1.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(file);
            
           NodeList list =  document.getElementsByTagName("compound");
           
           for(int i  = 0; i < list.getLength() ; i++){
               Node item = list.item(i);
               
                components.add(component(item));
               
           }
//            System.out.println("compounds found: " +list.getLength());
//            System.out.println("document" + document.getXmlVersion());
            
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(CompoundReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(CompoundReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CompoundReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
  
    
    public String stringProperty(String propertyName, Element el){
        Element elementValue =(Element) el.getElementsByTagName(propertyName).item(0);
        
        String value = elementValue.getAttribute("value");
        return value;
    }
    public double numberProperty(String propertyName, Element el){
        
        
        String value = stringProperty(propertyName, el);
//        System.out.println("value: " + value);
        return Double.valueOf(value);
    }
    
    public Compound component(Node node){
        Element el = (Element)node;
        String name = stringProperty("CompoundID", el);
//        System.out.println("name: " + name);
        double ct = numberProperty("CriticalTemperature",el);
        double cp = numberProperty("CriticalPressure",el);
        double cv = numberProperty("CriticalVolume", el);
        
        double w = numberProperty("AcentricityFactor", el);
        
        double h = numberProperty("HeatOfFormation",el);
        double s = numberProperty("AbsEntropy",el);
        
        
        Compound com = new Compound(name);
        com.setCriticalTemperature(ct);
        com.setCriticalPressure(cp);
        com.setCriticalVolume(cv);
        com.setAcentricFactor(w);
        com.setEnthalpyofFormationofIdealgasat298_15Kand101325Pa(h);
        com.setAbsoluteEntropyofIdealGasat298_15Kand101325Pa(s);
        
        if(hasEquationNumber("10", el)){
            Eqn10VaporPressure eqn10 = getEquation10(el);
            eqn10.setComponent(com);
            com.setEqn10VaporPressure(eqn10);
            fillVaporPressureList(eqn10, com);
            
        }if(hasEquationNumber("101", el)){
            Eqn101VaporPressure eqn101 = getEquation101(el);
            eqn101.setComponent(com);
            com.setEqn101VaporPressure(eqn101);
            fillVaporPressureList(eqn101, com);
        }
        
        
        Eqn16IGHeatCapacity eqn16 = getCpEquation(el);
        com.setCp(eqn16);
        
//        fillVaporPressureList(com);
        return com;
        
    }
    
   
    
    public void fillVaporPressureList(EqnVaporPressure vp,Compound compound){
    	double minT = vp.getMinTemperature();
    	double maxT = vp.getMaxTemperature();
    	Integer n = 60;
    	ExperimentalDataList datalist = new ExperimentalDataList();
    	String equationNumber= (vp instanceof Eqn101VaporPressure)? "101": "10";
    	datalist.setName("Lista generada con la ecuación de presión de vapor número " + equationNumber );
    	datalist.setSource("Pequeña base de datos Chemsep");
    	double tempStep = (maxT-minT)/n.doubleValue();
    	double temperature;
    	double vaporPressure;
    	ExperimentalData data;
    	for(int i = 0; i < n; i++){
    		temperature = minT + i *tempStep;
    		vaporPressure = vp.vaporPressure(temperature);
    		data = new ExperimentalData(temperature, vaporPressure)
    		;
    		datalist.getList().add(data);
//    		datalist.addExperimentalData(data);
        }
    	compound.getExperimentalLists().add(datalist);
//    	compound.addExperimentalDataList(datalist);
    	
    }
   
    
    
    public boolean hasEquationNumber(String number, Element element){
        Node vaporPressureNode = element.getElementsByTagName("VaporPressure").item(0);
        
        Element el = (Element )vaporPressureNode;
        String num = stringProperty("eqno", el);
        
        return number.equals(num);
    }
    
    public Eqn16IGHeatCapacity getCpEquation(Element element){
    	Eqn16IGHeatCapacity result= new Eqn16IGHeatCapacity();
    	Node cpnode = element.getElementsByTagName("IdealGasHeatCapacityCp").item(0);
    	Element el =(Element)cpnode;
    	
    	String eqno = stringProperty("eqno", el);
    	if(!eqno.equals("16")){
    		System.out.println(eqno);
    		return result;
    	}

        double A = numberProperty("A", el);
        double B = numberProperty("B", el);
        double C = numberProperty("C", el);
        double D = numberProperty("D", el);
        double E = numberProperty("E", el);
        
        double tmin = numberProperty("Tmin", el);
        double tmax = numberProperty("Tmax", el);
        
        result.setA(A);
        result.setB(B);
        result.setC(C);
        result.setD(D);
        result.setE(E);
        
        
    	
    	return result;
    	
    }
    public Eqn10VaporPressure getEquation10(Element element){
        Node vaporPressureNode10 = element.getElementsByTagName("VaporPressure").item(0);
        
        Element el = (Element )vaporPressureNode10;
        Eqn10VaporPressure eqn = new Eqn10VaporPressure();
        
        double A = numberProperty("A", el);
        double B = numberProperty("B", el);
        double C = numberProperty("C", el);
        
        double tmin = numberProperty("Tmin", el);
        double tmax = numberProperty("Tmax", el);
        
        eqn.setMinTemperature(tmin);
        eqn.setMaxTemperature(tmax);
        
        eqn.setA(A);
        eqn.setB(B);
        eqn.setC(C);
        
        return eqn;
    }
    
    public Eqn101VaporPressure getEquation101(Element element){
        Node vaporPressureNode101 = element.getElementsByTagName("VaporPressure").item(0);
        
        Element el = (Element )vaporPressureNode101;
        Eqn101VaporPressure eqn = new Eqn101VaporPressure();
        
        double A = numberProperty("A", el);
        double B = numberProperty("B", el);
        double C = numberProperty("C", el);
        double D = numberProperty("D", el);
        double E = numberProperty("E", el);
        
        double tmin = numberProperty("Tmin", el);
        double tmax = numberProperty("Tmax", el);
        
        eqn.setMinTemperature(tmin);
        eqn.setMaxTemperature(tmax);
        
        eqn.setA(A);
        eqn.setB(B);
        eqn.setC(C);
        eqn.setD(D);
        eqn.setE(E);
        
        return eqn;
        
    }

    /**
     * @return the components
     */
    public List<Compound> getComponents() {
        if(components.isEmpty()){
            read();
        }   
        return components;
    }

    /**
     * @param components the components to set
     */
    public void setComponents(List<Compound> components) {
        this.components = components;
    }
    
   
}

