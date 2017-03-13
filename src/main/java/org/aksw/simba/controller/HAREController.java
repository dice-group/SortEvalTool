package org.aksw.simba.controller;

import java.util.ArrayList;
import java.util.List;

import org.aksw.simba.HareEval.Data;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;

@Controller
public class HAREController {

	@Autowired
	private ArrayList<Data> dataset = new ArrayList<Data>();
	Data pivot;
	private final static org.slf4j.Logger logger = LoggerFactory
			.getLogger(HAREController.class);

	public HAREController() {

	}

	public Data getpivot() {
		int middle = (int) Math.ceil((double) dataset.size() / 2);
		return dataset.get(middle);
	}

	@RequestMapping(value = "/next", produces = "application/json;charset=utf-8")
	public ResponseEntity<String> nextPair(
			@RequestParam(value = "username") String userName) {
		Data p = getpivot();
		Gson gson = new Gson();
		String user = gson.toJson(dataset);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json;charset=utf-8");
		return new ResponseEntity<String>(user, responseHeaders, HttpStatus.OK);

	}

	private List<Data> quicksort(List<Data> input) {

		if (input.size() <= 1) {
			return input;
		}

		Data pivot = getpivot();

		List<Integer> less = new ArrayList<Integer>();
		List<Integer> greater = new ArrayList<Integer>();

		for (int i = 0; i < input.size(); i++) {
			if (input.get(i) <= pivot) {
				if (i == middle) {
					continue;
				}
				less.add(input.get(i));
			} else {
				greater.add(input.get(i));
			}
		}

		return concatenate(quicksort(less), pivot, quicksort(greater));
	}

	private List<Data> concatenate(List<Data> less, Data pivot,
			List<Data> greater) {

		List<Data> list = new ArrayList<Data>();

		for (int i = 0; i < less.size(); i++) {
			list.add(less.get(i));
		}

		list.add(pivot);

		for (int i = 0; i < greater.size(); i++) {
			list.add(greater.get(i));
		}

		return list;
	}
}
