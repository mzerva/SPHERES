package clusterManager;

import java.util.ArrayList;
import java.util.HashMap;

import dataManager.DistanceMatrix;
import dataManager.Release;

public class ClusteringEvaluator {
	private int level;
	private double cohesion;
	private double separation;
	private ArrayList<Cluster> currentClusters;
	private HashMap<Integer,Release> releases;
	
	public ClusteringEvaluator(int level, ArrayList<Cluster> currentClusters, HashMap<Integer,Release> releases){
		this.level=level;
		this.currentClusters=currentClusters;
		this.releases=releases;
	}
	
	public void process(){
		computeAvgCohesion();
		computeAvgSeparation();
	}
	
	public void computeAvgCohesion(){
		double totalSum=0.0;
		double totalCount=0.0;
		for(Cluster cluster : currentClusters){
			ArrayList<ClusterableObject> objects = cluster.getObjects();
			double sum=0.0;
			double count=0.0;
			for(ClusterableObject object : objects){
				count++;
				sum+=computeCohesion(cluster,object);
			}
			totalSum+=sum/count;
			totalCount++;
		}
		cohesion=totalSum/totalCount;
	}
	
	public double computeCohesion(Cluster cluster, ClusterableObject currentObject){
		double coh=0.0;
		ArrayList<ClusterableObject> objects = cluster.getObjects();
		double sum=0.0;
		double count=0.0;
		for(ClusterableObject object : objects){
			sum+=getEuclidean(releases.get(currentObject.getId()), releases.get(object.getId()));
			count++;
		}
		coh=-sum/count;
		return coh;
	}
	
	public void computeAvgSeparation(){
		double totalSum=0.0;
		double totalCount=0.0;
		for(Cluster cluster1 :currentClusters){
			for(Cluster cluster2 :currentClusters){
				if(checkIfNext(cluster1,cluster2)){
					totalSum+=computeSeperation(cluster1,cluster2);
					totalCount++;
				}
			}
		}
		separation=totalSum/totalCount;
	}
	
	public double computeSeperation(Cluster cluster1, Cluster cluster2){
		double sep=0.0;
		ArrayList<ClusterableObject> objects1 = cluster1.getObjects();
		ArrayList<ClusterableObject> objects2 = cluster2.getObjects();
		double sum=0.0;
		double count=0.0;
		for(ClusterableObject object1 : objects1){
			for(ClusterableObject object2 : objects2){
				sum+=getEuclidean(releases.get(object1.getId()), releases.get(object2.getId()));
				count++;
			}
		}
		sep=sum/count;
		return sep;
	}
	
	public boolean checkIfNext(Cluster c1, Cluster c2){
		boolean next=false;
		if(c1.getLastId()==c2.getFirstId()-1){
			next=true;
		}
		return next;
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
	
	public double getCohesion(){
		return cohesion;
	}
	
	public double getSeparation(){
		return separation;
	}
	
	public int getLevel(){
		return level;
	}
}
