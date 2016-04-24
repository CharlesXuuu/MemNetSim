package sim.queue;

/**                        
* Project: Queueing Model Simulation                                       
* Module ID: Agent
* JDK version used: <JDK1.7>                            
* Author: Charles Xu                        
* Create Date: 2014-03-11
* Version: 1.0             
* 
* Comments:  This class is a representation for each agent, it contains agents' attribute and actions
* 
*/ 

import java.util.HashMap;
import java.util.Map;



public class Agent {

  public Integer id;
  public Integer type;
  public Boolean isIdle;
  public Integer queueOfInterest;
  private float nextFinishingTime;
  
  public Agent(Integer id, Integer type, Integer queueOfInterest){
	  this.id = id;
	  this.type = type;
	  this.queueOfInterest = queueOfInterest;
	  this.nextFinishingTime = Float.MAX_VALUE;
  }

/** 
* FunName: getNextFinishingAgent
* Description: This function compares and gets the nearest finishing agent of specific type
* @param: myWaitingQueue	The waiting queue array
* @param: myAgent			The agent array
* @param: numAgent			The number of agent
* @param: type				The type of agent
* @return: map 				Map of string (id) and integer
*/ 
public static HashMap<String, Integer> getNextFinishingAgent(WaitingQueue[] myWaitingQueue, Agent[] myAgent, Integer numAgent, Integer type) {
	// TODO Auto-generated method stub
	float tempTime = Float.MAX_VALUE; 
	Integer tempId = null;
	
	for(int i=0; i<numAgent; i++){// check if there is previously idle agent
		if (myAgent[i].type==type && myAgent[i].nextFinishingTime == Float.MAX_VALUE){
			if (myWaitingQueue[myAgent[i].queueOfInterest].getCurNum()==0) {continue; }
			tempId = myAgent[i].id;
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			map.put("id", tempId);
			return map;
		}
	}	
	
	for(int i=0; i<numAgent; i++){// or find the nearest finished agent
		if (myAgent[i].type==type && myAgent[i].nextFinishingTime <= tempTime){
			tempTime = myAgent[i].nextFinishingTime;
			tempId = myAgent[i].id;
		}
	}
	
	HashMap<String, Integer> map = new HashMap<String, Integer>();
	map.put("id", tempId);
	return map;
}

public float getNextfinishingTime() {
	// TODO Auto-generated method stub
	return this.nextFinishingTime;
}

public Integer getQueueOfInterest() {
	// TODO Auto-generated method stub
	return this.queueOfInterest;
}

public float serve() {
	// TODO Auto-generated method stub
	return 0;
}

public void setNextfinishingTime(float nextFinishingTime) {
	// TODO Auto-generated method stub
	this.nextFinishingTime = nextFinishingTime;
}

/** 
* FunName: getIdleAgent
* Description: This function compares and gets the idle agent of specific type
* @param: myAgent			The agent array
* @param: type				The type of agent
* @param: arrivingTime		The arriving time of specific passenger
* @return: id 				The id of the agent
*/ 
public static Integer getIdleAgent(Agent[] myAgent, Integer numAgent, Integer type, float arrivingTime) {
	// TODO Auto-generated method stub
	for (int i=0;i<numAgent;i++){
		if (myAgent[i].type==type && myAgent[i].getNextfinishingTime()==Float.MAX_VALUE){//if there is an previously idle agent
			return i;
		}else{//or if the finishing time is ahead of the arriving time
			float nextFinishingTime = myAgent[i].getNextfinishingTime();
			if (myAgent[i].type==type && nextFinishingTime <= arrivingTime){
				return i;	
			}
		}
	}
	return null;//or there is no such agent
}



}