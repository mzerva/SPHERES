package phaseManager;

import java.util.HashMap;

public class Histogram {
	
	private int phaseID=-1;
	private HashMap<String,Double> growth = new  HashMap<String,Double>();
	private HashMap<String,Double> maintenance = new HashMap<String,Double>();
	
	public Histogram(int phaseID, HashMap<String,Double> growth, HashMap<String,Double> maintenance){
		this.phaseID=phaseID;
		this.growth=growth;
		this.maintenance=maintenance;
	}
	
	public Histogram(){}
	
	public int getPhaseID(){
		return phaseID;
	}
	
	public void setGrowth(HashMap<String,Double> growth){
		this.growth=growth;
	}
	
	public void setMaintenance(HashMap<String,Double> maintenance){
		this.maintenance=maintenance;
	}
	
	public HashMap<String,Double> getGrowth(){
		return growth;
	}
	
	public HashMap<String,Double> getMaintenance(){
		return maintenance;
	}
}
