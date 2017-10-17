package inOut;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import engine.*;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;

public class ArgumentPrinter {
	private ArrayList<Argument> arguments;
	private ArrayList<Fact> facts;
	
	private final Repository repo;
	private final ValueFactory factory;
	public final String NS;
	
	private final String RepositoryUrl = "http://localhost:8080/rdf4j-server/";
	private final String RepositoryID = "Argumentation";
	
	private PrintWriter pw;
	
	public ArgumentPrinter(){
		repo = new HTTPRepository(RepositoryUrl, RepositoryID);
		factory = repo.getValueFactory();
		NS = "http://Peti.meti#";
		//repo.initialize();
		
		try {
			pw = new PrintWriter("outPut.turtle");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addData(ArrayList<Argument> arguments, ArrayList<Fact> facts){
		this.arguments = arguments;
		this.facts = facts;
	}
	
	public void printAllArguments(){
		System.out.println("Printing all arguments");
		try(RepositoryConnection conn = repo.getConnection()){
			for(Argument i : arguments){
				System.out.println("Printing argument: " + i.getName());
				try{
					printArgument(i, conn);
				}catch (Exception e){
					System.out.println("Error printing argument name: " + i.getName() + " error: " + e.getMessage());
				}
			}
		}catch (Exception e) {
			System.out.println("Error opening connection from repo in printAllArguments." + e.getMessage());
		}finally{
			System.out.println("Done printing all arguments");
		}
	}
	public void printAllFacts(){
		System.out.println("Printing all facts");
		try(RepositoryConnection conn = repo.getConnection()){
			for(Fact i : facts){
				System.out.println("Printing fact: " + i.getExp());
				try{
					printFact(i, conn, findSourceArgOfFact(i));
				}catch (Exception e){
					System.out.println("Error printing fact exp: " + i.getExp() + " error: " + e.getMessage());
				}
			}
		}catch (Exception e) {
			System.out.println("Error getting connection from repo in  printAllFacts." + e.getMessage());
		}finally{
			System.out.println("Done printing all facts");
			pw.close();
		}
	}
	
	private Argument findSourceArgOfFact(Fact f) throws Exception{
		if(f.getSourceId() == -1)
			return null;
		for(Argument i : arguments){
			if(i.getId() == f.getSourceId())
				return i;
		}
		throw new Exception("Source of fact : " + f.getExp() + " id: " + f.getSourceId() + " not found");
	}
	
	private String createNameCodeArgument(Argument arg){
		return arg.getName().replace(" ", "");
	}
	
	private void printArgument(Argument arg, RepositoryConnection conn){
		
		String nameCode = createNameCodeArgument(arg);
		IRI subj = factory.createIRI(NS, "Argument" + nameCode);
		
		Literal typeLit  = factory.createLiteral("Argument");
		//conn.add(subj, RDF.TYPE, typeLit);
		addToConnectionWithLog(conn, subj, RDF.TYPE, typeLit);
		
		String defeasible = arg.getDefeasible() ? "defeasible" : "non-defeasible";
		typeLit = factory.createLiteral(defeasible);
		//conn.add(subj, RDF.TYPE, typeLit);
		addToConnectionWithLog(conn, subj, RDF.TYPE, typeLit);

		IRI pred = factory.createIRI(NS, "name");
		Literal nameLit  = factory.createLiteral(arg.getName());
		//conn.add(subj, pred, nameLit);
		addToConnectionWithLog(conn, subj, pred, nameLit);
		
		pred = factory.createIRI(NS, "support");
		Literal lit = factory.createLiteral(arg.getSupport());
		//conn.add(subj, pred, lit);
		addToConnectionWithLog(conn, subj, pred, lit);
		
		pred = factory.createIRI(NS, "conclusionState");
		lit = factory.createLiteral(arg.getConclusionState());
		//conn.add(subj, pred, lit);
		addToConnectionWithLog(conn, subj, pred, lit);
		
		pred = factory.createIRI(NS, "premiseState");
		lit = factory.createLiteral(arg.getPremiseState());
		//conn.add(subj, pred, lit);
		addToConnectionWithLog(conn, subj, pred, lit);
		
		pred = factory.createIRI(NS, "connectionState");
		lit = factory.createLiteral(arg.getConnectionState());
		//conn.add(subj, pred, lit);
		addToConnectionWithLog(conn, subj, pred, lit);
		
		pred = factory.createIRI(NS, "defeasible");
		lit = factory.createLiteral(arg.getDefeasible());
		//conn.add(subj, pred, lit);
		addToConnectionWithLog(conn, subj, pred, lit);
		
		pred = factory.createIRI(NS, "id");
		lit = factory.createLiteral(arg.getId());
		//conn.add(subj, pred, lit);
		addToConnectionWithLog(conn, subj, pred, lit);
		
		pred = factory.createIRI(NS, "premises");
		IRI obj = factory.createIRI(NS, nameCode + "Premises");
		//conn.add(subj, pred, obj);
		addToConnectionWithLog(conn, subj, pred, obj);
		printExpressionList(conn, obj, arg.getPremises(), nameCode + "Premises");
		
		pred = factory.createIRI(NS, "conclusions");
		obj = factory.createIRI(NS, nameCode + "Conclusions");
		//conn.add(subj, pred, obj);
		addToConnectionWithLog(conn, subj, pred, obj);
		
		printExpressionList(conn, obj, arg.getConclusions(), nameCode + "Conclusions");
		
		
	}
	
	private void printExpressionList(RepositoryConnection conn, IRI subject, ArrayList<Expression> args, String nameCode){
		IRI lengthPred = factory.createIRI(NS, "length");
		Literal lengthLiteral = factory.createLiteral(args.size());
		addToConnectionWithLog(conn, subject, lengthPred, lengthLiteral);
		
		for(int i=0; i<args.size(); i++){
			IRI pred = factory.createIRI(NS, "contains");
			IRI obj = factory.createIRI(NS, nameCode + "Member" + i);
			//conn.add(subject,  pred, obj);
			addToConnectionWithLog(conn, subject, pred, obj);
			
			pred = factory.createIRI(NS, "number");
			Literal lit = factory.createLiteral(i);
			//conn.add(obj, pred, lit);
			addToConnectionWithLog(conn, obj, pred, lit);
			
			pred = factory.createIRI(NS, "value");
			IRI expNode = factory.createIRI(NS, nameCode + "Value" + i);
			//conn.add(obj, pred, expNode);
			addToConnectionWithLog(conn, obj, pred, expNode);
			
			printExpression(conn, expNode, nameCode + "Value" + i, args.get(i));
		}
	}
	
	private void printExpression(RepositoryConnection conn, IRI subject, String nameCode, Expression exp){
		IRI pred = factory.createIRI(NS, "expression");
		Literal lit = factory.createLiteral(exp.getExp());
		//conn.add(subject, pred, lit);
		addToConnectionWithLog(conn, subject, pred, lit);
		
		pred = factory.createIRI(NS, "function");
		lit = factory.createLiteral(exp.getFunction());
		//conn.add(subject, pred, lit);
		addToConnectionWithLog(conn, subject, pred, lit);
		
		pred = factory.createIRI(NS, "sourceId");
		lit = factory.createLiteral(exp.getSourceID());
		//conn.add(subject, pred, lit);
		addToConnectionWithLog(conn, subject, pred, lit);
		
		pred = factory.createIRI(NS, "plus");
		lit = factory.createLiteral(exp.isPlus());
		//conn.add(subject, pred, lit); 
		addToConnectionWithLog(conn, subject, pred, lit);
		
		pred = factory.createIRI(NS, "variables");
		IRI listNode = factory.createIRI(NS, nameCode + "Variables");
		//conn.add(subject, pred, listNode);
		addToConnectionWithLog(conn, subject, pred, listNode);
		
		printVariableList(conn, listNode, exp.getVariables(), nameCode + "Variables");
	}
	
	private void printVariableList(RepositoryConnection conn, IRI subject, ArrayList<Variable> args, String nameCode){
		IRI lengthPred = factory.createIRI(NS, "length");
		Literal lengthLiteral = factory.createLiteral(args.size());
		addToConnectionWithLog(conn, subject, lengthPred, lengthLiteral);
		
		for(int i=0; i<args.size(); i++){
			IRI pred = factory.createIRI(NS, "contains");
			IRI obj = factory.createIRI(NS, nameCode + "Member" + i);
			//conn.add(subject,  pred, obj);
			addToConnectionWithLog(conn, subject, pred, obj);
			
			pred = factory.createIRI(NS, "number");
			Literal lit = factory.createLiteral(i);
			//conn.add(obj, pred, lit);
			addToConnectionWithLog(conn, obj, pred, lit);
			
			pred = factory.createIRI(NS, "value");
			IRI variableNode = factory.createIRI(NS, nameCode + "Value" + i);
			//conn.add(obj, pred, variableNode);
			addToConnectionWithLog(conn, obj, pred, variableNode);
			
			printVariable(conn, variableNode, args.get(i));
		}
	}
	
	private void printVariable(RepositoryConnection conn, IRI subject, Variable var){
		IRI pred = factory.createIRI(NS, "name");
		Literal lit = factory.createLiteral(var.getName());
		//conn.add(subject, pred, lit);
		addToConnectionWithLog(conn, subject, pred, lit);

		Literal universal = factory.createLiteral(var.universal() ? "universal" : "concrete");
		//conn.add(subject, RDF.TYPE, universal);
		addToConnectionWithLog(conn, subject, RDF.TYPE, universal);
		
	}
	
	//////////////////////////////////////////////////////////////////////FACT////////////////////////////
	
	private String createNameCodeFact(Fact f){
		return f.getExpression().getExp().replace(" ", "");
	}
	
	private void printFact(Fact f, RepositoryConnection conn, Argument source){
		String nameCode = createNameCodeFact(f);
		IRI subj = factory.createIRI(NS, "Fact" + nameCode);
		
		Literal typeLit  = factory.createLiteral("Fact");
		//conn.add(subj, RDF.TYPE, typeLit);
		addToConnectionWithLog(conn, subj, RDF.TYPE, typeLit);
		
		IRI pred = factory.createIRI(NS, "sourceId");
		Literal lit = factory.createLiteral(f.getSourceId());
		//conn.add(subj, pred, lit);
		addToConnectionWithLog(conn, subj, pred, lit);
		
		IRI obj;
		if(source != null){
			pred = factory.createIRI(NS, "source");
			obj = factory.createIRI(NS, createNameCodeArgument(source));
			//conn.add(subj, pred, obj);
			addToConnectionWithLog(conn, subj, pred, obj);
		} else {
			pred = factory.createIRI(NS, "source");
			//conn.add(subj, pred, RDF.NIL);
			addToConnectionWithLog(conn, subj, pred, RDF.NIL);
		}
		
		pred = factory.createIRI(NS, "expression");
		IRI expressionNode = factory.createIRI(NS, createNameCodeFact(f) + "Expression");
		addToConnectionWithLog(conn, subj, pred, expressionNode);
		
		printExpression(conn, expressionNode, createNameCodeFact(f) + "Expression", f.getExpression());
	}
	
	private void addToConnectionWithLog(RepositoryConnection conn, IRI subject, IRI pred, IRI obj){
		conn.add(subject, pred, obj);
		System.out.println("Adding triple: ");
		System.out.println("    " + subject.toString());
		System.out.println("    " + pred.toString());
		System.out.println("    " + obj.toString());
		
		pw.println("<" + subject.toString() + "> <" + pred.toString() + "> <" + obj.toString() + "> .");
	}
	
	private void addToConnectionWithLog(RepositoryConnection conn, IRI subject, IRI pred, Literal lit){
		conn.add(subject, pred, lit);
		System.out.println("Adding triple: ");
		System.out.println("    " + subject.toString());
		System.out.println("    " + pred.toString());
		System.out.println("    " + lit.toString());
		
		pw.println("<" + subject.toString() + "> <" + pred.toString() + "> " + lit.toString() + " .");
	}
}
