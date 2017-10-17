package engine;

import java.util.ArrayList;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import inOut.ArgumentPrinter;
import inOut.ArgumentParser;
import userInterfact.Window;

public class Controller {
	
	private KieSession kSession;
	private Window window;
	private ArgumentPrinter printer;
	private ArgumentParser parser;
	
	private ArrayList<String> factsToPrint=new ArrayList<>();
	private ArrayList<String> argumentsToPrint=new ArrayList<>();
	private ArrayList<String> resultsToPrint=new ArrayList<>();
	
	private ArrayList<Argument> argumentsCollected = new ArrayList<>();
	private ArrayList<Fact> factsCollected = new ArrayList<>();
	
	public void init(){
		this.window=new Window(this);
		initKieSession();
		window.setVisible(true);
		//kSession.insert(window);	//so rules can print on textAreas	NONO, MVC, CANT CONNECT MODEL TO VIEW >:(
		kSession.insert(this);		//so rules can deposit information here
		 printer = new ArgumentPrinter();
		 parser = new ArgumentParser();
	}
	
	/**Initialize KieSession
	 * 
	 * Creates new KieSession, does not fire rules
	 */
	public void initKieSession(){
        try {
            // load up the knowledge base
	        KieServices ks = KieServices.Factory.get();
    	    KieContainer kContainer = ks.getKieClasspathContainer();
        	kSession = kContainer.newKieSession("ksession-rules");
        } catch (Throwable t) {
            t.printStackTrace();
        }
	}
	
	public void createArgument(ArrayList<String> premises, ArrayList<String> conclusions, String name, boolean defeasible){
		try{
			Argument arg=new Argument();
			for(String i:premises){
				Expression exp=new Expression(i, arg.getId());
				arg.addPremise(exp);
				kSession.insert(exp);
			}
			for(String i:conclusions){
				Expression exp=new Expression(i, arg.getId());
				arg.addConclusion(exp);
				kSession.insert(exp);
			}
			arg.setName(name);
			arg.setDefeasible(defeasible);
			kSession.insert(arg);
			System.out.println("Argument created: " + arg.getPrintable());
		} catch(Exception e){
			System.out.println("ERROR While creating argument: \"" + name + "\", error: " + e.getMessage());
		}
	}
	
	public void addExistingArgument(Argument arg){
		/*ArrayList<String> premiseStrings = new ArrayList<>();
		for(Expression i: arg.getPremises()){
			premiseStrings.add(i.getExp());
		}
		ArrayList<String> conclusionStrings = new ArrayList<>();
		for(Expression i: arg.getConclusions()){
			conclusionStrings.add(i.getExp());
		}
		createArgument(premiseStrings, conclusionStrings, arg.getName(), arg.getDefeasible());*/
		try{
			for(Expression i: arg.getPremises()){
				kSession.insert(i);
			}
			for(Expression i: arg.getConclusions()){
				kSession.insert(i);
			}
			kSession.insert(arg);
			System.out.println("Argument inserted: " + arg.getPrintable());
		} catch (Exception e){
			System.out.println("ERROR While adding existing argument: \"" + arg.getName() + "\", error: " + e.getMessage());
		}
	}
	
	public void createFact(String exp){
		try {
			Fact f=new Fact(exp);
			kSession.insert(f);
			System.out.println("Created fact: " + exp + "id: " + f.getSourceId());
		} catch (Exception e) {
			System.out.println("ERROR While creating fact: \"" + exp + "\", error: " + e.getMessage());
		}
	}
	
	public void addExistingFact(Fact f){
		createFact(f.getExp());			//WHAT THE ACTUAL HECK this works, the commented version doesnt O.O
		/*try{
			kSession.insert(f);
			System.out.println("Adding existing fact: " + f.getExp() + "id: " + f.getSourceId());
		} catch (Exception e) {
			System.out.println("ERROR While creating fact: \"" + f.getExp() + "\", error: " + e.getMessage());
		}*/
	}
	
	public void removeFact(String exp){
		//System.out.println("Removing fact: " + exp);
		kSession.insert(new removeFact(exp));
		kSession.fireAllRules();
		/*kSession.fireAllRules(new AgendaFilter() {
			@Override
			public boolean accept(Match arg0) {
				if(arg0.getRule().getName().equals("Remove Selected Fact"))
					return true;
				return false;
			}
		});*/
		
		updateControllerInWorkingMemory();
	}
	
