package clusterManager;

import java.util.*;

import dataManager.DistanceMatrix;
import dataManager.Release;

public class AgglomerativeAlgorithm {
	
	private HashMap<Integer,ClusterSet> clusterSets = new HashMap<Integer,ClusterSet>();
	private HashMap<Integer,DistanceMatrix> distanceMatrices = new HashMap<Integer,DistanceMatrix>();
	private int currentLevel;
	private int clusterCount;
	private HashMap<Integer,Release> releases = new HashMap<Integer,Release>();

	public AgglomerativeAlgorithm(ArrayList<Cluster> initialClusters, HashMap<Integer,Release> releases){
		ClusterSet set= new ClusterSet(0,initialClusters);
		set.setCohesion(500.3);
		set.setSeparation(800.4);
		clusterSets.put(0, set);
		currentLevel=0;
		this.releases=releases;
		computeInitialMatrix();
		clusterCount=initialClusters.size();
	}

	
	public void computeInitialMatrix(){
		DistanceMatrix initialMatrix = new DistanceMatrix(releases);
		initialMatrix.fillMatrix();
		distanceMatrices.put(0, initialMatrix);
	}
	
	public void start(){
		while(clusterSets.get(currentLevel).getClusters().size()>1){
			Cluster[] closest=getClosestPair();
			
			if(closest[0]!=null){
				
				ClusterSet currentSet = clusterSets.get(currentLevel);
				if(currentLevel==0){
					double values[]=evaluateClustering();
					currentSet.setCohesion(values[0]);
					currentSet.setSeparation(values[1]);
				}
				ArrayList<Cluster> currentClusters = currentSet.getClusters();
				ArrayList<ClusterableObject> objects=mergeClusters(closest);
				double[] avgValues = computeAvgCluster(objects);
				Cluster newCluster = new Cluster(clusterCount, objects, avgValues);
				clusterCount++;
				ArrayList<Cluster> newClusters = new ArrayList<Cluster>();
				for(int i=0;i<currentClusters.size();i++){
					if(currentClusters.get(i).getId()!=closest[0].getId() && currentClusters.get(i).getId()!=closest[1].getId()){
						newClusters.add(currentClusters.get(i));
					}
				}
				newClusters.add(newCluster);
				
				
				currentLevel++;
				ClusterSet newClusterSet = new ClusterSet(currentLevel, newClusters);
				clusterSets.put(currentLevel, newClusterSet);
				updateMatrix(newCluster, closest);
				if(currentLevel==1){
					newClusterSet.setSilhouette(0.0);
				}
				else{
					newClusterSet.setSilhouette(computeCurrentSilhouette());
				}
				double values[]=evaluateClustering();
				newClusterSet.setCohesion(values[0]);
				newClusterSet.setSeparation(values[1]);
			}
		}
	}
	
	private double[] evaluateClustering(){
		ClusterSet currentClusterSet = clusterSets.get(currentLevel);
		ArrayList<Cluster> currentClusters = currentClusterSet.getClusters();
		ClusteringEvaluator evaluator = new ClusteringEvaluator(currentLevel,currentClusters,releases);
		evaluator.process();
		
		double values[] ={evaluator.getCohesion(),evaluator.getSeparation()};
		return values;
	}
	
	private double computeCurrentSilhouette(){
		ClusterSet currentClusterSet = clusterSets.get(currentLevel);
		ArrayList<Cluster> currentClusters = currentClusterSet.getClusters();
		Silhouette currentSilhouette = new Silhouette(currentClusters,releases,distanceMatrices.get(0));

		currentSilhouette.computeSilhouetteValues();

		return currentSilhouette.getSilhouetteValue();
	}
	
	private ArrayList<ClusterableObject> mergeClusters(Cluster[] closest){
		ArrayList<ClusterableObject> newObjects = new  ArrayList<ClusterableObject>();
		
		ArrayList<ClusterableObject> objects1 = closest[0].getObjects();
		ArrayList<ClusterableObject> objects2 = closest[1].getObjects();
		for(int k=0;k<objects1.size();k++){
			newObjects.add(objects1.get(k));
		}
		for(int l=0;l<objects2.size();l++){
			newObjects.add(objects2.get(l));
		}
		return newObjects;
	}
	
	private double[] computeAvgCluster(ArrayList<ClusterableObject> objects){
		double sumG=0.0;
		double sumM=0.0;
		double count=0.0;
		for(ClusterableObject object : objects){
			sumG+=releases.get(object.getId()).getNormGrowth();
			sumM+=releases.get(object.getId()).getNormMaintenance();
			count++;
		}
		double avgG=sumG/count;
		double avgM=sumM/count;
		double[] values = {avgG, avgM};
		return values;
	}
	
