package main.java.entities;

import main.java.system.UID.ID;
import main.java.system.UID.IDGenerator;
import main.java.system.event.Event;
import main.java.system.Trip;
import main.java.users.Rider;

import java.util.ArrayList;

/**
 * An object representing a transit card, used by a rider to tap in and out of
 * the transit system
 */
public class Card {
    private float balance;
    private final ID id;
    private boolean active;
    private final Rider bearer;
    private ArrayList<Trip> trips;

    /**
     * Create a new Card belonging to a given user with the given ID
     * 
     * @param issuedTo this User
     */
    public Card(Rider issuedTo) {
        this.active = true;
        this.balance = 19;
        this.bearer = issuedTo;
        this.id = IDGenerator.generateID('C');
        this.trips = new ArrayList<Trip>();
    }

    /**
     * Create a new Card belonging to a given user with the given ID and ID
     *
     * @param issuedTo this User
     */
    public Card(Rider issuedTo, String ID) {
        this.active = true;
        this.balance = 19;
        this.bearer = issuedTo;
        this.id = IDGenerator.fromString(ID);
        this.trips = new ArrayList<Trip>();
    }

    /**
     * Create a new Card belonging to a given user with the given ID, Card ID, and
     * balance
     *
     * @param issuedTo this User
     */
    public Card(Rider issuedTo, String ID, float balance) {
        this.active = true;
        this.balance = balance;
        this.bearer = issuedTo;
        this.id = IDGenerator.fromString(ID);
        this.trips = new ArrayList<Trip>();
    }

    /**
     * Sets the balance of this card
     * 
     * @param balance is the new balance of this card
     */
    public void setBalance(float balance) {
        this.balance = balance;
    }

    /**
     * Return the current balance of this card
     * 
     * @return this card's balance
     */
    public float getBalance() {
        return this.balance;
    }

    /**
     * returns a list of the trips on this card
     *
     * @return ArrayList
     */
    public ArrayList<Trip> getTrips() {
        return this.trips;
    }

    /**
     * Starts a new trip on this card with the given start event and returns that
     * trip
     * 
     * @param initialEvent the tap which started this trip
     * @return the trip which was just started
     */
    public Trip startTrip(Event initialEvent) {
        Trip newTrip = new Trip(initialEvent);
        this.trips.add(newTrip);

        return newTrip;
    }

    /**
     * Removes the most recent event from the given trip. Removes the trip from the
     * card if this operation leaves the trip with no events.
     * 
     * @param trip the trip from which to remove an event
     */
    public void removeLatestEventFromTrip(Trip trip) {
        if (trip.getNumEvents() == 1) {
            trips.remove(trip);
        } else {
            trip.removeEvent(trip.getLatestEvent());
        }
    }

    /**
     * Returns whether or not this card is active and may be charged
     * 
     * @return true iff this card is active, false if it is currently suspended
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * Returns the Rider who owns this card
     * 
     * @return this card's bearer
     */
    public Rider getBearer() {
        return this.bearer;
    }

    /**
     * Returns this card's id number
     * 
     * @return the ID of this card
     */
    public ID getId() {
        return this.id;
    }

    /**
     * Adds value to this card's balance
     * 
     * @param value in dollars to be added to the card
     */
    public void addBalance(float value) {
        this.balance += value;
    }

    /**
     * Suspends the current card, authenticated with the ID of the bearer.
     * Suspension only happens when the correct user id is passed. Returns whether
     * the suspension was successful or not
     * 
     * @param userId of the bearer
     * @return true iff the deactivation was successful (i.e. the given user id
     *         matches that of this card's bearer)
     */
    public boolean suspend(ID userId) {
        if (userId.equals(this.bearer.getId())) {
            this.active = false;
            return true;
        }
        return false;
    }

    /**
     * Sets this card's status to active. Authenticated with the ID of the user who
     * is this card's bearer. Returns whether the activation was suscessful or not
     * 
     * @param userId of this card's bearer
     * @return true iff the activation was successful (i.e. if the given ID matches
     *         that of this card's bearer)
     */
    public boolean reActivate(ID userId) {
        if (userId.equals(this.bearer.getId())) {
            this.active = true;
            return true;
        }
        return false;
    }

    /**
     * Charges the given amount to the card. The card must be active with a positive
     * balance to be charged.
     * 
     * @param amount to be charged
     * @return whether the charge was successful. True iff the card is active with a
     *         positive balance when tapped
     */
    public boolean charge(float amount) {
        if (this.active && this.balance > 0) {
            this.balance -= amount;
            return true;
        }
        return false;
    }
}