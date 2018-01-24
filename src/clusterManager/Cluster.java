package clusterManager;

import java.util.ArrayList;
import java.util.Collections;

public class Cluster {
	
	private ArrayList<ClusterableObject> objects;
	private int id;
	private int firstId;
	private int lastId;
	private double avgNormGrowth;
	private double avgNormMaintenance;
	
	public Cluster(int id, ArrayList<ClusterableObject> objects, double[] values){
		this.id=id;
		this.objects=objects;
		ArrayList<Integer> tmpID = new ArrayList<Integer>();
		for(ClusterableObject object : objects){
			tmpID.add(object.getId());
		}
		Collections.sort(tmpID);
		firstId=tmpID.get(0);
		lastId=tmpID.get(tmpID.size()-1);
		avgNormGrowth=values[0];
		avgNormMaintenance=values[1];
	}
	
	public double getAvgNormGrowth(){
		return avgNormGrowth;
	}
	
	public double getAvgNormMaintenance(){
		return avgNormMaintenance;
	}
	
	public int getFirstId(){
		return firstId;
	}
	
	public int getLastId(){
		return lastId;
	}
	
	public ArrayList<ClusterableObject> getObjects(){
		return objects;
	}
	
	public int getId(){
		return id;
	}

}
