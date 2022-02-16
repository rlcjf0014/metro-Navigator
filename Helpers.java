import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.List;

/**
 * List of helper methods for format, conversion and validation. 
 */
public class Helpers {

    /**
     * Check whether this train stops at this stop
     * @param finder PathFinder
     * @param stop string that represents the stop
     * @param trainType string that represents the trainType
     * @return boolean that represents whether the train stops at this stop
     */
    public boolean validateTrainTypeWithStops(PathFinder finder, String stop, String trainType) {
        int stopId = finder.vertexToId.get(stop);

        String stopType = finder.idToStop.get(stopId).findData("TypeOfStop");

        if (trainType.toLowerCase().equals("express")) {
            if (!stopType.equals("Express") && !stopType.equals("All")) {
                return false;
            }
        } else if (trainType.toLowerCase().equals("tour")) {
            if (!stopType.equals("Tour") && !stopType.equals("All")) {
                return false;
            }
        } 
        return true;
    }

     /**
     * Returns the path from start stop to end stop
     * @param stops list of stops
     * @return String that represents the path from start stop to end stop
     */
    public String stopsToString(List<Stop> stops, String trainType) {
        String result = "";
        for (int i = 0; i < stops.size(); i++) {
            String stopType = stops.get(i).findData("TypeOfStop");
            String stopName = stops.get(i).findData("Name");
            if (trainType.toLowerCase().equals("express")) {
                // Only Add stops that the train stops at
                if (stopType.equals("Express") || stopType.equals("All")) {
                    if (i == stops.size() - 1) {
                        result += stopName;
                    } else {
                        result += stopName + " -> ";
                    }
                }
            } else if (trainType.toLowerCase().equals("tour")) {
                // Only Add stops that the train stops at
                if (stopType.equals("Tour") || stopType.equals("All")) {
                    if (i == stops.size() - 1) {
                        result += stopName;
                    } else {
                        result += stopName + "-> ";
                    }
                }
            } else {
                // Add all the stops for normal train
                if (i == stops.size() - 1) {
                    result += stopName;
                } else {
                    result += stopName + "-> ";
                }
            }
        }
        return "[ " + result + " ]";
    }

    /**
     * Check if day is valid
     * 
     * @param day string that represents day
     * @return boolean that represents whether the day is valid or not
     */
    public boolean validateDay(String day) {
        if (!day.equals("Monday") && !day.equals("Tuesday") && !day.equals("Wednesday") && !day.equals("Thursday")
                && !day.equals("Friday") && !day.equals("Saturday") && !day.equals("Sunday")) {
            return false;
        }
        return true;

    }

    /**
     * Check if departure time lies in the range of train operating time
     * 
     * @param time string that represents time
     * @return boolean that represents whether the train runs at departure time
     */
    public boolean checkTrainTime(String time) {
        LocalTime now = LocalTime.parse(time);
        // Start time for train
        LocalTime morning = LocalTime.parse("05:30");
        // End time for train
        LocalTime night = LocalTime.parse("23:30");
        if (now.isBefore(morning) || now.isAfter(night)) {
            return false;
        }
        return true;
    }

    /**
     * Check if the string is in valid time format
     * 
     * @param time string that represents time
     * @return boolean that represents if the string is valid
     */
    public boolean validateTime(String time) {
        try {
            LocalTime.parse(time);
            return true;
        } catch (DateTimeException | NullPointerException e) {
            return false;
        }
    }
}
