package org.aksw.simba.hare.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.aksw.simba.hare.input.JsonReader;
import org.aksw.simba.hare.model.Data;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EvalController {

	ArrayList<Data> entityList = new ArrayList<Data>();
	// should be equal to the return of mergesort_sort () function
	ArrayList<Data> resultList = new ArrayList<Data>();

	// contructor to read the file
	public EvalController() {
		JsonReader jr = new JsonReader();
		try {
			this.entityList = jr.readFile("lists.json");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// temp solution to Database
	HashMap<String, String> userLog = new HashMap<String, String>();

	// result file creating function
	public void writeToFile(String userInput, String userName)
			throws IOException {
		File resultfile = new File("user_data" + File.separator + userName
				+ ".json");
		if (!resultfile.exists()) {

			resultfile.getParentFile().mkdirs();
			resultfile.createNewFile();
		}
		FileWriter fileWriter = new FileWriter(resultfile, true);

		for (Data res : resultList) {
			JSONObject obj = new JSONObject();
			obj.put("Category", res.getCategory());
			JSONArray uris = new JSONArray();
			uris.add(res.getUri());
			obj.put("URI", uris);

		}

		fileWriter.write(userInput);
		fileWriter.flush();
		fileWriter.close();

	}

	// the function to interact with user
	@RequestMapping(value = "/next", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Data> getEntity() throws ParseException {
		Data entityForComparison;
		// temp
		entityForComparison = entityList.get(0);
		return new ResponseEntity<Data>(entityForComparison, HttpStatus.OK);
	}

	// merge sort code to sort the list
	public ArrayList<Data> mergesort_sort(ArrayList<Data> list) {
		if (list.size() == 1) {
			return list;
		} else {
			ArrayList<Data> left = new ArrayList<Data>();
			for (int i = 0; i < list.size() / 2; i++) {
				left.add(list.get(i));
			}
			ArrayList<Data> right = new ArrayList<Data>();
			for (int i = list.size() / 2; i < list.size(); i++) {
				right.add(list.get(i));
			}
			return mergesort_merge(mergesort_sort(left), mergesort_sort(right));
		}
	}

	// Actual merging of the left and right list
	private ArrayList<Data> mergesort_merge(ArrayList<Data> leftList,
			ArrayList<Data> rightList) {
		ArrayList<Data> result = new ArrayList<Data>();
		while (leftList.size() > 0 && rightList.size() > 0) {
			// TODO: Code to send to user
		}
		for (Data i : leftList) {
			result.add(i);
		}
		for (Data i : rightList) {
			result.add(i);
		}
		return result;
	}

}
