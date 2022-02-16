import java.util.*;
import java.io.*;


public class PathFinder {
  UnweightedGraph trainStopGraph = new MysteryUnweightedGraphImplementation();
  Map<String, Integer> vertexToId = new HashMap<String, Integer>();
  Map<Integer, String> idToVertex = new HashMap<Integer, String>();
  
  // Maps to manage vertexId with Stop Instances
  Map<Integer, Stop> idToStop = new HashMap<Integer, Stop>();
  Map<Stop, Integer> StopToId = new HashMap<Stop, Integer>();
  /**
  * Constructs a PathFinder that represents the graph with nodes (vertices) specified as in
  * nodeFile and edges specified as in edgeFile.
  * @param nodeFile name of the file with the node names
  * @param edgeFile name of the file with the edge names
  */
  public PathFinder(String nodeFile, String edgeFile){
    readNodes(nodeFile);
    readEdges(edgeFile);
  }

  /**
   * reads the nodes from the file that contains all of the article titles
   * @param nodeFile the file that contains the articles titles
   * returns void
   */
  private void readNodes(String nodeFile) {
    File inputFile = new File(nodeFile);
    Scanner scanner = null;
    try {
        scanner = new Scanner(inputFile);
    } catch (FileNotFoundException e) {
        System.err.println(e);
        System.exit(1);
    }

    while(scanner.hasNextLine()) {
      String line = scanner.nextLine();
      if(line.length() > 0 && !line.substring(0,1).equals("#")) {
        
        Integer nodeNum = trainStopGraph.addVertex();
        
        // New Instance of Stop
        Stop currStop = new Stop(line);
        String stopName = currStop.findData("Name");

        // Put info in Map
        vertexToId.put(stopName, nodeNum);
        idToVertex.put(nodeNum, stopName);
        idToStop.put(nodeNum, currStop);
        StopToId.put(currStop, nodeNum);
      }
    }
  }

  /**
   * reads the edges from the file that contains all of the paths between certain articles
   * @param edgeFile the file that contains the links and paths
   * returns void
   */
  private void readEdges(String edgeFile) {
    File inputFile = new File(edgeFile);
    Scanner scanner = null;
    try {
        scanner = new Scanner(inputFile);
    } catch (FileNotFoundException e) {
        System.err.println(e);
        System.exit(1);
    }

    while(scanner.hasNextLine()) {
      String line = scanner.nextLine();
      if(line.length() > 0 && !line.substring(0,1).equals("#")) {
        String[] splitline = line.split("\\s+");
        
        String begin = splitline[0];
        String end = splitline[1];
        
        int beginNum = vertexToId.get(begin);
        int endNum = vertexToId.get(end);
        trainStopGraph.addEdge(beginNum, endNum);
      }
    }
  }


  /**
   * this method returns a map of the shortest path from the starting node by looking at the previous path of each node 
   * @param start this represents the starting node/article that the user will want to start on
   * @return a map that shows the shortest path of Stops from the start node to a certain node
   */
  public Map<Integer, ArrayList<Stop>> breadthFirstSearch(String start) {
    HashMap<Integer, ArrayList<Stop>> newMap = new HashMap<>();
    ArrayList<Stop> newList = new ArrayList<Stop>();
    
    // Get the id for the first stop
    int stopId = vertexToId.get(start);

    // Add the initial value to the hashmap
    newMap.put(stopId, newList);

    // Queue to use for BFS
    Queue<Integer> bfsQueue = new LinkedList<Integer>();
    bfsQueue.add(stopId);

    while(!bfsQueue.isEmpty()){
      // Get neighboring vertex Ids
      for(int nodeId : trainStopGraph.getNeighbors(bfsQueue.peek())){
        if (!newMap.containsKey(nodeId)){
          ArrayList<Stop> pathPrevious = newMap.get(bfsQueue.peek());
          ArrayList<Stop> newPath = new ArrayList<Stop>();
          // Add all the predecessor Stops
          for(int i = 0 ;i < pathPrevious.size(); i++){
            newPath.add(pathPrevious.get(i));
          }
          // Add current stop
          newPath.add(idToStop.get(nodeId));
          newMap.put(nodeId, newPath);
          bfsQueue.add(nodeId);
        }
      }
      // Dequeue
      bfsQueue.remove();
    }

    return newMap;
  }
  
  /**
  * Returns a shortest path from node1 to node2, represented as list that has node1 at
  * position 0, node2 in the final position, and the names of each node on the path
  * (in order) in between. If the two nodes are the same, then the "path" is just a
  * single node. If no path exists, returns an empty list.
  * @param node1 name of the starting article node
  * @param node2 name of the ending article node
  * @return list of the names of nodes on the shortest path
  */
  public List<Stop> getShortestPath(String node1, String node2){
    int endNode = vertexToId.get(node2);
    Map<Integer, ArrayList<Stop>> newMap = breadthFirstSearch(node1);

    // Get the arraylist of Stops until the destination stop
    ArrayList<Stop> newList = newMap.get(endNode);
    if(newList != null){
      int startNode = vertexToId.get(node1);
      Stop firstStop = idToStop.get(startNode);
      newList.add(0, firstStop);
    }else{
      newList = new ArrayList<Stop>();
    }
    
    return newList;
  }

  /**
  * this toString method will return the the number of verticies and edges that are in the graph
  * @return a string of how many verticies and edges that are in the graph
  */
  public String toString() {
    int numVerts = trainStopGraph.numVerts();
    int numEdges = trainStopGraph.numEdges();

    return "Vertices: " + numVerts + "\nEdges: " + numEdges;
  }
}
                  