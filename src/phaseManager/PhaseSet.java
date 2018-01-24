package phaseManager;

import dataManager.HistogramComputer;
import dataManager.Release;

import java.util.*;

import clusterManager.Cluster;
import clusterManager.ClusterSet;

public class PhaseSet {
	private ArrayList<Phase> phases = new ArrayList<Phase>();
	private HashMap<Integer,ArrayList<Release>> currentReleaseMap;
	private HashMap<Integer,Integer> phaseIDsMap = new HashMap<Integer,Integer>();
	private String title;
	private HashMap<Integer,Release> firstMap = new HashMap<Integer,Release>();
	private HashMap<Integer,Release> lastMap = new HashMap<Integer,Release>();
	private HashMap<Integer,Histogram> histogramMap = new HashMap<Integer,Histogram>();
	
	public PhaseSet(String title,HashMap<Integer,ArrayList<Release>> currentReleaseMap){
		this.currentReleaseMap=currentReleaseMap;
		this.title=title;
		sortPhaseIDs();
	}
	
	private void sortPhaseIDs(){
		
		ArrayList<Integer> starters = new ArrayList<Integer>();
		HashMap<Integer,Integer> tmpMap = new HashMap<Integer,Integer>();
		for(Integer key : currentReleaseMap.keySet()){
			HashMap<Integer,Release> releases = new HashMap<Integer,Release>();
			ArrayList<Integer> tmp = new ArrayList<Integer>();
			for(Release release : currentReleaseMap.get(key)){
				tmp.add(release.getId());
				releases.put(release.getId(), release);
			}
			Collections.sort(tmp);
			starters.add(tmp.get(0));
			tmpMap.put(tmp.get(0), key);
			firstMap.put(key,releases.get(tmp.get(0)));
			lastMap.put(key,releases.get(tmp.get(tmp.size()-1)));
		}
		Collections.sort(starters);
		
		int counter=0;
		for(Integer start : starters){
			phaseIDsMap.put(tmpMap.get(start), counter);
			counter++;
		}
	}
	
	public ArrayList<Phase> computePhases(){
		for(Integer key : currentReleaseMap.keySet()){
			int newKey = phaseIDsMap.get(key);
			
			HistogramComputer hComputer = new HistogramComputer(newKey, currentReleaseMap.get(key));
			hComputer.processGrowth();
			hComputer.processMaintenance();
			
			//creates the phases objects with their histograms and characterization already computed
			
			Phase newPhase = new Phase(newKey, hComputer.getHistogram(),firstMap.get(key), lastMap.get(key));
			newPhase.getCharacterized();
			//System.out.println("Phase ID: "+ newKey +", was characterized as: "+ newPhase.getCharacterization());			
			
			histogramMap.put(newKey,hComputer.getHistogram());
			
			phases.add(newPhase);
		}
		
		
		
		return phases;
	}
	
	public HashMap<Integer,Histogram> getHistograms(){
		return histogramMap;
	}
	
}
