package dataManager;

import java.util.ArrayList;
import java.util.HashMap;

import phaseManager.Histogram;

public class HistogramComputer {

	private int clusterID;
	
	private ArrayList<Release> values = new ArrayList<Release>();
	
	private Histogram histogram;
	
	public HistogramComputer(int clusterID, ArrayList<Release> values){
		this.clusterID=clusterID;
		this.values=values;
		histogram = new Histogram();
	}
	
	public void processGrowth(){
		
		int zeroCounter=0;
		int lowCounter=0;
		int mediumCounter=0;
		int highCounter=0;
		
		for(int i=0;i<values.size();i++){
			Release currentRelease=values.get(i);
			if(currentRelease.getGrowthIntensity().equals("-")){
				zeroCounter++;
			}
			else if(currentRelease.getGrowthIntensity().equals("Low")){
				lowCounter++;
			}
			else if(currentRelease.getGrowthIntensity().equals("Medium")){
				mediumCounter++;
			}
			else{
				highCounter++;
			}
		}
		
		HashMap<String,Double> growth = new HashMap<String,Double>();
		
		growth.put("Zero", ( (double)  zeroCounter/ (double) values.size() )  );
		growth.put("Low", ( (double)  lowCounter/ (double) values.size() ) );
		growth.put("Medium", ( (double)  mediumCounter/ (double) values.size()  ) );
		growth.put("High", ( (double)  highCounter/ (double) values.size() )  );		
		
		histogram.setGrowth(growth);
	}
	
	public void processMaintenance(){
		int zeroCounter=0;
		int lowCounter=0;
		int mediumCounter=0;
		int highCounter=0;
		
		for(int i=0;i<values.size();i++){
			Release currentRelease=values.get(i);
			if(currentRelease.getMaintenanceIntensity().equals("-")){
				zeroCounter++;
			}
			else if(currentRelease.getMaintenanceIntensity().equals("Low")){
				lowCounter++;
			}
			else if(currentRelease.getMaintenanceIntensity().equals("Medium")){
				mediumCounter++;
			}
			else{
				highCounter++;
			}
		}
		
		HashMap<String,Double> maintenance = new HashMap<String,Double>();
		
		maintenance.put("Zero", ( (double) zeroCounter/ (double) values.size()  ) );
		maintenance.put("Low", ( (double) lowCounter/ (double) values.size()  ) );
		maintenance.put("Medium", ( (double)  mediumCounter/ (double) values.size()  ) );
		maintenance.put("High", ( (double)  highCounter/ (double) values.size()  ) );
		
		histogram.setMaintenance(maintenance);
			
	}
	
	public  Histogram getHistogram(){
		return histogram;
	}
	
	public int getClusterID(){
		return clusterID;
	}
	
}
