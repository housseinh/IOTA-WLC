package net.barmajiyat.load_balancer;

import java.util.List;

public class Network {
  String id;
  public List<Node> nodes;

  public Network(String id, List<Node> nodes) {
    this.id = id;
    this.nodes = nodes;
  }
  
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<Node> getNodes() {
    return nodes;
  }

  public void setNodes(List<Node> nodes) {
    this.nodes = nodes;
  }
  
  @Override
  public String toString() {
    String output = "id: " + this.getId() + ", Nodes: ";
    int count = 1;
    for (Node n : this.getNodes()) {
      output += n.getId();
      if (count < this.getNodes().size()) {
        output += ", ";
      }
      count++;
    }
    return output;
  }
}
