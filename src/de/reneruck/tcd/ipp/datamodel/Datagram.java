package de.reneruck.tcd.ipp.datamodel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

/**
 * A {@link Datagram} is a container class to transport transitions and control
 * messages in the communication of between the helicopter and the server in the
 * city or the client int the camp.<br>
 * A {@link Datagram} serializes into a gson string.
 * 
 * @author Rene
 * 
 */
public class Datagram implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TRANSITION = "transition";
	
	private String type;
	private Map<String, Object> payload = new HashMap<String, Object>();

	public Datagram(String type, Map<String, Object> content) {
		this.type = type;
		this.payload = content;
	}

	public Datagram(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	public void addPayloadEntry(String identfier, Object content) {
		this.payload.put(identfier, content);
	}
	
	public Object getPayload(String identifier) {
		return this.payload.get(identifier);
	}
	
	public Set<String> getKeys() {
		return this.payload.keySet();
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
