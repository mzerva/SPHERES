package phaseManager;
import java.util.*;

import clusterManager.ClusterSet;

public class TopClusterSetsManager {
	private HashMap<Integer,ClusterSet> clusterSets= new HashMap<Integer,ClusterSet>();
	private double minCoh;
	private double maxCoh;
	private double minSep;
	private double maxSep;
	
	public TopClusterSetsManager(HashMap<Integer,ClusterSet> clusterSets){
		this.clusterSets=clusterSets;
	}
	
	public int[] getTopClusterSets(){
		int[] top = new int[3];
		
		ArrayList<Double> cohesionValues = new ArrayList<Double>();
		ArrayList<Double> separationValues = new ArrayList<Double>();
		
		for(Integer key: clusterSets.keySet()){
			ClusterSet current = clusterSets.get(key);
			//if(current.getClusters().size()>1){
				if(!Double.isNaN(current.getCohesion()) && !Double.isNaN(current.getSeparation())){
					cohesionValues.add(current.getCohesion());
					separationValues.add(current.getSeparation());
				}
				/*if(!Double.isNaN(current.getSeparation())){
					separationValues.add(current.getSeparation());
				}*/
			//}
		}
		
		Collections.sort(cohesionValues);
		Collections.sort(separationValues);
		
		minCoh=cohesionValues.get(0);
		maxCoh=cohesionValues.get(cohesionValues.size()-1);
		minSep=separationValues.get(0);
		maxSep=separationValues.get(separationValues.size()-1);
		
		ArrayList<Double> normalizedSums = new ArrayList<Double>();
		HashMap<Double,Integer> clusterMap = new HashMap<Double,Integer>();
		
		for(Integer key: clusterSets.keySet()){
			ClusterSet current = clusterSets.get(key);
			//if(current.getClusters().size()>1){
			if(!Double.isNaN(current.getCohesion()) && !Double.isNaN(current.getSeparation())){
				double normSum=getNormalizedSum(current.getCohesion(), current.getSeparation());
				clusterMap.put(normSum, key);
				normalizedSums.add(normSum);
			}
			//}
			//System.out.println("KEY: "+key+" , NORMSUM: "+normSum);
		}
		
		Collections.sort(normalizedSums);
		
		top[0]=clusterMap.get(normalizedSums.get(normalizedSums.size()-1));
		top[1]=clusterMap.get(normalizedSums.get(normalizedSums.size()-2));		
		top[2]=clusterMap.get(normalizedSums.get(normalizedSums.size()-3));
		
		return top;
	}
	
	public double getNormalizedSum(double cohesion, double separation){
		//System.out.println(maxSep);
		//System.out.println(minSep);
		double normCoh=0.0;
		double normSep=0.0;
		double normSum=0.0;
		
		normCoh=(minCoh-cohesion)/(minCoh-maxCoh);
		normSep=(separation-minSep)/(maxSep-minSep);
		//System.out.println(normCoh);
		//System.out.println(normSep);
		normSum=normCoh+normSep;
		
		return normSum;
	}
	
	

}
