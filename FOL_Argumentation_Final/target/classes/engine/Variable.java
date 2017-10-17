package engine;

public class Variable implements Comparable<Variable>{
	private String name;
	
	public Variable(String name){
		this.name=name;
	}
	public Variable(Variable x){
		this.name=x.getName();
	}
	
	public boolean universal(){
		return name.length()==1;
	}
	public boolean concrete(){
		return !(name.length()==1);
	}
	public boolean substitutable(Variable other){
		if(this.concrete() && other.concrete())
			return false;
		return true;
	}
	
	/** Gets the stricter of the two variables
	 *  If both are universal, returns original
	 *  
	 * @param x the variable to be compared
	 * @returns the stricter of the two variables
	 * @throws Exception if both are concrete variables, thus cannot be substituted
	 */
	public Variable getStricter(Variable x) throws Exception{
		if(!substitutable(x)){
			throw new Exception("cannot substitute variable");
		} else {
			if(this.concrete())
				return this;
			if(x.concrete())
				return x;
			return this;
		}
	}
	public boolean isStricter(Variable x){
		if(this.concrete() && x.universal())
			return true;
		else
			return false;
	}
	////////////NECESSARY FOR COMPARING VARIABLES IN HASHMAP IN VariableHierarchyHolder
	@Override
	public boolean equals(Object o){
		return name.equals(((Variable) o).getName());
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	public int compareTo(Variable o) {
		System.out.println(this + " compared to " + o);
		if(equals(o))
			return 0;
		else
			return 1;
	}
	//////////////////////////////////////////GETTERS AND SETTERS/////////////////////////////////////
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String toString(){
		return name;
	}
	public boolean equals(Variable other){
		return this.name.equals(other.getName());
	}
	

}
