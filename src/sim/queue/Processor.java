package sim.queue;

/**                        
* Project: MemNet Simulation                                       
* Module ID: Processor
* JDK version used: <JDK1.7>                            
* Author: Charles Xu                        
* Create Date: 2016-03-11
* Version: 1.0             
* 
* Comments:  This class is a representation for each processor, it contains processors' attribute and actions
* 
*/ 

import java.util.HashMap;
import java.util.Map;



public class Processor {

  public Integer id;
  public Integer type;
  public Boolean isIdle;
  public Integer queueOfInterest;
  private double nextFinishingTime;
  
  public Processor(Integer id, Integer type, Integer queueOfInterest){
	  this.id = id;
	  this.type = type;
	  this.queueOfInterest = queueOfInterest;
	  this.nextFinishingTime = Double.MAX_VALUE;
  }

/** 
* FunName: getNextFinishingProcessor
* Description: This function compares and gets the nearest finishing Processor of specific type
* @param: myWaitingQueue	The waiting queue array
* @param: myProcessor			The processor array
* @param: numProcessor			The number of processor
* @param: type				The type of processor
* @return: map 				Map of string (id) and integer
*/ 
public static HashMap<String, Integer> getNextFinishingProcessor(WaitingQueue[] myWaitingQueue, Processor[] myProcessor, Integer numProcessor, Integer type) {
	// TODO Auto-generated method stub
	double tempTime = Double.MAX_VALUE; 
	Integer tempId = null;
	
	for(int i=0; i<numProcessor; i++){// check if there is previously idle agent
		if (myProcessor[i].type==type && myProcessor[i].nextFinishingTime == Double.MAX_VALUE){
			if (myWaitingQueue[myProcessor[i].queueOfInterest].getCurNum()==0) {continue; }
			tempId = myProcessor[i].id;
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			map.put("id", tempId);
			return map;
		}
	}	
	
	for(int i=0; i<numProcessor; i++){// or find the nearest finished agent
		if (myProcessor[i].type==type && myProcessor[i].nextFinishingTime <= tempTime){
			tempTime = myProcessor[i].nextFinishingTime;
			tempId = myProcessor[i].id;
		}
	}
	
	HashMap<String, Integer> map = new HashMap<String, Integer>();
	map.put("id", tempId);
	return map;
}

public double getNextfinishingTime() {
	// TODO Auto-generated method stub
	return this.nextFinishingTime;
}

public Integer getQueueOfInterest() {
	// TODO Auto-generated method stub
	return this.queueOfInterest;
}

public double serve() {
	// TODO Auto-generated method stub
	return 0;
}

public void setNextfinishingTime(double nextFinishingTime) {
	// TODO Auto-generated method stub
	this.nextFinishingTime = nextFinishingTime;
}

/** 
* FunName: getIdleProcessor
* Description: This function compares and gets the idle processor of specific type
* @param: myProcessor			The processor array
* @param: type				The type of processor
* @param: arrivingTime		The arriving time of specific job
* @return: id 				The id of the processor
*/ 
public static Integer getIdleProcessor(Processor[] myProcessor, Integer numProcessor, Integer type, Double arrivingTime) {
	// TODO Auto-generated method stub
	for (int i=0;i<numProcessor;i++){
		if (myProcessor[i].type==type && myProcessor[i].getNextfinishingTime()==Double.MAX_VALUE){//if there is an previously idle agent
			return i;
		}else{//or if the finishing time is ahead of the arriving time
			double nextFinishingTime = myProcessor[i].getNextfinishingTime();
			if (myProcessor[i].type==type && nextFinishingTime <= arrivingTime){
				return i;	
			}
		}
	}
	return null;//or there is no such processor
}



}