import java.util.List;
import java.util.Scanner;

/**
 * MetroNavigator.java Joshua Song, Sam Kim Carleton College, 2021-11-24
 *
 * A class that takes two text files to implement a queue to implement
 * breadth-first search to find the path between train stops.
 * 
 * The program can be run like: java MetroNavigator stops.tsv stopslinks.tsv
 * 
 * After the program runs, it will calculate and show the estimate of the journey from the
 * starting stop to the ending stop with the shortest path. 
 */
public class MetroNavigator {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Please enter in the correct command line arguments");
            System.exit(1);
        }

        String articleTitles = args[0];
        String links = args[1];

        PathFinder pathFinder = new PathFinder(articleTitles, links);

        // Helper methods for format and validation
        Helpers newHelpers = new Helpers();

        System.out.println("\nHello! Welcome to the Metro Navigator!");
        // Create a scanner to read user's input
        Scanner inputScanner = new Scanner(System.in);

        // Boolean to indicate whether the program continues 
        Boolean stopProgram = false;

        while (!stopProgram) {
            System.out.println("\nPlease select the type of train to check your travel: ");
            System.out.println("(Enter Express, Tour, Normal or All)");

            String trainType = inputScanner.nextLine().trim();

            // Validate train type
            while (!trainType.toLowerCase().equals("express") && !trainType.toLowerCase().equals("tour")
                    && !trainType.toLowerCase().equals("normal") && !trainType.toLowerCase().equals("all")) {
                System.out.println("\nPlease enter a valid train type.");
                trainType = inputScanner.nextLine().trim();
                ;
            }

            System.out.println("\nStart Stop: ");

            String startStop = inputScanner.nextLine().trim();
            ;
            // Validation for startStop
            while (pathFinder.vertexToId.get(startStop) == null
                    || !newHelpers.validateTrainTypeWithStops(pathFinder, startStop, trainType)) {
                // Check if the stop exists
                if (pathFinder.vertexToId.get(startStop) == null) {
                    System.out.println("\nPlease enter a valid stop.");
                    startStop = inputScanner.nextLine().trim();
                    ;
                } else if (!newHelpers.validateTrainTypeWithStops(pathFinder, startStop, trainType)) {
                    // Check if the train stops at the stop
                    System.out.println("\n" + trainType + " train does not stop at this stop.");
                    System.out.println("Please pick a stop that " + trainType + " train stops at.");
                    startStop = inputScanner.nextLine().trim();
                    ;
                }
            }

            System.out.println("\nEnd Stop: ");

            String endStop = inputScanner.nextLine().trim();
            ;
            // Validation for endStop
            while (pathFinder.vertexToId.get(endStop) == null
                    || !newHelpers.validateTrainTypeWithStops(pathFinder, endStop, trainType)) {
                // Check if the stop exists
                if (pathFinder.vertexToId.get(endStop) == null) {
                    System.out.println("\nPlease enter a valid stop.");
                    endStop = inputScanner.nextLine().trim();
                    ;
                } else if (!newHelpers.validateTrainTypeWithStops(pathFinder, endStop, trainType)) {
                    // Check if the train stops at the stop
                    System.out.println("\n" + trainType + " train does not stop at this stop.");
                    System.out.println("Please pick a stop that " + trainType + " train stops at.");
                    endStop = inputScanner.nextLine().trim();
                    ;
                }
            }

            System.out.println("\nEnter your time of departure in a 24-hour format.");
            System.out.println("Ex) 1:52PM -> 13:52, 6:30AM -> 06:30");

            String departureTime = inputScanner.nextLine().trim();

            // Validate departure time
            while (!newHelpers.validateTime(departureTime)) {
                System.out.println("\nPlease enter the time in valid format.");
                departureTime = inputScanner.nextLine().trim();
                ;
            }

            System.out.println("\nEnter your day of departure. (One day)");
            System.out.println("Ex) Monday, Friday, Saturday...");

            String departureDay = inputScanner.nextLine().trim();
            // Validate departure day
            while (!newHelpers.validateDay(departureDay)) {
                System.out.println("\nPlease enter a day of the week.");
                departureDay = inputScanner.nextLine().trim();
                ;
            }

            // Check whether the train operates at departure time
            Boolean trainOperation = newHelpers.checkTrainTime(departureTime);

            // Get the shortest path
            List<Stop> stopsList = pathFinder.getShortestPath(startStop, endStop);
            String stopPath;

            // If user selected to use express train
            if (trainType.toLowerCase().equals("express") || trainType.toLowerCase().equals("all")) {
                // Whether an estimate should be made
                Boolean estimateExpress = true;
                // Check if the train stops at the stops
                if (trainType.toLowerCase().equals("all")) {
                    if (!newHelpers.validateTrainTypeWithStops(pathFinder, endStop, "Express")
                            || !newHelpers.validateTrainTypeWithStops(pathFinder, startStop, "Express")) {
                        // train does not stop at these stops
                        estimateExpress = false;
                        System.out.println("\n{Express train does not stop at start or end stop.}");
                    }
                }
                if (estimateExpress) {
                    System.out.println("\n** Here is your route from " + startStop + " to " + endStop + " on "
                            + "Express train. **");

                    // List of Stops in Path
                    stopPath = newHelpers.stopsToString(stopsList, "Express");
                    System.out.println("\n" + stopPath);
                    ExpressTrain newExpress = new ExpressTrain(false, 30, departureTime, departureDay);

                    // Get Express Cost
                    Double cost = newExpress.calculateCost(stopsList);

                    // Get Time Taken
                    int timeTaken = newExpress.calculateTimeTaken(stopsList);

                    // Get chance of sitting in train
                    newExpress.calculateChanceOnSeat();
                    int chanceToSit = newExpress.getChanceOfSeatInTrain();

                    // Get chance of boarding the train
                    newExpress.calculateChanceOnTrain();
                    int chanceToBoard = newExpress.getOnTrain();

                    // How often the train runs
                    int timeRunning = newExpress.getTimeRunning();

                    // Whether the train has bathroom
                    boolean hasBathRoom = newExpress.getHasBathroom();
                    String bathroomInfo = hasBathRoom ? "Yes" : "No";

                    System.out.println("\nTravel Summary (Express Train): ");
                    System.out.println("\nExpress Train runs every " + timeRunning + " minutes.");
                    System.out.println("Has Bathroom?: " + bathroomInfo);
                    System.out.println("Estimated Cost: $" + cost);
                    System.out.println("Chance of Boarding: " + chanceToBoard + "%");
                    System.out.println("Chance of Getting a Seat: " + chanceToSit + "%");
                    System.out.println("Duration of Travel: " + timeTaken + " minutes");

                    // Train does operate, so we can show the arrival time.
                    if (trainOperation) {
                        String eta = newExpress.calculateArrivalTime(stopsList, timeTaken);
                        System.out.println("\nEstimated Time of Arrival: " + eta);
                    }
                }
            }

            // If user selected to use tour train
            if (trainType.toLowerCase().equals("tour") || trainType.toLowerCase().equals("all")) {
                // Whether an estimate should be made
                Boolean estimateTour = true;
                if (trainType.toLowerCase().equals("all")) {
                    // Check if the train stops at the stops
                    if (!newHelpers.validateTrainTypeWithStops(pathFinder, endStop, "Tour")
                            || !newHelpers.validateTrainTypeWithStops(pathFinder, startStop, "Tour")) {
                        // train does not stop at these stops
                        estimateTour = false;
                        System.out.println("\n{Tour train does not stop at start or end stop.}");
                    }
                }
                if (estimateTour) {
                    System.out.println(
                            "\n** Here is your route from " + startStop + " to " + endStop + " on " + "Tour train. **");
                    // List of Stops in Path
                    stopPath = newHelpers.stopsToString(stopsList, "Tour");
                    System.out.println("\n" + stopPath);
                    TourTrain newTour = new TourTrain(true, 45, departureTime, departureDay);

                    // Get Express Cost
                    Double cost = newTour.calculateCost(stopsList);

                    // Get Time Taken
                    int timeTaken = newTour.calculateTimeTaken(stopsList);

                    // Get chance of sitting in train
                    newTour.calculateChanceOnSeat();
                    int chanceToSit = newTour.getChanceOfSeatInTrain();

                    // Get chance of using the bathroom in the train
                    newTour.calculateChanceOnBathroom();
                    int chanceOfFullBathroom = newTour.getChanceOfBathroomInTrain();

                    // Get chance of boarding the train
                    int chanceToBoard = newTour.getOnTrain();

                    // How often the train runs
                    int timeRunning = newTour.getTimeRunning();

                    // Whether the train has bathroom
                    boolean hasBathRoom = newTour.getHasBathroom();
                    String bathroomInfo = hasBathRoom ? "Yes" : "No";

                    String foodMenu = newTour.getTourFoodInfo();

                    System.out.println("\nTravel Summary (Tour Train): ");
                    System.out.println("\nTour Train runs every " + timeRunning + " minutes.");
                    System.out.println("Has Bathroom?: " + bathroomInfo);
                    System.out.println("Chance of Bathroom being full: " + chanceOfFullBathroom + "%");
                    System.out.println("Estimated Cost: $" + cost);
                    System.out.println("Chance of Boarding: " + chanceToBoard + "%");
                    System.out.println("Chance of Getting a Seat: " + chanceToSit + "%");
                    System.out.println("Duration of Travel: " + timeTaken + " minutes");

                    // Train does operate, so we can show the arrival time.
                    if (trainOperation) {
                        String eta = newTour.calculateArrivalTime(stopsList, timeTaken);
                        System.out.println("\nEstimated Time of Arrival: " + eta);
                    }
                    System.out.println("\nMenu for food on Tour Train: \n" + foodMenu);
                }
            }

            // If user selected to use normal train
            if (trainType.toLowerCase().equals("normal") || trainType.toLowerCase().equals("all")) {
                System.out.println(
                        "\n** Here is your route from " + startStop + " to " + endStop + " on " + "Normal train. **");
                // List of Stops in Path
                stopPath = newHelpers.stopsToString(stopsList, "Normal");

                System.out.println("\n" + stopPath);
                NormalTrain newNormal = new NormalTrain(true, 15, departureTime, departureDay);

                // Get Express Cost
                Double cost = newNormal.calculateCost(stopsList);

                // Get Time Taken
                int timeTaken = newNormal.calculateTimeTaken(stopsList);

                // Get chance of sitting in train
                newNormal.calculateChanceOnSeat();
                int chanceToSit = newNormal.getChanceOfSeatInTrain();

                // Get chance of having an empty bathroom in the train
                newNormal.calculateChanceOnBathroom();
                int chanceOfFullBathroom = newNormal.getChanceOfBathroomInTrain();

                // Get chance of boarding the train
                int chanceToBoard = newNormal.getOnTrain();

                // How often the train runs
                int timeRunning = newNormal.getTimeRunning();

                // Whether the train has bathroom
                boolean hasBathRoom = newNormal.getHasBathroom();
                String bathroomInfo = hasBathRoom ? "Yes" : "No";

                String foodMenu = newNormal.getTourFoodInfo();

                System.out.println("\nTravel Summary (Normal Train): ");
                System.out.println("\nNormal Train runs every " + timeRunning + " minutes.");
                System.out.println("Has Bathroom?: " + bathroomInfo);
                System.out.println("Chance of Bathroom being full: " + chanceOfFullBathroom + "%");
                System.out.println("Estimated Cost: $" + cost);
                System.out.println("Chance of Boarding: " + chanceToBoard + "%");
                System.out.println("Chance of Getting a Seat: " + chanceToSit + "%");
                System.out.println("Duration of Travel: " + timeTaken + " minutes");

                // Train does operate, so we can show the arrival time.
                if (trainOperation) {
                    String eta = newNormal.calculateArrivalTime(stopsList, timeTaken);
                    System.out.println("\nEstimated Time of Arrival: " + eta);
                }
                System.out.println("\nMenu for food on Normal Train: \n" + foodMenu);
            }

            // Show information on end stop
            System.out.println("\nHere is some information about your end stop.");
            Stop destination = stopsList.get(stopsList.size() - 1);
            System.out.println(destination.toString());

            System.out.println("\nWould you like to get path with different stops or quit?");
            System.out.println("\nEnter `Again` to get path or `Quit` to quit program.");

            String userCommand = inputScanner.nextLine().trim();
            while (!userCommand.toLowerCase().equals("again") && !userCommand.toLowerCase().equals("quit")) {
                System.out.println("\nPlease enter Again or Quit.");
                userCommand = inputScanner.nextLine().trim();
            }
            if (userCommand.toLowerCase().equals("quit")) {
                stopProgram = true;
            }
        }

        System.out.println("\n\nThank you for using Metro Navigator. Hope to see you again!");
        inputScanner.close();
    }
}
