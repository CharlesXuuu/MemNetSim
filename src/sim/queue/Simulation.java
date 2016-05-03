/**                        
* Project: MemNetSim                                   
* Module ID: sim.queue
* JDK version used: <JDK1.7>                            
* Author: chix                        
* Create Date: 2016-05-02
* Version:             
* 
* Comments:  
*
*/ 
package sim.queue;


public class Simulation {

	/** 
	* FunName: main
	* Description: Main function 
	* @param: command line parameter			
	*/ 
	public static void main(String argv[]) {
		SimulationOneRun sm = new SimulationOneRun();
		// generate passenger
		sm.generateJob();
		// set strategy
		//sm.setStrategy(DISTRIBUTE16);
		sm.setStrategy(sm.CENTER2);
		// doing simulation
		sm.simulation();
		System.out.println("numCandianAgent = "+ sm.numType0Processor +"\t" + "numVisitorAgent = " + sm.numType1Processor+ "\n");
		// output the result
		float meanResponseTime = SimulationAnalyzer.analyzeJob(sm.myJob, sm.numJob);
		
		sm = new SimulationOneRun();
		// generate passenger
		sm.generateJob();
		// set strategy
		//sm.setStrategy(DISTRIBUTE16);
		sm.setStrategy(sm.CENTER2);
		sm.setAvgProcessTime(SimulationOneRun.avgTimeType0, 22);
		// doing simulation
		sm.simulation();
		System.out.println("numCandianAgent = "+ sm.numType0Processor +"\t" + "numVisitorAgent = " + sm.numType1Processor+ "\n");
		// output the result
		meanResponseTime = SimulationAnalyzer.analyzeJob(sm.myJob, sm.numJob);
		
		
		
	}

}
