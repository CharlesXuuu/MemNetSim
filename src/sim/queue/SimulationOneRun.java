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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;


public class SimulationOneRun {
	
	public File inputFile; // The random number input file
	public String filePath = "res/";
	public String fileName = "rdnumber.txt";
	
	public static final Integer CENTER2 = 0; //Define the first strategy
	public static final Integer DISTRIBUTE16 = 1; //Define the second strategy

	/**！ adjust ratio and number of processors*/
	
	public static final int Type0 = 0; //Define the type0 job, and their processing event  
	public static final int Type1 = 1;  //Define the type1 job, and their processing event
	public static final int ARRIVING = 2; //Define the arriving event
	public static double avgTimeType0 = 6; // Average time for processing a Type0 job
	public static double avgTimeType1 = 75; // Average time for processing a Type1 job
	public static final double ratio = 1; // ratio of Type0 Job
	
	

	// Simulation Parameters
	public Integer strategy = CENTER2;  //The default strategy
	public Integer arrivingPeriod =  20 * 60; //The arriving period
	public Integer numJob = 100; //The default job number
	public Integer numProcessor = 16; // The total processor number
	public Integer numType0Processor = 16;  // The type0 processor number
	public Integer numType1Processor = numProcessor - numType0Processor; // The type1 processor number
	public Integer numQueue; // The queue number
	public double avgTimeArriving = 15; //Average Interarrival time for Poisson distribution
	 
	public Job[] myJob;
	private WaitingQueue[] myWaitingQueue;
	private Processor[] myProcessor;

	private double coefficient_k= (Double)20.0; //113.24;
	private double coefficient_b= (Double)4.0;  //31.46;
	private double memory;
	
	public SimulationOneRun(double init_m) {
		this.memory = init_m;   //
	}

	/**
	 * FunName: calculateAvgProcessTime
	 * Description: This function adjust the memspace and return the updated avgTime
	 * @param avgTime 	the original avgTime
	 * @param memspace 	memnet space size
	 * @param k 		platform related coefficient k
	 * @param b			platform related coefficient b
	 * @return			updated avgTime
	 */
	public double calculateAvgProcessTime(double avgTime, double memspace){
		return this.coefficient_k * avgTime / memspace + this.coefficient_b ;
	}
	
