package main.java.system.UID;

import java.util.ArrayList;

/**
 * This class is responsible for generating ID's for objects in the application.
 * It accounts for all current ID's in the system as well as their types;
 */
public class IDGenerator {

    private static ArrayList<ID> IDList = new ArrayList<>();

    /**
     * Creates a new ID object based on the object type;
     * 
     * @param Type is the object type for this particular ID
     * @return ID
     */
    public static ID generateID(char Type) {
        ID id = new ID(Type);
        IDList.add(id);
        return id;
    }

    /**
     * Creates a new ID object based on the String; if the String for this
     * particular ID already exists, create new one based on the type instead.
     * 
     * @param text is the string ID that will be the ID
     * @return ID
     */
    public static ID fromString(String text) {
        char type = text.charAt(0);
        ID id;
        String temp = text;

        while (findIDString(temp) != null) { // If ID exists in array then create ID based off globalCount

            String strID;
            int intID = Integer.parseInt(temp);

            if (type == 'C') {
                strID = "1" + String.format("%08d", ID.getCount(type));
            } else {
                strID = "2" + String.format("%08d", ID.getCount(type));
            }

            if (temp.equals(strID)) {
                ID.addToCount(type);
            }

            intID++;
            temp = Integer.toString(intID);

        }

        id = new ID(temp); // If ID does not exist in array then create an ID based off String
        IDList.add(id);
        return id;
    }

    /**
     * Checks whether or not any ID in the system carries the same String
     * 
     * @param text is the string ID that we test for equality
     * @return true or false depending on whether the string exists in the IDs or
     *         not
     */
    public static ID findIDString(String text) {
        for (ID id : IDList) {
            if (id.toString().equals(text)) {
                return id;
            }
        }
        return null;
    }
}
