package engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
/**Unused
 * 
 * @author Prodinal
 *
 */
public abstract class VariableHierarcyHolder {
	private static Map<Variable, ArrayList<Variable>> variableAncestors=new HashMap<Variable, ArrayList<Variable>>();
	
	public static void add(Variable name, ArrayList<Variable> subTypeOf){
		variableAncestors.put(name, subTypeOf);
	}
	public static Set<Variable> getAllAncestors(Variable name){
		HashSet<Variable> tmp=new HashSet<Variable>();
		tmp.add(name);
		Set<Variable> result=listAncestors(tmp, name, new HashSet<Variable>());
		return result;
	}
	
	private static Set<Variable> listAncestors(Set<Variable> result, Variable name, Set<Variable> alreadyProcessed){
		if(alreadyProcessed.contains(name))			//to avoid cycles
			return result;
		alreadyProcessed.add(name);
		
		if(!variableAncestors.containsKey(name))	//if it is not present
			return result;
		if(variableAncestors.get(name).isEmpty())	//or it has no ancestor RETURN
			return result;
		
		for(Variable i:variableAncestors.get(name)){
			result.add(i);
			listAncestors(result, i, alreadyProcessed);
		}
		
		return result;
	}
}
