package net.barmajiyat.load_balancer;

import org.json.JSONObject;

public class Node {
	// TODO we can add for ease part of which network
	private String id;
	private double cpuPower; // in MHz
	private int ramCapacity; // in MB
	private int numberOfActiveConnections;

	public Node(String id, double cpuPower, int ramCapacity) {
		this.setId(id);
		this.setCpuPower(cpuPower);
		this.setRamCapacity(ramCapacity);
		this.setNumberOfActiveConnections(1);
	}

	public Node(String id, double cpuPower, int ramCapacity, int numberOfActiveConnections) {
		this.setId(id);
		this.setCpuPower(cpuPower);
		this.setRamCapacity(ramCapacity);
		this.setNumberOfActiveConnections(numberOfActiveConnections);
	}

	public Node(JSONObject nodeJsonObject) {
		if (nodeJsonObject == null) {
			return;
		}
		this.setId(nodeJsonObject.get("id").toString());
		this.setCpuPower(nodeJsonObject.getDouble("cpuPower"));
		this.setRamCapacity(nodeJsonObject.getInt("ramCapacity"));
		this.setNumberOfActiveConnections(1);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getCpuPower() {
		return cpuPower;
	}

	public void setCpuPower(double cpuPower) {
		cpuPower = Math.round(cpuPower * 100) / 100;
		this.cpuPower = cpuPower;
	}

	public int getRamCapacity() {
		return ramCapacity;
	}

	public void setRamCapacity(int ramCapacity) {
		this.ramCapacity = ramCapacity;
	}

	public int getNumberOfActiveConnections() {
		return numberOfActiveConnections;
	}

	public void setNumberOfActiveConnections(int numberOfActiveConnections) {
		this.numberOfActiveConnections = numberOfActiveConnections;
	}

	public void addConnection() {
		// TODO load balance from here
		this.setNumberOfActiveConnections(this.getNumberOfActiveConnections() + 1);
	}

	public double getInitialWeight() {
		// double weight = Math.sqrt(Math.pow(this.getCpuPower(), 2) +
		// Math.pow(this.getRamCapacity(), 2));
		double weight = 100
				* (this.getCpuPower() * 0.5 / this.getCpuPower() + this.getRamCapacity() * 0.5 / this.getRamCapacity());
		// weight = Math.round(weight * 100) / 100;
		return weight;
	}

	// TODO
	public double getWeight1() {
		return getInitialWeight();
	}

	public double getRemainingWeight() {
		// double weight = Math.sqrt(Math.pow(this.getCpuPower(), 2) +
		// Math.pow(this.getRamCapacity(), 2));
		// double reaminingWeight =
		// (this.getRemainingCpuPower()/this.getCpuPower())+(this.getRemainingRamSpace()/this.getRamCapacity());
		double reaminingWeight = 100 * ((this.getRemainingCpuPower() * 0.5 / this.getCpuPower())
				+ (this.getRemainingRamSpace() * 0.5 / this.getRamCapacity()));
		// double reaminingWeight =
		// (this.getRemainingCpuPower()/7168)+(this.getRemainingRamSpace()/12384);
		// reaminingWeight = Math.round(reaminingWeight * 100) / 100;
		return reaminingWeight;
	}

	public double getOriginalWLC() {
		return this.getNumberOfActiveConnections() / this.getRemainingWeight();
	}

	public double getOptimizedWLC(Network n) {
		double wlc = 0.0;
		int sumOfNetworkConnections = 0;
		for (Node node : n.getNodes()) {
			sumOfNetworkConnections += node.getNumberOfActiveConnections();
		}
		//nodeActiveConnections/(remainingWeigth*sum(connections in network))
		wlc = this.getNumberOfActiveConnections()/(this.getRemainingWeight()*sumOfNetworkConnections);
		/*
		// sum of connection in network*weight
		networkWeight = sumOfNetworkConnections * this.getRemainingWeight();
		networkWeight = this.getNumberOfActiveConnections() / networkWeight;
		*/
		return wlc;
	}

	public double getOptimizedWLC(DataCenter dc) {
		double minWeight = -1.0;
		System.out.println("\n***Getting Wlc for Node:"  + this.getId() + "**");
		for (Network n : dc.allNetworks) {
			if (this.isInNetwork(n)) {
				double tmpMin = getOptimizedWLC(n);
				System.out.println("Min Weight:" + tmpMin);
				if (minWeight == -1.0) {
					minWeight = tmpMin;
				} else if (tmpMin < minWeight) {
					minWeight = tmpMin;
				}	
			}
		}
		return minWeight;
	}
	
	public boolean isInNetwork(Network network) {
		boolean inNetwork = false;
		if (network != null) {
			for(Node node : network.getNodes()) {
				if (node.getId().equalsIgnoreCase(this.getId())) {
					inNetwork = true;
					break;
				}
			}
		}
		return inNetwork;
	}

	/**
	 * Helper function
	 * 
	 * 
	 * @return
	 */
	public double getRemainingCpuPower() {
		return this.getCpuPower() - this.getNumberOfActiveConnections();
	}

	
	public double getRemainingRamSpace() {
		return this.getRamCapacity() - this.getNumberOfActiveConnections() * 2;
	}

	public String toString() {
		return "id: " + this.getId() + ", cpuPower: " + this.getCpuPower() + ", Number of Active Connections: "
				+ this.getNumberOfActiveConnections() + ", RAM Capacity: " + this.getRamCapacity() + ", Weight: "
				+ this.getInitialWeight() + ", remainingWeight: " + this.getRemainingWeight()
				+ ", Remaining CPU Power: " + this.getRemainingCpuPower() + ", RemainingRAMSpace:"
				+ this.getRemainingRamSpace() + ", Networkweight:";
	}
}