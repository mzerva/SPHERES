package clusterManager;

import java.util.ArrayList;

public class ClusterSet {
	private ArrayList<Cluster> clusters;
	private int id;
	private double silhouette;
	private double cohesion;
	private double separation;
	
	public ClusterSet(int id, ArrayList<Cluster> clusters){
		this.id=id;
		this.clusters=clusters;
	}
	
	public ArrayList<Cluster> getClusters(){
		return clusters;
	}
	
	public int getId(){
		return id;
	}
	
	public void setSilhouette(double silhouette){
		this.silhouette=silhouette;
	}
	
	public double getSilhouette(){
		return silhouette;
	}
	
	public void setCohesion(double cohesion){
		this.cohesion=cohesion;
	}
	
	public double getCohesion(){
		return cohesion;
	}

	public void setSeparation(double separation){
		this.separation=separation;
	}
	
	public double getSeparation(){
		return separation;
	}
	
	public int getNumOfClusters(){
		return clusters.size();
	}
}
