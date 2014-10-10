package bookexamples;

import hugo.productions.google.ListPoint;
import hugo.productions.google.Point;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.google.gson.Gson;

import termo.Constants;
import termo.activityModel.NRTLActivityModel;
import termo.activityModel.VanLaarActivityModel;
import termo.binaryParameter.ActivityModelBinaryParameter;
import termo.binaryParameter.InteractionParameter;
import termo.component.Compound;
import termo.eos.Cubic;
import termo.eos.EquationsOfState;
import termo.eos.alpha.Alpha;
import termo.eos.alpha.Alphas;
import termo.eos.mixingRule.MixingRule;
import termo.eos.mixingRule.MixingRules;
import termo.matter.HeterogeneousMixture;
import termo.phase.Phase;
import chimicae.AvailableCompounds;

@Named("bookExamples")
@SessionScoped
public class BookExamples implements Serializable {
	private static final long serialVersionUID = -456711596676697713L;
	
	Compound methane;
	Compound pentane;
	
	Compound isopropanol;
	Compound water;
	
	MixtureSystem methanePentaneSystem;
	MixtureSystem isopropanolWaterSystem;
	
	
	List<BookExample> list = new ArrayList<>();
	

//	BookExample ipropanewater;
//	BookExample ipropanewaterWS;

//	BookExample iPropaneWater2PDVW;

	
	@Inject AvailableCompounds availableCompounds;
	
	@PostConstruct
	public void init(){
		initializeCompounds();
		Cubic eos = EquationsOfState.pengRobinson();
		Alpha alpha = Alphas.getStryjekAndVeraExpression();
		ActivityModelBinaryParameter k = new ActivityModelBinaryParameter();
		
		methanePentaneSystem = new MixtureSystem();
		methanePentaneSystem.setEos(eos);
		methanePentaneSystem.setAlpha(alpha);
		methanePentaneSystem.setReferenceCompound(methane);
		methanePentaneSystem.setNonReferenceCompound(pentane);
		methanePentaneSystem.setExperimentalLines(getMethanePentaneLines());
		
		
		isopropanolWaterSystem = new MixtureSystem();
		isopropanolWaterSystem.setEos(eos);
		isopropanolWaterSystem.setAlpha(alpha);
		isopropanolWaterSystem.setReferenceCompound(isopropanol);
		isopropanolWaterSystem.setNonReferenceCompound(water);
		isopropanolWaterSystem.setExperimentalLines(getIsopropanolWaterLines());
		
		
//	_________________________________________________________	
		
		methanePentaneSystem.setMr(MixingRules.vanDerWaals());
		k.setSymmetric(true);
		k.setValue(methane, pentane, 0.0215);
		methanePentaneSystem.setK(k);
		list.add(new BookExample(methanePentaneSystem,"mpvdw","/images/methaneandnpentane.png"));
		//methanenpentane = new MethanePentane(availableCompounds);
		
		
		isopropanolWaterSystem.setMr(MixingRules.huronVidal(new NRTLActivityModel(), eos));
		k = new ActivityModelBinaryParameter();
		
		k.getAlpha().setSymmetric(true);
		k.getAlpha().setValue(isopropanol,water, 0.2893);
		
		k.getA().setValue(isopropanol,water, 0.7882*Constants.R*353);
		k.getA().setValue(water, isopropanol, 3.9479*Constants.R*353);
		isopropanolWaterSystem.setK(k);
		list.add(new BookExample(isopropanolWaterSystem, "iwhvnrtl", "/images/2propanolWater.png"));
		//ipropanewater = new IpropaneWater(availableCompounds);
		
		
		isopropanolWaterSystem.setMr(MixingRules.wongSandler(new NRTLActivityModel(),eos));
		k = new ActivityModelBinaryParameter();
		
		k.getAlpha().setSymmetric(true);
		k.getAlpha().setValue(isopropanol, water, 0.2529);
		
		k.getA().setValue(isopropanol, water, 0.1562*Constants.R*353);
		k.getA().setValue(water, isopropanol, 2.7548*Constants.R*353);
		
		k.getK().setSymmetric(true);
		k.getK().setValue(isopropanol, water, 0.2529);
		isopropanolWaterSystem.setK(k);
		list.add(new BookExample(isopropanolWaterSystem, "iwwsnrtl", "/images/2propanolWater_WS.png"));
		//ipropanewaterWS = new IPropaneWaterWS(availableCompounds);
		
		
		methanePentaneSystem.setMr(MixingRules.huronVidal(
				new VanLaarActivityModel(), eos));
		k = new ActivityModelBinaryParameter();
		k.getA_vanLaar().setValue(methane, pentane, 0.1201);
		k.getA_vanLaar().setValue(pentane, methane, 0.1430);
		methanePentaneSystem.setK(k);
		list.add(new BookExample(methanePentaneSystem,"mphvvl","/images/metanepentaneHVVL.png"));
		//methanepentaneHVVL = new MethanePentaneHVVanLaar(availableCompounds);
		
		
		//iPropaneWater2PDVW = new IpropaneWater2PDVW(availableCompounds);
		
		methanePentaneSystem.setMr(MixingRules.ModifiedHuronVidalFirstOrderMixingRule(new VanLaarActivityModel(), eos));
		k = new ActivityModelBinaryParameter();
		
		k.getA_vanLaar().setValue(methane, pentane, -0.428);
		k.getA_vanLaar().setValue(pentane, methane, -0.632);
		methanePentaneSystem.setK(k);
		//list.add(new BookExample(methanePentaneSystem,"mpmhv1vl","/images/metanepentaneMHV1VL.png"));
		//methanepentaneMHV1VL = new MethanePentaneMHV1VL(availableCompounds);
	}
	
