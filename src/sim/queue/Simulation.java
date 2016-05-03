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
		
		double ref_d = 10;   //10s response
		//last time output deltad
		double init_m = 4;   //4M RAM
		
		double delta_d=1;
		//last time input deltam
		double delta_m=1;
		//this time output deltad
		double delta_d_new=1;
		
		QueueAdapter qa = new QueueAdapter(delta_d, delta_m, delta_d_new);
		
		for (int i=0; i<10; i++){
			
		SimulationOneRun sm = new SimulationOneRun(init_m);
		// generate passenger
		sm.generateJob();
		// set strategy
		//sm.setStrategy(DISTRIBUTE16);
		sm.setStrategy(sm.CENTER2);
		// doing simulation
		sm.simulation();
		System.out.println("numType0Processor = "+ sm.numType0Processor +"\t" + "numType1Processor = " + sm.numType1Processor+ "\n");
		// output the result
		double meanResponseTime = SimulationAnalyzer.analyzeJob(sm.myJob, sm.numJob);
		
		
		
		double avgTime = sm.calculateAvgProcessTime(SimulationOneRun.avgTimeType0, 1000*i);
		
		//System.out.println("avgTime="+avgTimeType0+"\n");
		SimulationOneRun.setAvgProcessTime(avgTime, SimulationOneRun.Type0);
		}
		
		
	}

}
