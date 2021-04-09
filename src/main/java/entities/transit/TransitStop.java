package main.java.entities.transit;

/**
 * An Object representing a stop along a transit line, can be on either a bus or
 * subway line
 */
public class TransitStop {
    private String name;
    private TransitLine line;
    private int x, y;

    /**
     * Create a new stop on a transit line with the given name
     * 
     * @param name of the stop
     * @param line this stop belongs to
     */
    public TransitStop(String name, TransitLine line) {
        this.name = name;
        this.line = line;
    }

    /**
     * Get the x-coordinate of this stop on the map
     * 
     * @return the x coordinate of this stop
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x-coordinate of this stop to the given value
     * 
     * @param x the new x-coordinate of this stop
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Get the y-coordinate of this stop on the map
     * 
     * @return the y coordinate of this stop
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y-coordinate of this stop to the given value
     * 
     * @param y the new y-coordinate of this stop
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Return the name of this stop
     * 
     * @return this stop's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Return the line to which this stop belongs
     * 
     * @return this stop's line
     */
    public TransitLine getLine() {
        return this.line;
    }

    /**
     * Return whether or not a rider can transfer to the candidate stop from this
     * stop
     *
     * @param candidate the stop which will be determined to be a transfer or not
     * @return true iff the candidate stop can be transferred to from this stop
     */
    public boolean canTransferTo(TransitStop candidate) {
        return name.equals(candidate.getName()) && line != candidate.getLine();
    }
}
