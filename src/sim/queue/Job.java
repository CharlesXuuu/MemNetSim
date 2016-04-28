package sim.queue;

/**                        
* Project: MemNet Simulation                                       
* Module ID: Job
* JDK version used: <JDK1.7>                            
* Author: Charles Xu                        
* Create Date: 2016-03-11
* Version: 1.0             
* 
* Comments:  This class is a representation for each job, it contains jobs' attributes and actions
* 
*/ 


public class Job {
	
  private final static int TYPE0 = 0; 
  private final static int TYPE1 = 1;
  
  private Integer id;
  private Integer type; //Define the type of this job
  private float arrivingTime;// Define the arriving time of this job
  private float servingTime;// Define the serving time of this job
  private float servingLength;// Define the serving length of this job
  private float leavingTime;// Define the leaving time of this job
  private boolean served;

  public Job(Integer id, Integer type, float landingTime){
	  this.id = id;
	  this.type = type;
	  
	  this.arrivingTime = landingTime;
	  this.served = false;
	  //System.out.println(""+id+"\t"+type+"\t"+landingTime);
  }
  
  
  public Job(Integer id, Integer type) {
	  this.id = id;
	  this.type = type;
	  
	  this.served = false;
}


public void joinQueue() {
  }

  public float getArrivingTime(){
	  return this.arrivingTime;
  }
  
  public void setArrivingTime(float arrivingTime){
	  this.arrivingTime = arrivingTime;
  }
  
  public float getServingTime(){
	  return this.servingTime;
  }
  
  public void setServingTime(float servingTime){
	  this.servingTime = servingTime;
  }
  
  
  public float getLeavingTime(){
	  return this.leavingTime;
  }
  
  public void setLeavingTime(float leavingTime){
	  this.leavingTime = leavingTime;
  }
  
public Integer getType() {
	// TODO Auto-generated method stub
	return this.type;
}


public void setServingLength(float servingLength) {
	// TODO Auto-generated method stub
	this.servingLength = servingLength;
}


public float getServingLength() {
	// TODO Auto-generated method stub
	return this.servingLength;
}


public void setIsServed(boolean b) {
	// TODO Auto-generated method stub
	this.served = b;
}


public static void myprint(Job[] myJobs, Integer numJobs) {
	// TODO Auto-generated method stub
	for (int i=0;i<numJobs;i++){
		System.out.println("id:"+i+"\ttype:"+myJobs[i].getType()+"\tArrivingTime:"+myJobs[i].getArrivingTime()+"\tServingTime:"+myJobs[i].getServingTime()+"\tServingLength:"+myJobs[i].getServingLength()+"\tLeavingTime:"+myJobs[i].getLeavingTime()+"\n");
	}
}

}