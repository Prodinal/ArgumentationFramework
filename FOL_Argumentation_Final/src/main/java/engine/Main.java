package engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.kie.api.runtime.KieSession;

public class Main {
	
	private static Controller cont;
	public static void main(String[] args) {
		cont=new Controller();
		cont.init();
		
		debug(cont.getkSession(), cont);
	}
	
	private static void debug(KieSession kSession, Controller cont){
		try{
			//debugMapContainsTest();
			//debugVariableHierarchyControllerTest();
			//debugExpressionNegatedTest();
			//debugSubstitutableExpressionTest();
			//debugSmartSubstitutionTest(kSession);
			//debugCheckExpressionRecreate();
			//debugTestExpressionSubstituteAndCreate();
			//debugTestBasicSubstitution(kSession);
			//debugTestRuleflowGroupAgendafilter(kSession);
			
			//debugGatheringFactsThatMightBeSubstituted(kSession);
			
			//debugTestDatabasePrinting(cont);
			//debugTestInfiniteLoop(cont);
			
			debugTestAfterParsing(cont);
			//debugPrintSingleFactToDB(cont);
		}
		catch(Exception e){
			System.out.println("AFITYFENÉT: " + e.getMessage());
			//System.out.println(e.)
		}
	}

	@SuppressWarnings("unused")
	private static void debugSamePremiseAttackTest(KieSession kSession) throws Exception{
		Argument attacker=new Argument();
		Expression expAtk=new Expression("~alma(x)");
		attacker.addConclusion(expAtk);
		attacker.setConclusionState(Argument.TRUE);
		attacker.setConnectionState(Argument.TRUE);
		attacker.setName("attacker");
		attacker.setPremiseState(Argument.TRUE);
		Argument defender=new Argument();
		Expression expDef=new Expression("alma(x)");
		defender.addPremise(expDef);
		defender.setConnectionState(Argument.TRUE);
		defender.setName("defender");
		defender.setPremiseState(Argument.TRUE);
		
		kSession.insert(expAtk);
		kSession.insert(expDef);
		kSession.insert(attacker);
		kSession.insert(defender);
		kSession.fireAllRules();
	}
	@SuppressWarnings("unused")
	private static void debugExpressionBreak_up_Test(KieSession kSession) throws Exception{
		Expression e=new Expression("van_neki(Pista,pénz)");
		System.out.println("Expression: " + e.getExp());
		System.out.println("Function: " + e.getFunction());
		System.out.println("Variables: " + e.getVariables());
	}
	@SuppressWarnings("unused")
	private void debugSubstitutionTest(KieSession kSession) throws Exception{
		Argument attacker=new Argument();
		Expression expAtk=new Expression("~gyumolcsok(x,y)");
		attacker.addConclusion(expAtk);
		attacker.setConclusionState(Argument.TRUE);
		attacker.setConnectionState(Argument.TRUE);
		attacker.setName("attacker");
		attacker.setPremiseState(Argument.TRUE);
		Argument defender=new Argument();
		Expression expDef=new Expression("gyumolcs(dio)");
		defender.addPremise(expDef);
		defender.setConnectionState(Argument.TRUE);
		defender.setName("defender");
		defender.setPremiseState(Argument.TRUE);
		
		for(Variable i:expAtk.getVariables())
			kSession.insert(i);
		for(Variable i:expDef.getVariables())
			kSession.insert(i);
		kSession.insert(expAtk);
		kSession.insert(expDef);
		kSession.insert(attacker);
		kSession.insert(defender);
		kSession.fireAllRules();
	}
	@SuppressWarnings("unused")
	private static void debugMapContainsTest(){
		Map<Variable, ArrayList<Variable>> test=new HashMap<>();
		Variable tmp=new Variable("alma");
		ArrayList<Variable> almaProps=new ArrayList<>();
		almaProps.add(new Variable("gyümölcs"));
		test.put(tmp, almaProps);
		System.out.println(test.containsKey(new Variable("alma")));
	}
	@SuppressWarnings("unused")
	private static void debugVariableHierarchyControllerTest(){
		ArrayList<Variable> almaProps=new ArrayList<>();
		almaProps.add(new Variable("gyümölcs"));
		almaProps.add(new Variable("piros"));
		almaProps.add(new Variable("növény"));
		VariableHierarcyHolder.add(new Variable("alma"), almaProps);
		
		ArrayList<Variable> korteProps=new ArrayList<>();
		korteProps.add(new Variable("gyümölcs"));
		korteProps.add(new Variable("sárga"));
		VariableHierarcyHolder.add(new Variable("körte"), korteProps);
		
		ArrayList<Variable> gyumolcsProps=new ArrayList<>();
		gyumolcsProps.add(new Variable("ehető"));
		gyumolcsProps.add(new Variable("fánnő"));
		gyumolcsProps.add(new Variable("alma"));
		gyumolcsProps.add(new Variable("növény"));
		VariableHierarcyHolder.add(new Variable("gyümölcs"), gyumolcsProps);
		
		ArrayList<Variable> novenyProps=new ArrayList<>();
		novenyProps.add(new Variable("vízkell"));
		novenyProps.add(new Variable("él"));
		VariableHierarcyHolder.add(new Variable("növény"), novenyProps);
		
		System.out.println(VariableHierarcyHolder.getAllAncestors(new Variable("alma")));
		System.out.println(VariableHierarcyHolder.getAllAncestors(new Variable("körte")));
	}
	@SuppressWarnings("unused")
	private static void debugExpressionNegatedTest() throws Exception{
		Expression tmp=new Expression("~alma(x)");
		System.out.println("Expression:" + tmp.getExp());
		System.out.println("Function:" + tmp.getFunction());
		System.out.println("Plus:" + tmp.isPlus());
		
		tmp=new Expression("korte(x)");
		System.out.println("Expression:" + tmp.getExp());
		System.out.println("Function:" + tmp.getFunction());
		System.out.println("Plus:" + tmp.isPlus());
	}
	@SuppressWarnings("unused")
	private static void debugSubstitutableExpressionTest() throws Exception{
		Expression exp1=new Expression("alma(x)");
		Expression exp2=new Expression("alma(józsef)");
		Expression exp3=new Expression("alma(y)");
		Expression exp4=new Expression("alma(ilona)");
		System.out.println(exp1.getExp() + " " + exp2.getExp() + " substitutable->: " + exp1.substitutable(exp2) + " the other way<- " + exp2.substitutable(exp1));
		System.out.println(exp1.getExp() + " " + exp3.getExp() + " substitutable->: " + exp1.substitutable(exp3) + " the other way<- " + exp2.substitutable(exp1));
		System.out.println(exp1.getExp() + " " + exp4.getExp() + " substitutable->: " + exp1.substitutable(exp4) + " the other way<- " + exp2.substitutable(exp1));
		
		System.out.println(exp2.getExp() + " " + exp3.getExp() + " substitutable->: " + exp2.substitutable(exp3) + " the other way<- " + exp3.substitutable(exp2));
		System.out.println(exp2.getExp() + " " + exp4.getExp() + " substitutable->: " + exp2.substitutable(exp4) + " the other way<- " + exp4.substitutable(exp2));
		
		System.out.println(exp3.getExp() + " " + exp4.getExp() + " substitutable->: " + exp3.substitutable(exp4) + " the other way<- " + exp4.substitutable(exp3));
	}
	@SuppressWarnings("unused")
	private static void debugSmartSubstitutionTest(KieSession kSession) throws Exception{
		Argument attacker1=new Argument();
		Expression expAtkPrem1=new Expression("fa(x)");
		Expression expAtk1=new Expression("~gyumolcs(x)");		//ha x fa, akkor x nem gyümölcs
		attacker1.addConclusion(expAtk1);
		attacker1.addPremise(expAtkPrem1);
		attacker1.setPremiseState(Argument.TRUE);
		attacker1.setConnectionState(Argument.TRUE);
		attacker1.setName("ha fa akk nem gyumolcs");
		
		Argument attacker2=new Argument();
		Expression expAtk2=new Expression("~eheto(x)");		//ha x nem gyumolcs, akkor x nem eheto
		Expression expAtkPrem2=new Expression("~gyumolcs(x)");
		attacker2.addConclusion(expAtk2);
		attacker2.addPremise(expAtkPrem2);
		attacker2.setPremiseState(Argument.TRUE);
		attacker2.setConnectionState(Argument.TRUE);
		attacker2.setName("ha nem gyumolcs akk nem eheto");
		
		Argument defender=new Argument();
		Expression expDef1=new Expression("fa(x)");
		Expression expDef2=new Expression("eheto(x)");
		Expression expDefConc=new Expression("ragos(x)");
		defender.addPremise(expDef1);
		defender.addPremise(expDef2);
		defender.addConclusion(expDefConc);
		defender.setConnectionState(Argument.TRUE);
		defender.setName("ha eheto es fa, akkor ragos");
		
		/*for(Variable i:expAtk.getVariables())
			kSession.insert(i);
		for(Variable i:expDef.getVariables())
			kSession.insert(i);*/
		kSession.insert(expAtk1);
		kSession.insert(expAtkPrem1);
		kSession.insert(expAtk2);
		kSession.insert(expAtkPrem2);
		kSession.insert(expDef1);
		kSession.insert(expDef2);
		kSession.insert(expDefConc);
		kSession.insert(attacker1);
		kSession.insert(defender);
		kSession.fireAllRules();
	}
	@SuppressWarnings("unused")
	private static void debugCheckExpressionRecreate() throws Exception{
		Expression exp=new Expression("almafa(x,y)");
		System.out.println("old exp: " + exp.getExp());
		exp.getVariables().get(0).setName("q");
		exp.recreateExpression();
		System.out.println("new exp (with q) " + exp.getExp());
	}
	@SuppressWarnings("unused")
	private static void debugGatheringFactsThatMightBeSubstituted(KieSession kSession) throws Exception{
		Fact f1=new Fact("alma(Béla)");		//Béla egy alma
		Fact f2=new Fact("érett(Béla)");	//Béla érett
		Fact f3=new Fact("~rothadt(y)");	//semmi nem rothadt
		
		Argument fix1=new Argument();
		Expression prem1=new Expression("alma(x)");			//alma
		Expression prem2=new Expression("érett(x)");		//érett
		Expression prem3=new Expression("~rothadt(x)");		//nem rothadt
		Expression conc=new Expression("ehető(x)");			//ehető
		fix1.addPremise(prem1);
		fix1.addPremise(prem2);
		fix1.addPremise(prem3);
		fix1.addConclusion(conc);
		fix1.setDefeasible(false);
		
		kSession.insert(f1);
		kSession.insert(f2);
		kSession.insert(f3);
		kSession.insert(prem1);
		kSession.insert(prem2);
		kSession.insert(prem3);
		kSession.insert(conc);
		kSession.insert(fix1);
		
		kSession.fireAllRules();
	}
	@SuppressWarnings("unused")
	private static void debugTestExpressionSubstituteAndCreate() throws Exception{
		Expression original=new Expression("veszvalamennyiért(bazsi,x,y)");
		Expression result=original.substituteAndCreate(new Variable("x"), new Variable("q"));
		Expression result2=result.substituteAndCreate(new Variable("bazsi"), new Variable("Béla"));
		
		System.out.println("Original: " + original.getExp());
		System.out.println("substituted: " + result.getExp());
		System.out.println(result.getVariables());
		
		System.out.println("substituted: " + result2.getExp());
		System.out.println(result2.getVariables());
	}
	private static void debugTestBasicSubstitution(KieSession kSession) throws Exception{
		//debugTestBasicSubstitution1(kSession);
		//cont.init();
		//System.out.println("--------------------------------------------------------------------------");
		//debugTestBasicSubstitution2(kSession);
		//debugTestBasicSubstitution3(kSession);
		//debugTestBasicSubstitution4(kSession);
		//debugTestBasicSubstitution5(kSession);
		//debugTestBasicSubstitution6(kSession);
		//debugTestBasicSubstitution7(kSession);
		//debugTestBasicSubstitution8(kSession);
		//debugTestBasicSubstitution9(kSession);
		debugTestBasicSubstitution10(kSession);
	}
	@SuppressWarnings("unused")
	private static void debugTestBasicSubstitution1(KieSession kSession) throws Exception{
		Argument simple=new Argument();
		Expression prem=new Expression("alma(x)");
		Expression conc=new Expression("eheto(x)");
		simple.addPremise(prem);
		simple.addConclusion(conc);
		simple.setDefeasible(false);
		
		Fact fact=new Fact("alma(béla)");
		
		System.out.println("Argument created");
		System.out.println("Fact created: " + fact.getExp());
		
		kSession.insert(simple);
		kSession.insert(prem);
		kSession.insert(conc);
		kSession.insert(fact);
		
		kSession.fireAllRules();
	}
	@SuppressWarnings("unused")
	private static void debugTestBasicSubstitution2(KieSession kSession) throws Exception{
		Argument simple=new Argument();
		Expression prem=new Expression("alma(béla)");
		Expression conc=new Expression("eheto(béla)");
		simple.addPremise(prem);
		simple.addConclusion(conc);
		simple.setDefeasible(false);
		
		Fact fact=new Fact("alma(x)");
		
		System.out.println("Argument created");
		System.out.println("Fact created: " + fact.getExp());
		
		kSession.insert(simple);
		kSession.insert(prem);
		kSession.insert(conc);
		kSession.insert(fact);
		
		kSession.fireAllRules();
	}
	private static void debugTestBasicSubstitution3(KieSession kSession) throws Exception{
		Argument simple=new Argument();
		Expression prem1=new Expression("alma(x)");
		Expression prem2=new Expression("érett(x)");
		Expression conc=new Expression("eheto(x)");
		simple.addPremise(prem1);
		simple.addPremise(prem2);
		simple.addConclusion(conc);
		simple.setDefeasible(false);
		
		Fact fact1=new Fact("alma(béla)");
		Fact fact2=new Fact("érett(gyuri)");
		
		System.out.println("Argument created");
		
		kSession.insert(simple);
		kSession.insert(prem1);
		kSession.insert(prem2);
		kSession.insert(conc);
		kSession.insert(fact1);
		kSession.insert(fact2);
		
		kSession.fireAllRules();
	}
	private static void debugTestBasicSubstitution4(KieSession kSession) throws Exception{
		Argument simple=new Argument();
		Expression prem1=new Expression("vanneki(béla,x)");
		//Expression prem2=new Expression("érett(x)");
		Expression conc=new Expression("drága(x)");
		simple.addPremise(prem1);
		//simple.addPremise(prem2);
		simple.addConclusion(conc);
		simple.setDefeasible(false);
		
		Fact fact1=new Fact("vanneki(y,z)");
		//Fact fact2=new Fact("érett(gyuri)");
		
		System.out.println("Argument created");
		
		kSession.insert(simple);
		kSession.insert(prem1);
		//kSession.insert(prem2);
		kSession.insert(conc);
		kSession.insert(fact1);
		//kSession.insert(fact2);
		
		kSession.fireAllRules();
	}
	private static void debugTestBasicSubstitution5(KieSession kSession) throws Exception{
		Argument simple=new Argument();
		Expression prem1=new Expression("alma(x)");
		Expression prem2=new Expression("vanneki(béla,x)");
		Expression conc=new Expression("eszik(béla,x)");
		simple.addPremise(prem1);
		simple.addPremise(prem2);
		simple.addConclusion(conc);
		simple.setDefeasible(false);
		
		Fact fact1=new Fact("alma(y)");
		Fact fact2=new Fact("vanneki(béla, margit)");
		
		System.out.println("Argument created");
		
		kSession.insert(simple);
		kSession.insert(prem1);
		kSession.insert(prem2);
		kSession.insert(conc);
		kSession.insert(fact1);
		kSession.insert(fact2);
		
		kSession.fireAllRules();
	}
	private static void debugTestBasicSubstitution6(KieSession kSession) throws Exception{
		Argument simple=new Argument();
		Expression prem1=new Expression("alma(x)");
		Expression prem2=new Expression("rossz(x)");
		Expression conc1=new Expression("ehetetlen(x)");
		Expression conc2=new Expression("szereti(patkány,x)");
		simple.addPremise(prem1);
		simple.addPremise(prem2);
		simple.addConclusion(conc1);
		simple.addConclusion(conc2);
		simple.setDefeasible(false);
		
		Fact fact1=new Fact("alma(béla)");
		Fact fact2=new Fact("rossz(bélus)");
		
		System.out.println("Argument created");
		
		kSession.insert(simple);
		kSession.insert(prem1);
		kSession.insert(prem2);
		kSession.insert(conc1);
		kSession.insert(conc2);
		kSession.insert(fact1);
		kSession.insert(fact2);
		
		kSession.fireAllRules();
	}
	private static void debugTestBasicSubstitution7(KieSession kSession) throws Exception{
		Argument simple1=new Argument();
		Expression prem11=new Expression("alma(x)");
		Expression prem12=new Expression("rossz(x)");
		Expression conc11=new Expression("ehetetlen(x)");
		Expression conc12=new Expression("szereti(patkány,x)");
		simple1.addPremise(prem11);
		simple1.addPremise(prem12);
		simple1.addConclusion(conc11);
		simple1.addConclusion(conc12);
		simple1.setDefeasible(false);
		
		Argument simple2=new Argument();
		Expression prem21=new Expression("szereti(x,y)");
		Expression prem22=new Expression("nigga(y)");
		Expression conc21=new Expression("~rassizsta(x)");
		Expression conc22=new Expression("meglopják(x)");
		simple2.addPremise(prem21);
		simple2.addPremise(prem22);
		simple2.addConclusion(conc21);
		simple2.addConclusion(conc22);
		simple2.setDefeasible(false);
		
		Fact fact1=new Fact("alma(béla)");
		Fact fact2=new Fact("rossz(béla)");
		Fact fact3=new Fact("nigga(béla)");
		
		System.out.println("Argument created");
		
		kSession.insert(simple1);
		kSession.insert(simple2);
		kSession.insert(prem11);
		kSession.insert(prem12);
		kSession.insert(conc11);
		kSession.insert(conc12);
		kSession.insert(prem21);
		kSession.insert(prem22);
		kSession.insert(conc21);
		kSession.insert(conc22);
		kSession.insert(fact1);
		kSession.insert(fact2);
		kSession.insert(fact3);
		
		kSession.fireAllRules();
	}
	private static void debugTestBasicSubstitution8(KieSession kSession) throws Exception{
		Argument simple=new Argument();
		Expression prem=new Expression("alma(béla)");
		Expression conc=new Expression("~alma(béla)");
		simple.addPremise(prem);
		simple.addConclusion(conc);
		simple.setDefeasible(false);
		simple.setName("Ha alma akkor nem alma.");
		
		Argument following=new Argument();
		Expression prem2=new Expression("~alma(y)");
		Expression conc2=new Expression("körte(y)");
		following.setDefeasible(false);
		following.addPremise(prem2);
		following.addConclusion(conc2);
		following.setName("Ha nem alma akkor körte");
		
		Fact fact=new Fact("alma(x)");
		
		System.out.println("Argument created");
		System.out.println("Fact created: " + fact.getExp());
		
		kSession.insert(simple);
		kSession.insert(prem);
		kSession.insert(conc);
		kSession.insert(fact);
		kSession.insert(following);
		kSession.insert(prem2);
		kSession.insert(conc2);
		
		kSession.fireAllRules();
	}
	private static void debugTestBasicSubstitution9(KieSession kSession) throws Exception{
		Argument simple=new Argument();
		Expression prem=new Expression("alma(x)");
		Expression conc=new Expression("finom(x)");
		simple.addPremise(prem);
		simple.addConclusion(conc);
		simple.setDefeasible(false);
		simple.setName("Ha alma akkor finom");
		
		Argument following=new Argument();
		Expression prem2=new Expression("finom(x)");
		Expression prem3=new Expression("szép(x)");
		Expression conc2=new Expression("eszik(béla,x)");
		Expression conc3=new Expression("jóllakik(béla)");
		following.setDefeasible(true);
		following.setConnectionState(Argument.TRUE);
		following.addPremise(prem2);
		following.addPremise(prem3);
		following.addConclusion(conc2);
		following.addConclusion(conc3);
		following.setName("Finom és szép, akkor eszik béla, jóllakik béla");
		
		Fact fact=new Fact("alma(józsi)");
		Fact fact2=new Fact("szép(y)");
		
		System.out.println("Argument created");
		System.out.println("Fact created: " + fact.getExp());
		
		kSession.insert(simple);
		kSession.insert(prem);
		kSession.insert(conc);
		kSession.insert(fact);
		kSession.insert(fact2);
		kSession.insert(following);
		kSession.insert(prem2);
		kSession.insert(prem3);
		kSession.insert(conc2);
		kSession.insert(conc3);
		
		kSession.fireAllRules();
		
		System.out.println("First session over");
		System.out.println("--------------------------------------");
		
		Fact contra=new Fact("~szép(y)");
		Fact contra2=new Fact("~jóllakik(x)");
		
		kSession.insert(contra);
		kSession.insert(contra2);
		
		kSession.fireAllRules();
		System.out.println("Second session over");
		System.out.println("--------------------------------------");
		
		cont.updateControllerInWorkingMemory();
		cont.updateListsFromWorkingMemory();
		cont.updateControllerInWorkingMemory();
		cont.getWindow().rePaint();
		
		System.out.println("Debug done");
	}
	
