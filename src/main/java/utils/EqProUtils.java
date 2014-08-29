package utils;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import termo.activityModel.NRTLActivityModel;
import termo.activityModel.WilsonActivityModel;
import termo.component.Compound;
import termo.eos.Cubic;
import termo.eos.EquationsOfState;
import termo.eos.alpha.Alpha;
import termo.eos.alpha.Alphas;
import termo.eos.mixingRule.HuronVidalMixingRule;
import termo.eos.mixingRule.MathiasKlotzPrausnitzMixingRule;
import termo.eos.mixingRule.MixingRule;
import termo.eos.mixingRule.VDWMixingRule;
import termo.eos.mixingRule.WongSandlerMixingRule;
import termo.phase.Phase;

@Named("eqProUtils")
@SessionScoped
public class EqProUtils implements Serializable{

	private static final long serialVersionUID = 9099469579674262309L;
	private final Set<Compound> components = new HashSet<>();
    private final List<Alpha> alphaList = new ArrayList<>();
    private final List<Cubic> eos = new ArrayList<>();
    private final List<Phase> phases = new ArrayList<>();
    private final List<MixingRule> mixingRules = new ArrayList<>();
    
    public EqProUtils(){   
         
        phases.add(Phase.LIQUID);
        phases.add(Phase.VAPOR);
        alphaList.addAll(Alphas.getAllAvailableAlphas());
        eos.addAll(EquationsOfState.getAllAvailableCubicEquations());
        
        
        mixingRules.add(new VDWMixingRule());
        mixingRules.add(new MathiasKlotzPrausnitzMixingRule());
        NRTLActivityModel nrt = new NRTLActivityModel();
        mixingRules.add(new HuronVidalMixingRule(nrt, eos.get(0)));
        mixingRules.add(new WongSandlerMixingRule(nrt, eos.get(0)));
        WilsonActivityModel wilson = new WilsonActivityModel();
        mixingRules.add(new HuronVidalMixingRule(wilson, eos.get(0)));
        mixingRules.add(new WongSandlerMixingRule(wilson, eos.get(0)));
        
        
       
    }
    
    
    public Compound getComponent(String name){
        for(Compound componen: components){
            if(componen.getName().equals(name)){
                return componen;
            }
        }
        System.out.println("componente con encontrado");
        return null;
    }
    
        /**
     * @return the components
     */
    public  Set<Compound> getComponents() {
        return components;
    }
    public  void addComponent(Compound com){
        components.add(com);
    }
    
    /**
     * @return the alphaList
     */
    public  List<Alpha> getAlphaList() {
        return alphaList;
    }
    public  void addAlpha(Alpha alpha){
        alphaList.add(alpha);
    }
    
    
    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/index.xhtml?faces-redirect=true";
    }

    /**
     * @return the eos
     */
    public List<Cubic> getEos() {
        return eos;
    }

    /**
     * @return the phases
     */
    public List<Phase> getPhases() {
        return phases;
    }

    /**
     * @return the mixingRules
     */
    public List<MixingRule> getMixingRules() {
        return mixingRules;
    }

  


    
}
