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

	private static final int CANADIAN = 0; //Define the type of Canadian passenger, and their processing event  
	private static final int VISITOR = 1;  //Define the type of Visitor, and their processing event
	private static final int ARRIVING = 2; //Define the arriving event
	private static final int lambdaCanadian = 40; // Average time for processing a Canadian passenger
	private static final int lambdaVisitor = 75; // Average time for processing a Visitor passenger

	private File inputFile; // The random number input file
	private String filePath = "res/";
	private String fileName = "rdnumber.txt";

	// Simulation Parameters
	private Integer strategy = CENTER2;  //The default strategy
	private Integer arrivingPeriod =  20 * 60; //The arriving period
	private Integer numPessenger = 700; //The default passenger number
	private Integer numAgent = 16; // The total agent number
	private Integer numCanadianAgent = 9;  // The Canadian agent number
	private Integer numVisitorAgent = numAgent - numCanadianAgent; // The visitor agent number
	private Integer numQueue; // The queue number
	
	 
	private Job[] myPessenger;
	private WaitingQueue[] myWaitingQueue;
	private Processor[] myAgent;

	
	/** 
	* FunName: generatePessenger
	* Description: This function generate passenger from the random number file provided in the course page  
	*/ 
	private void generatePessenger() {

		File file = new File(filePath, fileName);
		int randomNumber;
		if (file.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				myPessenger = new Job[numPessenger];
				Integer type;
				float landingTime;
				float servingLength;
				for (int i = 0; i < 7; i++) {
					System.out.println(br.readLine());
				}// handling the blank line

				for (int i = 0; i < numPessenger; i++) {
					// need to remove the blank space
					randomNumber = Integer.parseInt(br.readLine().replaceAll(
							"\\s", ""));

					// decide if the pessenger is canadian or visitor
					if (randomNumber <= 6500) {
						type = CANADIAN;
					} else {
						type = VISITOR;
					}

					//set the landing time
					landingTime = i
							* ((float) arrivingPeriod / (float) numPessenger);
					myPessenger[i] = new Job(i, type, landingTime);
				}
				for (int i=0; i<numPessenger; i++){
					// use another random number to decide the serving length  
					randomNumber = Integer.parseInt(br.readLine().replaceAll(
							"\\s", ""));
					if (myPessenger[i].getType() == CANADIAN){
					servingLength = genServingLength(randomNumber, lambdaCanadian);
					myPessenger[i].setServingLength(servingLength);
					} else if (myPessenger[i].getType() == VISITOR){
					servingLength = genServingLength(randomNumber, lambdaVisitor);
					myPessenger[i].setServingLength(servingLength);
					}
				}

				br.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/** 
	* FunName: genServingLength
	* Description: This function using a randomnumber to generate the serving length for passengers
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
			myWaitingQueue[0] = new WaitingQueue(0, CANADIAN);
			myWaitingQueue[1] = new WaitingQueue(1, VISITOR);
			// setting agent 
			myAgent = new Processor[numAgent];

			for (int i = 0; i < numAgent; i++) {
				if (i < numCanadianAgent) {
					myAgent[i] = new Processor(i, CANADIAN, 0);
				} else {
					myAgent[i] = new Processor(i, VISITOR, 1);
				}
			}
		}
		
		// the second strategy
		if (strategy == DISTRIBUTE16) {
			numQueue = numAgent;
			myWaitingQueue = new WaitingQueue[numQueue];
			myAgent = new Processor[numAgent];
			//setting queue and agent
			for (int i = 0; i < numAgent; i++) {
				if (i < numCanadianAgent) {
					myAgent[i] = new Processor(i, CANADIAN, i);
					myWaitingQueue[i] = new WaitingQueue(i, CANADIAN);
				} else {
					myAgent[i] = new Processor(i, VISITOR, i);
					myWaitingQueue[i] = new WaitingQueue(i, VISITOR);
				}
			}
		}

	}
	
	
	/** 
	* FunName: simulation
	* Description: This function simulate the whole process, it monitors the discrete event and perform the corresponding actions on these events
	*/ 
	private void simulation() {
	  Integer curArriving = 0;  //current arriving passenger id 
	  Integer servedPessenger = 0; //number of passengers who have been served
	  while(true){		
		//ceaselessly looking for the next event	  
		//Categories:1. join queue, 2. finishing service
		float nextArrivingEventTime; // get the next arriving event time
		if (curArriving <numPessenger){
			nextArrivingEventTime = myPessenger[curArriving].getArrivingTime();}
		else{
			nextArrivingEventTime = Float.MAX_VALUE;// all passenger are arrived
		}
		
		//get the id of the next finished Canadian agent 
		Integer nextCanadianAgentId = Processor.getNextFinishingProcessor(myWaitingQueue, myAgent, numAgent, CANADIAN).get("id");
		//get the finishing event time
		float nextCanadianServingEventTime =  myAgent[nextCanadianAgentId].getNextfinishingTime(); 
		//get the id of the next finished Visitor agent
		Integer nextVisitorAgentId = Processor.getNextFinishingProcessor(myWaitingQueue, myAgent, numAgent, VISITOR).get("id");
		//get the finishing event time
		float nextVisitorServingEventTime =  myAgent[nextVisitorAgentId].getNextfinishingTime();
        
		//compare and get the nearest event 
		int event = getNextEvent(nextArrivingEventTime, nextCanadianServingEventTime, nextVisitorServingEventTime);

        //doing the corresponding processing
		switch(event){
		case CANADIAN:		
			Integer servedCanadian = null;
			
			if (myWaitingQueue[myAgent[nextCanadianAgentId].getQueueOfInterest()].getCurNum()!=0){
				// if this agent's corresponding queue is not empty, then pop the first Canadian passenger and process it
				servedCanadian = myWaitingQueue[myAgent[nextCanadianAgentId].getQueueOfInterest()].pop();
				}
			
			if (servedCanadian==null){
				// if this agent's corresponding queue is empty, then the agent remains idle and finish the processing
				myAgent[nextCanadianAgentId].setNextfinishingTime(Float.MAX_VALUE);
			break;
			}else{
				// update the state for both agent and passenger
				float finalTime = myAgent[nextCanadianAgentId].getNextfinishingTime() + myPessenger[servedCanadian].getServingLength();
				myPessenger[servedCanadian].setServingTime(myAgent[nextCanadianAgentId].getNextfinishingTime());
				myPessenger[servedCanadian].setLeavingTime(finalTime);
				myPessenger[servedCanadian].setIsServed(true);
				myAgent[nextCanadianAgentId].setNextfinishingTime(finalTime);
				servedPessenger += 1;
				break;
			}
			
		case VISITOR:
			Integer servedVisitor = null;
			if (myWaitingQueue[myAgent[nextVisitorAgentId].getQueueOfInterest()].getCurNum()!=0){
				// if this agent's corresponding queue is not empty, then pop the first visitor passenger and process it
				servedVisitor = myWaitingQueue[myAgent[nextVisitorAgentId].getQueueOfInterest()].pop();
			}
			if (servedVisitor==null){
				// if this agent's corresponding queue is empty, then the agent remains idle and finish the processing
				myAgent[nextVisitorAgentId].setNextfinishingTime(Float.MAX_VALUE);
			break;
			}else{
				// update the state of both agent and passenger
				float finalTime = myAgent[nextVisitorAgentId].getNextfinishingTime() + myPessenger[servedVisitor].getServingLength();
				myPessenger[servedVisitor].setServingTime(myAgent[nextVisitorAgentId].getNextfinishingTime());
				myPessenger[servedVisitor].setLeavingTime(finalTime);				
				myPessenger[servedVisitor].setIsServed(true);
				myAgent[nextVisitorAgentId].setNextfinishingTime(finalTime);
				servedPessenger += 1;
				break;
			}
			
		case ARRIVING:
			//if there is an idle agent then find the idle agent
			Integer targetAgent = Processor.getIdleProcessor(myAgent, numAgent, myPessenger[curArriving].getType(), myPessenger[curArriving].getArrivingTime());
			if (targetAgent == null){
			//if not, find the shortest queue of its type
			Integer shortestQueueId = WaitingQueue.getShortestQueue(myWaitingQueue, numQueue, myPessenger[curArriving].getType());
			myWaitingQueue[shortestQueueId].add(curArriving);
			curArriving += 1;
			break;
			}else{//update the state of both agent and passenger	
				float finalTime = myPessenger[curArriving].getArrivingTime() + myPessenger[curArriving].getServingLength();
				System.out.println("finalTime:"+finalTime);
				myPessenger[curArriving].setServingTime(myPessenger[curArriving].getArrivingTime());
				myPessenger[curArriving].setLeavingTime(finalTime);
				myPessenger[curArriving].setIsServed(true);
				myAgent[targetAgent].setNextfinishingTime(finalTime);
				servedPessenger += 1;
				curArriving += 1;
				break;
			}
		}
		//check if the simulation still goes on
		//System.out.println("servedPessenger = "+ servedPessenger +"\n");
		if (servedPessenger >= numPessenger){
			Job.myprint(myPessenger, numPessenger);
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
			return CANADIAN;}
		else{return VISITOR; }
	}
	
	//compare three kind of events
	if (nextArrivingEventTime <= nextCanadianServingEventTime && nextArrivingEventTime<=nextVisitorServingEventTime){
		return ARRIVING;
	}else{ 
		if (nextCanadianServingEventTime <= nextVisitorServingEventTime){ 
			return CANADIAN;}
		else{return VISITOR; }
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
		sm.generatePessenger();
		// set strategy
		//sm.setStrategy(DISTRIBUTE16);
		sm.setStrategy(CENTER2);
		// doing simulation
		sm.simulation();
		System.out.println("numCandianAgent = "+ sm.numCanadianAgent +"\t" + "numVisitorAgent = " + sm.numVisitorAgent+ "\n");
		// output the result
		SimulationAnalyzer.analyzeJob(sm.myPessenger, sm.numPessenger);

	}

}