	private static void debugTestBasicSubstitution10(KieSession kSession) throws Exception{
		Argument simple=new Argument();
		Expression prem=new Expression("kutya(Buksi,t)");
		Expression conc=new Expression("állat(Buksi,mindig)");
		simple.addPremise(prem);
		simple.addConclusion(conc);
		simple.setDefeasible(false);
		
		Fact fact=new Fact("kutya(Buksi,most)");
		
		System.out.println("Argument created");
		System.out.println("Fact created: " + fact.getExp());
		
		kSession.insert(simple);
		kSession.insert(prem);
		kSession.insert(conc);
		kSession.insert(fact);
		
		System.out.println("SubstitutableTest: ");
		System.out.print("Is " + prem.getExp() + "substitutable with " + fact.getExp() + ": ");
		System.out.println(fact.substitutable(prem));
		
		kSession.fireAllRules();
	}
	
	private static void debugTestRuleflowGroupAgendafilter(KieSession kSession) throws Exception{
		Fact f=new Fact("alma(x)");
		kSession.insert(f);
		//kSession.inse
		System.out.println("defeasible: " + f.getExp() + " " + f.defeasible());
		cont.updateListsFromWorkingMemory();
		System.out.print("Done");
	}
	
	private static void debugTestDatabasePrinting(Controller cont) throws Exception{	
		
		cont.createFact("kutya(Buksi,most)");
		
		ArrayList<String> prems = new ArrayList<>();
		prems.add("kutya(Buksi,t)");
		ArrayList<String> concs = new ArrayList<>();
		concs.add("állat(Buksi,mindig)");
		cont.createArgument(prems, concs, "Ha buksi most kutya, akkor Buksi mindig állat", false);
		
		System.out.println("Argument created");
		System.out.println("Fact created: kutya(Buksi,most)");
		
		cont.fireRules();
		
		cont.printToDataBase();
	}
	
