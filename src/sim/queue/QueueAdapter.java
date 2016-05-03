package sim.queue;
/**                        
* Project: MemNetSim                                   
* Module ID: QueueAdapter
* JDK version used: <JDK1.7>                            
* Author: Charles Xu                       
* Create Date: 2016-04-28
* Version: 1.0        
* 
* Comments:  
*
*/ 

import Jama.Matrix;

public class QueueAdapter {

	//input y(last time) u(last time) y_updated
	
	// construct mphy by y and u
	double y;
	double u;
	Matrix mphy;
	
	double y_updated;

	// F_init static final
	static final double[][] F_init = {{0.001,0},{0,0.001}};
	static final double[][] theta_init = {{1},{1}};
	
	// Update F and theta every turn
	static Matrix F = new Matrix (F_init);
	static Matrix theta = new Matrix (theta_init);
	
	public QueueAdapter(double y, double u, double y_updated){
		this.y = y;
		this.u = u;
		this.y_updated = y_updated;
		
		this.mphy = generatePhy(y,u);
	}
	
	
	private Matrix generatePhy(double y, double u) {
		
		//double[][] phy = new double[2][1];
		//phy[0][0]=y;
		//phy[1][0]=u;
		
		double[][] phy = {{y},{u}};
		Matrix mphy = new Matrix(phy);
		return mphy;
	}


	/**
	 * FunName: 
	 * Description: 
	 * @param F
	 * @param phy
	 * @return
	 */
	//checked
	private static Matrix Update_F(Matrix F, Matrix phy ){
		Matrix F_updated;
		double temp1 = 1/(1+(phy.transpose().times(F).times(phy)).get(0,0));
		Matrix temp2 = F.times(phy).times(phy.transpose()).times(F);
		F_updated = F.minus(temp2.times(temp1));
		return F_updated;
	}
	
	
	/**
	 * FunName: 
	 * Description: 
	 * @param theta
	 * @param F_updated
	 * @param phy
	 * @param y_updated
	 * @return
	 */
	//checked
	private static Matrix Update_theta(Matrix theta, Matrix F_updated, Matrix phy, double y_updated){
		Matrix theta_updated;
		double error_updated = y_updated - phy.transpose().times(theta).get(0,0);
		theta_updated = theta.plus(F_updated.times(phy).times(error_updated));
		return theta_updated;
	}
	
	public double adjustFeedback(){
		Matrix F = this.F;
		Matrix theta = this.theta;
		
		
		Matrix F_updated = Update_F(F, this.mphy);
		Matrix theta_updated = Update_theta(this.theta,F_updated,this.mphy,this.y_updated);
		
		
		this.F = F_updated;
		this.theta = theta_updated;
		
		double feedback = -(theta.get(0,0)/theta.get(1,0))*this.y_updated; 
		
		return feedback;
	}
	
	
	
	public static void main(String argv[]) {
		//check Update_F  --- checked
		/*double[][] F = {{1,0},{0,1}};
		Matrix mF = new Matrix(F);
		double[][] phy = {{2},{2}};
		Matrix mphy = new Matrix(phy);
		
		Matrix mF_updated = Update_F(mF, mphy);
		double[][] F_updated = mF_updated.getArray();
		System.out.println("" + F_updated[0][0] + " "+ F_updated[0][1] + "\n" 
		+ F_updated[1][0] + " " + F_updated[1][1] + "\n");
		*/
		
		
		//check Update_theta --- checked
		/*
		double[][] theta = {{2},{2}};
		Matrix mtheta = new Matrix(theta);
		Matrix mtheta_updated = Update_theta(mtheta,mF,mphy,1);
		double[][] theta_updated = mtheta_updated.getArray();
		System.out.println("" + theta_updated[0][0] + "\n" 
				+ theta_updated[1][0] +  "\n");
		*/
		
		
	
	}
}
