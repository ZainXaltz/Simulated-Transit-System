package main.java.system.metrics;

import java.util.HashMap;

/**
 * Class implementing the Observer. It is where the fare amount is getting
 * updated in the System for the Administrator to get access to each day's total
 * fare collected.
 */
public class ObserverAdmin implements Observer {
	private HashMap<String, Float> fareDayMap = new HashMap<String, Float>();
	private HashMap<String, Integer> stopsDayMap = new HashMap<String, Integer>();

	/**
	 * Updates the observer with each day's date and collected fare amount. The fare
	 * amount received every day from the taps is added to the hashmap as a value to
	 * the date key in the hashmap.
	 */
	@Override
	public void update(Observable o, String date, float fareCharged) {
		if (fareDayMap.containsKey(date)) {
			fareDayMap.put(date, fareDayMap.get(date) + fareCharged);
		}
		// Adding new [key:value] to the map if there were no other taps that same day
		else {
			fareDayMap.put(date, fareCharged);
		}
	}

	/**
	 * Updates the observer with each day's date. One is added to the hashmap as a
	 * value to the date key in the hashmap for every stop that is travelled to on
	 * the respective date.
	 */
	@Override
	public void update(Observable o, String date, int n) {
		if (stopsDayMap.containsKey(date)) {
			stopsDayMap.put(date, stopsDayMap.get(date) + n);
		}
		// Adding new [key:value] to the map if there were no other taps that same day
		else {
			stopsDayMap.put(date, n);
		}
	}

	/**
	 * Returns the float value representing the fare collected on a specific date.
	 * 
	 * @param date String representing a date.
	 * @return the Fare amount collected for a specific given day.
	 */
	public float todayCharge(String date) {
		if (fareDayMap.containsKey(date)) {
			return fareDayMap.get(date);
		}
		return 0;
	}

	/**
	 * Returns the int value representing the number of stops traveled on a specific
	 * date.
	 * 
	 * @param date String representing a date.
	 * @return the stops traveled on a specific day.
	 */
	public int todayStops(String date) {
		if (stopsDayMap.containsKey(date)) {
			return stopsDayMap.get(date);
		}
		return 0;
	}
}