package org.aksw.simba.hare.input;

import java.io.BufferedWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.aksw.simba.hare.model.Data;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class StatGeneration {

	public static void main(String[] args) throws IOException {
		HAREResultReader hr = new HAREResultReader();

		StatGeneration sg = new StatGeneration();
		ArrayList<Data> hareResult = new ArrayList<Data>();
		hareResult = hr.getHareResults();
		ArrayList<Data> prResult = new ArrayList<Data>();
		prResult = hr.getPRResults();

		sg.writeToFile(sg.calcuatePRSetDifference(sg.generatePair(hareResult), sg.generatePair(prResult)), "PAGERANK");
		sg.writeToFile(sg.calcuateHareSetDifference(sg.generatePair(hareResult), sg.generatePair(prResult)), "HARE");

	}

	public void writeToFile(HashMap<String, ArrayList<Pair<String, String>>> setDiffPR, String result)
			throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter("output_"+result+".txt"));

		for (String key : setDiffPR.keySet()) {
			out.write(result + "\n");
			out.write("=============" + "\n");
			out.write(key + "\n");
			out.write("======================================" + "\n");
			ArrayList<Pair<String, String>> res = setDiffPR.get(key);
			for (Pair<String, String> ele : res) {
				out.write("=============" + "\n");
				out.write(ele.getLeft().toString() + "\n");
				out.write(ele.getRight().toString() + "\n");
				out.write("============END OF PAIR=====================" + "\n");
			}
			out.write("============END OF CATEGORY=====================" + "\n\n");
		}
	}

	public HashMap<String, ArrayList<Pair<String, String>>> calcuatePRSetDifference(
			HashMap<String, ArrayList<Pair<String, String>>> hare,
			HashMap<String, ArrayList<Pair<String, String>>> pr) {
		HashMap<String, ArrayList<Pair<String, String>>> setDifference = new HashMap<String, ArrayList<Pair<String, String>>>();
		for (String key : pr.keySet()) {
			if (hare.containsKey(key)) {
				ArrayList<Pair<String, String>> prUriList = pr.get(key);
				ArrayList<Pair<String, String>> hareUriList = hare.get(key);
				System.out.println(key);
				System.out.println("pR LIST " + prUriList.size());
				ArrayList<Pair<String, String>> res = new ArrayList<Pair<String, String>>(prUriList);
				for (Pair<String, String> ele : prUriList) {
					if (existinList(hareUriList, ele)) {
						res.remove(ele);
					}
				}
				System.out.println("Final");
				System.out.println(res.size());
				setDifference.put(key, res);
			}
		}
		return setDifference;
	}

	public HashMap<String, ArrayList<Pair<String, String>>> calcuateHareSetDifference(
			HashMap<String, ArrayList<Pair<String, String>>> hare,
			HashMap<String, ArrayList<Pair<String, String>>> pr) {
		HashMap<String, ArrayList<Pair<String, String>>> setDifference = new HashMap<String, ArrayList<Pair<String, String>>>();
		for (String key : pr.keySet()) {
			if (hare.containsKey(key)) {
				ArrayList<Pair<String, String>> prUriList = pr.get(key);
				ArrayList<Pair<String, String>> hareUriList = hare.get(key);
				System.out.println(key);
				System.out.println("hare LIST " + hareUriList.size());
				ArrayList<Pair<String, String>> res1 = new ArrayList<Pair<String, String>>(hareUriList);
				for (Pair<String, String> ele : hareUriList) {
					if (existinList(prUriList, ele)) {
						res1.remove(ele);
					}
				}
				System.out.println("Final");
				System.out.println(res1.size());
				setDifference.put(key, res1);
			}
		}
		return setDifference;
	}

	public HashMap<String, ArrayList<Pair<String, String>>> generatePair(ArrayList<Data> list) {
		HashMap<String, ArrayList<Pair<String, String>>> hmap = new HashMap<String, ArrayList<Pair<String, String>>>();

		for (Data cat : list) {
			ArrayList<Pair<String, String>> uriList = new ArrayList<Pair<String, String>>();
			for (String ele : cat.getUri()) {
				for (String ele1 : cat.getUri()) {
					if (!ele.equals(ele1)) {
						Pair<String, String> newPair = new ImmutablePair<String, String>(ele, ele1);
						if (uriList.size() == 0) {
							uriList.add(newPair);
						} else {
							if (!existinList(uriList, newPair))
								uriList.add(newPair);
						}
					}
				}
				hmap.put(cat.getCategory(), uriList);

			}

		}
		return hmap;

	}

	public boolean existinList(ArrayList<Pair<String, String>> list, Pair<String, String> newPair) {
		for (Pair<String, String> ele : list) {
			if ((ele.getLeft().equals(newPair.getLeft()) && (ele.getRight().equals(newPair.getRight())))
					|| ((ele.getRight().equals(newPair.getLeft())) && (ele.getLeft().equals(newPair.getRight())))) {
				return true;
			}
		}

		return false;
	}

}
