package main.java.entities.transit;

import javafx.scene.paint.Paint;

/**
 * An Object representing a line in the transit system. Can be either a Bus or
 * Subway line
 */
public abstract class TransitLine {
    private String name;
    private Paint color;

    /**
     * Create a new TransitLine called `name`
     * 
     * @param name of this line
     */
    public TransitLine(String name) {
        this.name = name;
    }

    /**
     * Set the color of this line on the map to the given paint color
     * 
     * @param color what color to color this line
     */
    public void setColor(Paint color) {
        this.color = color;
    }

    /**
     * Returns the Paint color of this line
     * 
     * @return this line's color
     */
    public Paint getColor() {
        return this.color;
    }

    /**
     * Return the name of this line
     * 
     * @return this line's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Return all the stops along this line
     * 
     * @return an array of the stops on this line
     */
    public abstract TransitStop[] getStops();

    /**
     * Gets a stop along this line by its name
     * 
     * @param targetName name of the stop on this line
     * @return the stop object corresponding to the name. null if no such stop
     *         exists
     */
    public abstract TransitStop findStopOnLine(String targetName);

    /**
     * Returns the number of stops reached in a ride including the stop exited at,
     * and excluding the stop entered at.
     *
     * @param start name of the starting location on this line
     * @param end   the name of the location of drop off on this line
     *
     * @return the number of stops traveled in the ride
     */
    public int getDistTraveled(TransitStop start, TransitStop end) {
        if (start.getLine() != this || end.getLine() != this) {
            return 0;
        }
        int count = 0;
        boolean foundStart = false;
        int i = 0;

        TransitStop[] stops = this.getStops();
        while (stops[i] != end) {
            if (stops[i++] == start) {
                foundStart = true;
            }
            if (foundStart) {
                count++;
            }
        }
        return count;
    }
}
