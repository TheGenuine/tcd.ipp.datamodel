package de.reneruck.tcd.datamodel;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Bean to hold the data transported by the Helicopter
 * 
 * @author Rene
 *
 */
public class TransportContainer {

	private List<Transition> transitions = new LinkedList<Transition>();
	
	public TransportContainer() {
		loadInitalTransitions();
	}
	
	private void loadInitalTransitions() {
		this.transitions.add(new NewBookingTransition(new Booking("Hans Wurst", new Date(1349456400000L), Airport.city)));
		this.transitions.add(new NewBookingTransition(new Booking("Hans Wurst", new Date(1349611200000L), Airport.camp)));
		this.transitions.add(new NewBookingTransition(new Booking("Peter Weber", new Date(1349456400000L), Airport.camp)));
		this.transitions.add(new NewBookingTransition(new Booking("Peter Weber", new Date(1349640000000L), Airport.city)));
	}

	public List<Transition> getTransitions() {
		return this.transitions;
	}

}
