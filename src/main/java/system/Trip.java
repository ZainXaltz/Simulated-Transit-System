package main.java.system;

import main.java.system.event.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

/**
 * An Object representing a list of events in a time frame.
 */
public class Trip {
    float MAX = 6;
    private ArrayList<Event> events;
    private float price;

    /**
     * Construct a new Trip starting with the given event.
     *
     * @param initialEvent the event which started this trip
     */
    public Trip(Event initialEvent) {
        events = new ArrayList<Event>();
        events.add(initialEvent);

        this.price = 0;
    }

    /**
     * Adds the given event to the trip. The event is inserted in the correct spot
     * chronologically;
     * 
     * @param e the event to be inserted into this trip
     */
    public void addEvent(Event e) {
        Event curr = events.get(0);

        int i = 1;
        while (curr.getTimeStamp().isBefore(e.getTimeStamp()) && i < getNumEvents()) {
            curr = events.get(i++);
        }
        events.add(i, e);
    }

    /**
     * Removes the given event from this trip
     * 
     * @param e the event to remove
     */
    public void removeEvent(Event e) {
        events.remove(e);
    }

    /**
     * Returns the most recent event in this trip
     *
     * @return the latest event
     */
    public Event getLatestEvent() {
        return this.events.get(events.size() - 1);
    }

    /**
     * Returns the second last event in this trip
     * 
     * @return the second last event. null if there are fewer than two events
     */
    public Event getSecondLastEvent() {
        if (getNumEvents() < 2) {
            return null;
        }
        return this.events.get(getNumEvents() - 2);
    }

    /**
     * Returns when this trip started
     *
     * @return the DateTime of the first event
     */
    public LocalDateTime getStartTime() {
        return events.get(0).getTimeStamp();
    }

    /**
     * Returns when the latest event in this trip occurred
     *
     * @return the DateTime of the latest event
     */
    public LocalDateTime getEndTime() {
        return getLatestEvent().getTimeStamp();
    }

    /**
     * Return the number of events in this trip
     *
     * @return the number of events
     */
    public int getNumEvents() {
        return events.size();
    }

    /**
     * Return the amount spent on this trip so far
     * 
     * @return this trip's current price total
     */
    public float getCurrentPrice() {
        return this.price;
    }

    /**
     * Adds the given amount to this trips total cost, capping the value at the max
     * allowed for a single trip. Returns the difference between the price before
     * adding cost and after.
     * 
     * @param amount the amount of money to add to this trip's cost
     * 
     * @return the amount of value actually added to the price
     */
    public float addCost(float amount) {
        float originalPrice = this.price;

        if (originalPrice + amount > MAX) {
            this.price = MAX;
        } else {
            this.price += amount;
        }

        return this.price - originalPrice;
    }

    /**
     * Returns a human-readable string representation of this trip containing the
     * name of the rider, the date on which it occurred, the duration, the cost, and
     * the route taken.
     * 
     * @return this trip as a string
     */
    @Override
    public String toString() {
        String stops = "Route: ";
        for (Event e : events.subList(0, getNumEvents() - 1)) {
            stops += e.getLocation().getName() + " -> ";
        }
        stops += getLatestEvent().getLocation().getName();

        String cost = String.format("Cost: $%.2f", price);

        String rider = getLatestEvent().getCard().getBearer().getName();

        String date = getStartTime().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));
        String timeData = String.format("On: %s\nDuration: %s min", date,
                getStartTime().until(getEndTime(), ChronoUnit.MINUTES));

        String ret = String.format("Trip Summary for %s\n%s\n%s\n%s", rider, timeData, cost, stops);
        return ret;
    }
}
