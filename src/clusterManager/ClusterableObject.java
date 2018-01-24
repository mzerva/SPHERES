package clusterManager;

public class ClusterableObject {
	private Object theObject;
	private int id = -1;
	
	public Object getObject(){
		return theObject;
	}
	public int getId(){ 
		return id;
	}
	public void setId(int n){ 
		id=n; 
	}
	
	
}
