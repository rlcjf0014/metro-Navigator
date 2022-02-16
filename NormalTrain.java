import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class NormalTrain extends Train {
    private String dayOfDeparture;
    private int inBathroomPercentage;
    private int onSeatPercentage;

    public NormalTrain(boolean hasBathroom, int timeRunning, String time, String day) {
        super(hasBathroom, timeRunning, time, day);
        this.dayOfDeparture = day;
    }

    /**
     * Calculate the chance of finding a seat by the day
     */
    public void calculateChanceOnSeat() {
        Random r = new Random();
        // Harder to find a seat on the weekend
        if (dayOfDeparture.toLowerCase().equals("saturday") || dayOfDeparture.toLowerCase().equals("sunday")) {
            int weekendSeatChance = r.nextInt(90);
            onSeatPercentage = weekendSeatChance;
        } else {
            int weekdaysSeatChance = r.nextInt(100);
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
     * Calculate the chance of finding a seat by the day
     */
    public void calculateChanceOnBathroom() {
        Random r = new Random();
        // Harder to find an empty Bathroom on the weekend
        if (dayOfDeparture.toLowerCase().equals("saturday") || dayOfDeparture.toLowerCase().equals("sunday")) {
            int weekendBathroomChance = r.nextInt(80);
            inBathroomPercentage = weekendBathroomChance;
        } else {
            int weekdaysBathroomChance = r.nextInt(100);
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
     * Returns string that show the list of food
     * 
     * @return String that represents the list of food
     */
    public String getTourFoodInfo() {
        HashMap<String, Integer> tourTrainFood = super.addFoodInfo();
        return super.getFoodInfo(tourTrainFood);
    }

    /**
     * Returns the total cost of the trip in Double
     * 
     * @param stopsList list of stops
     * @return Double that represents the total cost of the trip
     */
    @Override
    public Double calculateCost(List<Stop> stopsList) {
        Double normalCharge = super.calculateCost(stopsList);
        // Additional Fee for usage on weekend
        if (dayOfDeparture.toLowerCase().equals("saturday") || dayOfDeparture.toLowerCase().equals("sunday")) {
            return normalCharge + 0.5;
        } else {
            return normalCharge;
        }
    }

}
