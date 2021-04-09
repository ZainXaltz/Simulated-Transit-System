package main.java.system;

import main.java.entities.Card;
import main.java.system.UID.IDGenerator;
import main.java.entities.transit.BusLine;
import main.java.entities.transit.BusStop;
import main.java.entities.transit.SubwayLine;
import main.java.entities.transit.SubwayStation;
import main.java.users.Rider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * A class that handles all File inputs into the system. It is responsible for
 * taking into account for all lines, riders, and events and processing lines
 * into objects in the system.
 */
public class Parser {
    File lines;
    File riders;
    File events;
    File cards;

    /**
     * Construct the parser, attach File variables to the file path
     */
    public Parser() {
        String path = Paths.get(".").toAbsolutePath().toString();

        this.riders = new File(path + "/src/main/java/files/Riders.txt");
        this.cards = new File(path + "/src/main/java/files/Cards.txt");
        this.lines = new File(path + "/src/main/java/files/Lines.txt");
        this.events = new File(path + "/src/main/java/files/Events.txt");
    }

    /**
     * Creates Riders based on lines given in the RidersFile. Lines will be written
     * in the format of : "[RiderName];[RiderEmail];[RiderID]
     * [RiderName];[RiderEmail];[RiderID] ..." where
     * <ul>
     *
     * <li>[RiderName] is the name of the Rider</li>
     *
     * <li>[RiderEmail] is a correctly formatted email for the rider</li>
     *
     * </ul>
     * 
     * @return Rider[] : Array of all Riders
     * @throws IOException when an exception occurs during file IO
     */
    public Rider[] riderParser() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(riders));
        ArrayList<Rider> riderList = new ArrayList<>();
        String st;
        String[] data;
        while ((st = br.readLine()) != null) {
            data = st.split(";");
            Rider rider = new Rider(data[0], data[1], data[2]);
            riderList.add(rider);
        }
        Rider[] riders = new Rider[riderList.size()];
        for (int i = 0; i < riderList.size(); i++) {
            riders[i] = riderList.get(i);
        }

        return riders;
    }

    /**
     * Creates Events based on lines given in the RidersFile. Lines are written in
     * the format of : "[card id] [action] [stop] [line] [time]" (further
     * explanation in the handleTapEvent Doc)
     * 
     * @param transitSystem is the transit system that handles main activities
     * @throws IOException when an exception occurs during file IO
     */
    public void eventParser(TransitSystem transitSystem) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(events));
        String st;
        String[] data = new String[5];
        while ((st = br.readLine()) != null) {
            transitSystem.handleTapEvent(st);
        }
    }

    /**
     * Creates Array of BusLines based on the lines given in the LinesFile. Lines
     * will be written in the format of
     * "[LineType];[LineName];[StopName1];[StopName2];....;[StopNameN]" where
     * <ul>
     * <li>[LineType] is the type of the Line, either bus or subway</li>
     *
     * <li>[LineName] is the name of the Line</li>
     *
     * <li>[StopName(n)] is the name of the Stop</li>
     * </ul>
     *
     * @return Busline[] which is an array of all BusLines
     * @throws IOException when an exception occurs during file IO
     */
    public BusLine[] busParser() throws IOException {

        BufferedReader brLines = new BufferedReader(new FileReader(this.lines));

        int buses = busLineCount();
        BusLine[] busLines = new BusLine[buses];
        int k = 0;
        String st;
        while ((st = brLines.readLine()) != null) {
            String[] data = st.split(";");
            if (data[0].equals("Bus")) {
                busLines[k] = new BusLine(data[1]);
                BusStop[] busStops = new BusStop[data.length - 2];
                for (int i = 0; i < busStops.length; i++) {
                    busStops[i] = new BusStop(data[i + 2], busLines[k]);
                }
                busLines[k].setRoute(busStops);
                k++;
            }
        }
        return (busLines);
    }

    /**
     * Responsible for returning the count of the amount of busLines in the file
     * 
     * @return int number of busLines
     * @throws IOException when an exception occurs during file IO
     */
    private int busLineCount() throws IOException {

        BufferedReader brLines = new BufferedReader(new FileReader(this.lines));

        int count = 0;
        String st;
        while ((st = brLines.readLine()) != null) {
            count++;
        }
        if (count == 0) {
            return 0;
        }
        return count - 1;
    }

    /**
     * Creates a SubwayLine based on the lines given in the LinesFile. Lines will be
     * written in the format of
     * "[LineType];[LineName];[StopName1];[StopName2];....;[StopNameN]" where
     * <ul>
     * <li>[LineType] is the type of the Line, either bus or subway</li>
     *
     * <li>[LineName] is the name of the Line</li>
     *
     * <li>[StopName(n)] is the name of the Stop</li>
     * </ul>
     *
     * @return SubwayLine which is an array of all BusLines
     * @throws IOException when an exception occurs during file IO
     */
    public SubwayLine subwayParser() throws IOException {

        BufferedReader brLines = new BufferedReader(new FileReader(this.lines));

        SubwayLine subwayLines = null;
        String st;
        while ((st = brLines.readLine()) != null) {
            String[] data = st.split(";");
            if (data[0].equals("Subway")) {
                subwayLines = new SubwayLine(data[1]);
                SubwayStation[] subwayStation = new SubwayStation[data.length - 2];
                for (int i = 0; i < subwayStation.length; i++) {
                    subwayStation[i] = new SubwayStation(data[i + 2], subwayLines);
                }
                subwayLines.setRoute(subwayStation);
            }
        }
        return (subwayLines);
    }

    /**
     * Creates cards based on the Lines given in the Cards File. Cards will be
     * written in the format of: "[RiderID];[CardID]" or "[RiderID];[CardID];[Bal]"
     * where
     *
     * <ul>
     * <li>[RiderID] is the ID of the Rider to which this card belongs to</li>
     *
     * <li>[CardID] is the ID of the Card</li>
     *
     * <li>[Bal] is the Balance of this card</li>
     * </ul>
     *
     * @param transitSystem is the transit system that will parse these cards
     * @throws IOException when an exception occurs during file IO
     */
    public void cardParser(TransitSystem transitSystem) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(cards));
        String st;
        while ((st = br.readLine()) != null) {
            Card card;
            String[] data = st.split(";");
            Rider rider = transitSystem.findRiderInSystem(IDGenerator.findIDString(data[0]));
            if (data.length == 2) {
                card = new Card(rider, data[1]);
            } else {
                card = new Card(rider, data[1], Float.parseFloat(data[2]));
            }
            rider.issueCard(card);
        }
    }
}