package dataManager;

import java.util.*;

public class DistanceMatrix {

	private HashMap<String,Double> objectDistances = new HashMap<String,Double>();
	private HashMap<Integer,Release> releases = new HashMap<Integer,Release>();
	private static double inf=1000.0;
	
	public DistanceMatrix(HashMap<Integer,Release> releases){
		this.releases=releases;
	}
	
	public DistanceMatrix(){}
	
	
	public void fillMatrix(){
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for(Integer id : releases.keySet()){
			ids.add(id);
		}
		Collections.sort(ids);
		
		for(int i=0;i<ids.size();i++){
			int previous=i-1;
			int next=i+1;
			if(i>0){
				Release current1 = releases.get(i);
				Release current2 = releases.get(previous);
				double distance=computeDistance(current1,current2);
				objectDistances.put(current1.getId()+";"+current2.getId(), distance);
				objectDistances.put(current2.getId()+";"+current1.getId(), distance);
			}
			if(i<ids.size()-1){
				Release current1 = releases.get(i);
				Release current2 = releases.get(next);
				double distance=computeDistance(current1,current2);
				objectDistances.put(current1.getId()+";"+current2.getId(), distance);
				objectDistances.put(current2.getId()+";"+current1.getId(), distance);
			}
			for(int j=0;j<ids.size();j++){
				if(i==j){
					Release current1 = releases.get(i);
					Release current2 = releases.get(j);
					objectDistances.put(current1.getId()+";"+current2.getId(), 0.0);
					objectDistances.put(current2.getId()+";"+current1.getId(), 0.0);
				}
				else if(j!=previous && j!=next){
					Release current1 = releases.get(i);
					Release current2 = releases.get(j);
					objectDistances.put(current1.getId()+";"+current2.getId(), inf);
					objectDistances.put(current2.getId()+";"+current1.getId(), inf);
				}
			}
		}
		
	}
	
	public double computeDistance(Release r1, Release r2){
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
	
	public void setObjectMatrix(HashMap<String,Double> distances){
		this.objectDistances=distances;
	}
	
	public double getObjectDistance(int i,int j){
		return objectDistances.get(i+";"+j);
	}
		
}
