package org.aksw.simba.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.aksw.simba.HareEval.Data;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HAREController {

	@Autowired
	private ArrayList<ArrayList<Data>> classList = new ArrayList<ArrayList<Data>>();

	// temp solution to Database
	HashMap<String, Integer> userLog = new HashMap<String, Integer>();

	private final static org.slf4j.Logger logger = LoggerFactory
			.getLogger(HAREController.class);

	public int getUserClass(String userName) {
		int userClassindex;
		if (userLog.containsKey(userName) == false) {
			userLog.put(userName, 0);

		}
		userClassindex = userLog.get(userName);
		return userClassindex;
	}

	public HAREController() {

	}

	public Data getpivot(ArrayList<Data> dataset) {
		int middle = (int) Math.ceil((double) dataset.size() / 2);
		return dataset.get(middle);
	}

	@RequestMapping(value = "/next", produces = "application/json;charset=utf-8")
	public ResponseEntity<String> getList(
			@RequestParam(value = "username") String userName) {
		logger.info("Got a message to /next!");

		int classIndex = getUserClass(userName);
		ArrayList<Data> inputData = classList.get(classIndex);
		if (inputData.isEmpty()) {
			// Code to write in the same format as input.
			return new ResponseEntity<String>("null", null,
					HttpStatus.BAD_REQUEST);
		}
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json;charset=utf-8");
		return new ResponseEntity<String>(transformListToJson(inputData),
				responseHeaders, HttpStatus.OK);

	}

	public void writeToFile(String userInput, String userName)
			throws IOException {
		File resultfile = new File("user_data" + File.separator + userName
				+ ".txt");
		if (!resultfile.exists()) {
			logger.info("Creating new file");
			resultfile.getParentFile().mkdirs();
			resultfile.createNewFile();
		}

		FileWriter fileWriter = new FileWriter(resultfile, true);

		fileWriter.write(userInput);
		fileWriter.flush();
		fileWriter.close();
		logger.info("Result created");

	}

	@RequestMapping(value = "/submitResults", method = RequestMethod.POST)
	public String processUserinput(
			@RequestParam(value = "userResult") String userResult,
			@RequestParam(value = "username") String userName) {

		// ArrayList<Data> userRes = transformListFromJson(userResult);

		// Write in the required format
		try {
			this.writeToFile(userResult, userName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:next?username=" + userName;
	}

	/*private ArrayList<Data> transformListFromJson(String userInput) {
		ArrayList<Data> userOrder = new ArrayList<Data>();
		JSONArray list = new JSONArray(userInput);

		for (int i = 0; i < list.length(); i++) {
			Data ele = new Data(list.getJSONObject(i).getString("uri").trim());
			userOrder.add(ele);
		}
		return userOrder;
	}*/

	public String transformListToJson(ArrayList<Data> inputList) {
		JSONObject listToUser = new JSONObject();
		Data p = getpivot(inputList);

		JSONArray pivotArray = new JSONArray();

		JSONObject pivot = new JSONObject();
		// Add other properties of pivot
		pivot.append("pivotUri", p.getUri());

		pivotArray.put(pivot);

		listToUser.append("pivot", pivot);
		JSONObject element;
		JSONArray elementArray = new JSONArray();
		for (Data ele : inputList) {
			element = new JSONObject();
			element.append("EleUri", ele.getUri());
			// add other parameters element

			elementArray.put(ele);
		}

		listToUser.append("list", elementArray);
		return listToUser.toString();

	}

}
