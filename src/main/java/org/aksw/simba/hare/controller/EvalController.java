package org.aksw.simba.hare.controller;

import java.util.Collection;

import org.aksw.simba.hare.input.JsonReader;
import org.aksw.simba.hare.model.Data;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EvalController {

	@RequestMapping(value = "/api/getentitiy", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Data>> getGreetings()
			throws ParseException {
		JsonReader jr = new JsonReader();
		Collection<Data> greetings = jr
				.readFile("/Users/Kunal/Downloads/lists.json");

		return new ResponseEntity<Collection<Data>>(greetings, HttpStatus.OK);
	}
}
