package de.reneruck.tcd.ipp.datamodel.transition;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import de.reneruck.tcd.ipp.fsm.FiniteStateMachine;

public class TransitionExchangeBean {

	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Socket connection;
	private FiniteStateMachine fsm;
	
	public ObjectOutputStream getOut() {
		return out;
	}
	public void setOut(ObjectOutputStream out) {
		this.out = out;
	}
	public ObjectInputStream getIn() {
		return in;
	}
	public void setIn(ObjectInputStream in) {
		this.in = in;
	}
	public Socket getConnection() {
		return this.connection;
	}
	public void setConnection(Socket connection) {
		this.connection = connection;
	}
	public FiniteStateMachine getFsm() {
		return fsm;
	}
	public void setFsm(FiniteStateMachine fsm) {
		this.fsm = fsm;
	}
}
