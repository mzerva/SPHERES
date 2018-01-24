package dataManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Release {

	private int id;
	private String name;
	private String date;
	private int totalChanges;
	private int growth;
	private int maintenance;
	private double normGrowth;
	private double normMaintenance;
	private String growthIntensity;
	private String maintenanceIntensity;
	private int schemaSize;
	private String firstCommitDate;
	private String lastCommitDate;	
	private String firstDate;
	private String lastDate;
	private int commits;
	
	public Release(int id, String name, String date, int totalChanges, int growth, int maintenance, double normGrowth, double normMaintenance,String growthIntensity, String maintenanceIntensity, int schemaSize, String firstCommitDate, String lastCommitDate, int commits){
		this.id=id;
		this.name=name;
		this.date=date;
		this.totalChanges=totalChanges;
		this.growth=growth;
		this.maintenance=maintenance;
		this.normGrowth=normGrowth;
		this.normMaintenance=normMaintenance;
		this.growthIntensity=growthIntensity;
		this.maintenanceIntensity=maintenanceIntensity;
		this.schemaSize=schemaSize;
		this.firstCommitDate=firstCommitDate;
		this.lastCommitDate=lastCommitDate;
		this.commits=commits;
		computeDates();
	}
	
	public void computeDates(){
		String timestamp1=firstCommitDate;
		String timestamp2=lastCommitDate;
		Long unixSeconds1 = Long.parseLong(timestamp1);
		Long unixSeconds2 = Long.parseLong(timestamp2);
		Date date1 = new Date(unixSeconds1*1000L); // *1000 is to convert seconds to milliseconds
		Date date2 = new Date(unixSeconds2*1000L);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); // the format of your date
		sdf.setTimeZone(TimeZone.getTimeZone("GMT-4")); // give a timezone reference for formating (see comment at the bottom
		firstDate = sdf.format(date1);
		lastDate = sdf.format(date2);
	}
	
	public int getCommits(){
		return commits;
	}
	
	public String getFirstCommitDate(){
		return firstDate;
	}
	
	public String getLastCommitDate(){
		return lastDate;
	}
	
	public int getSchemaSize(){
		return schemaSize;
	}
	
	public int getId(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public String getDate(){
		return date;
	}

	public int getTotalChanges(){
		return totalChanges;
	}
	
	public int getGrowth(){
		return growth;
	}
	
	public int getMaintenance(){
		return maintenance;
	}
	
	public double getNormGrowth(){
		return normGrowth;
	}
	
	public double getNormMaintenance(){
		return normMaintenance;
	}
	
	public String getMaintenanceIntensity(){
		return maintenanceIntensity;
	}
	
	public String getGrowthIntensity(){
		return growthIntensity;
	}
	
}
