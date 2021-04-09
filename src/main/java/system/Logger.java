package main.java.system;

import main.java.system.event.Event;
import main.java.users.Rider;

import java.time.LocalDateTime;

/**
 * A class containing the logic to print transit system messages to the console
 */
public class Logger {
    /**
     * Writes a formatted output to the console with the given timestamp. Writes the
     * message with format [scope] timestamp: msg
     * 
     * @param scope     the string representing the type of message being logged
     * @param msg       the string to be logged
     * @param timestamp the datetime which the message will be tagged with
     */
    private static void output(String scope, String msg, LocalDateTime timestamp) {
        String prefix;
        if (timestamp == null) {
            prefix = String.format("[%s] ", scope);
        } else {
            prefix = String.format("[%s] %s: ", scope, timestamp.toString());
        }

        System.out.println(prefix + msg);
    }

    /**
     * Writes a formatted log message to the console with the given scope and
     * message. Writes the message with format [scope] msg
     * 
     * @param scope the scope to tag the message with
     * @param msg   the message itself
     */
    private static void output(String scope, String msg) {
        output(scope, msg, null);
    }

    /**
     * Writes an the given error message to the console formatted with the ERROR tag
     * 
     * @param err_msg text saying what the error is
     * @param remedy  an explanation on how one might fix this error
     */
    public static void error(String err_msg, String remedy) {
        output("ERROR", err_msg + " " + remedy);
    }

    /**
     * Writes the given tap event to the console. Unpacks the information from the
     * given event and prints in a human readable format with the EVENT tag
     * 
     * @param evt the event object to be printed
     */
    public static void event(Event evt) {
        String cardholder = evt.getCard().getBearer().getName();
        String action = evt.getDirection() + "ed";
        String loc = evt.getLocation().getName();
        String line = evt.getLocation().getLine().getName();

        String msg = String.format("%s %s %s on %s", cardholder, action, loc, line);

        output("EVENT", msg, evt.getTimeStamp());
    }

    /**
     * Logs each trip in an array of trips with the TRIP tag
     * 
     * @param trips the array of trips to display
     */
    public static void trips(Trip[] trips) {
        for (Trip t : trips) {
            if (t != null) {
                output("TRIP", t.toString());
            }
        }
    }

    /**
     * Logs the given message and tags it as for the given user. This approximates a
     * user getting information displayed at a kiosk in a station
     * 
     * @param user the user who the message is targeted at
     * @param msg  the message to display to the user
     */
    public static void user(Rider user, String msg) {
        output(user.getName(), msg);
    }

    /**
     * Logs the given message to the system admin. Logs with ADMIN tag
     *
     * @param msg the message to display
     */
    public static void admin(String msg) {
        output("ADMIN", msg);
    }
}
