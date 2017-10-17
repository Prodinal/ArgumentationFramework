package engine;

import java.util.ArrayList;

public class Expression {
	private String exp;
	private String function;
	private ArrayList<Variable> variables=new ArrayList<>();
	//pon�lt, vagy neg�lt a kifejez�s
	private boolean plus=true;
	private int sourceId;
	
	public Expression(String expression) throws Exception{
		this(expression, -1);
	}
	
	public Expression(Expression expression) throws Exception{
		this(expression, -1);
	}
	
	public Expression(Expression x, int sourceId) throws Exception{
		this.exp=x.getExp();
		break_to_parts();
		this.sourceId=sourceId;
	}
	
	public Expression(String expression, int sourceId) throws Exception{
		exp=expression;
		break_to_parts();
		this.sourceId=sourceId;
	}
	
	private void break_to_parts() throws Exception{
		String workOn=exp;
		if(exp.substring(0, 1).equals("~")){	//if ~ present, then remove and set negated
			plus=false;
			workOn=exp.substring(1);
		}
		String[] parts=workOn.split("\\(");
		if(parts.length!=2){
			throw new Exception("wrong format: " + exp);
		}
		parts[1]=parts[1].replace(")", "");
		
		function=parts[0];
		String[] vars=parts[1].split(",");
		
		//TEMPORARY SOLUTION, DEFINING SUBTYPEOF SHOULD BE MORE SOPHISTICATED
		//TODO
		for(String i : vars){
			Variable tmp=new Variable(i);
			variables.add(tmp);
		}
	}
	
	/**Some standard format check
	 * 
	 * @param exp
	 * @returns true is correct format
	 */
	public static boolean correctFormat(String exp){
		if(!exp.substring(exp.length()-1).equals(")")){
			System.out.println("Wrong format, no last ')': " + exp);
			return false;
		}
		if(exp.split("\\(").length!=2){
			System.out.println("Wrong format, no two parts: " + exp);
			return false;
		}
		
		return true;
	}
	
	public void recreateExpression(){
		String newExp="";
		if(!plus)
			newExp+="~";
		newExp+=function;
		if(variables.isEmpty())
			return;
		newExp+="(";
		for(Variable i:variables){
			newExp+=i.getName() + ",";
		}
		newExp=newExp.subSequence(0, newExp.length()-1).toString();		//remove the last comma
		newExp+=(")");
		exp=newExp;
	}
	
	public boolean substitutable(Expression other){
		if(equalFunctions(other)){
			int j=0;
			for(Variable i:variables){						//for each variable
				if(i.concrete() && other.getVariables().get(j).concrete()){		//if both concrete
					if(!i.equals(other.getVariables().get(j))){					//and not the same name
						System.out.println("Incompatible variables: " + i.getName() + " and " + other.getVariables().get(j) + " in function " + other.getFunction());
						return false;											//then cant be substituted
					}
				}
				j++;
			}
			return true;
		} else {
			return false;
		}
	}
	/**Returns new Expression with the given variables substituted
	 * 
	 * @param which
	 * @param with
	 * @return
	 * @throws Exception 
	 */
	public Expression substituteAndCreate(Variable which, Variable with) throws Exception{
		Expression result=new Expression(this.exp, this.sourceId);
		int index=0;
		for(Variable i:result.getVariables()){
			if(i.equals(which)){
				result.getVariables().set(index, new Variable(with));
			}
			index++;
		}
		result.recreateExpression();
		return result;
	}
	
	public boolean equalFunctions(Expression other){
		return (function.equals(other.getFunction())) && (variables.size()==other.getVariables().size());	//same function, same amount of variables
	}
	public boolean equalExpressions(Expression other){
		return exp.equals(other.getExp());
	}
	public boolean oppositeSignum(Expression other){
		return plus!=other.isPlus();
	}
	public boolean conflicting(Expression other){
		return equalFunctions(other) && oppositeSignum(other);
	}
	@Override
	public String toString(){
		return exp;
	}
////////////////////////////////////////////SETTERS, GETTERS///////////////////////////////////////////
	public String getExp() {
		return exp;
	}
	public void setExp(String exp) {
		this.exp = exp;
	}
	public boolean isPlus() {
		return plus;
	}
	public void setPlus(boolean plus) {
		this.plus = plus;
	}
	public boolean isNegated(){
		return !plus;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public ArrayList<Variable> getVariables() {
		return variables;
	}

	public void setVariables(ArrayList<Variable> variables) {
		this.variables = variables;
	}
	public int getSourceID() {
		return sourceId;
	}
	public void setSourceID(int sourceID) {
		this.sourceId = sourceID;
	}
}
