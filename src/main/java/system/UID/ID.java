package main.java.system.UID;

/**
 * This is the ID class responsible for representing objects
 */
public class ID {

    private static final char CARD = 'C';
    private static final char RIDER = 'R';
    private static int globalCardCount = 0;
    private static int globalRiderCount = 0;

    private char type; // Type of Object this ID represents
    private String UID; // Unique ID

    /**
     * Creates a new ID based on the type of the object given
     * 
     * @param Type is the type of the object
     */
    public ID(char Type) {

        this.type = Type;

        if (Type == CARD) {
            this.UID = "1" + String.format("%08d", globalCardCount);
            globalCardCount++;
        } else if (Type == RIDER) {
            this.UID = "2" + String.format("%08d", globalRiderCount);
            globalRiderCount++;
        } else {
            System.out.println("Input Invalid");
        }
    }

    /**
     * Creates an ID if input given is a string, Assumes the correct format, inputs
     * the type and UID directly
     * 
     * @param id is the id string of the object
     */
    public ID(String id) {
        this.type = id.charAt(0);
        this.UID = id;
    }

    /**
     * Adds to the global Count
     */
    public static void addToCount(char Type) {
        if (Type == CARD) {
            globalCardCount++;
        } else if (Type == RIDER) {
            globalRiderCount++;
        }
    }

    /**
     * Gets the string representation of the ID
     * 
     * @return String of the ID
     */
    public String toString() {
        return this.UID;
    }

    /**
     * Checks equality of this object in comparison to another
     * 
     * @param otherObject is the other object in question
     * @return a boolean whether it is equal or not
     */
    public boolean equals(Object otherObject) {
        if (!(otherObject instanceof ID)) {
            return false;
        }
        return (this.UID.equals(otherObject.toString()));
    }

    /**
     * Gets the type of the ID
     * 
     * @return char of the type
     */
    public char getType() {
        return this.type;
    }

    /**
     * gets count of this particular type
     * 
     * @return int count of this type
     */
    public static int getCount(char type) {
        if (type == CARD) {
            return globalCardCount;
        } else {
            return globalRiderCount;
        }
    }

}
