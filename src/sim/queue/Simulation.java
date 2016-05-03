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
		
		double memory = init_m;
		
		double delta_d_old=0;
		//last time input deltam
		double delta_m=0;
		//this time output deltad
		//double delta_d_new=0;
		
		
		
		for (int i=0; i<100; i++){
		memory+=delta_m;
		SimulationOneRun sm = new SimulationOneRun(memory);
		// generate passenger
		sm.generateJob();
		// set strategy
		//sm.setStrategy(DISTRIBUTE16);
		sm.setStrategy(sm.CENTER2);
		// doing simulation
		sm.simulation();
		//System.out.println("numType0Processor = "+ sm.numType0Processor +"\t" + "numType1Processor = " + sm.numType1Processor+ "\n");
		// output the result
		double meanResponseTime = SimulationAnalyzer.analyzeJob(sm.myJob, sm.numJob);
		
		QueueAdapter qa = new QueueAdapter(delta_d_old, delta_m, ref_d-meanResponseTime);
		delta_m = qa.adjustFeedback();
		
		System.out.println("meanResponseTime="+meanResponseTime);
		delta_d_old  = ref_d-meanResponseTime;
		}
		
		
	}

}
