package org.aksw.simba.hare.input;

import java.util.ArrayList;
import java.util.Arrays;

import com.hp.hpl.jena.query.ParameterizedSparqlString;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;

public class StatGeneration {

	private static final String BASE_URI = "http://dbpedia.org/sparql";
	ArrayList<String> CATEGORIES = new ArrayList<String>(Arrays.asList("City",
			"University", "VideoGame", "SpaceStation", "Mountain", "Hotel",
			"EurovisionSongContestEntry", "Drug", "Comedian", "ChessPlayer",
			"Band", "Album", "Automobile", "HistoricPlace", "Town",
			"MilitaryConflict", "ProgrammingLanguage", "Country", "Disease"));

	public ResultSet getPageRankResults(String endpoint, String cat) {
		ParameterizedSparqlString sparql_query = new ParameterizedSparqlString(
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
						+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
						+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
						+ "PREFIX dbo:<http://dbpedia.org/ontology/>\n"
						+ "PREFIX vrank:<http://purl.org/voc/vrank#>\n"
						+ "SELECT DISTINCT ?s ?v \n" + "WHERE {\n"
						+ "?s rdf:type+ dbo:" + cat + ".\n"
						+ "?s vrank:hasRank/vrank:rankValue ?v. \n" + "}\n"
						+ "ORDER BY DESC(?v) LIMIT 10\n");

		QueryExecution exec = QueryExecutionFactory.sparqlService(endpoint,
				sparql_query.asQuery());
		return exec.execSelect();
	}

	public ResultSet getDegreeResults(String endpoint, String cat) {
		ParameterizedSparqlString sparql_query = new ParameterizedSparqlString(
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
						+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
						+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
						+ "PREFIX dbo:<http://dbpedia.org/ontology/>\n"
						+ "PREFIX vrank:<http://purl.org/voc/vrank#>\n"
						+ "SELECT  ?cent (( ?indegree + ?outdegree ) AS ?degree)\n WHERE \n { \n { SELECT  (?s AS ?cent) (count(*) AS ?outdegree)\n"
						+ "WHERE {\n"
						+ "?s ?p ?o. \n"
						+ "?s rdf:type+ dbo:"
						+ cat
						+ ".\n"
						+ "}\n"
						+ "GROUP BY ?s\n ORDER BY DESC(?outdegree) \n }\n{\n "
						+ "SELECT  (?o AS ?cent) (count(*) AS ?indegree) \n"
						+ "WHERE {\n"
						+ "?s ?p ?o. \n"
						+ "?o rdf:type+ dbo:"
						+ cat
						+ ".\n"
						+ "}\n"
						+ "GROUP BY ?o\n ORDER BY DESC(?indegree) \n }\n	}ORDER BY DESC(?degree) LIMIT 10 ");

		QueryExecution exec = QueryExecutionFactory.sparqlService(endpoint,
				sparql_query.asQuery());
		return exec.execSelect();
	}

	public ResultSet getOutdegreeResults(String endpoint, String cat) {
		ParameterizedSparqlString sparql_query = new ParameterizedSparqlString(
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
						+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
						+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
						+ "PREFIX dbo:<http://dbpedia.org/ontology/>\n"
						+ "PREFIX vrank:<http://purl.org/voc/vrank#>\n"
						+ "SELECT  ?s (count(*) AS ?outdegree)\n" + "WHERE {\n"
						+ "?s ?p ?o. \n" + "?s rdf:type+ dbo:" + cat + ".\n"
						+ "}\n"
						+ "GROUP BY ?s \n ORDER BY DESC(?outdegree) LIMIT 10\n");

		QueryExecution exec = QueryExecutionFactory.sparqlService(endpoint,
				sparql_query.asQuery());
		return exec.execSelect();
	}

	public ResultSet getIndegreeResults(String endpoint, String cat) {
		ParameterizedSparqlString sparql_query = new ParameterizedSparqlString(
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
						+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
						+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
						+ "PREFIX dbo:<http://dbpedia.org/ontology/>\n"
						+ "PREFIX vrank:<http://purl.org/voc/vrank#>\n"
						+ "SELECT  ?o (count(*) AS ?indegree) \n" + "WHERE {\n"
						+ "?s ?p ?o. \n" + "?o rdf:type+ dbo:" + cat + ".\n"
						+ "}\n" + "GROUP BY ?o \nORDER BY DESC(?indegree) LIMIT 10\n");

		QueryExecution exec = QueryExecutionFactory.sparqlService(endpoint,
				sparql_query.asQuery());
		return exec.execSelect();
	}

	public static void main(String[] args) {
		StatGeneration comparator = new StatGeneration();
		for (String catName : comparator.CATEGORIES) {
			System.out.println(catName + ",");
			ResultSetFormatter.outputAsCSV(comparator.getDegreeResults(
					comparator.getBaseUri(), catName));
		}
	}

	public String getBaseUri() {
		return BASE_URI;
	}
}
