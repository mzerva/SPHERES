package phaseManager;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class PhaseExporter {
	private String title;
	private ArrayList<Phase> phases;
	private String dataset;
	private String level;
	private HashMap<Integer,String> output = new HashMap<Integer,String>();
	private String outputPath;
	private PrintWriter outputWriter = null;
	private File outputFile;
	
	public PhaseExporter(String title, ArrayList<Phase> phases){
		this.title=title;
		this.phases=phases;
		String fields[] = title.split(":");
		dataset=fields[0].trim();
		level=fields[1].trim();
		outputPath= "files/output/phases/"+dataset+"_"+level+".csv";
	}
	
	public void process(){
		outputFile = new File(outputPath);
		try 
		 { 
			outputWriter = new PrintWriter(new FileOutputStream(outputFile)); 
			
			outputWriter.println(title);
			outputWriter.println();
			
			outputWriter.println("phaseID;firstRelease;lastRelease;firstSchemaSize;lastSchemaSize;firstDate;lastDate;zeroGrowth;lowGrowth;mediumGrowth;highGrowth;zeroMaintenance;lowMaintenance;mediumMaintenance;highMaintenance;growthWinner;maintenanceWinner;Characterization");
			
			ArrayList<Integer> ids = new ArrayList<Integer>();
			
			for(Phase phase : phases){
				Histogram hist = phase.getHistogram();
				HashMap<String,Double> growth = hist.getGrowth();
				HashMap<String,Double> maintenance = hist.getMaintenance();
				
				String firstDate=phase.getFirstRelease().getFirstCommitDate();
				String lastDate=phase.getLastRelease().getLastCommitDate();
				
				String tmp=(phase.getPhaseID()+";"+phase.getFirstRelease().getId()+";"+phase.getLastRelease().getId()+";"+phase.getFirstRelease().getSchemaSize()+";"+phase.getLastRelease().getSchemaSize()+
						";"+firstDate+";"+lastDate+";"+String.valueOf(growth.get("Zero")).replace(".", ",")+";"+String.valueOf(growth.get("Low")).replace(".", ",")+";"
				+String.valueOf(growth.get("Medium")).replace(".", ",")+";"+String.valueOf(growth.get("High")).replace(".", ",")+";"+String.valueOf(maintenance.get("Zero")).replace(".", ",")
				+";"+String.valueOf(maintenance.get("Low")).replace(".", ",")+";"+String.valueOf(maintenance.get("Medium")).replace(".", ",")+";"+String.valueOf(maintenance.get("High")).replace(".", ",")+";"+phase.getWinnerGrowth()+";"+phase.getWinnerMaintenance()+";"+phase.getCharacterization());
				
				output.put(phase.getPhaseID(), tmp);
				ids.add(phase.getPhaseID());
				
			}
			
			Collections.sort(ids);
			
			for(Integer id : ids){
				outputWriter.println(output.get(id));
			}
			
			outputWriter.close();
			
		 } 
		 catch(FileNotFoundException e) 
		 { 
			 System.out.printf("Error opening the file %s.txt.\n",outputPath);
		 }
	}
	
}
