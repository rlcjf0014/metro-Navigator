import java.time.LocalTime;
import java.util.List;
import java.util.Random;

public class ExpressTrain extends Train {
    // Time in 24-hour format "Hour:Minutes"
    private String timeOfDeparture;
    private int onTrainPercentage;
    private String dayOfDeparture;
    private int onSeatPercentage;

    public ExpressTrain(boolean hasBathroom, int timeRunning, String time, String day) {
        super(hasBathroom, timeRunning, time, day);
        this.timeOfDeparture = time;
        this.dayOfDeparture = day;
    }

    /**
     * Set random number as percentage of getting a seat
     */
    public void calculateChanceOnSeat() {
        Random r = new Random();
        // Easier to find a seat on the weekend
        if (dayOfDeparture.toLowerCase().equals("saturday") || dayOfDeparture.toLowerCase().equals("sunday")) {
            int weekendSeatChance = r.nextInt(80);
            onSeatPercentage = weekendSeatChance;
        } else {
            // Harder to find a seat on the weekend
            int weekdaysSeatChance = r.nextInt(40);
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
     * Set random number as percentage of getting on train
     */
    public void calculateChanceOnTrain() {
        // "HH:MM"
        LocalTime now = LocalTime.parse(timeOfDeparture);
        LocalTime morning = LocalTime.parse("10:30");
        LocalTime evening = LocalTime.parse("18:00");
        LocalTime eveningEnd = LocalTime.parse("20:30");

        Random r = new Random();

        int constantChangePercentage;
        // Change upper bound by which day it is
        if (dayOfDeparture.toLowerCase().equals("saturday") || dayOfDeparture.toLowerCase().equals("sunday")) {
            constantChangePercentage = 5;
        } else {
            constantChangePercentage = -5;
        }

        // If the time of departure is in the morning, make the random chance less than
        // 65
        if (now.isBefore(morning)) {
            int morningChance = r.nextInt(65 + constantChangePercentage);
            onTrainPercentage = morningChance;
        } else if (now.isAfter(evening) && now.isBefore(eveningEnd)) {
            // If the time of departure is in the evening, make the random chance less than
            // 75
            int eveningChance = r.nextInt(75 + constantChangePercentage);
            onTrainPercentage = eveningChance;
        } else {
            // Return random chance out of 90 in other times
            int expressChance = r.nextInt(90 + constantChangePercentage);
            onTrainPercentage = expressChance;
        }
    }

    /**
     * Return integer that represents user's chance on getting on the train
     */
    public int getOnTrain() {
        return onTrainPercentage;
    }

    /**
     * Returns the total cost of the trip in Double
     * 
     * @param stopsList list of stops
     * @return Double that represents the total cost of the trip
     */
    @Override
    public Double calculateCost(List<Stop> stopsList) {
        int numExpressStops = 0;
        for (Stop stops : stopsList) {
            String stopType = stops.findData("TypeOfStop");
            if (stopType.equals("Express") || stopType.equals("All")) {
                numExpressStops += 1;
            }
        }

        // Start stop does not count
        numExpressStops -= 1;

        // Add Base Price by number of stops
        if (numExpressStops < 3) {
            return 5.0;
        } else {
            return 8.0;
        }
    }

    /**
     * Returns the total minutes taken for reaching the ending stop
     * 
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
            if (stopType.equals("Express") || stopType.equals("All")) {
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
}
