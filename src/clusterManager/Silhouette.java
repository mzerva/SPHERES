package clusterManager;

import java.util.ArrayList;
import java.util.HashMap;

import dataManager.DistanceMatrix;
import dataManager.Release;

public class Silhouette {
	
	private int level;
	private double value;
	private ClusterSet currentClusterSet;
	private ArrayList<Cluster> currentClusters;
	private DistanceMatrix initialMatrix;
	private HashMap<Integer,Release> releases;
	
	public Silhouette(ArrayList<Cluster> currentClusters, HashMap<Integer,Release> releases,DistanceMatrix initialMatrix){
		this.initialMatrix=initialMatrix;
		this.currentClusters=currentClusters;
		this.releases=releases;
	}
	
	public void computeSilhouetteValues(){
		double allSilhouettes=0.0;
		int countObjects=0;
		
		for(int i=0;i<currentClusters.size();i++){
			ArrayList<ClusterableObject> obj1 = currentClusters.get(i).getObjects();
			countObjects+=obj1.size();
			for(int k=0;k<obj1.size();k++){
				
				double silhouetteI=0.0;
				double ai=computeA(obj1.get(k),currentClusters.get(i));
				double bi=computeB(obj1.get(k),i);
			
				if(obj1.size()==1){
					silhouetteI=0;
				}
				else{
					double max=0.0;
					if(bi>ai){
						max=bi;
						silhouetteI=(bi-ai)/max;
					}
					else if (ai>bi){
						max=ai;
						silhouetteI=(bi-ai)/max;
					}
					else{
						silhouetteI=0;
					}
				}
				allSilhouettes+=silhouetteI;
			}
		}
		
		value=allSilhouettes/countObjects;
	}
	
	public double computeA(ClusterableObject currentObject, Cluster currentCluster){
		double sum=0.0;
		double ai=0.0;
		ArrayList<ClusterableObject> obj2 = currentCluster.getObjects();
		for(int l=0;l<obj2.size();l++){
			sum+=getEuclidean(releases.get(currentObject.getId()), releases.get(obj2.get(l).getId()));
		}
		ai=sum/currentCluster.getObjects().size();
		return ai;
	}
	
	public double computeB(ClusterableObject currentObject, int  currentClusterId){
		double bi=0.0;
		boolean flag=false;
		
		for(int j=0;j<currentClusters.size();j++){
			ArrayList<ClusterableObject> obj2 = currentClusters.get(j).getObjects();
			if(checkIfNext(currentClusters.get(currentClusterId), currentClusters.get(j))){
				double tempBi=0.0;
				double sum=0.0;
				for(int l=0;l<obj2.size();l++){
					sum+=getEuclidean(releases.get(currentObject.getId()), releases.get(obj2.get(l).getId()));
				}
				tempBi=sum/obj2.size();
				if(flag==false){
					flag=true;
					bi=tempBi;
				}
				else{
					if(tempBi<bi){
						bi=tempBi;
					}
				}
			}
		}
		
		return bi;
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
	
	public double getSilhouetteValue(){
		return value;
	}

}