	public static void setAvgProcessTime(double avgTime, int type){
		if (type==Type0){
			avgTimeType0 = (int)avgTime;
			System.out.println("avgTimeType0="+avgTimeType0);
		}
		
		if (type==Type1){
			avgTimeType1 = (int)avgTime;
		}
		
	}
	
	
	/** 
	* FunName: generateJob
	* Description: This function generate job from the random number file provided 
	*/ 
	void generateJob() {

		File file = new File(filePath, fileName);
		int randomNumber;
		if (file.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				myJob = new Job[numJob];
				Integer type;
				double arrivingTime=0;
				double servingLength;
				for (int i = 0; i < 7; i++) {
					System.out.println(br.readLine());
				}// handling the blank line

				for (int i = 0; i < numJob; i++) {
					// need to remove the blank space
					randomNumber = Integer.parseInt(br.readLine().replaceAll(
							"\\s", ""));

					// decide if the Job is type0 or type1
					if (randomNumber <= (10000*this.ratio)) {
						type = Type0;
					} else {
						type = Type1;
					}

					//set the uniform arriving time
					//arrivingTime = i
					//		* ((Double) arrivingPeriod / (Double) numJob); // uniform distribution
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

	
	private double getExpNext(int randomNumber, double alpha) {
		// TODO Auto-generated method stub
		double servingLength = (double) (-1 * alpha * Math.log((double)(randomNumber)/10000));
		return servingLength;
	}

	
	/** 
	* FunName: genServingLength
	* Description: This function using a random number to generate the serving length for jobs
	* @param: randomNumber		The required random number
	* @param: alpha				The average service length in exponential distribution
	* @return: servingLength	The serving time length for each job
	*/ 
	private double genServingLength(int randomNumber, int alpha) {
		// TODO Auto-generated method stub
		double servingLength = (double) (-1 * alpha * Math.log((double)(randomNumber)/10000));
		return servingLength;
	}

	/** 
	* FunName: genUniformServingLength
	* Description: This function using a random number to generate the uniform serving length for jobs
	* @param: randomNumber		The required random number
	* @param: maxServingLength	The maximum service length in uniform distribution 
	* @return: servingLength	The serving time length for each job
	*/ 
	private double genUniformServingLength(int randomNumber, double maxServingLength) {
		// TODO Auto-generated method stub
		double servingLength = (double) (maxServingLength * (double)(randomNumber)/10000);
		return servingLength;
	}
	
	
	/** 
	* FunName: setStrategy
	* Description: This function set the simulation strategy
	* @param: strategy		The strategy is either CENTER2 or DISTRIBUTED16 
	*/ 
	void setStrategy(Integer strategy) {

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
	void simulation() {
	  Integer curArriving = 0;  //current arriving job id 
	  Integer servedJob = 0; //number of jobs who have been served
	  while(true){		
		//ceaselessly looking for the next event	  
		//Categories:1. join queue, 2. finishing service
		double nextArrivingEventTime; // get the next arriving event time
		if (curArriving <numJob){
			nextArrivingEventTime = myJob[curArriving].getArrivingTime();}
		else{
			nextArrivingEventTime = Double.MAX_VALUE;// all job are arrived
		}
		
		double nextType0FinishServingEventTime;
		double nextType1FinishServingEventTime;
		Integer nextType0ProcessorId;
		Integer nextType1ProcessorId;
		
		if(numType0Processor>0){
		//get the id of the next finished type0 processor
		nextType0ProcessorId = Processor.getNextFinishingProcessor(myWaitingQueue, myProcessor, numProcessor, Type0).get("id");
		//get the finishing event time
		nextType0FinishServingEventTime =  myProcessor[nextType0ProcessorId].getNextfinishingTime(); 
		}else{
		nextType0ProcessorId = null;
		nextType0FinishServingEventTime = Double.MAX_VALUE; 	
		}
		
		if(numType1Processor>0){
		//get the id of the next finished type1 processor
		nextType1ProcessorId = Processor.getNextFinishingProcessor(myWaitingQueue, myProcessor, numProcessor, Type1).get("id");
		//get the finishing event time
		nextType1FinishServingEventTime =  myProcessor[nextType1ProcessorId].getNextfinishingTime();
		}else{
		nextType1ProcessorId = null;
		nextType1FinishServingEventTime = Double.MAX_VALUE;	
		}
		
		//compare and get the nearest event 
		int event = getNextEvent(nextArrivingEventTime, nextType0FinishServingEventTime, nextType1FinishServingEventTime);

        //doing the corresponding processing
		switch(event){
		case Type0:		
			Integer servedType0 = null;
			
			if (myWaitingQueue[myProcessor[nextType0ProcessorId].getQueueOfInterest()].getCurNum()!=0){
				// if this agent's corresponding queue is not empty, then pop the first Type0 and process it
				servedType0 = myWaitingQueue[myProcessor[nextType0ProcessorId].getQueueOfInterest()].pop();
				}
			
			if (servedType0==null){
				// if this processor's corresponding queue is empty, then the processor remains idle and finish the processing
				myProcessor[nextType0ProcessorId].setNextfinishingTime(Double.MAX_VALUE);
			break;
			}else{
				// update the state for both type0 job and processor
				double finalTime = myProcessor[nextType0ProcessorId].getNextfinishingTime() + myJob[servedType0].getServingLength();
				myJob[servedType0].setServingTime(myProcessor[nextType0ProcessorId].getNextfinishingTime());
				myJob[servedType0].setLeavingTime(finalTime);
				myJob[servedType0].setIsServed(true);
				myProcessor[nextType0ProcessorId].setNextfinishingTime(finalTime);
				servedJob += 1;
				break;
			}
			
		case Type1:
			Integer servedType1 = null;
			if (myWaitingQueue[myProcessor[nextType1ProcessorId].getQueueOfInterest()].getCurNum()!=0){
				// if this processor's corresponding queue is not empty, then pop the first type1 job and process it
				servedType1 = myWaitingQueue[myProcessor[nextType1ProcessorId].getQueueOfInterest()].pop();
			}
			if (servedType1==null){
				// if this agent's corresponding queue is empty, then the agent remains idle and finish the processing
				myProcessor[nextType1ProcessorId].setNextfinishingTime(Double.MAX_VALUE);
			break;
			}else{
				// update the state of both type1 job and processor
				double finalTime = myProcessor[nextType1ProcessorId].getNextfinishingTime() + myJob[servedType1].getServingLength();
				myJob[servedType1].setServingTime(myProcessor[nextType1ProcessorId].getNextfinishingTime());
				myJob[servedType1].setLeavingTime(finalTime);				
				myJob[servedType1].setIsServed(true);
				myProcessor[nextType1ProcessorId].setNextfinishingTime(finalTime);
				servedJob += 1;
				break;
			}
			
		case ARRIVING:
			//if there is an idle processor then find the idle processor
			Integer targetProcessor = Processor.getIdleProcessor(myProcessor, numProcessor, myJob[curArriving].getType(), myJob[curArriving].getArrivingTime());
			if (targetProcessor == null){
			//if not, find the shortest queue of its type
			Integer shortestQueueId = WaitingQueue.getShortestQueue(myWaitingQueue, numQueue, myJob[curArriving].getType());
			myWaitingQueue[shortestQueueId].add(curArriving);
			curArriving += 1;
			break;
			}else{//update the state of both job and processor	
				double finalTime = myJob[curArriving].getArrivingTime() + myJob[curArriving].getServingLength();
				//System.out.println("finalTime:"+finalTime);
				myJob[curArriving].setServingTime(myJob[curArriving].getArrivingTime());
				myJob[curArriving].setLeavingTime(finalTime);
				myJob[curArriving].setIsServed(true);
				myProcessor[targetProcessor].setNextfinishingTime(finalTime);
				servedJob += 1;
				curArriving += 1;
				break;
			}
		}
		//check if the simulation still goes on
		//System.out.println("servedJob = "+ servedJob +"\n");
		if (servedJob >= numJob){
			//Job.myprint(myJob, numJob);
			return;
		}
	  }
	  
  }

	/** 
	* FunName: getNextEvent
	* Description: This function compares and gets the nearest event
	* @param: nextArrivingEventTime				The nearest arriving event time
	* @param: nextType0ServingEventTime		The nearest Type0 Processor finishing event time
	* @param: nextType1ServingEventTime		The nearest Type1 Processor finishing event time
	* @return: Eventid							The nearest event id
	*/ 
	private int getNextEvent(double nextArrivingEventTime,
		double nextType0ServingEventTime, double nextType1ServingEventTime) {
	// if no more arriving event happens 	
	//System.out.println(""+nextArrivingEventTime+"\t"+nextType0ServingEventTime+"\t"+nextType1ServingEventTime+"\n");
	if (nextArrivingEventTime == Double.MAX_VALUE){
		//only compare the job finishing event
		if (nextType0ServingEventTime <= nextType1ServingEventTime){ 
			return Type0;}
		else{return Type1; }
	}
	
	//compare three kind of events
	if (nextArrivingEventTime <= nextType0ServingEventTime && nextArrivingEventTime<=nextType1ServingEventTime){
		return ARRIVING;
	}else{ 
		if (nextType0ServingEventTime <= nextType1ServingEventTime){ 
			return Type0;}
		else{return Type1; }
		}
	}

/*	
	private int getNextEvent(double nextArrivingEventTime,
			double nextType0ServingEventTime, double nextType1ServingEventTime) {
		HashMap h = new HashMap<Integer, Double>();
		h.put(ARRIVING,nextArrivingEventTime);
		h.put(Type0,nextType0ServingEventTime);
		h.put(Type1,nextType1ServingEventTime);
		Math.min(a, b)
		
	}
*/	
	

}