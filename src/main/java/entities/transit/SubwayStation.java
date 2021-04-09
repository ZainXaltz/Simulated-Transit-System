package main.java.entities.transit;

/**
 * An Object representing a station on a subway line
 */
public class SubwayStation extends TransitStop {
    /**
     * Create a new Subway station
     * 
     * @param name of this stop
     * @param line this stop belongs to
     */
    public SubwayStation(String name, SubwayLine line) {
        super(name, line);
    }
}
