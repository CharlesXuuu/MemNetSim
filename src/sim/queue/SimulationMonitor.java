package sim.queue;

/**                        
* Project: Queueing Model Simulation                                       
* Module ID: Simulation Monitor
* JDK version used: <JDK1.7>                            
* Author: Charles Xu                        
* Create Date: 2016-03-12
* Version: 1.0             
* 
* Comments:  This class performs the whole simulation process, it is designed using event-driven strategy, 
* and it contains the main functions, the process is mainly in four steps:
* 
* 1. Generating the processor, the queue and the job
* 2. Setting the simulation strategy, is either 2-queue strategy or 16-queue strategy.
* 3. Begin simulation, monitor the discrete event, currently there are three events: (1). Arriving Event (2). Type0 job finishing Event
* 		(3). Type1 job finishing event. All events will be handled in different ways.
*                                             
* The simulation class has high adaptivity to simulate different number of processors, jobs and queues. 
*/ 


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;


public class SimulationMonitor {

	private static final Integer CENTER2 = 0; //Define the first strategy
	private static final Integer DISTRIBUTE16 = 1; //Define the second strategy

	private static final int Type0 = 0; //Define the type0 job, and their processing event  
	private static final int Type1 = 1;  //Define the type1 job, and their processing event
	private static final int ARRIVING = 2; //Define the arriving event
	private static final int avgTimeType0 = 40; // Average time for processing a Type0 job
	private static final int avgTimeType1 = 75; // Average time for processing a Type1 job

	private File inputFile; // The random number input file
	private String filePath = "res/";
	private String fileName = "rdnumber.txt";

	// Simulation Parameters
	private Integer strategy = CENTER2;  //The default strategy
	private Integer arrivingPeriod =  20 * 60; //The arriving period
	private Integer numJob = 100; //The default job number
	private Integer numProcessor = 16; // The total processor number
	private Integer numType0Processor = 9;  // The type0 processor number
	private Integer numType1Processor = numProcessor - numType0Processor; // The type1 processor number
	private Integer numQueue; // The queue number
	private Integer avgTimeArriving = 80; //Average Interarrival time for Poisson distribution
	 
