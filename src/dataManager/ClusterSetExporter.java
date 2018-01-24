package dataManager;
import java.util.*;
import java.io.*;

public class ClusterSetExporter {
	
	private HashMap<Integer, ArrayList<Release>> releaseMap = new HashMap<Integer, ArrayList<Release>>();
	private String title;
	private String dataset;
	private String level;
	private HashMap<Integer,String> output = new HashMap<Integer,String>();
	private String outputPath;
	private PrintWriter outputWriter = null;
	private File outputFile;
	private HashMap<Integer,Integer> phaseIDsMap = new HashMap<Integer,Integer>();
	
	public ClusterSetExporter(String title, HashMap<Integer, ArrayList<Release>> releaseMap){
		this.title=title;
		this.releaseMap=releaseMap;
		String fields[] = title.split(":");
		dataset=fields[0].trim();
		level=fields[1].trim();
		outputPath= "files/output/cluster_sets/"+dataset+"_"+level+".csv";
	}
	
	private void sortPhaseIDs(){
		
		ArrayList<Integer> starters = new ArrayList<Integer>();
		HashMap<Integer,Integer> tmpMap = new HashMap<Integer,Integer>();
		for(Integer key : releaseMap.keySet()){
			ArrayList<Integer> tmp = new ArrayList<Integer>();
			for(Release release : releaseMap.get(key)){
				tmp.add(release.getId());
			}
			Collections.sort(tmp);
			starters.add(tmp.get(0));
			tmpMap.put(tmp.get(0), key);
		}
		Collections.sort(starters);
		
		int counter=0;
		for(Integer start : starters){
			phaseIDsMap.put(tmpMap.get(start), counter);
			counter++;
		}
	}
	
	public void process(){
		sortPhaseIDs();
		outputFile = new File(outputPath);
		try 
		 { 
			outputWriter = new PrintWriter(new FileOutputStream(outputFile));
			
			outputWriter.println("phaseId;releaseId;name;date;totalChanges;growth;maintenance;normGrowth;normMaintenance;growthIntensity;maintenanceIntensity;schemaSize;firstCommitDate;lastCommitDate;#commits");
			
			ArrayList<Integer> phases = new ArrayList<Integer>();
			HashMap<Integer,ArrayList<Release>> phaseMap = new HashMap<Integer,ArrayList<Release>>();
			
			for(Integer cluster: releaseMap.keySet()){
				phases.add(phaseIDsMap.get(cluster));
				phaseMap.put(phaseIDsMap.get(cluster), releaseMap.get(cluster));
			}
			
			Collections.sort(phases);
			
			for(Integer phase: phases){
				ArrayList<Release> releases = phaseMap.get(phase);
				for(Release release : releases){
					outputWriter.println(phase+";"+release.getId()+";"+release.getName()+";"+release.getDate()+";"+release.getTotalChanges()+";"+release.getGrowth()+";"+
							release.getMaintenance()+";"+String.valueOf(release.getNormGrowth()).replace(".", ",")+";"+String.valueOf(release.getNormMaintenance()).replace(".", ",")+";"+release.getGrowthIntensity()+";"+release.getMaintenanceIntensity()+";"+
							release.getSchemaSize()+";"+release.getFirstCommitDate()+";"+release.getLastCommitDate()+";"+release.getCommits());
				}
			}
			
			outputWriter.close();
			
		 } 
		 catch(FileNotFoundException e) 
		 { 
			 System.out.printf("Error opening the file %s.txt.\n",outputPath);
		 }
	}
}
