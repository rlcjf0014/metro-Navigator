import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TourTrain extends Train{
    private String dayOfDeparture;
    private int onSeatPercentage;
    private int inBathroomPercentage;
    private String timeOfDeparture;

    public TourTrain(boolean hasBathroom, int timeRunning, String time, String day) {
        super(hasBathroom, timeRunning, time, day);
        this.dayOfDeparture = day;
        this.timeOfDeparture = time;
    }

     /**
     * this will get the int value for the chance of user getting on the train
     * 
     * @return integer that represents the chance of user getting on the train
     */
    @Override
    public int getOnTrain() {
        int onTrainPercentage = super.getOnTrain();
        if (dayOfDeparture.toLowerCase().equals("saturday") || dayOfDeparture.toLowerCase().equals("sunday")) {
            onTrainPercentage -= 20;
        }
        LocalTime now = LocalTime.parse(timeOfDeparture);
        LocalTime evening = LocalTime.parse("18:00");
        LocalTime eveningEnd = LocalTime.parse("20:30");

        if (now.isAfter(evening) && now.isBefore(eveningEnd)) {
            onTrainPercentage -= 10;
        }
        return onTrainPercentage;
    }

     /**
     * Calculate the chance of finding a seat by the day and set as variable
     */
    public void calculateChanceOnBathroom() {
        Random r = new Random();
        // Harder to find an empty Bathroom on the weekend
        if (dayOfDeparture.toLowerCase().equals("saturday") || dayOfDeparture.toLowerCase().equals("sunday")) {
            int weekendBathroomChance = r.nextInt(70);
            inBathroomPercentage = weekendBathroomChance;
        } else {
            int weekdaysBathroomChance = r.nextInt(90);
            inBathroomPercentage = weekdaysBathroomChance;
        }
    }

      /**
     * Return integer that represents user's chance on sitting in the train
     */
    public int getChanceOfBathroomInTrain() {
        return inBathroomPercentage;
    }

    /**
     * Calculate the chance of finding a seat by the day and set as variable
     */
    public void calculateChanceOnSeat() {
        Random r = new Random();
        // Harder to find a seat on the weekend
        if (dayOfDeparture.toLowerCase().equals("saturday") || dayOfDeparture.toLowerCase().equals("sunday")) {
            int weekendSeatChance = r.nextInt(70);
            onSeatPercentage = weekendSeatChance;
        } else {
            int weekdaysSeatChance = r.nextInt(88);
            onSeatPercentage = weekdaysSeatChance;
        }
    }

      /**
     * Return integer that represents user's chance on sitting in the train
     */
    public int getChanceOfSeatInTrain() {
        return onSeatPercentage;
    }

    /**
     * Returns the total cost of the trip in Double
     * @param stopsList list of stops
     * @return Double that represents the total cost of the trip
     */
    @Override
    public Double calculateCost(List<Stop> stopsList) {
        int numTourStops = 0;
        
        for (Stop stops: stopsList) {
            String stopType = stops.findData("TypeOfStop");
            if (stopType.equals("Tour") || stopType.equals("All")) {
                numTourStops += 1;
            } 
        }
        // Start stop does not count
        numTourStops -= 1;

         // Add Base Price by number of stops 
         if (numTourStops < 5) {
             return 7.0;
         } else {
             return 10.0;
         }
    }

    /**
     * Returns the total minutes taken for reaching the ending stop
     * @param stopsList list of stops to the end Stop
     * @return integer that represents the minutes taken to reach the stop
     */
    @Override
    public int calculateTimeTaken(List<Stop> stopsList) {
        int totalTime = 0;

        // First stop does not count
        for (int i =1; i < stopsList.size(); i++) {
            Stop currStop = stopsList.get(i);
            String stopType = currStop.findData("TypeOfStop");
            
            // Time taken to get to the stop
            int timeTaken = Integer.parseInt(currStop.findData("minutesTaken"));
            
            // Time stopped at the stop
            int timeStopped = Integer.parseInt(currStop.findData("minutesStopped"));

            // If the express train stops at the stop, add both
            if (stopType.equals("Tour") || stopType.equals("All")) {
                // For the last stop, there is no time stopped
                if (i == stopsList.size() - 1) {
                    totalTime += timeTaken;
                } else {
                    totalTime += timeTaken + timeStopped;
                }
            } else {
                // If the express train does not stop at the stop, do not add minute stopped.
                totalTime += timeTaken;
            }
        }

        return totalTime;
    }

    /**
     * Returns string that show the list of food available by day
     * @return String that represents the list of food 
     */
    public String getTourFoodInfo() {
        HashMap<String, Integer> tourTrainFood = super.addFoodInfo();
        // Different menu by day
        if (dayOfDeparture.toLowerCase().equals("saturday") || dayOfDeparture.toLowerCase().equals("sunday")) {
            tourTrainFood.put("Lobster", 55);
            tourTrainFood.put("Bulgogi", 25);
            tourTrainFood.put("Lamb Chop", 35);
        } else if (dayOfDeparture.toLowerCase().equals("monday") || dayOfDeparture.toLowerCase().equals("wednesday") || dayOfDeparture.toLowerCase().equals("friday")) {
            tourTrainFood.put("Pasta", 18);
            tourTrainFood.put("Pho", 13);
            tourTrainFood.put("Steak", 25);
        } else {
            tourTrainFood.put("Soup", 5);
            tourTrainFood.put("Turkey", 14);
            tourTrainFood.put("Pork Chop", 17);
        }

        return super.getFoodInfo(tourTrainFood);
    }

}
