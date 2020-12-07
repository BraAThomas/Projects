This Document is a general "How to Run" with included references to certain papers that added to the latency calculation along with my Signature.

This program is written in C using gcc compiler:
gcc (Ubuntu 7.4.0-1ubuntu1~18.04.1) 7.4.0
Copyright (C) 2017 Free Software Foundation, Inc.
This is free software; see the source for copying conditions.  There is NO
warranty; not even for MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

Built on Visual Studio using its C/C++ extension: 
C/C++ v1.1.3 ms-vscode.cpptools.

To compile, I have been running the following command with all relivant files in the same directory:
cc   -pthread  TestsForLatency.c   -o TestsForLatency
And subsequent:
./TestsForLatency

You should only need this TestsForLatency.c file in a directory as all other files are created through the program. Following files may be created based on options chosen during program usage:
temperatureC.txt - File that stores 'temperature' Data as a random number 0-50 for each line of text. 
			Should be treated as Celsius temperatures taken by a sensor. 
temperatureF.txt - File created as a mirror to temperatureC, this is the taken 'celsius' numbers and converted 				to Fahrenheit.
temperatureC1/C2/C3/C4.txt - the same data from temperatureC just split by the number of 'devices' are on the 				network.
temperatureF1/F2/F3/F4.txt -  same date taken from C1-C4 converted to Fahrenheit based on number of 'devices'.
results.txt - Gives the results of the experiment based on: time it took to calculate the data + latency of 		travel either to a 'cloud center' or 'to the devices on the network'. Results given in microseconds to 		show the difference in the processing time as well as the latency.

When the program starts, it will ask either 2/3 questions. 
	1: How large of a data set.
		- For simplicity, this program gives on 5 choices: 1000, 2000, 5000, 7500, 10000
	2: Test with a Cloud center or a Manet network
	3: (If chosen Manet) Chose between 1-4 devices to run with.  

Program will append its results onto results.txt or create results.txt if not yet created. 

Refernces:
These references were utilized for their latency calculations in Cloud and Manet networks. 
	More precise information given in comments above each timing calculation line. 

-- For Manet Latency calculations using AODVUU-RM routing protocol.
Ahmed, Foez & Rahim, Muhammad. (2011). Performance Investigation on Two-Classes of Manet Routing Protocols Across Various Mobility Models With QoS Constraints. International Journal of Computer Networks & Communications. 3. 197-215. 10.5121/ijcnc.2011.3213. 

-- For Cloud center Latency (AWS latency)
https://www.cloudping.info/


Code Written By: Brandon Thomas, 4 December 2020
For the purpose of: UNR CPE600 taught by: Prof. Shamik Sengupta, Final Project