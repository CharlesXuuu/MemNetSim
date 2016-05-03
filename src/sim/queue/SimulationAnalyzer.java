package sim.queue;


/**                        
* Project: MemNet Simulation                                       
* Module ID: Simulation Analyzer
* JDK version used: <JDK1.7>                            
* Author: Charles Xu                        
* Create Date: 2016-03-13
* Version: 1.0             
* 
* Comments:  This class analyze the result of the simulation process
*/ 
public class SimulationAnalyzer {

/** 
* FunName: analyzeJob
* Description: This function analyze the jobs' information and output it
* @param: myPessenger			The job array
* @param: numPessenger			The number of jobs
*/ 
public static double analyzeJob(Job[] myJob, Integer numJob) {
	// TODO Auto-generated method stub
	double totalResponseTime=0;
	double meanResponseTime;
	for(int i=0; i<numJob; i++){
		totalResponseTime = totalResponseTime + (myJob[i].getLeavingTime() - myJob[i].getArrivingTime());
	}
	meanResponseTime = totalResponseTime / numJob;
	System.out.println("meanResponseTime = " + meanResponseTime + "\n");
	return meanResponseTime;
}

}