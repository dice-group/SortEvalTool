package org.aksw.simba.hare.input;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.aksw.simba.hare.model.Data;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonReader {

	public ArrayList<Data> readFile(String fileName) throws ParseException {
		ArrayList<Data> elementList = new ArrayList<Data>();
		JSONParser parser = new JSONParser();
		ArrayList<String> uriList;
		try {

			Object obj = parser.parse(new FileReader(fileName));

			JSONObject jsonObject = (JSONObject) obj;

			Iterator<?> keys = jsonObject.keySet().iterator();

			while (keys.hasNext()) {

				uriList = new ArrayList<String>();

				String category = (String) keys.next();

				JSONArray jsonArray = (JSONArray) jsonObject.get(category);

				for (Object object : jsonArray) {
					uriList.add(object.toString());
				}

				elementList.add(new Data(category, uriList));
			}

		} catch (IOException e) {

		}
		return elementList;

	}
}
