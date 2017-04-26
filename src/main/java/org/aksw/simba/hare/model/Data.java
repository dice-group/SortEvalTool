package org.aksw.simba.hare.model;

import java.util.ArrayList;

public class Data {

	ArrayList<String> uri;
	String category;

	public void setUri(ArrayList<String> uri) {
		this.uri = uri;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Data(String category, ArrayList<String> uriList) {
		// TODO Auto-generated constructor stub
		this.category = category;
		this.uri = uriList;
	}

	public ArrayList<String> getUri() {
		return uri;
	}

}
