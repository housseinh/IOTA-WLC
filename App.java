package net.barmajiyat.load_balancer;

import java.io.FileWriter;
import java.io.IOException;

public class App {

  public static void main(String[] args) {
    DataCenter dataCenterNormal = new DataCenter("normal");
    DataCenter dataCenterOptimized = new DataCenter("optimized");
    
    // Before any operation
    dataCenterNormal.printInTable();

    int numberOfLoops = 1001;
    // Load balance
    //dataCenterNormal.allNodes.get(4).setNumberOfActiveConnections(150);
    //dataCenterOptimized.allNodes.get(4).setNumberOfActiveConnections(150);
    for (int i = 0; i < numberOfLoops; i++) {
      dataCenterNormal.loadBalanceOriginal();
      dataCenterOptimized.loadBalanceOptimized();
    }
    saveAllLoops();
    // print after load balancing optimized values
    dataCenterNormal.saveResultsFile("");
    dataCenterOptimized.saveResultsFile("");
    
    System.out.println("Done!");
  }
  
  private static void saveAllLoops() {
	  FileWriter myWriter;
	    try {
	      myWriter = new FileWriter("out/all_loops.csv");
	      myWriter.write(DataCenter.allLoopsCSV);
	      myWriter.close();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
  }

}
