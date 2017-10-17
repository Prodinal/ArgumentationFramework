package engine;

import java.util.ArrayList;

public class Fact {
	private Expression expression;
	private int sourceId;		//which arg this fact comes from, so they can be retracted if the argument turns out to be wrong, negative equals absolute fact, no source
	
/////////////////////////////////////////////////////CONSTRUCTORS/////////////////////////////////////////////////////////////
	public Fact(Expression exp, int source) throws Exception{
		expression=new Expression(exp, source);
		this.sourceId=source;
	}
	public Fact(Expression exp) throws Exception{
		this(exp,-1);
	}
	public Fact(String exp, int source) throws Exception{
		expression=new Expression(exp, source);
		this.sourceId=source;
	}
	public Fact(String exp) throws Exception{
		this(exp,-1);
	}
	
/////////////////////////////////////////////////////FUNCTIONS/////////////////////////////////////////////////////////////
	public boolean defeasible(){
		return !(sourceId<0);
	}
	public boolean equalFunctions(Expression other){
		return expression.equalFunctions(other);
	}
	public boolean equalFunctions(Fact other){
		return expression.equalFunctions(other.getExpression());
	}
	public boolean equalExpressions(Expression other){
		System.out.println("Comparing " + expression + " and " + other.getExp() + " = " + expression.equalExpressions(other));
		return expression.equalExpressions(other);
	}
	public boolean equalExpressions(Fact other){
		return expression.equalExpressions(other.getExpression());
	}
	public boolean oppositeSignum(Expression other){
		return expression.oppositeSignum(other);
	}
	public boolean oppositeSignum(Fact other){
		return expression.oppositeSignum(other.getExpression());
	}
	public boolean conflicting(Expression other){
		return expression.conflicting(other);
	}
	public boolean substitutable(Expression other){
		return expression.substitutable(other);
	}
	public boolean substitutable(Fact other){
		return expression.substitutable(other.getExpression());
	}
	/*public boolean isEqual(Fact f){
		return this.getExp() == f.getExp();
	}*/
	public boolean hasPair(ArrayList<Expression> exps){
		for(Expression i:exps){
			if(i.equalFunctions(this.expression) && !i.oppositeSignum(expression))
				return true;
		}
		return false;
	}
	public Fact substituteAndCreate(Variable which, Variable with) throws Exception{
		return new Fact(expression.substituteAndCreate(which, with));
	}
	
	@Override
	public String toString(){
		return expression.toString();
	}
////////////////////////////////////////////SETTERS, GETTERS///////////////////////////////////////////////////////////////
	public String getExp() {
		return expression.getExp();
	}
	public void setExp(String exp) {
		this.expression.setExp(exp);
	}
	public boolean isPlus() {
		return expression.isPlus();
	}
	public void setPlus(boolean plus) {
		this.expression.setPlus(plus);
	}
	public boolean isNegated(){
		return expression.isNegated();
	}
	
	public Expression getExpression(){
		return expression;
	}
	public void setExpression(Expression exp){
		this.expression=exp;
	}
	public ArrayList<Variable> getVariables() {
		return this.expression.getVariables();
	}

	public void setVariables(ArrayList<Variable> variables) {
		this.expression.setVariables(variables);
	}
	
	public String getFunction(){
		return expression.getFunction();
	}

	public int getSourceId() {
		return sourceId;
	}

	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}
}