	private static void debugTestInfiniteLoop(Controller cont){
		cont.createFact("állat(Buksi,mindig)");
		
		ArrayList<String> prems = new ArrayList<>();
		prems.add("állat(x,t)");
		ArrayList<String> concs = new ArrayList<>();
		concs.add("állat(x,mindig)");
		cont.createArgument(prems, concs, "Ha x most állat, akkor x mindig állat", false);
	}
	
	private static void debugTestAfterParsing(Controller cont){
		
		cont.readFromDataBase();
		
		ArrayList<String> prems1 = new ArrayList<>();
		prems1.add("állat(x,t)");
		ArrayList<String> concs1 = new ArrayList<>();
		concs1.add("ehető(x)");
		cont.createArgument(prems1, concs1, "állat(x,t) -> ehető(x)", true);
		
		/*cont.createFact("kutya(Buksi,most)");
		
		ArrayList<String> premsOriginal = new ArrayList<>();
		premsOriginal.add("kutya(Buksi,t)");
		ArrayList<String> concsOriginal = new ArrayList<>();
		concsOriginal.add("állat(Buksi,mindig)");
		cont.createArgument(premsOriginal, concsOriginal, "Ha buksi most kutya, akkor Buksi mindig állat", false);*/
		
		
		
		/*ArrayList<String> prems2 = new ArrayList<>();
		prems2.add("állat(Buksi,t)");
		ArrayList<String> concs2 = new ArrayList<>();
		concs2.add("ehető(Buksi)");
		cont.createArgument(prems2, concs2, "állat(Buksi,t) -> ehető(Buksi)", true);
		
		ArrayList<String> prems3 = new ArrayList<>();
		prems3.add("állat(Buksi,t)");
		ArrayList<String> concs3 = new ArrayList<>();
		concs3.add("ehető(x)");
		cont.createArgument(prems3, concs3, "állat(Buksi,t) -> ehető(x)", true);
		
		ArrayList<String> prems4 = new ArrayList<>();
		prems4.add("állat(Buksi,mindig)");
		ArrayList<String> concs4 = new ArrayList<>();
		concs4.add("ehető(x)");
		cont.createArgument(prems4, concs4, "állat(Buksi,mindig) -> ehető(x)", true);
		
		cont.createFact("yes(yes)");
		ArrayList<String> prems5 = new ArrayList<>();
		prems5.add("yes(x)");
		ArrayList<String> concs5 = new ArrayList<>();
		concs5.add("no(x)");
		cont.createArgument(prems5, concs5, "yes(x) -> no(x)", true);*/
		
		cont.fireRules();
	}
	
	private static void debugPrintSingleFactToDB(Controller cont){
		cont.createFact("állat(Buksi,mindig)");
		cont.fireRules();
		cont.printToDataBase();
	}
}
