package sim.queue;

/**                        
* Project: MemNet Simulation                                       
* Module ID: Waiting Queue
* JDK version used: <JDK1.7>                            
* Author: Charles Xu                        
* Create Date: 2016-03-11
* Version: 1.0             
* 
* Comments:  This class is a representation for each waiting queue, it contains queues' attributes and actions
* 
*/

//using the java built-in class of de-queue 
import java.util.ArrayDeque;


public class WaitingQueue {

 
  public Integer id;
  public Integer curNum; // Define the current number of this queue
  private Integer type; // Define if this queue is for the specific processor or for specific type of job
  private ArrayDeque<Integer> data = new ArrayDeque<Integer>();

  public WaitingQueue(Integer id, Integer type){
	  this.id = id;
	  this.curNum = 0;
	  this.type = type;
  }
  
  
  public void add(Integer jobId) {
	  data.add(jobId);
	  curNum += 1;
  }

  public Integer pop() {
	  Integer id = data.remove();
	  curNum -=1;
	  return id;
  }

/** 
* FunName: getShortestQueue
* Description: This function compares and gets the shortest queue for specific type of job
* @param: myWaitingQueue					The waiting queue
* @param: numQueue							The number of all waiting queue
* @param: type								The job type
* @return: tempid							The shortest queue id
*/ 
public static Integer getShortestQueue(WaitingQueue[] myWaitingQueue, Integer numQueue,
		Integer type) {
	// TODO Auto-generated method stub
	Integer tempNum = Integer.MAX_VALUE; 
	Integer tempId = null;
	
	for(int i=0; i<numQueue; i++){
		if (myWaitingQueue[i].type==type && myWaitingQueue[i].curNum <= tempNum){
			tempNum = myWaitingQueue[i].curNum;
			tempId = myWaitingQueue[i].id;
		}
	}
	
	return tempId;
}


public Integer getCurNum() {
	// TODO Auto-generated method stub
	return this.curNum;
}

}