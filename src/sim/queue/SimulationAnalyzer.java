package sim.queue;


/**                        
* Project: Queueing Model Simulation                                       
* Module ID: Simulation Analyzer
* JDK version used: <JDK1.7>                            
* Author: Charles Xu                        
* Create Date: 2014-03-13
* Version: 1.0             
* 
* Comments:  This class analyze the result of the simulation process
*/ 
public class SimulationAnalyzer {

/** 
* FunName: analyzePessenger
* Description: This function analyze the passengers' information and output it
* @param: myPessenger			The passenger array
* @param: numPessenger			The number of passengers
*/ 
public static void analyzePessenger(Pessenger[] myPessenger, Integer numPessenger) {
	// TODO Auto-generated method stub
	float totalResponseTime=0;
	float meanResponseTime;
	for(int i=0; i<numPessenger; i++){
		totalResponseTime = totalResponseTime + (myPessenger[i].getLeavingTime() - myPessenger[i].getArrivingTime());
	}
	meanResponseTime = totalResponseTime / numPessenger;
	System.out.println("meanResponseTime = " + meanResponseTime + "\n");
}

}