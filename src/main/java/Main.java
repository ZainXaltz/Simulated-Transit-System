package main.java;

import javafx.application.Application;
import javafx.stage.Stage;
import main.java.entities.transit.BusLine;
import main.java.entities.transit.SubwayLine;
import main.java.system.Logger;
import main.java.system.Parser;
import main.java.system.TransitSystem;
import main.java.ui.client.TransitMap;
import main.java.users.Admin;
import main.java.users.Rider;

import java.io.IOException;

/**
 * The entry point into the transit system
 */
public class Main extends Application {
    /**
     * Create a transit system from the data in the input files
     * 
     * @return a transit system object with the riders and transit lines which have
     *         been read from the input files
     * @throws IOException if there is an error parsing data from the input files
     */
    private static TransitSystem loadSystem(Parser parser) throws IOException {
        Rider[] riders = parser.riderParser();

        BusLine[] busLines = parser.busParser();

        SubwayLine subwayLine = parser.subwayParser();

        TransitSystem system = new TransitSystem(riders, busLines, subwayLine);

        parser.cardParser(system);

        return system;
    }

    /**
     * Launches the application and displays the map of the transit network.
     * 
     * @throws IOException if there is a problem parsing the input files
     */
    @Override
    public void start(Stage stage) throws IOException {
        Parser parser = new Parser();

        // read in data to make system
        TransitSystem system = loadSystem(parser);
        // draw map of network
        TransitMap map = new TransitMap(system, 800, 40);

        Admin admin = new Admin(system);

        // parse events from Events.txt
        parser.eventParser(system);

        stage.setTitle("Transit Map");
        stage.setScene(map.getTransitMap());
        stage.show();

        // Any subsequent actions the system may take should be included here
        Rider testSubject = system.getCardholders()[0];
        Logger.trips(testSubject.recentTrips());
        testSubject.reloadCard(15, testSubject.getCards().get(0));
        testSubject.reloadCard(20, testSubject.getCards().get(0));
        testSubject.changeName("!@#");
        testSubject.changeName("");
        testSubject.changeName("  ");
        testSubject.changeName("Bob Vance");
        // notice the name in the log has changed
        Logger.user(testSubject, String.format("Avg. Cost per Month: $%.2f", testSubject.averageMonthlyCost()));

        // Admin stats
        Logger.admin("Total fare today: $" + admin.getFareOnDate(11, 11, 2020));
        Logger.admin("Total stops today: " + admin.getStopsOnDate(11, 11, 2020));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
