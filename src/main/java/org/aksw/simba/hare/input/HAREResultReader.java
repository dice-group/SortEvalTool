package org.aksw.simba.hare.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.aksw.simba.hare.model.Data;

import com.hp.hpl.jena.query.ParameterizedSparqlString;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class HAREResultReader {
	ArrayList<Data> hareResults = new ArrayList<Data>();
	private final String BASE_URI = "http://dbpedia.org/sparql";
	ArrayList<String> CATEGORIES = new ArrayList<String>(
			Arrays.asList("City", "University", "VideoGame", "SpaceStation", "Mountain", "Hotel",
					"EurovisionSongContestEntry", "Drug", "Comedian", "ChessPlayer", "Band", "Album", "Person",
					"Automobile", "HistoricPlace", "Town", "MilitaryConflict", "ProgrammingLanguage", "Country"));

	public ArrayList<Data> getHareResults() throws IOException {
		File f = new File("/Users/Kunal/workspace/RankedLists");
		File[] files = f.listFiles();
		for (File file : files) {
			String category = file.getName();
			if (category.endsWith(".txt")) {
				BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
				String line;
				ArrayList<String> uriList = new ArrayList<String>();
				while (((line = br.readLine()) != null) && uriList.size() != 10) {
					line = line.split(" ")[0];
					uriList.add(line);
				}
				category = category.split("_H")[0];
				hareResults.add(new Data(category, uriList));
				//System.out.println("HARE   size " + category + " " + uriList.size());
			}
		}

		return hareResults;
	}

	public ResultSet executePageRankQuery(String endpoint, String cat) {
		ParameterizedSparqlString sparql_query = new ParameterizedSparqlString(
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
						+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
						+ "PREFIX dbo:<http://dbpedia.org/ontology/>\n" + "PREFIX vrank:<http://purl.org/voc/vrank#>\n"
						+ "SELECT DISTINCT ?s ?v \n" + "WHERE {\n" + "?s rdf:type+ dbo:" + cat + ".\n"
						+ "?s vrank:hasRank/vrank:rankValue ?v. \n" + "}\n" + "ORDER BY DESC(?v) LIMIT 10\n");

		QueryExecution exec = QueryExecutionFactory.sparqlService(endpoint, sparql_query.asQuery());
		return exec.execSelect();
	}

	public ArrayList<Data> getPRResults() {
		ArrayList<Data> pageRankResults = new ArrayList<Data>();
		for (String catName : this.CATEGORIES) {
			ResultSet list = this.executePageRankQuery(this.BASE_URI, catName);
			ArrayList<String> members = new ArrayList<String>();
			while (list.hasNext()) {
				QuerySolution qs = list.next();
				members.add(new String(qs.getResource("s").getURI()));
			}
			pageRankResults.add(new Data(catName, members));
		}
		return pageRankResults;
	}

}
