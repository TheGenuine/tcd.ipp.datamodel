package de.reneruck.tcd.ipp.datamodel.transition;

import java.net.ConnectException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import de.reneruck.tcd.ipp.datamodel.database.DatabaseConnection;
import de.reneruck.tcd.ipp.datamodel.database.SqliteDatabaseConnection;
import de.reneruck.tcd.ipp.datamodel.exceptions.DatabaseException;

/**
 * The {@link TemporalTransitionsStore} constitutes an intelligent storage to
 * temporary store transitions.
 * 
 * @author Rene
 * 
 */
public class TemporalTransitionsStore implements Iterable<Transition>{

	private Set<Transition> transitions = new LinkedHashSet<Transition>();
	private Queue<Transition> transitionsToPersist = new LinkedBlockingQueue<Transition>();
	private Queue<Transition> transitionsToDepersist = new LinkedBlockingQueue<Transition>();
	private ListPersitence listPersitence;
	
	
	public TemporalTransitionsStore(String databseFile) throws ConnectException, DatabaseException {
		this.listPersitence = new ListPersitence(new SqliteDatabaseConnection(databseFile), this.transitionsToPersist, this.transitionsToDepersist);
		this.listPersitence.setRunning(true);
		this.listPersitence.start();
	}
	
	public Transition getTransitionById(long transitionId) {
		for (Transition transition : this.transitions) {
			if(transition.getTransitionId() == transitionId)
			{
				return transition;
			}
		}
		return null;
	}
	
	public Set<Transition> getAllTransitionsByState(TransitionState state)
	{
		Set<Transition> result = new LinkedHashSet<Transition>();
		for (Transition transition : this.transitions) {
			if(state.equals(transition.getTransitionState()))
			{
				result.add(transition);
			}
		}
		return result;
	}
	
	public void addTransition(Transition transition) {
		this.transitions.add(transition);
	//	this.transitionsToPersist.add(transition);
	}
	
	public void removeTransition(Transition transition) {
		this.transitions.remove(transition);
	//	this.transitionsToDepersist.add(transition);
	}
	
	public void removeTransitionById(long transitionId) {
		Transition transitionById = getTransitionById(transitionId);
		if(transitionById != null) {
			removeTransition(transitionById);
		}
	}

	public boolean containsTransition(Transition transition) {
		return this.transitions.contains(transition);
	}
	
	public boolean isEmpty() {
		return this.transitions.isEmpty();
	}

	@Override
	public Iterator<Transition> iterator() {
		return this.transitions.iterator();
	}
	
	class ListPersitence extends Thread {

		private DatabaseConnection dbConnection;
		private Queue<Transition> transitionsToDepersist;
		private Queue<Transition> transitionsToPersist;
		private boolean running;
		
		public ListPersitence(SqliteDatabaseConnection sqliteDatabaseConnection, Queue<Transition> transitionsToPersist, Queue<Transition> transitionsToDepersist) {
			this.dbConnection = sqliteDatabaseConnection;
			this.transitionsToPersist = transitionsToPersist;
			this.transitionsToDepersist = transitionsToDepersist;
		}

		@Override
		public void run() {
			while(this.running) {
				if(!this.transitionsToPersist.isEmpty()) {
					try {
						this.dbConnection.persistTransitionStoreEntry(this.transitionsToPersist.poll());
					} catch (ConnectException e) {
						System.err.println("Cannot persist TransitionStore Entry " + e.getMessage());
					}
				}
				if(!this.transitionsToDepersist.isEmpty()) {
					try {
						this.dbConnection.dePersistTransitionStoreEntry(this.transitionsToDepersist.poll());
					} catch (ConnectException e) {
						System.err.println("Cannot depersist TransitionStore Entry " + e.getMessage());
					}
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		public boolean isRunning() {
			return running;
		}

		public void setRunning(boolean running) {
			this.running = running;
		}
		
	}
}
