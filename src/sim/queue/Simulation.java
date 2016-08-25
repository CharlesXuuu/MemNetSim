/**                        
* Project: MemNetSim                                   
* Module ID: sim.queue
* JDK version used: <JDK1.7>                            
* Author: chix                        
* Create Date: 2016-05-02
* Version:             
* 
* Comments:  Simulation main function
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
		
		// Reference delay
		double ref_d = 10;   //10s response
		// Initial RAM
		double init_m = 4;   //4M RAM
		
		// RAM space
		double memory = init_m;
		
		//last time output delta_d
		double delta_d_old=0;
		
		//last time input delta_m
		double delta_m=0;

		//this time output deltad
		//double delta_d_new=0;
		
		
		for (int i=0; i<100; i++){
			
		memory+=delta_m;
		
		//Under this memory setting run tasks one time
		SimulationOneRun sm = new SimulationOneRun(memory);
		
		// Generate Job
		sm.generateJob();
		// Set one queue
		sm.setStrategy(sm.CENTER2);
		// doing simulation
		sm.simulation();
		
		
		//System.out.println("numType0Processor = "+ sm.numType0Processor +"\t" + "numType1Processor = " + sm.numType1Processor+ "\n");
		
		// output the result
		double meanResponseTime = SimulationAnalyzer.analyzeJob(sm.myJob, sm.numJob);
		
		// Calculate the result
		QueueAdapter qa = new QueueAdapter(delta_d_old, delta_m, ref_d-meanResponseTime);
		delta_m = qa.adjustFeedback();
		
		System.out.print(""+meanResponseTime+"\t");
		delta_d_old  = ref_d-meanResponseTime;
		}
		

		
		
	}

}
