package chimicae;

import termo.eos.alpha.Alpha;
import termo.optimization.NewtonMethodSolver;

/**
 *
 * @author Hugo
 */
public class ParameterViewModel{
    int index;
    NewtonMethodSolver  solver;
    Alpha alpha;
    public ParameterViewModel(int index, NewtonMethodSolver solver,Alpha alpha) {
        this.index = index;
        this.solver = solver;
        this.alpha = alpha;
    }
    
    public double getValue(){
        return solver.getErrorFunction().getParameter(index);
    }
    public void setValue(double value){
        solver.getErrorFunction().setParameter(value, index);
    }
    public boolean getFix(){
        return solver.getFixParameters()[index];
    }
    public void setFix(boolean fix){
        solver.getFixParameters()[index] = fix;
    }
    public boolean getConstrain(){
        return solver.getConstrainParameters()[index];
    }
    public void setConstrain(boolean fix){
        solver.getConstrainParameters()[index] = fix;
    }
    public double getMaxVariation(){
        return solver.getMaxVariationParameters()[index];
    }
    public void setMaxVariation(double variation){
        solver.getMaxVariationParameters()[index] = variation;
    }
    public String getName(){
        return alpha.getParameterName(index);
    }
    
  
    
}