package engine;

import java.util.HashMap;
import java.util.Map;

public class VariableSubstitutePairHolder {
	Map<Variable, Variable> pairs=new HashMap<>();
	
	/*public void put(Variable a, Variable b){
		if(!pairs.containsKey(a)){
			pairs.put(a, b);
		}else {
			if(pairs.get(a).substitutable(b)){
				
			}
		}
	}*/
	public void put(Variable a, Variable b){
		boolean overWritten=false;
		if(pairs.containsKey(a))
			overWritten=true;
		Variable tmp=pairs.put(a, b);
		if(overWritten)
			System.out.println("Overwritten variable, old: " + tmp + " new: " + b);
	}
	public Variable get(Variable a){
		return pairs.get(a);
	}
	public boolean holds(Variable x){
		return pairs.containsKey(x);
	}
}
