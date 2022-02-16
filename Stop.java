import java.util.HashMap;


public class Stop {
    HashMap<String, String> stopInfo = new HashMap<String, String>();

    public Stop(String str){
        String[] splitline = str.split(",");
        stopInfo.put("Name",splitline[0].trim());
        stopInfo.put("minutesTaken", splitline[1].trim());
        stopInfo.put("minutesStopped", splitline[2].trim());
        stopInfo.put("StopStationType", splitline[3].trim());
        stopInfo.put("Bathroom", splitline[4].trim());
        stopInfo.put("TypeOfFood", splitline[5].trim());
        stopInfo.put("TypeOfStop", splitline[6].trim());
    }

    /**
     * returns an aspect of the information of a stop
     * @param str the aspect of the stop that the information is wanted for 
     * @return a string that represents an aspect of the information of a stop
     */
    public String findData(String str) {
        return stopInfo.get(str);
    }

    /**
     * toString method that returns much information of a stop
     * @return string that represents the name of the stop, the type of station, whether the stop has a bathroom
     * and whether there is food at the stop
     */
    public String toString() {
        String name = stopInfo.get("Name");
        String stationType = stopInfo.get("StopStationType");
        String hasBathroom = stopInfo.get("Bathroom").equals("True") ? "Yes" : "No";
        String foodType = stopInfo.get("TypeOfFood");

        return "\nName of Stop: " + name + "\nStation Type: " + stationType + "\nHas Bathroom?: " + hasBathroom + "\nFood at Stop: " + foodType;
    }


}
