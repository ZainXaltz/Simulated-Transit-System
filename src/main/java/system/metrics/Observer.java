package main.java.system.metrics;

/**
 * This class is the observer that gets updated once any changes have been made
 * to the Transit System that is being Observed.
 */
public interface Observer {

	/**
	 * Updates the Observer of the changes that happened in the Transit System.
	 * 
	 * @param o           Object that is being observed by the observer.
	 * @param date        Date that the event happened.
	 * @param fareCharged the float fare that was inputed into the system once the
	 *                    tap occurred.
	 */
	public void update(Observable o, String date, float fareCharged);

	/**
	 * Updates the Observer of the changes that happened in the Transit System.
	 * 
	 * @param o    Object that is being observed by the observer.
	 * @param date Date that the event happened.
	 * @param n    the number of stops traveled to
	 */
	public void update(Observable o, String date, int n);
}