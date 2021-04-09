package main.java.system.event;

import main.java.entities.Card;
import main.java.entities.transit.TransitStop;

import java.time.LocalDateTime;

/**
 * An object representing an event where a user taps their transit card
 */
public class Event {
    private TransitStop location;
    private LocalDateTime timeStamp;
    private String direction;
    private Card card;

    /**
     * Create a tap event where the location, time, and card creating the action are
     * known
     * 
     * @param location  of this event
     * @param timeStamp of when the event occurred
     * @param card      the card which was tapped
     */
    public Event(TransitStop location, LocalDateTime timeStamp, Card card) {
        this.location = location;
        this.timeStamp = timeStamp;
        this.card = card;
    }

    /**
     * Create a new tap event with where the location, time, card creating the
     * action, and direction are known
     * 
     * @param location  at which the user tapped their card
     * @param timeStamp the date and time that the tap occurred
     * @param card      the card which was tapped
     * @param direction whether the user is entering or exiting transit
     */
    public Event(TransitStop location, LocalDateTime timeStamp, Card card, String direction) {
        this(location, timeStamp, card);
        this.setDirection(direction);
    }

    /**
     * Return the location of this event
     * 
     * @return the Stop at which the event happened
     */
    public TransitStop getLocation() {
        return this.location;
    }

    /**
     * Return the timestamp of this event
     * 
     * @return the date and time at which the event occurred
     */
    public LocalDateTime getTimeStamp() {
        return this.timeStamp;
    }

    /**
     * Return the card that created this event
     * 
     * @return this event's card
     */
    public Card getCard() {
        return this.card;
    }

    /**
     * Return whether the cardholder is entering or exiting transit
     * 
     * @return the direction the user is going in the system. Either "enter",
     *         "exit", or null if the direction is unknown
     */
    public String getDirection() {
        return this.direction;
    }

    /**
     * Return whether or not the direction of this event is enter
     * 
     * @return true iff the direction of this trip is "enter", false otherwise
     */
    public boolean isEntering() {
        return this.direction.equals("enter");
    }

    /**
     * Sets the direction of this event to the given direction. Does nothing if the
     * given string not a valid direction
     *
     * @param direction of the event. either "enter" or "exit"
     */
    public void setDirection(String direction) {
        if (direction.equals("enter") || direction.equals("exit")) {
            this.direction = direction;
        }
    }
}
