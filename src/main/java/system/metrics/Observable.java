package main.java.system.metrics;

import java.util.ArrayList;

/**
 * This class is responsible for holding the list of all the Observers. And
 * notifying the Observers Of any updates or changes in the transitSystem.
 */
public class Observable {
	private ArrayList<Observer> observers = new ArrayList<Observer>();

	/**
	 * Adds an observer to the list of Observers.
	 * 
	 * @param o an Observer
	 */
	public void attach(Observer o) {
		this.observers.add(o);
	}

	/**
	 * Removes an Observer from the list of Observers.
	 * 
	 * @param o an Observer
	 */
	public void detach(Observer o) {
		this.observers.remove(o);
	}

	/**
	 * Notifies all the Observers that the tap event happened in the TransitSystem.
	 * 
	 * @param date        the date that the tap event happened.
	 * @param fareCharged the fare that rider was charged when card was tapped.
	 */
	public void notifyObservers(String date, float fareCharged) {
		for (Observer o : this.observers) {
			o.update(this, date, fareCharged);
		}
	}

	/**
	 * Notifies all the Observers that an event happened in the TransitSystem.
	 * 
	 * @param date the date that the tap event happened.
	 * @param n    the number of stops between two tap events
	 */
	public void notifyObservers(String date, int n) {
		for (Observer o : this.observers) {
			o.update(this, date, n);
		}
	}
}