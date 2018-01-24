package phaseManager;

public class PhaseClassifier {
	private int phaseID;
	private String winnerGrowth;
	private String winnerMaintenance;
	private String characterization;
	private boolean growthSpikes=false;
	private boolean maintenanceSpikes=false;
	
	public PhaseClassifier(int phaseID, String winnerGrowth, String winnerMaintenance){
		this.phaseID=phaseID;
		this.winnerGrowth=winnerGrowth;
		this.winnerMaintenance=winnerMaintenance;
	}
	
	public PhaseClassifier(int phaseID, String winnerGrowth, String winnerMaintenance, boolean growthSpikes, boolean maintenanceSpikes){
		this.phaseID=phaseID;
		this.winnerGrowth=winnerGrowth;
		this.winnerMaintenance=winnerMaintenance;
		this.growthSpikes=growthSpikes;
		this.maintenanceSpikes=maintenanceSpikes;
	}

	public void processCharacterization(){
		//RULES
		
		//ignore zero rule
		/*if(winnerGrowth.equals("Zero") && winnerMaintenance.equals("Zero")){
			characterization="Zero";
		}
		else */if( ( winnerGrowth.equals("Low") || winnerGrowth.equals("Zero") ) && ( winnerMaintenance.equals("Medium") || winnerMaintenance.equals("High") ) ){
			characterization=winnerMaintenance + " Maintenance";
		}
		else if( ( winnerGrowth.equals("Medium") || winnerGrowth.equals("High") ) && ( winnerMaintenance.equals("Low") || winnerMaintenance.equals("Zero") ) ){
			characterization=winnerGrowth + " Growth";
		}
		else if(( winnerGrowth.equals("Low") || winnerGrowth.equals("Zero") ) && ( winnerMaintenance.equals("Low") || winnerMaintenance.equals("Zero") ) ){
			characterization="Minor Activity";
			if(growthSpikes || maintenanceSpikes){
				characterization +=" with";
				if(growthSpikes){
					characterization+=" growth";
					if(maintenanceSpikes){
						characterization+= " and maintenance spike(s)";
					}
					else{
						characterization+=" spike(s)";
					}
				}
				else if(maintenanceSpikes){
					characterization+= " maintenance spike(s)";
				}
			}
			
		}
		else if(winnerGrowth.equals("Medium") && winnerMaintenance.equals("High")){
			characterization="Medium Growth - High Maintenance";
		}
		else if(winnerGrowth.equals("High") && winnerMaintenance.equals("Medium")){
			characterization="High Growth - Medium Maintenance";
		}
		else if(winnerGrowth.equals("Medium") && winnerMaintenance.equals("Medium")){
			characterization="Restructuring";
		}
		else if(winnerGrowth.equals("High") && winnerMaintenance.equals("High")){
			characterization="Intense Evolution";
		}		
	}
	
	public String getCharacterization(){
		return characterization;
	}
	
}
