package phaseManager;

import dataManager.Release;

import java.util.*;

import clusterManager.Cluster;

public class Phase {
	private int clusterID;
	private Histogram histogram;
	private String winnerGrowth;
	private String winnerMaintenance;
	private HashMap<String,Integer> intensityValues = new HashMap<String,Integer>();
	private String characterization;
	private Release firstRelease;
	private Release lastRelease;
	private double threshold=0.25;
	private boolean growthSpikes=false;
	private boolean maintenanceSpikes=false;
	
	
	public Phase(int clusterID, Histogram histogram, Release firstRelease, Release lastRelease){
		this.clusterID=clusterID;
		this.histogram=histogram;
		intensityValues.put("Zero", 0);
		intensityValues.put("Low", 1);
		intensityValues.put("Medium", 2);
		intensityValues.put("High", 3);
		this.firstRelease=firstRelease;
		this.lastRelease=lastRelease;
	}
	
	public Release getFirstRelease(){
		return firstRelease;
	}
	
	public Release getLastRelease(){
		return lastRelease;
	}
	
	public Histogram getHistogram(){
		return histogram;
	}
	
	public int getPhaseID(){
		return clusterID;
	}
	
	public void getCharacterized(){
		computeThresholdWinners();
		//PhaseClassifier characterizer = new PhaseClassifier(getPhaseID(),winnerGrowth, winnerMaintenance);
		PhaseClassifier characterizer = new PhaseClassifier(getPhaseID(),winnerGrowth, winnerMaintenance,growthSpikes,maintenanceSpikes);
		characterizer.processCharacterization();
		characterization=characterizer.getCharacterization();
	}
	
	public String getCharacterization(){
		return characterization;
	}
	
	private void computeWinners(){
		HashMap<String,Double> growth = histogram.getGrowth();
		HashMap<String,Double> maintenance = histogram.getMaintenance();
		
		if(growth.get("High")>0.0){
			winnerGrowth="High";
		}
		else if(growth.get("Medium")>0.0){
			winnerGrowth="Medium";
		}
		else if(growth.get("Low")>0.0){
			winnerGrowth="Low";
		}
		else{
			winnerGrowth="Zero";
		}
		
		if(maintenance.get("High")>0.0){
			winnerMaintenance="High";
		}
		else if(maintenance.get("Medium")>0.0){
			winnerMaintenance="Medium";
		}
		else if(maintenance.get("Low")>0.0){
			winnerMaintenance="Low";
		}
		else{
			winnerMaintenance="Zero";
		}
		
	}
	
	private void computeThresholdWinners(){
		HashMap<String,Double> growth = histogram.getGrowth();
		HashMap<String,Double> maintenance = histogram.getMaintenance();
		
		if(growth.get("High")>0.0 || growth.get("Medium")>0.0){
			growthSpikes=true;
		}
		if(maintenance.get("High")>0.0 || maintenance.get("Medium")>0.0){
			maintenanceSpikes=true;
		}
		
		if(growth.get("High")>=threshold){
			winnerGrowth="High";
		}
		else if(growth.get("Medium")>=threshold){
			winnerGrowth="Medium";
		}
		else if(growth.get("Low")>=threshold){
			winnerGrowth="Low";
		}
		else{
			winnerGrowth="Zero";
		}
		
		if(maintenance.get("High")>=threshold){
			winnerMaintenance="High";
		}
		else if(maintenance.get("Medium")>=threshold){
			winnerMaintenance="Medium";
		}
		else if(maintenance.get("Low")>=threshold){
			winnerMaintenance="Low";
		}
		else{
			winnerMaintenance="Zero";
		}
		
	}
	
	private void computeOldWinners(){
		HashMap<String,Double> growth = histogram.getGrowth();
		HashMap<String,Double> maintenance = histogram.getMaintenance();
		
		double growthMax=0.0;
		String currentGrowthMax="";
		boolean gFlag=false;
		
		double maintenanceMax=0.0;
		String currentMaintenanceMax="";
		boolean mFlag=false;
		
		boolean gHigh=false;
		boolean mHigh=false;
		
		for(String key : growth.keySet()){
			//System.out.println(key);
			if(gFlag==false){
				gFlag=true;
				growthMax=growth.get(key);
				currentGrowthMax=key;
			}
			else{
				if(growth.get(key)>growthMax){
					growthMax=growth.get(key);
					currentGrowthMax=key;
				}
				else if(growth.get(key)== growthMax){
					if(intensityValues.get(key)>intensityValues.get(currentGrowthMax)){
						growthMax=growth.get(key);
						currentGrowthMax=key;
					}
				}
				
			}
			
			if(key.equals("High") && growth.get(key)>0){
				gHigh=true;
			}
			
			if(mFlag==false){
				mFlag=true;
				maintenanceMax=maintenance.get(key);
				currentMaintenanceMax=key;
			}
			else{
				if(maintenance.get(key)>maintenanceMax){
					maintenanceMax=maintenance.get(key);
					currentMaintenanceMax=key;
				}
				else if(maintenance.get(key)== maintenanceMax){
					if(intensityValues.get(key)>intensityValues.get(currentMaintenanceMax)){
						maintenanceMax=maintenance.get(key);
						currentMaintenanceMax=key;
					}
				}
			}
			
			if(key.equals("High") && maintenance.get(key)>0){
				mHigh=true;
			}
			
		}
		
		if(gHigh==true){
			currentGrowthMax="High";
		}
		
		if(mHigh==true){
			currentMaintenanceMax="High";
		}
		
		winnerGrowth=currentGrowthMax;
		winnerMaintenance=currentMaintenanceMax;
		
	}
	
	public String getWinnerGrowth(){
		return winnerGrowth;
	}
	
	public String getWinnerMaintenance(){
		return winnerMaintenance;
	}
	
}
