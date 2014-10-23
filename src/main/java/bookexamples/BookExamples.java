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
import termo.activityModel.UNIQUACActivityModel;
import termo.activityModel.VanLaarActivityModel;
import termo.binaryParameter.ActivityModelBinaryParameter;
import termo.binaryParameter.InteractionParameter;
import termo.component.Compound;
import termo.eos.Cubic;
import termo.eos.EquationsOfState;
import termo.eos.alpha.Alpha;
import termo.eos.alpha.Alphas;
import termo.eos.mixingRule.HuronVidalModified2Order;
import termo.eos.mixingRule.HuronVidalOrbeySandlerModification;
import termo.eos.mixingRule.MixingRule;
import termo.eos.mixingRule.MixingRules;
import termo.eos.mixingRule.TwoParameterVanDerWaals;
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
	
	Compound carbonDioxide;
	Compound propane;
	
	Compound ethanol;
	
	Compound acetone,benzene,cyclohexane,methanol,
	methylAcetate,butanol,decane,heptane,hexane;
	HuronVidalModified2Order mhv2;
	
	
	
	MixtureSystem methanePentaneSystem;//
	MixtureSystem carbonDioxidePropaneSystem;//
	MixtureSystem pentaneEthanolSystem;//
	MixtureSystem propaneMethanolSystem;//	
	MixtureSystem isopropanolWaterSystem,//
	
	acetoneWaterSystem,//
	methylAcetateCyclohexaneSystem,//
	ethanolHeptaneSystem;//
	
	String viewName = "bookExamples";
	
	public void createEthanolHeptaneSystem(){
		ethanolHeptaneSystem = new MixtureSystem(ethanol, heptane);
		//ethanolHeptaneSystem.setExperimentalLines(getEthanolHeptaneLines());
	}
	
	public void createMethylAcetateCyclohexaneSystem(){
		methylAcetateCyclohexaneSystem= new MixtureSystem(methylAcetate, cyclohexane);
		//methylAcetateCyclohexaneSystem.setExperimentalLines(getMethylAcetateCyclohexaneLines());
	}
	
	public void createAcetoneWaterSystem(){
		acetoneWaterSystem = new MixtureSystem(acetone,water);
		acetoneWaterSystem.setExperimentalLines(getAcetoneWaterLines());
	}
	
	public void createPropaneMethanolSystem(){
		propaneMethanolSystem = new MixtureSystem();
		propaneMethanolSystem.setReferenceCompound(propane);
		propaneMethanolSystem.setNonReferenceCompound(methanol);
		propaneMethanolSystem.setExperimentalLines(getPropaneMethanolLines());
	}
	
	public void createMethanePentaneSystem(){
		methanePentaneSystem = new MixtureSystem();
		methanePentaneSystem.setReferenceCompound(methane);
		methanePentaneSystem.setNonReferenceCompound(pentane);
		methanePentaneSystem.setExperimentalLines(getMethanePentaneLines());
		
	}
	public void createIsopropanolWaterSystem(){
		isopropanolWaterSystem = new MixtureSystem();
		isopropanolWaterSystem.setReferenceCompound(isopropanol);
		isopropanolWaterSystem.setNonReferenceCompound(water);
		isopropanolWaterSystem.setExperimentalLines(getIsopropanolWaterLines());		
	}
	
	public void createCarbonDioxidePropaneSystem(){
		carbonDioxidePropaneSystem = new MixtureSystem();
		carbonDioxidePropaneSystem.setReferenceCompound(carbonDioxide);
		carbonDioxidePropaneSystem.setNonReferenceCompound(propane);
		carbonDioxidePropaneSystem.setExperimentalLines(getCarbonDioxidePropaneLines());
		
	}
	public void createPentaneEthanolSystem(){
		pentaneEthanolSystem= new MixtureSystem();
		pentaneEthanolSystem.setReferenceCompound(pentane);
		pentaneEthanolSystem.setNonReferenceCompound(ethanol);
		pentaneEthanolSystem.setExperimentalLines(getPentaneEthanolLines());
	}
	
	
	
	
	List<BookExample> list = new ArrayList<>();
	@Inject AvailableCompounds availableCompounds;
	
	public String vdwDemos(){
		Cubic eos = EquationsOfState.pengRobinson();		
		ActivityModelBinaryParameter k = new ActivityModelBinaryParameter();
		list.clear();
		
		methanePentaneSystem.setMr(MixingRules.vanDerWaals());
		k.setSymmetric(true);
		k.setValue(methane, pentane, 0.0215);
		methanePentaneSystem.setK(k);
		list.add(new BookExample(methanePentaneSystem,"mpvdw","/images/methaneandnpentane.png"));
		//methanenpentane = new MethanePentane(availableCompounds);
		
		carbonDioxidePropaneSystem.setMr(MixingRules.vanDerWaals());
		k = new ActivityModelBinaryParameter();
		k.setSymmetric(true);
		k.setValue(carbonDioxide, propane, 0.121);
		carbonDioxidePropaneSystem.setK(k);
		list.add(new BookExample(carbonDioxidePropaneSystem,"cpvdw","/images/carbondioxidepropanevdw.png"));
		
		pentaneEthanolSystem.setMr(MixingRules.vanDerWaals());
		k= new ActivityModelBinaryParameter();
		k.setSymmetric(true);
		k.setValue(pentane, ethanol, .0964);
		pentaneEthanolSystem.setK(k);
		list.add(new BookExample(pentaneEthanolSystem, "pevdw", "/images/pentaneethanolvdw.png"));
		
		propaneMethanolSystem.setMr(MixingRules.vanDerWaals());
		k = new ActivityModelBinaryParameter();
		k.setSymmetric(true);
		k.setValue(propane, methanol, 0.0451);
		propaneMethanolSystem.setK(k);
		list.add(new BookExample(propaneMethanolSystem, "pmvdw", "/images/propanemethanolvdw.png"));
		
		
		isopropanolWaterSystem.setMr(MixingRules.vanDerWaals());
		k = new ActivityModelBinaryParameter();
		k.setSymmetric(true);
		k.setValue(isopropanol, water,-0.1621);
		isopropanolWaterSystem.setK(k);
		list.add(new BookExample(isopropanolWaterSystem, "iwvdw", "/images/propanolwatervdw.png"));
		
		
		
		
		isopropanolWaterSystem.setExperimentalLines(getPropanolWaterLines());
		k = new ActivityModelBinaryParameter();
		k.setSymmetric(true);
		k.setValue(isopropanol, water, -0.1120);
		isopropanolWaterSystem.setK(k);
		list.add(new BookExample(isopropanolWaterSystem,"iwvdw523","/images/propanolwater523vdw.png"));
		
		
		createIsopropanolWaterSystem();//restaurando lineas experimentales
		
		acetoneWaterSystem.setMr(MixingRules.vanDerWaals());
		k = new ActivityModelBinaryParameter();
		k.setSymmetric(true);
		k.setValue(acetone, water, -0.256);
		acetoneWaterSystem.setK(k);
		list.add(new BookExample(acetoneWaterSystem, "awvdw", "/images/acetonewatervdw.png"));
		
		acetoneWaterSystem.setMr(MixingRules.vanDerWaals());
		k = new ActivityModelBinaryParameter();
		k.setSymmetric(true);
		k.setValue(acetone, water, -0.093);
		acetoneWaterSystem.setK(k);
		acetoneWaterSystem.setExperimentalLines(getAcetoneWater523Lines());
		list.add(new BookExample(acetoneWaterSystem, "aw523vdw", "/images/acetonewater523vdw.png"));
		
		
		createAcetoneWaterSystem();//restaurandolineas experimentales
		
		return viewName;
	}
	
	
	public String hvoDemos(){
		Cubic eos = EquationsOfState.pengRobinson();		
		ActivityModelBinaryParameter k = new ActivityModelBinaryParameter();
		list.clear();
		//_____________________________________________________________hvo
		
				methanePentaneSystem.setMr(MixingRules.huronVidal(
						new VanLaarActivityModel(), eos));
				k = new ActivityModelBinaryParameter();
				k.getA_vanLaar().setValue(methane, pentane, 0.1201);
				k.getA_vanLaar().setValue(pentane, methane, 0.1430);
				methanePentaneSystem.setK(k);
				list.add(new BookExample(methanePentaneSystem,"mphvvl","/images/metanepentaneHVVL.png"));
				//methanepentaneHVVL = new MethanePentaneHVVanLaar(availableCompounds);
				
				carbonDioxidePropaneSystem.setMr(MixingRules.huronVidal(new VanLaarActivityModel(), eos));
				k = new ActivityModelBinaryParameter();
				
				k.getA_vanLaar().setValue(carbonDioxide, propane, 1.1816);
				k.getA_vanLaar().setValue(propane, carbonDioxide, 1.6901);
				carbonDioxidePropaneSystem.setK(k);
				list.add(new BookExample(carbonDioxidePropaneSystem, "cdphvvl", "/images/carbondioxidepropanehvvl.png"));
				
				
				acetoneWaterSystem.setMr(MixingRules.huronVidal(new VanLaarActivityModel(), eos));
				k = new ActivityModelBinaryParameter();
				
				k.getA_vanLaar().setValue(acetone, water, 3.5121);
				k.getA_vanLaar().setValue(water, acetone, 2.2227);
				acetoneWaterSystem.setK(k);
				list.add(new BookExample(acetoneWaterSystem, "awhvvl", "/images/acetonewaterhvvl.png"));
				
				acetoneWaterSystem.setMr(MixingRules.huronVidal(new VanLaarActivityModel(), eos));
				k = new ActivityModelBinaryParameter();
				
				k.getA_vanLaar().setValue(acetone, water, 4.2206);
				k.getA_vanLaar().setValue(water, acetone, 1.7264);
				acetoneWaterSystem.setExperimentalLines(getAcetoneWater523Lines());
				acetoneWaterSystem.setK(k);
				list.add(new BookExample(acetoneWaterSystem, "aw523hvvl", "/images/acetonewater523hvvl.png"));		
				createAcetoneWaterSystem();//restaurando lineas experimentales
				
				
				
				isopropanolWaterSystem.setMr(MixingRules.huronVidal(new NRTLActivityModel(), eos));
				k = new ActivityModelBinaryParameter();
				
				k.getAlpha().setSymmetric(true);
				k.getAlpha().setValue(isopropanol,water, 0.2893);
				
				k.getA().setValue(isopropanol,water, 0.7882*Constants.R*353);
				k.getA().setValue(water, isopropanol, 3.9479*Constants.R*353);
				isopropanolWaterSystem.setK(k);
				list.add(new BookExample(isopropanolWaterSystem, "iwhvnrtl", "/images/2propanolWater.png"));
				//ipropanewater = new IpropaneWater(availableCompounds);
				
				
				
				
				isopropanolWaterSystem.setMr(MixingRules.huronVidal(new NRTLActivityModel(), eos));
				k = new ActivityModelBinaryParameter();
				
				k.getAlpha().setSymmetric(true);
				k.getAlpha().setValue(isopropanol,water, 0.2893);
				
				k.getA().setValue(isopropanol,water, 0.3952*Constants.R*523);
				k.getA().setValue(water, isopropanol, 4.1518*Constants.R*523);
				isopropanolWaterSystem.setK(k);
				isopropanolWaterSystem.setExperimentalLines(getPropanolWaterLines());
				list.add(new BookExample(isopropanolWaterSystem, "iw523hvnrtl", "/images/propanolwater523hvnrtl.png"));
				
				
				
				createIsopropanolWaterSystem();//restaurando lineas experimentales
				
				
				
				UNIQUACActivityModel am = new UNIQUACActivityModel();
				isopropanolWaterSystem.setMr(MixingRules.huronVidal(am, eos));
				k = new ActivityModelBinaryParameter();
				k.getA().setValue(isopropanol, water, 646.29*1000*4.184);
				k.getA().setValue(water, isopropanol, 214.79*1000*4.184);
				isopropanolWaterSystem.setK(k);
				isopropanolWaterSystem.setExperimentalLines(getPropanolWater298Lines());
				list.add(new BookExample(isopropanolWaterSystem,"iw298uniquachv","/images/propanolwater_298_uniquacws.png"));
				
				System.out.println("u:" + am.u(isopropanol, water, k, 298));
				System.out.println("u:" + am.u(water, isopropanol, k, 298));
				createIsopropanolWaterSystem();
				
				
				//________________________________________________________-end hvo
				
		
		return viewName;
	}
	
	public String hvos_mhv2_mhv1_lcvm(){
		
		Cubic eos = EquationsOfState.pengRobinson();		
		ActivityModelBinaryParameter k = new ActivityModelBinaryParameter();
		list.clear();
		
		methanePentaneSystem.setMr(new HuronVidalOrbeySandlerModification(new VanLaarActivityModel(), eos));
		k = new ActivityModelBinaryParameter();
		
		k.getA_vanLaar().setValue(methane, pentane, -0.428);
		k.getA_vanLaar().setValue(pentane, methane, -0.632);
		methanePentaneSystem.setK(k);
		list.add(new BookExample(methanePentaneSystem,"mphvosm","/images/metanepentaneMHV1VL.png"));
		//methanepentaneMHV1VL = new MethanePentaneMHV1VL(availableCompounds);
		
		
		mhv2 = new HuronVidalModified2Order(new VanLaarActivityModel()	, eos);
		//mhv2.setL2(-0.003645);
		mhv2.setL2(-0.0045);
		//mhv2.setL2(eos.calculateL(1.9, 1));
		methanePentaneSystem.setMr(mhv2);
		k = new ActivityModelBinaryParameter();
		
		k.getA_vanLaar().setValue(methane, pentane, -0.154);
		k.getA_vanLaar().setValue(pentane, methane, -1.066);
		methanePentaneSystem.setK(k);
		list.add(new BookExample(methanePentaneSystem,"mpmhv2vl","/images/metanepentaneMHV1VL.png"));
		
		
		
		methanePentaneSystem.setMr(MixingRules.ModifiedHuronVidalFirstOrderMixingRule(new VanLaarActivityModel(), eos));
		k = new ActivityModelBinaryParameter();
		
		k.getA_vanLaar().setValue(methane, pentane, -0.432);
		k.getA_vanLaar().setValue(pentane, methane, -0.677);
		methanePentaneSystem.setK(k);
		list.add(new BookExample(methanePentaneSystem,"mpmhv1vl","/images/metanepentaneMHV1VL.png"));
		//methanepentaneMHV1VL = new MethanePentaneMHV1VL(availableCompounds);
		
		
		
		return viewName;
		
	}
	
	
	public String wsDemos(){
		Cubic eos = EquationsOfState.pengRobinson();		
		ActivityModelBinaryParameter k = new ActivityModelBinaryParameter();
		list.clear();
		//_____________________________________________________________ws
		
				methanePentaneSystem.setMr(MixingRules.wongSandler(new VanLaarActivityModel(), eos));
				k = new ActivityModelBinaryParameter();
				k.getA_vanLaar().setValue(methane, pentane, 0.1924);
				k.getA_vanLaar().setValue(pentane, methane, 0.6719);
				
				k.getK().setValue(methane	, pentane, 0.5216);
				
				methanePentaneSystem.setK(k);
				list.add(new BookExample(methanePentaneSystem, "mpwsvl", "/images/methanepentanewsvl.png"));
				
				
				
				carbonDioxidePropaneSystem.setMr(MixingRules.wongSandler(new VanLaarActivityModel(), eos));
				k = new ActivityModelBinaryParameter();
				k.getA_vanLaar().setValue(carbonDioxide, propane, 0.7897);
				k.getA_vanLaar().setValue(propane, carbonDioxide, 0.7928);
				
				k.getK().setValue(carbonDioxide, propane, 0.3565);
				carbonDioxidePropaneSystem.setK(k);
				list.add(new BookExample(carbonDioxidePropaneSystem, "cdpwsvl", "/images/carbondioxidepropanewsvl.png"));
				
				
				
				
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
				
				
				
				isopropanolWaterSystem.setMr(MixingRules.wongSandler(new NRTLActivityModel(),eos));
				k = new ActivityModelBinaryParameter();
				
				k.getAlpha().setSymmetric(true);
				k.getAlpha().setValue(isopropanol, water, 0.2893);
				
				k.getA().setValue(isopropanol, water, -0.4302*Constants.R*523);
				k.getA().setValue(water, isopropanol, 2.5280*Constants.R*523);
				
				k.getK().setSymmetric(true);
				k.getK().setValue(isopropanol, water, 0.3159);
				isopropanolWaterSystem.setK(k);
				isopropanolWaterSystem.setExperimentalLines(getPropanolWaterLines());
				list.add(new BookExample(isopropanolWaterSystem, "iw523wsnrtl", "/images/propanolwater523wsnrtl.png"));
				//ipropanewaterWS = new IPropaneWaterWS(availableCompounds);
						
				createIsopropanolWaterSystem();
				
				
				acetoneWaterSystem.setMr(MixingRules.wongSandler(new VanLaarActivityModel()		, eos));
				k= new ActivityModelBinaryParameter();
				k.getA_vanLaar().setValue(acetone, water, 1.7724);
				k.getA_vanLaar().setValue(water, acetone, 2.0291);
				k.getK().setValue(acetone, water, 0.2529);
				acetoneWaterSystem.setK(k);
				list.add(new BookExample(acetoneWaterSystem, "awwsvl", "/images/acetonewaterwsvl.png"));
			
				
				acetoneWaterSystem.setMr(MixingRules.wongSandler(new VanLaarActivityModel()		, eos));
				k= new ActivityModelBinaryParameter();
				k.getA_vanLaar().setValue(acetone, water, 2.0287);
				k.getA_vanLaar().setValue(water, acetone, 1.6009);
				k.getK().setValue(acetone, water, 0.2779);
				acetoneWaterSystem.setK(k);
				acetoneWaterSystem.setExperimentalLines(getAcetoneWater372Lines());
				list.add(new BookExample(acetoneWaterSystem, "aw373wsvl", "/images/acetonewater373wsvl.png"));
			
				
				acetoneWaterSystem.setMr(MixingRules.wongSandler(new VanLaarActivityModel()		, eos));
				k= new ActivityModelBinaryParameter();
				k.getA_vanLaar().setValue(acetone, water, 1.9520);
				k.getA_vanLaar().setValue(water, acetone, 1.3812);
				k.getK().setValue(acetone, water, 0.2641);
				acetoneWaterSystem.setK(k);
				acetoneWaterSystem.setExperimentalLines(getAcetoneWater523Lines());
				list.add(new BookExample(acetoneWaterSystem, "aw523wsvl", "/images/acetonewater523wsvl.png"));			
				createAcetoneWaterSystem();
				
				
				createIsopropanolWaterSystem();
				
				UNIQUACActivityModel am = new UNIQUACActivityModel();
				isopropanolWaterSystem.setMr(MixingRules.wongSandler(am, eos));
				k = new ActivityModelBinaryParameter();
				k.getA().setValue(isopropanol, water, 837.65*1000*4.184);
				k.getA().setValue(water, isopropanol, -28.38*1000*4.184);
				k.getK().setValue(isopropanol, water, 0.15);
				isopropanolWaterSystem.setK(k);
				isopropanolWaterSystem.setExperimentalLines(getPropanolWater298Lines());
				list.add(new BookExample(isopropanolWaterSystem,"iw298uniquacws","/images/propanolwater_298_uniquacws.png"));
				
				System.out.println("u:" + am.u(isopropanol, water, k, 298));
				System.out.println("u:" + am.u(water, isopropanol, k, 298));
				createIsopropanolWaterSystem();
				
				
		return viewName;
	}
	
	public String twoParameterVDW(){
		Cubic eos = EquationsOfState.pengRobinson();		
		ActivityModelBinaryParameter k = new ActivityModelBinaryParameter();
		list.clear();
		//_________________-two parameter vdw
		isopropanolWaterSystem.setMr(new TwoParameterVanDerWaals());
		k = new ActivityModelBinaryParameter();
		k.getTwoParameterVanDerWaals().setValue(isopropanol,water, -0.0911);
		k.getTwoParameterVanDerWaals().setValue(water, isopropanol, -0.1766);
		//iPropaneWater2PDVW = new IpropaneWater2PDVW(availableCompounds);
		isopropanolWaterSystem.setK(k);
		isopropanolWaterSystem.setExperimentalLines(getIsopropanolWaterLines());
		list.add(new BookExample(isopropanolWaterSystem,"iwtpvdw","/images/2pvdw2propanolwater.png"));
		
		
		propaneMethanolSystem.setMr(new TwoParameterVanDerWaals());
		k = new ActivityModelBinaryParameter();
		k.getTwoParameterVanDerWaals().setValue(propane, methanol, 0.0953);
		k.getTwoParameterVanDerWaals().setValue(methanol, propane, 0.0249);
		propaneMethanolSystem.setK(k);
		list.add(new BookExample(propaneMethanolSystem, "pmtpvdw", ""));
		
		
		isopropanolWaterSystem.setMr(new TwoParameterVanDerWaals());
		k = new ActivityModelBinaryParameter();
		k.getTwoParameterVanDerWaals().setValue(isopropanol	, water, -0.0239);
		k.getTwoParameterVanDerWaals().setValue(water, isopropanol, -0.1378);
		isopropanolWaterSystem.setK(k);
		isopropanolWaterSystem.setExperimentalLines(getPropanolWaterLines());
		list.add(new BookExample(isopropanolWaterSystem, "iwtpvdw523", ""));

		
		acetoneWaterSystem.setMr(new TwoParameterVanDerWaals());
		k = new ActivityModelBinaryParameter();
		k.getTwoParameterVanDerWaals().setValue(acetone	, water, -0.1416);
		k.getTwoParameterVanDerWaals().setValue(water, acetone, -0.2822);
		acetoneWaterSystem.setK(k);
		list.add(new BookExample(acetoneWaterSystem, "awtpvdw", ""));
		
		
		acetoneWaterSystem.setMr(new TwoParameterVanDerWaals());
		k = new ActivityModelBinaryParameter();
		k.getTwoParameterVanDerWaals().setValue(acetone	, water, 0.0445);
		k.getTwoParameterVanDerWaals().setValue(water, acetone, -0.1521);
		acetoneWaterSystem.setK(k);
		acetoneWaterSystem.setExperimentalLines(getAcetoneWater523Lines());
		list.add(new BookExample(acetoneWaterSystem, "awtpvdw523", ""));
		
		createAcetoneWaterSystem();
		
//		ethanolHeptaneSystem.setMr(new TwoParameterVanDerWaals());
//		k= new ActivityModelBinaryParameter();
//		//k.getTwoParameterVanDerWaals().setValue(ethanol, heptane, value);				
//		ethanolHeptaneSystem.setK(k);
//		list.add(new BookExample(ethanolHeptaneSystem,"ehtpvdw",""));
		return viewName;
	}
	
	@PostConstruct
	public void init(){
		initializeCompounds();
		Cubic eos = EquationsOfState.pengRobinson();		
		ActivityModelBinaryParameter k = new ActivityModelBinaryParameter();
		
		createMethanePentaneSystem();
		createIsopropanolWaterSystem();
		createCarbonDioxidePropaneSystem();
		createPentaneEthanolSystem();
		createPropaneMethanolSystem();
		createAcetoneWaterSystem();
		

		
		
		
		
		
		
		
		//_______________________________________________________________end ws

//		isopropanolWaterSystem.setMr(new TwoParameterVanDerWaals());
//		k = new ActivityModelBinaryParameter();
//		k.getTwoParameterVanDerWaals().setValue(isopropanol,water, 0.0953);
//		k.getTwoParameterVanDerWaals().setValue(water, isopropanol, 0.0249);
//		//iPropaneWater2PDVW = new IpropaneWater2PDVW(availableCompounds);
//		isopropanolWaterSystem.setK(k);
//		list.add(new BookExample(isopropanolWaterSystem,"iwtpvdw","/images/2pvdw2propanolwater.png"));
//		
//		methanePentaneSystem.setMr(MixingRules.ModifiedHuronVidalFirstOrderMixingRule(new VanLaarActivityModel(), eos));
//		k = new ActivityModelBinaryParameter();
//		
//		k.getA_vanLaar().setValue(methane, pentane, -0.428);
//		k.getA_vanLaar().setValue(pentane, methane, -0.632);
//		methanePentaneSystem.setK(k);
//		list.add(new BookExample(methanePentaneSystem,"mpmhv1vl","/images/metanepentaneMHV1VL.png"));
//		//methanepentaneMHV1VL = new MethanePentaneMHV1VL(availableCompounds);
	}
	
	
	
	public void initializeCompounds(){
		acetone = availableCompounds.getCompoundByExactName("acetone");
		acetone.setK_StryjekAndVera(-0.00888);
		
		benzene = availableCompounds.getCompoundByExactName("benzene");
		benzene.setK_StryjekAndVera(0.07019);
		
		cyclohexane = availableCompounds.getCompoundByExactName("cyclohexane");
		cyclohexane.setK_StryjekAndVera(0.07023);
		
		methanol= availableCompounds.getCompoundByExactName("methanol");
		methanol.setK_StryjekAndVera(-0.16816);
		
		methylAcetate = availableCompounds.getCompoundByExactName("methyl acetate");
		methylAcetate.setK_StryjekAndVera(0.05791);
		
		butanol = availableCompounds.getCompoundByExactName("1-butanol");
		butanol.setK_StryjekAndVera(0.33431);
		
		decane = availableCompounds.getCompoundByExactName("n-decane");
		decane.setK_StryjekAndVera(0.04510);
		
		heptane = availableCompounds.getCompoundByExactName("n-heptane");
		heptane.setK_StryjekAndVera(0.04648);
		
		hexane = availableCompounds.getCompoundByExactName("n-hexane");
		hexane.setK_StryjekAndVera(0.05104);
		
		
		methane = availableCompounds.getCompoundByExactName("methane");
		methane.setK_StryjekAndVera(-0.00159);
		
		pentane = availableCompounds.getCompoundByExactName("n-pentane");
		pentane.setK_StryjekAndVera(0.03946);
		
		
		isopropanol = availableCompounds.getCompoundByExactName("isopropanol");
		isopropanol.setK_StryjekAndVera(0.23264);
		isopropanol.setQ_UNIQUAC(2.51);
		isopropanol.setR_UNIQUAC(2.78);
		
		water= availableCompounds.getCompoundByExactName("water");
		water.setK_StryjekAndVera(-0.06635);
		water.setQ_UNIQUAC(1.4);
		water.setR_UNIQUAC(0.92);
		
		
		carbonDioxide = availableCompounds.getCompoundByExactName("carbon dioxide");
		carbonDioxide.setK_StryjekAndVera(0.04285);
		
		propane = availableCompounds.getCompoundByExactName("propane");
		propane.setK_StryjekAndVera(0.03136);
				
		ethanol = availableCompounds.getCompoundByExactName("ethanol");
		ethanol.setK_StryjekAndVera(-0.03374);
		
		
	}
	
	

	
	

	public static String[] files(String... files){
		return files;
	}
	
	public List<ListPoint> getPentaneEthanolLines(){
		List<ListPoint> result = new ArrayList<>();
		
		result.addAll(createLists(files(
				"/data/pentaneethanol/pentaneethanol_373_liquid.txt",
				"/data/pentaneethanol/pentaneethanol_373_vapor.txt",
				
				"/data/pentaneethanol/pentaneethanol_398_liquid.txt",
				"/data/pentaneethanol/pentaneethanol_398_vapor.txt",
				
				"/data/pentaneethanol/pentaneethanol_423_liquid.txt",
				"/data/pentaneethanol/pentaneethanol_423_vapor.txt"
				
				)
				,true,false,50,0d,1d));
		return result;
	}
	
	public List<ListPoint> getMethanePentaneLines(){
		List<ListPoint> result = new ArrayList<>();

		result.addAll(createLists(files(
				"/data/methaneandnpentane/methaneandnpentane_310_vapor.txt",
				"/data/methaneandnpentane/methaneandnpentane_310_liquid.txt",
				"/data/methaneandnpentane/methaneandnpentane_377_vapor.txt",
				"/data/methaneandnpentane/methaneandnpentane_377_liquid.txt",
				"/data/methaneandnpentane/methaneandnpentane_444_liquid.txt",
				"/data/methaneandnpentane/methaneandnpentane_444_vapor.txt")
				,true,false,100));
		
		return result;
	}
	
	public List<ListPoint> getAcetoneWater523Lines(){
		List<ListPoint> result = new ArrayList<>();
		result.addAll(createLists(files(
				"/data/acetonewater/acetonewater_523_liquid.txt",
				"/data/acetonewater/acetonewater_523_vapor.txt"
				),true,false,50));
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
	
	public List<ListPoint> getPropanolWaterLines(){
		List<ListPoint> result = new ArrayList<>();
		result.addAll(createLists(files(
			"/data/2propanolWater/propanolwater_523_liquid.txt",
			"/data/2propanolWater/propanolwater_523_vapor.txt"),
			true,false,25));
		return result;
	}
	
	public List<ListPoint> getPropanolWater298Lines(){
		List<ListPoint> result = new ArrayList<>();
		result.addAll(createLists(files(
			"/data/2propanolWater/propanolwater_298_liquid.txt",
			"/data/2propanolWater/propanolwater_298_vapor.txt"),
			true,false,25));
		return result;
	}
	
	
	public List<ListPoint> getCarbonDioxidePropaneLines(){
		List<ListPoint> result = new ArrayList<>();
		result.addAll(createLists(files(
				"/data/carbondioxidepropane/carbondioxidepropane_277_liquid.txt",
				"/data/carbondioxidepropane/carbondioxidepropane_277_vapor.txt",
				"/data/carbondioxidepropane/carbondioxidepropane_310_liquid.txt",
				"/data/carbondioxidepropane/carbondioxidepropane_310_vapor.txt",
				"/data/carbondioxidepropane/carbondioxidepropane_344_liquid.txt",
				"/data/carbondioxidepropane/carbondioxidepropane_344_vapor.txt")
				,true,false,80));
		return result;
	}
	
	public List<ListPoint> getPropaneMethanolLines(){
		List<ListPoint> result = new ArrayList<>();
		result.addAll(createLists(files(
				"/data/propanemethanol/propanemethanol_313_liquid.txt",
				"/data/propanemethanol/propanemethanol_313_vapor.txt"
				),true,false,70,0,1));
		return result;
	}
	public List<ListPoint> getAcetoneWaterLines(){
		List<ListPoint> result = new ArrayList<>();
		result.addAll(createLists(files(
				"/data/acetonewater/acetonewater_298_liquid.txt",
				"/data/acetonewater/acetonewater_298_vapor.txt"
				),true,false,70));
		return result;
	}
	
	public List<ListPoint> getAcetoneWater372Lines(){
		List<ListPoint> result = new ArrayList<>();
		result.addAll(createLists(files(
				"/data/acetonewater/acetonewater_373_liquid.txt",
				"/data/acetonewater/acetonewater_373_vapor.txt"
				),true,false,70));
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
	
	
	public List<ListPoint> createLists(String[] files,boolean calculateLine,
		boolean calculateOtherPhaseLine,Integer NForCalculation,
		double minX,double maxX){
		List<ListPoint>lines =createLists(files, calculateLine, 
				calculateOtherPhaseLine, NForCalculation);
		for(ListPoint line: lines){
			line.setMinX(minX);
			line.setMaxX(maxX);
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

						System.out.println("k:"+info.k);
						listPoint.setK(info.k);
						
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

	public HuronVidalModified2Order getMhv2() {
		return mhv2;
	}

	public void setMhv2(HuronVidalModified2Order mhv2) {
		this.mhv2 = mhv2;
	}



	
	
	

	
}


class MixtureSystem{
	Cubic eos = EquationsOfState.pengRobinson();
	Alpha alpha = Alphas.getStryjekAndVeraExpression();
	MixingRule mr;
	Compound referenceCompound;
	Compound nonReferenceCompound;
	InteractionParameter k;
	
	public MixtureSystem(Compound referenceCompound,
			Compound nonReferenceCompound){
		this.referenceCompound=referenceCompound;
		this.nonReferenceCompound= nonReferenceCompound;
	}
	public MixtureSystem(){
		
	}
	
	public String compoundInitials(){
		if(referenceCompound!=null && nonReferenceCompound!= null){
			return referenceCompound.getName().substring(0,1) +
					nonReferenceCompound.getName().substring(0,1); 
		}
		return "";
	}
	
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


