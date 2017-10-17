package inOut;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.management.AttributeList;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.QueryResult;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;

import engine.Argument;
import engine.Expression;
import engine.Fact;
import engine.Variable;

public class ArgumentParser {

	private final Repository repo;
	private final ValueFactory factory;
	
	private final String RepositoryUrl = "http://localhost:8080/rdf4j-server/";
	private final String RepositoryID = "Argumentation";
	
	public final String NS;
	
	public ArgumentParser(){
		repo = new HTTPRepository(RepositoryUrl, RepositoryID);
		factory = repo.getValueFactory();
		NS = "http://Peti.meti#";
	}
	
	public ArrayList<Argument> parseAllArguments() {
		System.out.println("Parsing all arguments.");
		
		ArrayList<Argument> resultArray = new ArrayList<>();
		
		try (RepositoryConnection conn = repo.getConnection()) {
			String getAllArguments = ""
					+ "SELECT * WHERE {"
					+ "  { ?argName <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> \"Argument\" }"
					+ "}";
			//System.out.println("getAllArguments string: " + getAllArguments);
			TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, getAllArguments);

			List<BindingSet> allArgumentsCollection = null;
			try(TupleQueryResult allArgumentsBindings = tupleQuery.evaluate()){
				allArgumentsCollection = QueryResults.asList(allArgumentsBindings);
			}catch (Exception e) {
				System.out.println("ERROR evaluating query getAllArguments: " + getAllArguments);
				System.out.println("Errormessage: " + e.getMessage());
			}
			Iterator<BindingSet> allArguments = allArgumentsCollection.iterator();	
			while (allArguments.hasNext()) {
				//System.out.println("Parsing single argument");
				BindingSet bindingSet = allArguments.next();
				IRI argName = (IRI) bindingSet.getValue("argName");
				Argument parsedArg = parseArg(conn, argName);
				resultArray.add(parsedArg);
				System.out.println("Parsed single argument: " + parsedArg.getPrintable());
			}
		} catch (Exception e) {
			System.out.println("Error parsing data: " + e.getMessage());
		}
		return resultArray;
	}
	
	private Argument parseArg(RepositoryConnection conn, IRI argName) throws Exception{
		Argument resultArgument = new Argument();
		
		String getArgumentAttributes = ""
				+ "SELECT * WHERE{"
				+ "?foundArg ?pred ?obj"
				+ "}";
		TupleQuery getAttributes = conn.prepareTupleQuery(QueryLanguage.SPARQL, getArgumentAttributes);
		getAttributes.setBinding("foundArg", argName);
		
		List<BindingSet> attributesCollection = null;
		try(TupleQueryResult attributesBindings = getAttributes.evaluate()){
			attributesCollection = QueryResults.asList(attributesBindings);
		} catch (Exception e) {
			System.out.println("ERROR evaluating query getArgumentAttributes: " + getArgumentAttributes);
			System.out.println("Errormessage: " + e.getMessage());
		}
		Iterator<BindingSet> attributes = attributesCollection.iterator();
		//System.out.println("Evaluated getAllAttributes of argument");
		while(attributes.hasNext()){
			BindingSet attr = attributes.next();
			IRI pred = (IRI) attr.getValue("pred");
			if(pred.getLocalName().equals("name")){
				Literal name = (Literal) attr.getValue("obj");
				resultArgument.setName(name.getLabel());
				System.out.println("Parsed name: " + name.getLabel());
			} else if(pred.getLocalName().equals("id")){
				Literal id = (Literal) attr.getValue("obj");
				resultArgument.setId(Integer.parseInt(id.getLabel()));
				System.out.println("Parsed id: " + id.getLabel());
			} else if(pred.getLocalName().equals("conclusionState")){
				Literal conclusionState = (Literal) attr.getValue("obj");
				resultArgument.setConclusionState(Integer.parseInt(conclusionState.getLabel()));
				System.out.println("Parsed conclusionState: " + conclusionState.getLabel());
			} else if(pred.getLocalName().equals("premiseState")){
				Literal premiseState = (Literal) attr.getValue("obj");
				resultArgument.setPremiseState(Integer.parseInt(premiseState.getLabel()));
				System.out.println("Parsed premiseState: " + premiseState.getLabel());
			} else if(pred.getLocalName().equals("connectionState")){
				Literal connectionState = (Literal) attr.getValue("obj");
				resultArgument.setConnectionState(Integer.parseInt(connectionState.getLabel()));
				System.out.println("Parsed connectionState: " + connectionState.getLabel());
			} else if(pred.getLocalName().equals("support")){
				Literal support = (Literal) attr.getValue("obj");
				resultArgument.setSupport(Integer.parseInt(support.getLabel()));
				System.out.println("Parsed support: " + support.getLabel());
			} else if(pred.getLocalName().equals("defeasible")){
				Literal defeasible = (Literal) attr.getValue("obj");
				resultArgument.setDefeasible(Boolean.getBoolean(defeasible.getLabel()));
				System.out.println("Parsed defeasible: " + defeasible.getLabel());
			} else if(pred.getLocalName().equals("premises")){
				IRI premises = (IRI) attr.getValue("obj");
				resultArgument.setPremises(parseExpressionList(conn, premises));
				//System.out.println("Parsed premises: " + premises.stringValue());
			} else if(pred.getLocalName().equals("conclusions")){
				IRI conclusions = (IRI) attr.getValue("obj");
				resultArgument.setConclusions(parseExpressionList(conn, conclusions));
				//System.out.println("Parsed conclusions: " + conclusions.stringValue());
			}
		}
		return resultArgument;
	}
	
	private ArrayList<Expression> parseExpressionList(RepositoryConnection conn, IRI node) throws Exception{
		
		ArrayList<Expression> resultExpression = new ArrayList<>();
		//System.out.println("Parsing expression list of node (localName): " + node.getLocalName());
		String getExpressionListLength = ""
				+ "SELECT * WHERE{"
				+ "?listNode ?pred ?length"
				+ "}";
		//String getManualExpressionListLength = "SELECT * WHERE{<http://Peti.meti#Habuksimostkutya,akkorBuksimindigállatConclusions> <http://Peti.meti#length> ?length}";
		TupleQuery getLength = conn.prepareTupleQuery(QueryLanguage.SPARQL, getExpressionListLength);
		getLength.setBinding("listNode", node);
		//System.out.println("Binding listNode to: " + node);
		getLength.setBinding("pred", factory.createIRI(NS, "length"));
		//System.out.println("Binding pred to: " + factory.createIRI(NS, "length"));
		//System.out.println("Query: " + getExpressionListLength);
		//System.out.println("Manual query: " + getManualExpressionListLength);
		int size = 0;
		
		try(TupleQueryResult attributes = getLength.evaluate()){
			//System.out.println("Expression evaluated: " + getExpressionListLength);
			if(attributes.hasNext()){
				BindingSet lengthBinding = attributes.next();
				Literal length = (Literal) lengthBinding.getValue("length");
				size = Integer.parseInt(length.getLabel());
				//System.out.println("Found length: " + size);
			} else {
				throw new Exception("Cannot find lenght property of expressionList");	
			}
		} catch (Exception e) {
			System.out.println("ERROR evaluating query getExpressionListLength: " + getExpressionListLength);
			System.out.println("Errormessage: " + e.getMessage());
		}
		
		String getAllExpressionListMembers = ""
				+ "SELECT * WHERE{"
				+ "?listNode ?pred ?member ."
				+ "?member ?numberPred ?index ."
				+ "?member ?valuePred ?obj"
				+ "}";
		//System.out.println("About to evaluate getAllExpressionListMembers");
		for(int i=0;i<size;i++){
			TupleQuery getListMember = conn.prepareTupleQuery(QueryLanguage.SPARQL, getAllExpressionListMembers);
			getListMember.setBinding("listNode", node);
			getListMember.setBinding("pred", factory.createIRI(NS, "contains"));
			getListMember.setBinding("numberPred", factory.createIRI(NS, "number"));
			getListMember.setBinding("index", factory.createLiteral(i));
			getListMember.setBinding("valuePred", factory.createIRI(NS, "value"));
			
			List<BindingSet> expressionNodeCollectionList = null;
			try(TupleQueryResult expressionNodeCollectionBindings = getListMember.evaluate()){
				expressionNodeCollectionList = QueryResults.asList(expressionNodeCollectionBindings);
			} catch (Exception e) {
				System.out.println("ERROR evaluating query getExpressionAllListMembers: " + getAllExpressionListMembers);
				System.out.println("Errormessage: " + e.getMessage());
			}
			Iterator<BindingSet> expressionNodeCollection = expressionNodeCollectionList.iterator();
			IRI expressionNode = null;
			if(expressionNodeCollection.hasNext()){
				expressionNode = (IRI) expressionNodeCollection.next().getValue("obj");
			} else {
				throw new Exception("Cannot find member number: " + i + " in expressionListNode: " + node.stringValue());
			}
			resultExpression.add(parseExpression(conn, expressionNode));
		}
		return resultExpression;
	}
	
	private Expression parseExpression(RepositoryConnection conn, IRI expressionNode) throws Exception{
		
		Expression result = null;
		try {
			result = new Expression("Placeholder(x)");
		} catch (Exception e) {
			System.out.println("Error creating result Expression");
		}
		
		String getExpressionAttributes = ""
				+ "SELECT * WHERE{"
				+ "?foundExp ?pred ?obj"
				+ "}";
		TupleQuery getAttributes = conn.prepareTupleQuery(QueryLanguage.SPARQL, getExpressionAttributes);
		getAttributes.setBinding("foundExp", expressionNode);
		
		List<BindingSet> attributesCollection = null;
		try(TupleQueryResult attributesBindings = getAttributes.evaluate()){
			attributesCollection = QueryResults.asList(attributesBindings);
		} catch (Exception e) {
			System.out.println("ERROR evaluating query getExpressionAttributes: " + getExpressionAttributes);
			System.out.println("Errormessage: " + e.getMessage());
		}
		Iterator<BindingSet> attributes = attributesCollection.iterator();
		while(attributes.hasNext()){
			BindingSet attr = attributes.next();
			IRI pred = (IRI) attr.getValue("pred");
			if(pred.getLocalName().equals("expression")){
				Literal expression = (Literal) attr.getValue("obj");
				result.setExp(expression.getLabel());
				System.out.println("Expression: parsed expression: " + expression.getLabel());
			} else if(pred.getLocalName().equals("sourceId")){
				Literal sourceId = (Literal) attr.getValue("obj");
				result.setSourceID(Integer.parseInt(sourceId.getLabel()));
				System.out.println("Expression: parsed sourceId: " + sourceId.getLabel());
			} else if(pred.getLocalName().equals("function")){
				Literal function = (Literal) attr.getValue("obj");
				result.setFunction(function.getLabel());
				System.out.println("Expression parsed function: " + function.getLabel());
			} else if(pred.getLocalName().equals("plus")){
				Literal plus = (Literal) attr.getValue("obj");
				result.setPlus(Boolean.getBoolean(plus.getLabel()));
				System.out.println("Expression: parsed plus: " + plus.getLabel());
			} else if(pred.getLocalName().equals("variables")){
				IRI variableListNode = (IRI) attr.getValue("obj");
				result.setVariables(parseVariableList(conn, variableListNode));
			}
		}
		
		return result;
	}
	
	private ArrayList<Variable> parseVariableList(RepositoryConnection conn, IRI node) throws Exception{
		ArrayList<Variable> results = new ArrayList<>();
		
		String getVariableListLength = ""
				+ "SELECT * WHERE{"
				+ "?listNode ?pred ?length"
				+ "}";
		TupleQuery getLength = conn.prepareTupleQuery(QueryLanguage.SPARQL, getVariableListLength);
		getLength.setBinding("listNode", node);
		getLength.setBinding("pred", factory.createIRI(NS, "length"));
		int size = 0;
		try(TupleQueryResult attributes = getLength.evaluate()){
			if(attributes.hasNext()){
				BindingSet lengthBinding = attributes.next();
				Literal length = (Literal) lengthBinding.getValue("length");
				size = Integer.parseInt(length.getLabel());
			} else {
				throw new Exception("Cannot find lenght property of expressionList");	
			}
		} catch (Exception e) {
			System.out.println("ERROR evaluating query getVariableListLength: " + getVariableListLength);
			System.out.println("Errormessage: " + e.getMessage());
		}
		
		String getAllListMembers = ""
				+ "SELECT * WHERE{"
				+ "?listNode ?pred ?member ."
				+ "?member ?numberPred ?index ."
				+ "?member ?valuePred ?obj"
				+ "}";
		for(int i=0;i<size;i++){
			TupleQuery getListMember = conn.prepareTupleQuery(QueryLanguage.SPARQL, getAllListMembers);
			getListMember.setBinding("listNode", node);
			getListMember.setBinding("pred", factory.createIRI(NS, "contains"));
			getListMember.setBinding("numberPred", factory.createIRI(NS, "number"));
			getListMember.setBinding("index", factory.createLiteral(i));
			getListMember.setBinding("valuePred", factory.createIRI(NS, "value"));
			
			List<BindingSet> expressionNodeCollectionList = null;
			try(TupleQueryResult expressionNodeCollectionBindings = getListMember.evaluate()){
				expressionNodeCollectionList = QueryResults.asList(expressionNodeCollectionBindings);
			} catch (Exception e) {
				System.out.println("ERROR evaluating query getAllListMembers: " + getAllListMembers);
				System.out.println("Errormessage: " + e.getMessage());
			}
			Iterator<BindingSet> expressionNodeCollection = expressionNodeCollectionList.iterator();
			IRI expressionNode = null;
			if(expressionNodeCollection.hasNext()){
				expressionNode = (IRI) expressionNodeCollection.next().getValue("obj");
			} else {
				throw new Exception("Cannot find member number: " + i + " in expressionListNode: " + node.stringValue());
			}
			results.add(parseVariable(conn, expressionNode));
		}
		
		return results;
	}
	
	private Variable parseVariable(RepositoryConnection conn, IRI variable){
		Variable result = new Variable("placeHolder");
		
		String getExpressionAttributes = ""
				+ "SELECT * WHERE{"
				+ "?foundVar ?pred ?obj"
				+ "}";
		TupleQuery getAttributes = conn.prepareTupleQuery(QueryLanguage.SPARQL, getExpressionAttributes);
		getAttributes.setBinding("foundVar", variable);
		
		List<BindingSet> attributesCollection = null;
		try(TupleQueryResult attributesFound = getAttributes.evaluate()){
			attributesCollection = QueryResults.asList(attributesFound);
		} catch (Exception e) {
			System.out.println("ERROR evaluating query getExpressionAttributes: " + getExpressionAttributes);
			System.out.println("Errormessage: " + e.getMessage());
		}
		Iterator<BindingSet> attributes = attributesCollection.iterator();
		while(attributes.hasNext()){
			BindingSet attr = attributes.next();
			IRI pred = (IRI) attr.getValue("pred");
			if(pred.getLocalName().equals("name")){
				Literal name = (Literal) attr.getValue("obj");
				result.setName(name.getLabel());
				System.out.println("Variable: parsed name: " + name.getLabel());
			}
		}
		return result;
	}
	
	//////////////////////////////////////////////////////////////////////FACT////////////////////////////
	
	public ArrayList<Fact> parseAllFacts(){
		System.out.println("Parsing all facts");
		ArrayList<Fact> resultArray = new ArrayList<>();
		
		try (RepositoryConnection conn = repo.getConnection()) {
			String getAllFacts = ""
					+ "SELECT * WHERE {\n"
					+ "  { ?factName <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> \"Fact\" }\n"
					+ "}";

			TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, getAllFacts);

			List<BindingSet> attributesCollection = null;
			try(TupleQueryResult allFactsBindings = tupleQuery.evaluate()){
				attributesCollection = QueryResults.asList(allFactsBindings);
			} catch (Exception e) {
				System.out.println("ERROR evaluating query getAllFacts: " + getAllFacts);
			}
			Iterator<BindingSet> allFacts = attributesCollection.iterator();
			while (allFacts.hasNext()) {
				BindingSet bindingSet = allFacts.next();
				IRI factName = (IRI) bindingSet.getValue("factName");
				resultArray.add(parseFact(conn, factName));
			}
		} catch (Exception e) {
			System.out.println("Fact: error parsing data: " + e.getMessage());
			System.out.println("Errormessage: " + e.getMessage());
		}
		return resultArray;
	}
	
	private Fact parseFact(RepositoryConnection conn, IRI fact) throws Exception{
		Fact result = new Fact("Placeholder(x)");
		
		String getFactAttributes = ""
				+ "SELECT * WHERE{"
				+ "?foundFact ?pred ?obj"
				+ "}";
		TupleQuery getAttributes = conn.prepareTupleQuery(QueryLanguage.SPARQL, getFactAttributes);
		getAttributes.setBinding("foundFact", fact);
		
		List<BindingSet> attributesCollection = null;
		try(TupleQueryResult attributesBindings = getAttributes.evaluate()){
			attributesCollection = QueryResults.asList(attributesBindings);
		} catch (Exception e) {
			System.out.println("ERROR evaluating query getFactAttributes: " + getFactAttributes);
			System.out.println("Errormessage: " + e.getMessage());
		}
		Iterator<BindingSet> attributes = attributesCollection.iterator();
		while(attributes.hasNext()){
			BindingSet attr = attributes.next();
			IRI pred = (IRI) attr.getValue("pred");
			if(pred.getLocalName().equals("sourceId")){
				Literal id = (Literal) attr.getValue("obj");
				result.setSourceId(Integer.parseInt(id.getLabel()));
				System.out.println("Parsed id: " + id.getLabel());
			} else if(pred.getLocalName().equals("expression")){
				//System.out.println("Type of obj: " + attr.getValue("obj"));
				IRI expression = (IRI) attr.getValue("obj");
				result.setExpression(parseExpression(conn, expression));
				//System.out.println("Parsed name: " + expression.getLocalName());
			}
		}	
		return result;
	}
}