	private Job[] myJob;
	private WaitingQueue[] myWaitingQueue;
	private Processor[] myProcessor;



	
	/** 
	* FunName: generatePessenger
	* Description: This function generate passenger from the random number file provided in the course page  
	*/ 
	private void generateJob() {

		File file = new File(filePath, fileName);
		int randomNumber;
		if (file.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				myJob = new Job[numJob];
				Integer type;
				float arrivingTime=0;
				float servingLength;
				for (int i = 0; i < 7; i++) {
					System.out.println(br.readLine());
				}// handling the blank line

				for (int i = 0; i < numJob; i++) {
					// need to remove the blank space
					randomNumber = Integer.parseInt(br.readLine().replaceAll(
							"\\s", ""));

					// decide if the Job is type0 or type1
					if (randomNumber <= 6500) {
						type = Type0;
					} else {
						type = Type1;
					}

					//set the uniform arriving time
					//arrivingTime = i
					//		* ((float) arrivingPeriod / (float) numJob); // uniform distribution
					//myJob[i] = new Job(i, type, servingLength);
					
					//set the poisson arriving time			
					
					myJob[i] = new Job(i, type);
				}
				
				for (int i = 0; i < numJob; i++) {
					// need to remove the blank space
					randomNumber = Integer.parseInt(br.readLine().replaceAll(
							"\\s", ""));
					
					//set the poisson arriving time			
					arrivingTime = arrivingTime + getExpNext(randomNumber,avgTimeArriving);
					myJob[i].setArrivingTime(arrivingTime);
				}
				
				
				for (int i=0; i<numJob; i++){
					// use another random number to decide the serving length  
					randomNumber = Integer.parseInt(br.readLine().replaceAll(
							"\\s", ""));
					if (myJob[i].getType() == Type0){
					servingLength = genUniformServingLength(randomNumber, avgTimeType0);
					myJob[i].setServingLength(servingLength);
					} else if (myJob[i].getType() == Type1){
					servingLength = genUniformServingLength(randomNumber, avgTimeType1);
					myJob[i].setServingLength(servingLength);
					}
				}

				br.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	
	private float getExpNext(int randomNumber, int alpha) {
		// TODO Auto-generated method stub
		float servingLength = (float) (-1 * alpha * Math.log((float)(randomNumber)/10000));
		return servingLength;
	}

	
	/** 
	* FunName: genServingLength
	* Description: This function using a random number to generate the serving length for passengers
	* @param: randomNumber		The required random number
	* @param: alpha				The average service length in exponential distribution
	* @return: servingLength	The serving time length for each passenger
	*/ 
	private float genServingLength(int randomNumber, int alpha) {
		// TODO Auto-generated method stub
		float servingLength = (float) (-1 * alpha * Math.log((float)(randomNumber)/10000));
		return servingLength;
	}

	/** 
	* FunName: genUniformServingLength
	* Description: This function using a random number to generate the uniform serving length for jobs
	* @param: randomNumber		The required random number
	* @param: maxServingLength	The maximum service length in uniform distribution 
	* @return: servingLength	The serving time length for each job
	*/ 
	private float genUniformServingLength(int randomNumber, int maxServingLength) {
		// TODO Auto-generated method stub
		float servingLength = (float) (maxServingLength * (float)(randomNumber)/10000);
		return servingLength;
	}
	
	
	/** 
	* FunName: setStrategy
	* Description: This function set the simulation strategy
	* @param: strategy		The strategy is either CENTER2 or DISTRIBUTED16 
	*/ 
	private void setStrategy(Integer strategy) {

		// the first strategy
		if (strategy == CENTER2) {
			numQueue = 2;
			// setting queue
			myWaitingQueue = new WaitingQueue[numQueue];
			myWaitingQueue[0] = new WaitingQueue(0, Type0);
			myWaitingQueue[1] = new WaitingQueue(1, Type1);
			// setting agent 
			myProcessor = new Processor[numProcessor];

			for (int i = 0; i < numProcessor; i++) {
				if (i < numType0Processor) {
					myProcessor[i] = new Processor(i, Type0, 0);
				} else {
					myProcessor[i] = new Processor(i, Type1, 1);
				}
			}
		}
		
		// the second strategy
		if (strategy == DISTRIBUTE16) {
			numQueue = numProcessor;
			myWaitingQueue = new WaitingQueue[numQueue];
			myProcessor = new Processor[numProcessor];
			//setting queue and agent
			for (int i = 0; i < numProcessor; i++) {
				if (i < numType0Processor) {
					myProcessor[i] = new Processor(i, Type0, i);
					myWaitingQueue[i] = new WaitingQueue(i, Type0);
				} else {
					myProcessor[i] = new Processor(i, Type1, i);
					myWaitingQueue[i] = new WaitingQueue(i, Type1);
				}
			}
		}

	}
	
	
	/** 
	* FunName: simulation
	* Description: This function simulates the whole process, it monitors the discrete event and perform the corresponding actions on these events
	*/ 
	private void simulation() {
	  Integer curArriving = 0;  //current arriving passenger id 
	  Integer servedPessenger = 0; //number of passengers who have been served
	  while(true){		
		//ceaselessly looking for the next event	  
		//Categories:1. join queue, 2. finishing service
		float nextArrivingEventTime; // get the next arriving event time
		if (curArriving <numJob){
			nextArrivingEventTime = myJob[curArriving].getArrivingTime();}
		else{
			nextArrivingEventTime = Float.MAX_VALUE;// all passenger are arrived
		}
		
		//get the id of the next finished type0 
		Integer nextType0ProcessorId = Processor.getNextFinishingProcessor(myWaitingQueue, myProcessor, numProcessor, Type0).get("id");
		//get the finishing event time
		float nextType0FinishServingEventTime =  myProcessor[nextType0ProcessorId].getNextfinishingTime(); 
		//get the id of the next finished Visitor agent
		Integer nextType1ProcessorId = Processor.getNextFinishingProcessor(myWaitingQueue, myProcessor, numProcessor, Type1).get("id");
		//get the finishing event time
		float nextType1FinishServingEventTime =  myProcessor[nextType1ProcessorId].getNextfinishingTime();
        
		//compare and get the nearest event 
		int event = getNextEvent(nextArrivingEventTime, nextType0FinishServingEventTime, nextType1FinishServingEventTime);

        //doing the corresponding processing
		switch(event){
		case Type0:		
			Integer servedCanadian = null;
			
			if (myWaitingQueue[myProcessor[nextType0ProcessorId].getQueueOfInterest()].getCurNum()!=0){
				// if this agent's corresponding queue is not empty, then pop the first Canadian passenger and process it
				servedCanadian = myWaitingQueue[myProcessor[nextType0ProcessorId].getQueueOfInterest()].pop();
				}
			
			if (servedCanadian==null){
				// if this agent's corresponding queue is empty, then the agent remains idle and finish the processing
				myProcessor[nextType0ProcessorId].setNextfinishingTime(Float.MAX_VALUE);
			break;
			}else{
				// update the state for both agent and passenger
				float finalTime = myProcessor[nextType0ProcessorId].getNextfinishingTime() + myJob[servedCanadian].getServingLength();
				myJob[servedCanadian].setServingTime(myProcessor[nextType0ProcessorId].getNextfinishingTime());
				myJob[servedCanadian].setLeavingTime(finalTime);
				myJob[servedCanadian].setIsServed(true);
				myProcessor[nextType0ProcessorId].setNextfinishingTime(finalTime);
				servedPessenger += 1;
				break;
			}
			
		case Type1:
			Integer servedVisitor = null;
			if (myWaitingQueue[myProcessor[nextType1ProcessorId].getQueueOfInterest()].getCurNum()!=0){
				// if this agent's corresponding queue is not empty, then pop the first visitor passenger and process it
				servedVisitor = myWaitingQueue[myProcessor[nextType1ProcessorId].getQueueOfInterest()].pop();
			}
			if (servedVisitor==null){
				// if this agent's corresponding queue is empty, then the agent remains idle and finish the processing
				myProcessor[nextType1ProcessorId].setNextfinishingTime(Float.MAX_VALUE);
			break;
			}else{
				// update the state of both agent and passenger
				float finalTime = myProcessor[nextType1ProcessorId].getNextfinishingTime() + myJob[servedVisitor].getServingLength();
				myJob[servedVisitor].setServingTime(myProcessor[nextType1ProcessorId].getNextfinishingTime());
				myJob[servedVisitor].setLeavingTime(finalTime);				
				myJob[servedVisitor].setIsServed(true);
				myProcessor[nextType1ProcessorId].setNextfinishingTime(finalTime);
				servedPessenger += 1;
				break;
			}
			
		case ARRIVING:
			//if there is an idle agent then find the idle agent
			Integer targetAgent = Processor.getIdleProcessor(myProcessor, numProcessor, myJob[curArriving].getType(), myJob[curArriving].getArrivingTime());
			if (targetAgent == null){
			//if not, find the shortest queue of its type
			Integer shortestQueueId = WaitingQueue.getShortestQueue(myWaitingQueue, numQueue, myJob[curArriving].getType());
			myWaitingQueue[shortestQueueId].add(curArriving);
			curArriving += 1;
			break;
			}else{//update the state of both agent and passenger	
				float finalTime = myJob[curArriving].getArrivingTime() + myJob[curArriving].getServingLength();
				System.out.println("finalTime:"+finalTime);
				myJob[curArriving].setServingTime(myJob[curArriving].getArrivingTime());
				myJob[curArriving].setLeavingTime(finalTime);
				myJob[curArriving].setIsServed(true);
				myProcessor[targetAgent].setNextfinishingTime(finalTime);
				servedPessenger += 1;
				curArriving += 1;
				break;
			}
		}
		//check if the simulation still goes on
		//System.out.println("servedPessenger = "+ servedPessenger +"\n");
		if (servedPessenger >= numJob){
			Job.myprint(myJob, numJob);
			return;
		}
	  }
	  
  }

	/** 
	* FunName: getNextEvent
	* Description: This function compares and gets the nearest event
	* @param: nextArrivingEventTime				The nearest arriving event time
	* @param: nextCanadianServingEventTime		The nearest Canadian agent finishing event time
	* @param: nextVisitorServingEventTime		The nearest Visitor agent finishing event time
	* @return: Eventid							The nearest event id
	*/ 
	private int getNextEvent(float nextArrivingEventTime,
		float nextCanadianServingEventTime, float nextVisitorServingEventTime) {
	
	// if no more arriving event happens 	
	if (nextArrivingEventTime == Float.MAX_VALUE){
		//only compare the agent finishing event
		if (nextCanadianServingEventTime <= nextVisitorServingEventTime){ 
			return Type0;}
		else{return Type1; }
	}
	
	//compare three kind of events
	if (nextArrivingEventTime <= nextCanadianServingEventTime && nextArrivingEventTime<=nextVisitorServingEventTime){
		return ARRIVING;
	}else{ 
		if (nextCanadianServingEventTime <= nextVisitorServingEventTime){ 
			return Type0;}
		else{return Type1; }
		}
}
	/** 
	* FunName: main
	* Description: Main function 
	* @param: command line parameter			
	*/ 
	public static void main(String argv[]) {
		SimulationMonitor sm = new SimulationMonitor();
		// generate passenger
		sm.generateJob();
		// set strategy
		//sm.setStrategy(DISTRIBUTE16);
		sm.setStrategy(CENTER2);
		// doing simulation
		sm.simulation();
		System.out.println("numCandianAgent = "+ sm.numType0Processor +"\t" + "numVisitorAgent = " + sm.numType1Processor+ "\n");
		// output the result
		SimulationAnalyzer.analyzeJob(sm.myJob, sm.numJob);

	}

}