	private Cluster[] getClosestPair(){
		Cluster[] closest = new Cluster[2];
		
		ClusterSet currentSet = clusterSets.get(currentLevel);
		ArrayList<Cluster> currentClusters = currentSet.getClusters();
		
		double min=0;
		boolean flag=false;
		
		//currentClusters arraylist is not sorted thus the double for and the subsequent call
		
		for(int i=0;i<currentClusters.size();i++){
			for(int j=i+1;j<currentClusters.size();j++){

				boolean subSequent=checkIfSubsequent(currentClusters.get(i), currentClusters.get(j));
				if(subSequent==true){
				
					ArrayList<ClusterableObject> objects1 = currentClusters.get(i).getObjects();
					ArrayList<ClusterableObject> objects2 = currentClusters.get(j).getObjects();
					double sum=0.0;
					double count=0.0;
					for(int k=0;k<objects1.size();k++){
						for(int l=0;l<objects2.size();l++){
							sum+=distanceMatrices.get(currentLevel).getObjectDistance(objects1.get(k).getId(), objects2.get(l).getId());
							count++;
						}
					}
					double avg = sum / count;
					
					if(flag==false){
						flag=true;
						min=avg;
						closest[0]=currentClusters.get(i);
						closest[1]=currentClusters.get(j);
					}
					
					if(avg<min){
						min=avg;
						closest[0]=currentClusters.get(i);
						closest[1]=currentClusters.get(j);
					}
				}
			}
		}
		
		return closest;
		
	}
	
	private boolean checkIfSubsequent(Cluster c1, Cluster c2){
		boolean flag=false;
		
		ArrayList<ClusterableObject> obj1 = c1.getObjects();
		ArrayList<ClusterableObject> obj2 = c2.getObjects();
		
		ArrayList<Integer> sortedIds1 = new ArrayList<Integer>();
		ArrayList<Integer> sortedIds2 = new ArrayList<Integer>();
		
		for(int i=0;i<obj1.size();i++){
			sortedIds1.add(obj1.get(i).getId());
		}
		
		for(int i=0;i<obj2.size();i++){
			sortedIds2.add(obj2.get(i).getId());
		}
		
		Collections.sort(sortedIds1);
		Collections.sort(sortedIds2);
		
		if(sortedIds1.get(sortedIds1.size()-1) == sortedIds2.get(0) -1 ){
			flag=true;
		}
		else if(sortedIds2.get(sortedIds2.size()-1) == sortedIds1.get(0) -1){
			flag=true;
		}
	
		return flag;
	}
	
	private void updateMatrix(Cluster newCluster, Cluster[] closest){
		DistanceMatrix newMatrix= new DistanceMatrix();
		ClusterSet currentClusterSet = clusterSets.get(currentLevel);
		ArrayList<Cluster> currentClusters = currentClusterSet.getClusters();
		HashMap<String,Double> newDistances = new HashMap<String,Double>();
		HashMap<String,Double> newClusterDistances = new HashMap<String,Double>();
		
		for(int i=0;i<currentClusters.size();i++){
			for(int j=+1;j<currentClusters.size();j++){
				if(currentClusters.get(j).getId()!=closest[0].getId() && currentClusters.get(j).getId()!=closest[1].getId()){
					boolean subSequent=checkIfSubsequent(currentClusters.get(i), currentClusters.get(j));
					if(subSequent==true){
						ArrayList<ClusterableObject> objects1 = currentClusters.get(i).getObjects();
						ArrayList<ClusterableObject> objects2 = currentClusters.get(j).getObjects();
						
						double avg = computeAvgClusterDistance(currentClusters.get(i), currentClusters.get(j));
						
						for(int k=0;k<objects1.size();k++){
							for(int l=0;l<objects2.size();l++){
								String key1=objects1.get(k).getId()+";"+objects2.get(l).getId();
								String key2=objects2.get(l).getId()+";"+objects1.get(k).getId();
								newDistances.put(key1, avg);
								newDistances.put(key2, avg);
							}
						}
						newClusterDistances.put(currentClusters.get(i).getId()+";"+currentClusters.get(j).getId(), avg);
						newClusterDistances.put(currentClusters.get(j).getId()+";"+currentClusters.get(i).getId(), avg);
						
					}
				}
			}
		}
		
		newMatrix.setObjectMatrix(newDistances);
		
		distanceMatrices.put(currentLevel, newMatrix);
		
	}
	
	public double computeAvgClusterDistance(Cluster c1, Cluster c2){
		double distance=0.0;
		ArrayList<ClusterableObject> objects1 = c1.getObjects();
		ArrayList<ClusterableObject> objects2 = c2.getObjects();
		double count=0.0;
		double sum=0.0;
		for(int k=0;k<objects1.size();k++){
			for(int l=0;l<objects2.size();l++){
				count++;
				sum+=getEuclidean(releases.get(objects1.get(k).getId()), releases.get(objects2.get(l).getId()));
			}
		}		
		distance=sum/count;		
		return distance;
	}
	
	public double getEuclidean(Release r1, Release r2){
		double distance=0.0;
		double g1=r1.getNormGrowth();
		double g2=r2.getNormGrowth();
		double m1=r1.getNormMaintenance();
		double m2=r2.getNormMaintenance();
		double g=Math.pow((g1-g2), 2);
		double m=Math.pow((m1-m2), 2);
		distance=Math.sqrt((g+m));
		return distance;
	}
		
	public HashMap<Integer,ClusterSet> getClusterSets(){
		return clusterSets;
	}
	
}