	public void fireRules(){
		resetLists();	//reset earlier results
		System.out.println("Firing rules");
		kSession.fireAllRules();
		System.out.println("Done firing rules");
		updateControllerInWorkingMemory();
		
		printToWindow();
	}
	private void resetLists(){
		factsToPrint=new ArrayList<>();
		argumentsToPrint=new ArrayList<>();
		resultsToPrint=new ArrayList<>();
	}
	private void printToWindow(){
		window.printFacts(factsToPrint);
		window.printArguments(argumentsToPrint);
		window.printResults(resultsToPrint);
	}
	
	public void updateListsFromWorkingMemory(){
		resetLists();
		System.out.println("Updating list from working memory");
		kSession.fireAllRules();
		/*kSession.fireAllRules(new AgendaFilter() {
			
			@Override
			public boolean accept(Match arg0) {
				if(arg0.getRule().getName().contains("Collect")){
					System.out.println("Collect to print: rule fired");
					return true;
				}
				System.out.println("Not suitable rule: " + arg0.getRule().getName());
				return false;
			}
		});*/
		System.out.println("Done collecting:");
		System.out.println("Facts: " + factsToPrint);
		System.out.println("Arguments: " + argumentsToPrint);
		System.out.println("Results: " + resultsToPrint);
		
		updateControllerInWorkingMemory();
		
		printToWindow();
	}
	
	public void updateControllerInWorkingMemory(){
		kSession.update(kSession.getFactHandle(this), this);		//update controller in knowledge base with itself, so the collecting rules can fire again
	}
	
	public void printToDataBase(){
		printer.addData(argumentsCollected, factsCollected);
		System.out.println("Argument Printer received the data");
		printer.printAllArguments();
		System.out.println("Printing arguments done");
		printer.printAllFacts();
		System.out.println("Printing Facts done");
	}
	
	public void readFromDataBase(){
		System.out.println("Parsing data from DB");
		ArrayList<Argument> resultArgs = parser.parseAllArguments();
		System.out.println("Amount of arguments read: " + resultArgs.size());
		insertArgumentsIntoWorkingMemory(resultArgs);
		ArrayList<Fact> resultFacts = parser.parseAllFacts();
		System.out.println("Amount of facts read: " + resultFacts.size());
		insertFactsIntoWorkingMemory(resultFacts);
		System.out.println("Done parsing data");
		resetLists();
		printToWindow();
	}
	
	private void insertArgumentsIntoWorkingMemory(ArrayList<Argument> args){
		int maxId=0;
		for(Argument i : args){
			if(i.getId()>maxId){
				maxId = i.getId();
			}
		}
		System.out.println("Argument new IDENTIFIER set: " + maxId);
		for(Argument i : args){
			addExistingArgument(i);
		}
		System.out.println("New IDENTIFIER of arguments: " + maxId);
		Argument.setIDENTIFIER(maxId);
	}
	
	private void insertFactsIntoWorkingMemory(ArrayList<Fact> facts){
		for(Fact i : facts){
			addExistingFact(i);
		}
	}
	
	//rules will put here the according strings and objects
	public void addResult(Fact result) throws Exception{
		if(!resultsToPrint.contains(result))
		{
			if(result == null){
				System.out.println("ERROR Trying to insert null result");
				throw new Exception("ERROR Trying to insert null result");
			}
			if(result.getExp() == null){
				System.out.println("ERROR Trying to insert result with null exp");
				throw new Exception("ERROR Trying to insert result with null exp");
			}
			resultsToPrint.add(result.getExp());
			factsCollected.add(result);				//might be WRONG
		} else {
			System.out.println("Result fact already contained: " + result.getExp());
		}
	}
	
	public void addFact(Fact fact){
		factsToPrint.add(fact.getExp());
		factsCollected.add(fact);
	}
	
	public void addArgument(Argument arg){
		argumentsToPrint.add(arg.getName());
		argumentsCollected.add(arg);
	}
	
/////////////////////////////////////////////////////////////GETTERS AND SETTERS///////////////////////////////////////////////////////////////
	public KieSession getkSession() {
		return kSession;
	}

	public void setkSession(KieSession kSession) {
		this.kSession = kSession;
	}
	
	public class removeFact{
		String exp;
		public removeFact(String exp){
			this.exp=exp;
		}
		public String getExp(){
			return exp;
		}
	}
	
	public Window getWindow(){
		return window;
	}
}
