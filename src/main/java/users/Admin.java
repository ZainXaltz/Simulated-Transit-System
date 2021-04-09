package main.java.users;

import main.java.system.metrics.ObserverAdmin;
import main.java.system.TransitSystem;

import java.time.LocalDateTime;

/**
 * This is the Admin class that represents the Administrators of the Transit
 * System and all the features that they have access to.
 */
public class Admin {
	public ObserverAdmin o;
	public TransitSystem ts;

	/**
	 * Constructs a new Admin given a TransitSystem.
	 * 
	 * @param ts TransitSystem which is transportation System.
	 */
	public Admin(TransitSystem ts) {
		this.o = new ObserverAdmin();
		this.ts = ts;
		ts.attach(o);
	}

	/**
	 * Returns the total fare collected of the day.
	 * 
	 * @return total fare collected this day.
	 */
	public float getTodayFare() {
		LocalDateTime today = LocalDateTime.now();

		int month = today.getMonth().getValue();
		int day = today.getDayOfMonth();
		int year = today.getYear();
		String todayDate = Integer.toString(month) + "/" + Integer.toString(day) + "/" + Integer.toString(year);

		return o.todayCharge(todayDate);
	}

	/**
	 * Returns the total fare collected by the TransitSystem for any given date by
	 * the Admin consisting of a day, month and year.
	 * 
	 * @param day   day number of the month (1-31)
	 * @param month month number of the year (1-12)
	 * @param year  year in the format YYYY.
	 * @return the total fare collected for a specific date.
	 */
	public float getFareOnDate(int day, int month, int year) {

		String dateStr = month + "/" + day + "/" + year;

		return o.todayCharge(dateStr);

	}

	/**
	 * Returns the total number of stops that were traveled today.
	 * 
	 * @return The total number of stops traveled today.
	 */
	public float getTodayStops() {
		LocalDateTime today = LocalDateTime.now();

		int month = today.getMonth().getValue();
		int day = today.getDayOfMonth();
		int year = today.getYear();
		String todayDate = Integer.toString(month) + "/" + Integer.toString(day) + "/" + Integer.toString(year);

		return o.todayStops(todayDate);
	}

	/**
	 * Returns the total number of stops traveled by riders on any entered day by
	 * the Admin.
	 * 
	 * @param day   number of day in the month (1-31)
	 * @param month number of month in the year (1-12)
	 * @param year  Year in format YYYY.
	 * @return the total number of stops traveled in a day.
	 */
	public int getStopsOnDate(int day, int month, int year) {

		String dateStr = month + "/" + day + "/" + year;

		return o.todayStops(dateStr);
	}
}