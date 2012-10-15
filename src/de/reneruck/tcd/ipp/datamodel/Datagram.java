package de.reneruck.tcd.ipp.datamodel;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

public class Datagram {

	public static final String TRANSITION = "transition";
	
	private String type;
	private Map<String, Object> content = new HashMap<String, Object>();

	public Datagram(String type, Map<String, Object> content) {
		this.type = type;
		this.content = content;
	}

	public Datagram(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	public void addContentEntry(String identfier, Object content) {
		this.content.put(identfier, content);
	}
	
	public Object getContent(String identifier) {
		return this.content.get(identifier);
	}
	
	public Set<String> getKeys() {
		return this.content.keySet();
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
