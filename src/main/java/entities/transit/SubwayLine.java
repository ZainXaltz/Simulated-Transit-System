package main.java.entities.transit;

/**
 * An object which is a subway TransitLine
 */
public class SubwayLine extends TransitLine {
    private SubwayStation[] stations;

    /**
     * Create a new SubwayLine with a given name
     * 
     * @param name of this line
     */
    public SubwayLine(String name) {
        super(name);
    }

    /**
     * Sets the stations along this route to the given array of subway stations. The
     * order of the stations along the route is the order of the elements in the
     * array
     * 
     * @param stations the ordered array of stations this line runs through
     */
    public void setRoute(SubwayStation[] stations) {
        this.stations = stations;
    }

    /**
     * Return all the SubwayStation
     */
    public SubwayStation[] getStops() {
        return this.stations;
    }

    /**
     * Return the SubwayStation on this line corresponding to the given station name
     * 
     * @param targetName name of the stop on this line
     * @return the SubwayStation on this line with name targetName. null f no such
     *         station exists
     */
    public SubwayStation findStopOnLine(String targetName) {
        for (SubwayStation station : this.stations) {
            if (station.getName().equals(targetName)) {
                return station;
            }
        }
        return null;
    }
}