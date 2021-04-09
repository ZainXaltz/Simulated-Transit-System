package main.java.entities.transit;

/**
 * An Object representing a stop on a bus line
 */
public class BusStop extends TransitStop {
    /**
     * Create a new BusStop along a given line called name
     * 
     * @param name of this stop
     * @param line this stop belongs to
     */
    public BusStop(String name, BusLine line) {
        super(name, line);
    }
}
