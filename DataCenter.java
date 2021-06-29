package net.barmajiyat.load_balancer;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class DataCenter {

  private String name;
  private boolean isInitialized = false;
  public List<Node> allNodes;
  public List<Network> allNetworks;
  public static String allLoopsCSV = "";
  public static int loopIndex = 0;
  
  public DataCenter(String name) {
    this.name = name;
    try {
      this.initialze();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void initialze() throws Exception {
    if (this.isInitialized()) {
      return;
    }
    String content = "";
    JSONObject dataCenterJson;
    JSONArray nodesJsonArray = new JSONArray();
    JSONArray networkJsonArray = new JSONArray();
    try {
      // read json file
      content = new String(Files.readAllBytes(Paths.get("network.json")), StandardCharsets.UTF_8);
      // parse
      dataCenterJson = new JSONObject(content);

      nodesJsonArray = dataCenterJson.getJSONArray("nodes");
      networkJsonArray = dataCenterJson.getJSONArray("networks");

    } catch (IOException e) {
      e.printStackTrace();
    }

    // fill nodes
    this.generateNodes(nodesJsonArray);

    // fill network
    this.generateNetworks(networkJsonArray);

    this.isInitialized = true;
  }

  private void generateNodes(JSONArray nodesJsonArray) {
    if (nodesJsonArray == null) {
      return;
    }
    allNodes = new ArrayList<Node>();
    for (Object o : nodesJsonArray) {
      JSONObject nodeJsonObject = new JSONObject(o.toString());
      Node n = new Node(nodeJsonObject);
      allNodes.add(n);
    }
  }

  /**
   * @author Houssein Hellani 
   * @param networksJsonArray
   * @throws Exception
   */
  private void generateNetworks(JSONArray networksJsonArray) throws Exception {
    if (networksJsonArray == null) {
      return;
    }
    allNetworks = new ArrayList<Network>();
    // loop over items
    for (int i = 0; i < networksJsonArray.length(); i++) {
      JSONObject networkJsonObject = networksJsonArray.getJSONObject(i);
      JSONArray networkNodesJsonArray = networkJsonObject.getJSONArray("nodes");
      List<Node> nodesList = new ArrayList<Node>();
      for (int j = 0; j < networkNodesJsonArray.length(); j++) {
        Node node = this.getNodeById(networkNodesJsonArray.getString(j));
        // check if node exists add it - else throw exception
        if (node != null) {
          nodesList.add(node);
        } else {
          throw new Exception("Node " + networkNodesJsonArray.getString(j) + " doesn't exist");
        }
      }
      Network network = new Network(networkJsonObject.getString("id"), nodesList);
      allNetworks.add(network);
    }
  }

  private boolean isInitialized() {
    return this.isInitialized;
  }

  private Node getNodeById(String id) {
    Node result = null;
    for (Node n : this.allNodes) {
      if (n.getId().contentEquals(id)) {
        result = n;
        break;
      }
    }
    return result;
  }

  public void printDataCenter() {
    System.out.println("\nNodes List:\n-----------");
    for (Node n : allNodes) {
      System.out.println(n);
    }
    System.out.println("\nNetwork List:\n---------------");
    for (Network n : allNetworks) {
      System.out.println(n);
    }
  }

  public void printInTable() {
    String output = "Nodes,cpuPower,RAM Capacity,Active Connections,Remaining CPU Power,Remaining RAM Space,Initial Weight";
    for (Node n : allNodes) {
      output += n.getId() + "," + n.getCpuPower() + "," + n.getRamCapacity() + "," + n.getNumberOfActiveConnections()
          + "," + n.getRemainingCpuPower() + "," + n.getRemainingRamSpace() + "," + n.getInitialWeight() + "," + n.getRemainingWeight() +  "\n";
    }
    System.out.println(output + "\nNetwork List:\n---------------");
    for (Network n : allNetworks) {
      System.out.println(n);
    }
  }

  public void loadBalanceOriginal() {
	    // load balance on weight
	    // loop over all nodes
	    // get the one with the least originalWLC
	    // send connection to it
	    double currentOriginalWLC = -1;
	    Node targetNode = null;

	    for (Node node : this.allNodes) {
	      if (currentOriginalWLC == -1) {
	        currentOriginalWLC = node.getOriginalWLC();
	        targetNode = node;
	      } else if (node.getOriginalWLC() < currentOriginalWLC) {
	        currentOriginalWLC = node.getOriginalWLC();
	        targetNode = node;
	      }
	    }
	    for (int i = 0; i < this.allNodes.size(); i++) {
	      if (this.allNodes.get(i).getId() == targetNode.getId() && this.allNodes.get(i).getRemainingCpuPower() >= 1
	          && this.allNodes.get(i).getRemainingRamSpace() >= 2) {
	        this.allNodes.get(i).addConnection();
	      }
	    }
	  }
  
  public void loadBalanceOptimized() {
    // load balance based on optimizedWLC
    // loop over all nodes
    Node targetNode = null;
	double currentOptimizedlWLC = -1;
    for (Node node : this.allNodes) {
		if (currentOptimizedlWLC == -1) {
		    currentOptimizedlWLC = node.getOptimizedWLC(this);//network);
		    targetNode = node;
		 } else
		  	if (node.getOptimizedWLC(this) < currentOptimizedlWLC && node.getRemainingCpuPower() >= 1
		      && node.getRemainingRamSpace() >= 2) {
		    currentOptimizedlWLC = node.getOptimizedWLC(this);
		    targetNode = node;
		 }
    }
    // hh >
    // hh <
    DataCenter.allLoopsCSV += "Loop: " + DataCenter.loopIndex++ + "\r\n" +getLoopCSV() + "\r\n";
    /*--
    for (Network network : this.allNetworks) {
      double currentOptimizedlWLC = -1;
      for (Node node : network.nodes) {
        if (currentOptimizedlWLC == -1) {
          currentOptimizedlWLC = node.getOptimizedWLC(this);//network);
          targetNode = node;
        } else
        	if (node.getOptimizedWLC(this) < currentOptimizedlWLC && node.getRemainingCpuPower() >= 1
            && node.getRemainingRamSpace() >= 2) {
          currentOptimizedlWLC = node.getOptimizedWLC(this);
          targetNode = node;
        }
      }
    }*/
    // get the one with the least optimizedWLC
    for (int i = 0; i < this.allNodes.size(); i++) {
      if (this.allNodes.get(i).getId().contentEquals(targetNode.getId())) {
        this.allNodes.get(i).setNumberOfActiveConnections(this.allNodes.get(i).getNumberOfActiveConnections() + 1);
      }
    }
  }
  
  public void printNodesToCSV() {
    String output = "Nodes,CPU Power,RAM Capacity,Active Connections,Remaining CPU Power,Remaining RAM Space,Initial Weight,"
    		+ "remaining weights \n";
    for (Node n : allNodes) {
      output += n.getId() + "," + n.getCpuPower() + "," + n.getRamCapacity() + "," + n.getNumberOfActiveConnections()
          + "," + n.getRemainingCpuPower() + "," + n.getRemainingRamSpace() + "," + n.getInitialWeight() + "," + n.getRemainingWeight() + "\n";
    }
    FileWriter myWriter;
    try {
      myWriter = new FileWriter("out/" + this.name + "_nodes.csv");
      myWriter.write(output);
      myWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public String getLoopCSV() {
    String output = "Nodes,CPU Power,RAM Capacity,Active Connections,Remaining CPU Power,Remaining RAM Space,Initial Weight,remaining weights," + "WLC" + "\n";
    for (Node n : allNodes) {
      output += n.getId() + "," + n.getCpuPower() + "," + n.getRamCapacity() + "," + n.getNumberOfActiveConnections()
          + "," + n.getRemainingCpuPower() + "," + n.getRemainingRamSpace() + "," + n.getInitialWeight() + "," + n.getRemainingWeight()
          + "," + n.getOptimizedWLC(this)  + "\n";
    
   
    
    
    }
    return output;
  }
  
  public void printNetworkToCSV(String tag) { 
    try {
      Files.createDirectories(Paths.get("out"));
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    String output = "Network,Nodes\n";
    for (Network n : allNetworks) {
      output += n.id + ",";
      for (Node node : n.nodes) {
        output += node.getId() + "-";
      }
      output += "\n";
    }
    FileWriter myWriter;
    try {
      myWriter = new FileWriter("out/" + this.name + "_" + tag + "_network.csv");
      myWriter.write(output);
      myWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public void saveResultsFile(String tag) {
    this.printNetworkToCSV(tag);
    this.printNodesToCSV();
  }
}
