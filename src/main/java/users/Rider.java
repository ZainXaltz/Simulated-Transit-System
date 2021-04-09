package main.java.users;

import main.java.entities.Card;
import main.java.system.UID.ID;
import main.java.system.UID.IDGenerator;
import main.java.system.Logger;
import main.java.system.Trip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * This is the Rider class, It represents each user of the application. Each
 * rider has a name, email, any amount of cards, and a specific ID.
 */

public class Rider {
    private String name;
    private String email;
    private ArrayList<Card> cards;
    private ID id;
    private float[] reloadAmounts;

    /**
     * Create a new rider within the transit system with the given name, email
     * address, and ID
     *
     * @param name  the name of this riders
     * @param email the email address of this rider
     * @param ID    the ID of this rider
     */
    public Rider(String name, String email, String ID) {
        this.name = name;
        this.email = email;
        this.id = IDGenerator.fromString(ID);
        cards = new ArrayList<Card>();
        reloadAmounts = new float[] { 10, 20, 50 };
    }

    /**
     * Create a new rider within the transit system with the given name and email
     * address
     * 
     * @param name  the name of this riders
     * @param email the email address of this rider
     */
    public Rider(String name, String email) {
        this.name = name;
        this.email = email;
        this.id = IDGenerator.generateID('R');
        cards = new ArrayList<Card>();
    }

    /**
     * returns name
     *
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * returns id
     *
     * @return int Id
     */
    public ID getId() {
        return id;
    }

    /**
     * returns Email
     *
     * @return String Email
     */
    public String getEmail() {
        return email;
    }

    /**
     * returns list of cards
     *
     * @return ArrayList of cards
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * Issues the given card to this rider if this rider is the rightful bearer of
     * the given card
     *
     * @param newCard the card to be given to this user
     */
    public void issueCard(Card newCard) {
        if (newCard.getBearer() == this) {
            this.cards.add(newCard);
        }
    }

    /**
     * Suspends the card from further use.
     *
     * @param card
     * @return boolean whether the card was suspended successfully or not
     */
    public boolean suspendCard(Card card) {
        return card.suspend(this.getId());
    }

    /**
     * Reactivates the card to be able to be used again.
     *
     * @param card
     * @return boolean whether the card was reactivated successfully or not
     */
    public boolean reActivateCard(Card card) {
        return card.reActivate(this.getId());
    }

    /**
     * Changes the name of the Rider.
     *
     * @param name String which represents the new name of the rider.
     */
    public void changeName(String name) {
        Pattern specialChars = Pattern.compile("[^A-Za-z\\s]");
        Matcher approvedName = specialChars.matcher(name);

        Pattern blanks = Pattern.compile("^\\s+$");
        Matcher allBlank = blanks.matcher(name);

        if (approvedName.find()) {
            Logger.error("Entered an invalid Name!", "Please make sure you do not have special characters");
        } else if (name.equals("") || allBlank.find()) {
            Logger.error("Entered an invalid Name!", "Please make sure you do not leave field empty");
        } else {
            Logger.user(this, "Name has been updated!");
            this.name = name;
        }
    }

    /**
     * Reloads card with the set amount of money. The amount must be an allowed
     * reload amount or nothing happens.
     *
     * @param amount of money to reload
     * @param card   to reload
     * @return amount left in the card
     */
    public float reloadCard(float amount, Card card) {
        String validAmts = "";
        for (float candidate : reloadAmounts) {
            validAmts += "$" + candidate + " ";
            if (candidate == amount) {
                card.addBalance(amount);
                Logger.user(this, "$" + amount + " has been added to the balance of card " + card.getId());
                return card.getBalance();
            }
        }

        Logger.error("Cannot reload with $" + amount, String.format("Valid amounts are: %s", validAmts));
        return card.getBalance();
    }

    /**
     * Returns a list of the 3 most recent trips of the User. If the user has not
     * taken atleast three trips it will return a list of the most recent trips, or
     * none otherwise
     *
     * @return Array of recent Trips
     */
    public Trip[] recentTrips() {
        Trip[] recTrips = new Trip[3];
        for (Card card : this.cards) {
            for (Trip trip : card.getTrips()) {
                if (recTrips[0] == null) { // Initial case where there is no trip to compare to
                    recTrips[0] = trip;
                    continue;
                }
                insertTrip(recTrips, trip);
            }
        }
        return recTrips;
    }

    /**
     * Inserts trip into the list in order. if the trip is after (timewise) a
     * current trip then
     * 
     * @param trips list of the current most recent trips
     * @param trip  the current trip being inserted
     */
    private void insertTrip(Trip[] trips, Trip trip) {

        for (int i = 0; i < trips.length; i++) {
            if (trip.getEndTime().isAfter(trips[i].getEndTime())) { // if trip time is after current trip in list shift
                Trip backupTrip = trips[i];
                for (int j = i; j < trips.length; j++) { // Iterate from the current index to the last and begin
                                                         // shifting
                    Trip tempTrip = trips[j];
                    if (j == i) {
                        trips[j] = trip;
                    } else {
                        trips[j] = backupTrip;
                    }
                    backupTrip = tempTrip;
                }
                break;
            }
        }
    }

    /**
     * Returns the average monthly cost of this particular rider, accounts for all
     * the cards this user may have
     *
     * @return Float of the average monthly cost
     */
    public float averageMonthlyCost() {
        HashMap<String, ArrayList<Trip>> monthlyTrip = new HashMap<String, ArrayList<Trip>>();
        ArrayList<Float> prices = new ArrayList<>();
        float totalSum = 0F;

        for (Card card : this.cards) {
            for (Trip trip : card.getTrips()) {
                int month = trip.getStartTime().getMonth().getValue();
                int year = trip.getStartTime().getYear();

                String yearMonth = year + "/" + month;

                monthlyTrip.computeIfAbsent(yearMonth, k -> new ArrayList<Trip>()).add(trip);
            }
        }

        for (String key : monthlyTrip.keySet()) {
            float sum = 0F;
            for (Trip trip : monthlyTrip.get(key)) {
                sum += trip.getCurrentPrice();
            }
            prices.add(sum);
        }

        for (float price : prices) {
            totalSum += price;
        }

        return totalSum / prices.size();

    }
}