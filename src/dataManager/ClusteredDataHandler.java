package dataManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;

import clusterManager.Cluster;
import clusterManager.ClusterSet;
import clusterManager.ClusterableObject;
import phaseManager.DissimilarityManager;
import phaseManager.Phase;
import phaseManager.PhaseSet;

public class ClusteredDataHandler {
	private HashMap<Integer,ClusterSet> clusterSets;
	private HashMap<Integer, Release> releases;
	private String outputFile;
	
	public ClusteredDataHandler(HashMap<Integer, Release> releases,HashMap<Integer,ClusterSet> clusterSets, String outputFile){
		this.releases=releases;
		this.clusterSets=clusterSets;
		this.outputFile=outputFile;
	}
	
	public ClusteredDataHandler(){}
	
	public void writeEvaluation(){
		String evaluationPath="files/output/evaluation/"+outputFile;
		File evaluationFile = new File(evaluationPath);
		try 
		 {
			PrintWriter evaluationWriter = new PrintWriter(new FileOutputStream(evaluationFile)); 

			evaluationWriter.println("level;numOfClusters;cohesion;separation;silhouette");
			for(Integer key: clusterSets.keySet()){
				evaluationWriter.println(key+";"+clusterSets.get(key).getClusters().size()+";"+String.valueOf(clusterSets.get(key).getCohesion()).replace(".",",")+";"+String.valueOf(clusterSets.get(key).getSeparation()).replace(".",",")+";"+String.valueOf(clusterSets.get(key).getSilhouette()).replace(".",","));

			}
			evaluationWriter.close();
		 }
		catch(FileNotFoundException e) 
		 { 
			 System.out.printf("Error opening the file %s.\n",evaluationPath);
		 }
	}
	
	public void writeFX(){
		String outputPath="files/output/fx_files/"+outputFile;
		File outputFile = new File(outputPath);
		try 
		 { 
			PrintWriter outputWriter = new PrintWriter(new FileOutputStream(outputFile)); 
			for(Integer key: clusterSets.keySet()){
				String header="LEVEL: "+key+" , SILHOUETTE: "+clusterSets.get(key).getSilhouette();
				outputWriter.println(header);
				String[] lines=getCurrentString(clusterSets.get(key));
				
				outputWriter.println(lines[0]);
				outputWriter.println(lines[1]);
				
				outputWriter.println();
			}
			outputWriter.close();
		 }
		catch(FileNotFoundException e) 
		 { 
			 System.out.printf("Error opening the file %s.\n",outputPath);
		 }	
	}
	
	/*public void writeDissimilarity(String dataset, HashMap<String,HashMap<Integer,ArrayList<Release>>> releaseMap){
		String output=dataset+".csv";
		String outputPath="files/output/dissimilarities/"+output;
		File outputFile = new File(outputPath);
		try 
		 { 
			PrintWriter outputWriter = new PrintWriter(new FileOutputStream(outputFile)); 
			
			outputWriter.println("level;dissimilarity");
			
			ArrayList<Integer> levels = new ArrayList<Integer>();
			HashMap<Integer,Double> dissimilarities = new HashMap<Integer,Double>();
			
			for(String key: releaseMap.keySet()){
				String fields1[] = key.split(":");
				String fields2[]=fields1[1].split(",");
				int tmpKey=Integer.parseInt(fields2[0].trim());
				levels.add(tmpKey);
				String title=dataset+" ["+key.split(":")[1]+"="+key.split(":")[2]+" ]";
				
				PhaseSet phaseSet = new PhaseSet(title,releaseMap.get(key));
				ArrayList<Phase> phases=phaseSet.computePhases();
				
				DissimilarityManager manager = new DissimilarityManager(phases);
				manager.process();
			
				dissimilarities.put(tmpKey, manager.getDissimilarity());
			}
			
			Collections.sort(levels);
			
			for(Integer level: levels){
			
				outputWriter.println(level+";"+String.valueOf(dissimilarities.get(level)).replace(".", ","));	
				
			}
				
			outputWriter.close();
		 }
		catch(FileNotFoundException e) 
		 { 
			 System.out.printf("Error opening the file %s.\n",output);
		 }
			
	}*/
	
	public void process(){
		writeFX();
		writeEvaluation();
	}
	
	private String[] getCurrentString(ClusterSet currentClusterSet){
		String[] lines = new String[2];
		
		ArrayList<Cluster> currentClusters = currentClusterSet.getClusters();
		
		ArrayList<Integer> allObjects = new ArrayList<Integer>();
		HashMap<Integer,Integer> objectClusters = new HashMap<Integer,Integer>();
		
		for(int i=0;i<currentClusters.size();i++){
			ArrayList<ClusterableObject> objects = currentClusters.get(i).getObjects();
			
			for(int j=0;j<objects.size();j++){
				allObjects.add(objects.get(j).getId());
				objectClusters.put(objects.get(j).getId(), currentClusters.get(i).getId());
			}
			
		}
		
		Collections.sort(allObjects);
		
		String tmp1="";
		String tmp2="";
		
		for(int i=0;i<allObjects.size()-1;i++){
			tmp1+=allObjects.get(i)+";";
			tmp2+=objectClusters.get(allObjects.get(i))+";";
		}
		
		tmp1+=allObjects.get(allObjects.size()-1)+";";
		tmp2+=objectClusters.get(allObjects.get(allObjects.size()-1))+";";
		
		lines[0]=tmp1;
		lines[1]=tmp2;
		
		return lines;
	}
	
	public HashMap<Integer,ClusterSet> getClusterSets(){
		return clusterSets;
	}
	
}
