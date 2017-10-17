package engine;

import java.util.ArrayList;

public class Argument {
	public static final int FALSE=0;
	public static final int TRUE=1;
	public static final int UNDECIDED=2;
	private static int IDENTIFIER=0;
	
	private ArrayList<Expression> premises=new ArrayList<>();
	private ArrayList<Expression> conclusions=new ArrayList<>();
	private int support=0;
	private String name;
	private int id;
	private boolean defeasible=true;
	
	private int conclusionState=UNDECIDED;
	private int premiseState=UNDECIDED;
	private int connectionState=UNDECIDED;
	
	public Argument(){
		IDENTIFIER++;
		this.setId(IDENTIFIER);
	}
	
	public ArrayList<Variable> getAllVariables(){
		ArrayList<Variable> result=new ArrayList<>();
		
		for(Expression i:premises){
			for(Variable j:i.getVariables()){
				if(!i.getVariables().contains(j))
					result.add(j);
			}
		}
		for(Expression i:conclusions){
			for(Variable j:i.getVariables()){
				if(!i.getVariables().contains(j))
					result.add(j);
			}
		}
		
		return result;
	}
	
	public String getPrintable(){
		return "id: " + id + " " + premises + " -> " + conclusions;
	}
////////////////////////////////////////////SETTERS, GETTERS///////////////////////////////////////////
	public void addPremise(Expression e){
		premises.add(e);
		e.setSourceID(id);
	}
	public void addConclusion(Expression e){
		conclusions.add(e);
		e.setSourceID(id);
	}
	public Expression getPremise(int index){
		return premises.get(index);
	}
	public Expression getConclusion(int index){
		return conclusions.get(index);
	}
	public ArrayList<Expression> getPremises() {
		return premises;
	}
	public void setPremises(ArrayList<Expression> premises) {
		this.premises = premises;
	}
	public ArrayList<Expression> getConclusions() {
		return conclusions;
	}
	public void setConclusions(ArrayList<Expression> conclusions) {
		this.conclusions = conclusions;
	}
	public int getConclusionState() {
		return conclusionState;
	}
	public void setConclusionState(int conclusionState) {
		this.conclusionState = conclusionState;
	}
	public int getPremiseState() {
		return premiseState;
	}
	public void setPremiseState(int premisesState) {
		this.premiseState = premisesState;
	}
	public int getConnectionState() {
		return connectionState;
	}
	public void setConnectionState(int connectionState) {
		this.connectionState = connectionState;
	}
	public int getSupport() {
		return support;
	}
	public void setSupport(int support) {
		this.support = support;
	}
	public void incSupport(){
		support++;
	}
	public void incSupport(int a){
		support+=a;
	}
	public void decSupport(){
		support--;
	}
	public void decSupport(int a){
		support-=a;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isDefeasable() {
		return defeasible;
	}
	public boolean getDefeasible() {
		return defeasible;
	}
	public void setDefeasible(boolean defeasable) {
		this.defeasible = defeasable;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public static int getIDENTIFIER(){
		return IDENTIFIER;
	}
	
	public static void setIDENTIFIER(int ID){
		IDENTIFIER = ID;
	}
}
