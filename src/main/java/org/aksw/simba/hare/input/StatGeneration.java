package org.aksw.simba.hare.input;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import org.aksw.simba.hare.model.Data;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.h2.command.dml.Set;

import com.hp.hpl.jena.query.ParameterizedSparqlString;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class StatGeneration {

	public static void main(String[] args) throws IOException {
		HAREResultReader hr = new HAREResultReader();

		StatGeneration sg = new StatGeneration();
		ArrayList<Data> hareResult = new ArrayList<Data>();
		hareResult = hr.getHareResults();
		ArrayList<Data> prResult = new ArrayList<Data>();
		prResult = hr.getPRResults();

		BufferedWriter out = new BufferedWriter(new FileWriter("output.txt"));

		HashMap<String, ArrayList<Pairs>> setDiff = sg.calcuateSetDifference(sg.generatePairs(hareResult, "HARE"),
				sg.generatePairs(prResult, "PAGERANK"));

		for (String key : setDiff.keySet()) {
			out.write(key + "\n");
			out.write("======================================" + "\n");
			ArrayList<Pairs> res = setDiff.get(key);
			for (Pairs ele : res) {
				out.write(ele.resultFrom + "\n");
				out.write("=============" + "\n");
				out.write(ele.left.toString() + "\n");
				out.write(ele.right.toString() + "\n");
				out.write("============END OF PAIR=====================" + "\n");
			}
			out.write("============END OF CATEGORY=====================" + "\n\n");
		}

	}

	public HashMap<String, ArrayList<Pairs>> calcuateSetDifference(HashMap<String, ArrayList<Pairs>> hare,
			HashMap<String, ArrayList<Pairs>> pr) {

		HashMap<String, ArrayList<Pairs>> setDifference = new HashMap<String, ArrayList<Pairs>>();

		for (String key : pr.keySet()) {
			if (hare.containsKey(key)) {
				ArrayList<Pairs> prUriList = pr.get(key);
				ArrayList<Pairs> hareUriList = hare.get(key);
				System.out.println(key);
				System.out.println("pR LIST " + prUriList.size());
				System.out.println("hare LIST " + hareUriList.size());
				ArrayList<Pairs> res = new ArrayList<Pairs>(prUriList) ;
				ArrayList<Pairs> res1 = new ArrayList<Pairs>(hareUriList);
				
				res.removeAll(hareUriList);
				res1.removeAll(prUriList);
				/*
				for (Pairs e : prUriList) {
					if (existinList(hareUriList, e)) {
						res.remove(e);
					}
				}
				for (Pairs e : hareUriList) {
					if (existinList(prUriList, e)) {
						res1.remove(e);
					}
				}*/
				res.addAll(res1);
				System.out.println("Final");
				System.out.println(res.size());
				setDifference.put(key, res);
			}
		}
		return setDifference;
	}

	public HashMap<String, ArrayList<Pairs>> generatePairs(ArrayList<Data> list, String resultFrom) {
		HashMap<String, ArrayList<Pairs>> hmap = new HashMap<String, ArrayList<Pairs>>();

		for (Data cat : list) {
			ArrayList<Pairs> uriList = new ArrayList<Pairs>();
			for (String ele : cat.getUri()) {
				for (String ele1 : cat.getUri()) {
					if (!ele.equals(ele1)) {
						Pairs newPair = new Pairs(ele1, ele, resultFrom);
						if (uriList.size() == 0) {
							uriList.add(newPair);
						} else {
							if (!existinList(uriList, newPair))
								System.out.println("Pair   size " + newPair.toString());
							uriList.add(newPair);
						}
					}
				}
				hmap.put(cat.getCategory(), uriList);

			}

		}
		return hmap;

	}

	public boolean existinList(ArrayList<Pairs> list, Pairs newPair) {
		for (Pairs ele : list) {
			if (ele.equals(newPair))
				return true;
		}

		return false;
	}

}
