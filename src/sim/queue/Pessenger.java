package sim.queue;

/**                        
* Project: Queueing Model Simulation                                       
* Module ID: Pessenger
* JDK version used: <JDK1.7>                            
* Author: Charles Xu                        
* Create Date: 2014-03-11
* Version: 1.0             
* 
* Comments:  This class is a representation for each passenger, it contains passengers' attribute and actions
* 
*/ 


public class Pessenger {
	
  private final static int CANADIAN = 0; 
  private final static int VISITOR = 1;
  
  private Integer id;
  private Integer type; //Define the type of this passenger
  private float arrivingTime;// Define the arriving time of this passenger
  private float servingTime;// Define the serving time of this passenger
  private float servingLength;// Define the serving length of this passenger
  private float leavingTime;// Define the laving time of this passenger
  private boolean served;

  public Pessenger(Integer id, Integer type, float landingTime){
	  this.id = id;
	  this.type = type;
	  
	  this.arrivingTime = landingTime;
	  this.served = false;
	  //System.out.println(""+id+"\t"+type+"\t"+landingTime);
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


public static void myprint(Pessenger[] myPessenger, Integer numPessenger) {
	// TODO Auto-generated method stub
	for (int i=0;i<numPessenger;i++){
		System.out.println("id:"+i+"\ttype:"+myPessenger[i].getType()+"\tArrivingTime:"+myPessenger[i].getArrivingTime()+"\tServingTime:"+myPessenger[i].getServingTime()+"\tServingLength:"+myPessenger[i].getServingLength()+"\tLeavingTime:"+myPessenger[i].getLeavingTime()+"\n");
	}
}

}