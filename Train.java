import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

public class Train {
    // Set value for chance of boarding train
    protected int onTrainPercentage = 95;
    private String timeOfDeparture;
    private String dayOfDeparture;
    private int timeRunning;
    private boolean hasBathroom;
    protected HashMap<String, Integer> foodInfo = new HashMap<String, Integer>();

    public Train(boolean hasBathroom, int timeRunning, String time, String day) {
        this.hasBathroom = hasBathroom;
        this.timeRunning = timeRunning;
        this.timeOfDeparture = time;
        this.dayOfDeparture = day;
    }

    /**
     * Returns the arrival time
     * 
     * @param stopsList list of stops
     * @param timeTaken timeTaken throughout the travel
     * @return String that represents the arrival time
     */
    public String calculateArrivalTime(List<Stop> stopsList, int timeTaken) {
        LocalTime now = LocalTime.parse(timeOfDeparture);
        return now.plusMinutes(timeTaken).toString();
    }

    /**
     * Returns the total cost of the trip in Double
     * 
     * @param stopsList list of stops
     * @return Double that represents the total cost of the trip
     */
    public Double calculateCost(List<Stop> stopsList) {
        // Total number of stops. First stop does not count.
        int stopsNumber = stopsList.size() - 1;

        // If less than 5 stops, pay base price
        if (stopsNumber <= 5) {
            return 3.5;
        } else if (stopsNumber > 5 && stopsNumber <= 10) {
            // If more than 5, but less than 11 stops, add overcharge by stops
            int overChargeStops = stopsNumber - 5;
            return 3.5 + 0.50 * Double.valueOf(overChargeStops);
        } else {
            // If more than 10, add overcharge by stops
            int overChargeStops = stopsNumber - 10;
            return 3.5 + 2.5 + 0.75 * Double.valueOf(overChargeStops);
        }
    }

    /**
     * Returns the total minutes taken for reaching the ending stop
     * 
     * @param stopsList list of stops to the end Stop
     * @return integer that represents the minutes taken to reach the stop
     */
    public int calculateTimeTaken(List<Stop> stopsList) {
        int totalTime = 0;

        // First stop does not count
        for (int i =1; i < stopsList.size(); i++) {
            Stop currStop = stopsList.get(i);
            
            // minutes taken to go to each stop
            int timeTaken = Integer.parseInt(currStop.findData("minutesTaken"));
            
            // minutes stopped at each stop
            int timeStopped = Integer.parseInt(currStop.findData("minutesStopped"));
            
            // For the last stop, there is no time stopped
            if (i == stopsList.size() - 1) {
                totalTime += timeTaken;
            } else {
                totalTime += timeTaken + timeStopped;
            }
        }

        return totalTime;
    }

    /**
     * this will get the int value for the chance of user getting on the train
     * 
     * @return integer that represents the chance of user getting on the train
     */
    public int getOnTrain() {
        if (dayOfDeparture.equals("saturday") || dayOfDeparture.equals("sunday")) {
            return onTrainPercentage - 10;
        }
        return onTrainPercentage - 5;
    }

    /**
     * Add Default food information
     */
    public HashMap<String, Integer> addFoodInfo() {
        foodInfo.put("Burger", 9);
        foodInfo.put("Pizza", 10);
        foodInfo.put("Hot Dog", 7);
        return foodInfo;
    }

    /**
     * this will get the list of food in train as string
     * 
     * @param foodInfo information of food in hashMap
     * @return String that represents information on food
     */
    public String getFoodInfo(HashMap<String, Integer> foodInfo) {
        String foodResult = "";
        for (String i : foodInfo.keySet()) {
            foodResult += "" + i + ": " + "$" + foodInfo.get(i) + "\n";
        }
        return foodResult;
    }

    /**
     * get how often the train runs (every 15 min or every 30 min)
     * 
     * @return how often the train runs
     */
    public int getTimeRunning() {
        return timeRunning;
    }

    /**
     * get whether the train has bathroom
     * 
     * @return boolean value for bathroom
     */
    public boolean getHasBathroom() {
        return hasBathroom;
    }

}