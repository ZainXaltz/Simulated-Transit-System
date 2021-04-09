package main.java.entities.transit;

/**
 * An Object representing a bus route with stops
 */
public class BusLine extends TransitLine {
    private BusStop[] stops;

    /**
     * Create a new BusLine called name
     * 
     * @param name of this bus line
     */
    public BusLine(String name) {
        super(name);
    }

    /**
     * Sets the stops along this route to the given array of bus stops. The order of
     * the stops along the route is the order of the elements in the array
     * 
     * @param stops the ordered array of stops this bus line runs through
     */
    public void setRoute(BusStop[] stops) {
        this.stops = stops;
    }

    /**
     * Return an array of all the stops along this line
     */
    public BusStop[] getStops() {
        return this.stops;
    }

    /**
     * Get a bus stop along this line with the given name
     */
    public BusStop findStopOnLine(String targetName) {
        for (BusStop stop : this.stops) {
            if (stop.getName().equals(targetName)) {
                return stop;
            }
        }
        return null;
    }
}
