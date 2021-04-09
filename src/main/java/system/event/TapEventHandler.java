package main.java.system.event;

import main.java.entities.Card;
import main.java.system.Trip;

import java.util.ArrayList;

/**
 * Abstract parent for TapEventHandlers. Child classes of this will be
 * responsible for taking in a tap event and applying system logic to charge the
 * rider, update trips, and keep metrics
 */
public abstract class TapEventHandler {
    Event tap;
    Card card;
    ArrayList<Trip> tripsOnCard;
    int numTrips;

    /**
     * Construct a new TapEventHandler to process the given tap event
     * 
     * @param tap the event to process
     */
    public TapEventHandler(Event tap) {
        this.tap = tap;
        this.card = tap.getCard();
        this.tripsOnCard = card.getTrips();
        this.numTrips = tripsOnCard.size();
    }

    /**
     * The entry point into a TapEventHandler. This processes the event and returns
     * the associated trip object.
     * 
     * @return the trip generated by handling this event
     */
    public abstract Trip handleTap();
}
