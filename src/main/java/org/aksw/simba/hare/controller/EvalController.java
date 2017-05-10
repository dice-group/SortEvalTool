package org.aksw.simba.hare.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.aksw.simba.hare.input.JsonReader;
import org.aksw.simba.hare.model.Data;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EvalController {

	ArrayList<Data> entityList = new ArrayList<Data>();

	// temp solution to Database
	HashMap<String, Integer> userLog = new HashMap<String, Integer>();

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

	public int getUserCategory(String userName) {
		int userCategory;
		if (userLog.containsKey(userName) == false) {
			userLog.put(userName, 0);
		}
		userCategory = userLog.get(userName);
		return userCategory;
	}

	public Data getNextCategory(String userName) {
		Data nextCategory = entityList.get(this.getUserCategory(userName));
		userLog.put(userName, userLog.get(userName) + 1);
		return nextCategory;
	}

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

		fileWriter.write(userInput);
		fileWriter.flush();
		fileWriter.close();

	}

	// the function to interact with user
	@RequestMapping(value = "/next", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Data> getEntity(
			@RequestParam(value = "username") String userName)
			throws ParseException {

		Data entityForComparison = getNextCategory(userName);
		if (entityForComparison == null) {
			return new ResponseEntity<Data>(entityForComparison,
					HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Data>(entityForComparison, HttpStatus.OK);
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public String saveResult(@RequestParam(value = "username") String userName) {
		try {
			this.writeToFile(this.toString(), userName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:/next?username=" + userName;
	}
}
