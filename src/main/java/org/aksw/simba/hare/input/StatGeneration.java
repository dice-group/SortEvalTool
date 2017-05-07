package org.aksw.simba.hare.input;

import java.util.ArrayList;
import java.util.Arrays;

import org.aksw.simba.hare.model.Data;

import com.hp.hpl.jena.query.ParameterizedSparqlString;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class StatGeneration {

	private static final String BASE_URI = "http://dbpedia.org/sparql";
	ArrayList<String> CATEGORIES = new ArrayList<String>(Arrays.asList("City",
			"University", "VideoGame", "SpaceStation", "Mountain", "Hotel",
			"EurovisionSongContestEntry", "Drug", "Comedian", "ChessPlayer",
			"Band", "Album", "Person", "Automobile", "HistoricPlace", "Town",
			"MilitaryConflict", "ProgrammingLanguage", "Country"));

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

	public static void main(String[] args) {
		StatGeneration comparator = new StatGeneration();
		ArrayList<Data> entity_list = new ArrayList<Data>();
		for (String catName : comparator.CATEGORIES) {
			System.out.println(catName + ",");

			ResultSet list = comparator.getPageRankResults(
					comparator.getBaseUri(), catName);
			ArrayList<String> members = new ArrayList<String>();
			while (list.hasNext()) {
				QuerySolution qs = list.next();
				members.add(new String(qs.getResource("s").getURI()));

			}
			entity_list.add(new Data(catName, members));
		}

	}

	public String getBaseUri() {
		return BASE_URI;
	}
}
