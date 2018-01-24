package phaseManager;
import java.util.*;

public class DissimilarityManager {
	
	private ArrayList<Phase> phases = new ArrayList<Phase>();
	private double dissimilarity=0.0;
	
	public DissimilarityManager(ArrayList<Phase> phases){
		this.phases=phases;
	}
	
	public void process(){
		int counter=0;
		double dis=0.0;
		
		for(int i=0;i<phases.size()-1;i++){
			Histogram histogram1 = phases.get(i).getHistogram();
			HashMap<String,Double> growth1 = histogram1.getGrowth();
			HashMap<String,Double> maintenance1 = histogram1.getMaintenance();
			int j=i+1;
			//for(int j=i+1;j<phases.size();j++){
				counter++;
				double tempDis=0.0;
				Histogram histogram2 = phases.get(j).getHistogram();
				HashMap<String,Double> growth2 = histogram2.getGrowth();
				HashMap<String,Double> maintenance2 = histogram2.getMaintenance();
				
				for(String key: growth1.keySet()){
					//if(!key.equals("Low") && !key.equals("Zero")){
						tempDis+= Math.pow((growth1.get(key)-growth2.get(key)),2);
						tempDis+= Math.pow((maintenance1.get(key)-maintenance2.get(key)), 2);
					//}
				}
				
				dis+=Math.sqrt(tempDis);
				
			//}
		}
		
		dissimilarity+=(double)dis/(double)counter;
		
		if(counter==0){
			dissimilarity=0;
		}
	}
	
	public double getDissimilarity(){
		return dissimilarity;
	}

}
