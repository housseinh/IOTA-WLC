Project Description

........................................

The project aims to achieve load balance among different decentralized nodes within different network scenarios. It is a simulation that tackles the distribution of the computing tasks of IOTA among its nodes. The code is based on JAVA platform composed of 4 main classes and JSON files. The main goal of this simulation is to test the effectiveness of the Weighted Least connection algorithm (WLC) within the decentralized environment of IOTA. Therefore, the tasks that reach nodes randomly will be distributed equally on all the nodes.

Technical Description

.......................................

The simulation code is composed of several classes and JSON files. It is intended to:

• Create two types of network to build different scenarios:

     o Create 16 similar nodes using a JSON file that specifies their name and resources (same CPU and memory)
     
     o Create 16 different nodes using a JSON file that specifies their name, different resources, and network.
     
• Create multiple networks and regroup the 16 nodes with different resources using another JSON file to represent three network groups that interacted with each other. 

• Test different levels of connection traffic (up to 11k connections).

• Extract CSV output files in order to analyze and draw the results.




Implementation and test

.......................................

This project is built using Eclipse IDE 2021. Below are the steps to run the software:

1-	Install JAVA Eclipse IDE on your machine

2-	Create a workspace and import the project files

3-	Once imported, the project is ready to create two different data centers. Each one is composed of a set of nodes and networks. 

4-	The normal datacenter tests the WLC within one network only. 

5-	The optimized datacenter considers three networks set that includes some nodes in common.

6-	The number of connections is set to 1000. It can be changed to another value. Once the code is executed, the outputs will be generated on the left side in ‘out’ folder.

7-	The main result is generated  under ‘all_loops.csv’ name. This file includes the results of the WLC algorithm for different nodes (different CPU and RAM) and located in multiple networks.

8-	To test similar node resources, go to Network similar nodes.json, copy the entire contact. Then browse the using notepad and replace. Rename the  network.json by network-old.json and rename ‘Network similar nodes’ by network.json.

9-	The networks can be changed for further testing in the array network of the JSON file. You can add: remove node(s) and network(s).

10-	 The generated reports are in CSV format. You can access it using excel or Matlab. Note: the delimiter used is the comma, so ensure your excel is using ‘comma’ before import it.

11-	Finally, you can find  CSV and excel demos of the last run in the same file.

 




