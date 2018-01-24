package clusterManager;

import java.util.ArrayList;
import java.util.HashMap;

import dataManager.ClusteredDataHandler;
import dataManager.InputManager;
import dataManager.Release;

public class ClusteringManager {
	
	private String dataset="";
	private HashMap<Integer,ClusterSet> clusterSets;
	private HashMap<Integer, Release> releases;
	
	public ClusteringManager(String dataset){
		this.dataset=dataset;
	}

	public HashMap<Integer,HashMap<Integer,ArrayList<Release>>> matchReleases(){
		HashMap<Integer,HashMap<Integer,ArrayList<Release>>> releaseMap = new HashMap<Integer,HashMap<Integer,ArrayList<Release>>>();
		
		for(Integer key: clusterSets.keySet()){
			ArrayList<Cluster> clusters = clusterSets.get(key).getClusters();
			HashMap<Integer,ArrayList<Release>> r = new HashMap<Integer,ArrayList<Release>>();
			for(Cluster cluster : clusters){
				ArrayList<ClusterableObject> objects = cluster.getObjects();
				ArrayList<Release> tmpReleases = new ArrayList<Release>();
				for(ClusterableObject object : objects){
					tmpReleases.add(releases.get(object.getId()));
				}
				r.put(cluster.getId(), tmpReleases);
			}
			releaseMap.put(clusterSets.get(key).getId(), r);
		}
		
		return releaseMap;
	}
	
	public void process(){
		String inputFile=dataset+".csv";
		InputManager manager = new InputManager(inputFile);
		manager.parseReleases();
		
		releases=manager.getReleases();
		
		ArrayList<Cluster> clusters = new ArrayList<Cluster>();
		
		for(Integer key : releases.keySet()){
			ArrayList<ClusterableObject> objects = new ArrayList<ClusterableObject>();
			ClusterableObject tmpObject = new ClusterableObject();
			tmpObject.setId(key);
			objects.add(tmpObject);
			double[] values ={releases.get(key).getNormGrowth(), releases.get(key).getNormMaintenance()};
			Cluster tmpCluster = new Cluster(key,objects, values);
			clusters.add(tmpCluster);
		}
		
		AgglomerativeAlgorithm algorithm = new AgglomerativeAlgorithm(clusters , releases);
		algorithm.start();			
		clusterSets = algorithm.getClusterSets();
		
		ClusteredDataHandler clusteredHandler = new ClusteredDataHandler(releases,clusterSets,inputFile);
		clusteredHandler.process();
	}
	
	public HashMap<Integer,ClusterSet> getClusterSets(){
		return clusterSets;
	}

	public HashMap<Integer, Release> getReleases(){
		return releases;
	}
}