	public void initializeCompounds(){
		methane = availableCompounds.getCompoundByExactName("methane");
		methane.setK_StryjekAndVera(-0.00159);
		
		pentane = availableCompounds.getCompoundByExactName("n-pentane");
		pentane.setK_StryjekAndVera(0.03946);
		
		
		isopropanol = availableCompounds.getCompoundByExactName("isopropanol");
		isopropanol.setK_StryjekAndVera(0.23264);
		
		water= availableCompounds.getCompoundByExactName("water");
		water.setK_StryjekAndVera(-0.06635);
	}
	public static String[] files(String... files){
		return files;
	}
	
	public List<ListPoint> getMethanePentaneLines(){
		List<ListPoint> result = new ArrayList<>();
		result.addAll(createLists(files("/data/methaneandnpentane/methaneandnpentane_310_liquid.txt",
				"/data/methaneandnpentane/methaneandnpentane_377_liquid.txt"				
				)));
		result.addAll(createLists(files("/data/methaneandnpentane/methaneandnpentane_310_vapor.txt",
				"/data/methaneandnpentane/methaneandnpentane_377_vapor.txt"
				
				)
				,true,true,100));
		result.addAll(createLists(files("/data/methaneandnpentane/methaneandnpentane_444_liquid.txt",
				"/data/methaneandnpentane/methaneandnpentane_444_vapor.txt")
				,false,false,50));
		
		return result;
	}
	
	public List<ListPoint> getIsopropanolWaterLines(){
		List<ListPoint> result = new ArrayList<>();
		result.addAll(createLists(files(
				"/data/2propanolWater/2propanolWater_353_liquid.txt",
				"/data/2propanolWater/2propanolWater_353_vapor.txt"),
				true,false,25));
		return result;
	}
	
	
	public List<ListPoint> createLists(String[] files,boolean calculateLine,
			boolean calculateOtherPhaseLine,Integer NForCalculation){
		List<ListPoint> lines = new ArrayList<>();
		for(String file:files){
			ListPoint lp =readFile(file);
			lp.setLineToBeCalculated(calculateLine);
			lp.setOtherPhaseToBeCalculated(calculateOtherPhaseLine);
			lp.setNForCalculation(NForCalculation);
			lines.add(lp);
		}
		return lines;
		
	}
	public List<ListPoint> createLists(String[]files){
		List<ListPoint> lines = new ArrayList<>();
		for(String file:files){
			ListPoint lp = readFile(file);
			lp.setLineToBeCalculated(false);
			lp.setOtherPhaseToBeCalculated(false);
			lines.add(lp);
		}
		return lines;
	}
	
	

	public Point readLine(String line){
		String[] words = line.split(",");
		double x = Double.valueOf(words[0]);
		double pressure = 100000*(Double.valueOf(words[1]));		
		return new Point(x,pressure);
	}
	
	public ListPoint readFile(String filePath) {
		ListPoint listPoint =new ListPoint();
		try{
			InputStream file = this.getClass().getResourceAsStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(file));
			String linew =null;
			while ((linew = br.readLine()) != null) {
				if(linew.substring(0, 1).equals("#")){
					if(linew.substring(1,5).equals("info")){
						String json = linew.substring(6);
						Class<?> classs = Info.class;
						Info info =(Info) new Gson().fromJson(json,classs);
						Double temp = info.getTemperature();
						Phase phase = info.getPhase();
						listPoint.setLabel("Experimental " + temp +" [K] ");

						
						listPoint.setPhase(phase);
						listPoint.setId(phase+ temp.toString());
						listPoint.setTemperature(temp);
						
					}			
				}else{
					listPoint.getList().add(readLine(linew));
				}
			}
		}catch(IOException e){
			System.out.println("Cant read file " + filePath);
		}
		return listPoint;
	}
	
	




	public List<BookExample> getList() {
		return list;
	}

	public void setList(List<BookExample> list) {
		this.list = list;
	}



	
	
	

	
}


class MixtureSystem{
	Cubic eos;
	Alpha alpha;
	MixingRule mr;
	Compound referenceCompound;
	Compound nonReferenceCompound;
	InteractionParameter k;
	
	List<ListPoint> experimentalLines;
	
	public Cubic getEos() {
		return eos;
	}
	public void setEos(Cubic eos) {
		this.eos = eos;
	}
	public Alpha getAlpha() {
		return alpha;
	}
	public void setAlpha(Alpha alpha) {
		this.alpha = alpha;
	}
	public MixingRule getMr() {
		return mr;
	}
	public void setMr(MixingRule mr) {
		this.mr = mr;
	}
	public Compound getReferenceCompound() {
		return referenceCompound;
	}
	public void setReferenceCompound(Compound referenceCompound) {
		this.referenceCompound = referenceCompound;
	}
	public Compound getNonReferenceCompound() {
		return nonReferenceCompound;
	}
	public void setNonReferenceCompound(Compound nonReferenceCompound) {
		this.nonReferenceCompound = nonReferenceCompound;
	}
	public InteractionParameter getK() {
		return k;
	}
	public void setK(InteractionParameter k) {
		this.k = k;
	}
	public List<ListPoint> getExperimentalLines() {
		return experimentalLines;
	}
	public void setExperimentalLines(List<ListPoint> experimentalLines) {
		this.experimentalLines = experimentalLines;
	}
}


