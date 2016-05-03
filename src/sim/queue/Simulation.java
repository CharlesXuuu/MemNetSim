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
		
		//QueueAdapter qa = new QueueAdapter(double y, double u, double y_updated);
		
		for (int i=0; i<10; i++){
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
		
		
		
		float avgTime = sm.calculateAvgProcessTime(SimulationOneRun.avgTimeType0, 1000*i);
		
		//System.out.println("avgTime="+avgTimeType0+"\n");
		SimulationOneRun.setAvgProcessTime(avgTime, SimulationOneRun.Type0);
		}
		
		
	}

}
