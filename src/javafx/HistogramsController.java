package javafx;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import phaseManager.Histogram;

public class HistogramsController implements Initializable{
	
	private ArrayList<BarChart> barcharts = new ArrayList<BarChart>();
	
	@FXML
    private CategoryAxis g1xAxis = new CategoryAxis();
	@FXML
	private NumberAxis g1yAxis = new NumberAxis();
	@FXML
    private BarChart growth1 = new BarChart(g1xAxis, g1yAxis);
	
	@FXML
    private CategoryAxis g2xAxis = new CategoryAxis();
	@FXML
	private NumberAxis g2yAxis = new NumberAxis();
	@FXML
    private BarChart growth2 = new BarChart(g2xAxis, g2yAxis);
	
	@FXML
    private CategoryAxis g3xAxis = new CategoryAxis();
	@FXML
	private NumberAxis g3yAxis = new NumberAxis();
	@FXML
    private BarChart growth3 = new BarChart(g3xAxis, g3yAxis);
	
	@FXML
    private CategoryAxis g4xAxis = new CategoryAxis();
	@FXML
	private NumberAxis g4yAxis = new NumberAxis();
	@FXML
    private BarChart growth4 = new BarChart(g4xAxis, g4yAxis);
	
	@FXML
    private CategoryAxis g5xAxis = new CategoryAxis();
	@FXML
	private NumberAxis g5yAxis = new NumberAxis();
	@FXML
    private BarChart growth5 = new BarChart(g5xAxis, g5yAxis);
	
	@FXML
    private CategoryAxis g6xAxis = new CategoryAxis();
	@FXML
	private NumberAxis g6yAxis = new NumberAxis();
	@FXML
    private BarChart growth6 = new BarChart(g6xAxis, g6yAxis);

	
	@FXML
    private CategoryAxis m1xAxis = new CategoryAxis();
	@FXML
	private NumberAxis m1yAxis = new NumberAxis();
	@FXML
    private BarChart maintenance1 = new BarChart(m1xAxis, m1yAxis);
	
	@FXML
    private CategoryAxis m2xAxis = new CategoryAxis();
	@FXML
	private NumberAxis m2yAxis = new NumberAxis();
	@FXML
    private BarChart maintenance2 = new BarChart(m2xAxis, m2yAxis);
	
	@FXML
    private CategoryAxis m3xAxis = new CategoryAxis();
	@FXML
	private NumberAxis m3yAxis = new NumberAxis();
	@FXML
    private BarChart maintenance3 = new BarChart(m3xAxis, m3yAxis);
	
	@FXML
    private CategoryAxis m4xAxis = new CategoryAxis();
	@FXML
	private NumberAxis m4yAxis = new NumberAxis();
	@FXML
    private BarChart maintenance4 = new BarChart(m4xAxis, m4yAxis);
	
	@FXML
    private CategoryAxis m5xAxis = new CategoryAxis();
	@FXML
	private NumberAxis m5yAxis = new NumberAxis();
	@FXML
    private BarChart maintenance5 = new BarChart(m5xAxis, m5yAxis);
	
	@FXML
    private CategoryAxis m6xAxis = new CategoryAxis();
	@FXML
	private NumberAxis m6yAxis = new NumberAxis();
	@FXML
    private BarChart maintenance6 = new BarChart(m6xAxis, m6yAxis);
	
	private HashMap<Integer,String> titles = new HashMap<Integer,String>();
	private ArrayList<String> intensities = new ArrayList<String>();
	private HashMap<Integer,Histogram> histograms = new HashMap<Integer,Histogram>();
	private ArrayList<Integer> sortedIDs = new ArrayList<Integer>();
	
	
	public void init(ArrayList<Integer> sortedIDs, HashMap<Integer,String> titles,HashMap<Integer,Histogram> histograms) {
		
		barcharts.add(growth1); barcharts.add(growth2); barcharts.add(growth3); barcharts.add(growth4); barcharts.add(growth5); barcharts.add(growth6); 
		barcharts.add(maintenance1); barcharts.add(maintenance2); barcharts.add(maintenance3); barcharts.add(maintenance4); barcharts.add(maintenance5); barcharts.add(maintenance6);
		
		this.sortedIDs=sortedIDs;
		this.titles=titles;
		this.histograms=histograms;
		 
		intensities.add("Zero");
		intensities.add("Low");
		intensities.add("Medium");
		intensities.add("High");
		 
		int counter=0;
		
		for(Integer sortedID : sortedIDs){
			barcharts.get(counter).setTitle("growth_"+titles.get(sortedID));
			barcharts.get(counter+6).setTitle("maintenance_"+titles.get(sortedID));
			counter++;
		}
		 
		 initGrowths();
		 initMaintenances();		 
	 }

		public void initGrowths(){
			int counter=0;
			
			for(Integer sortedID : sortedIDs){
				HashMap<String,Double> gvalues = histograms.get(sortedID).getGrowth();
        
		       ArrayList<XYChart.Series> series = new ArrayList<XYChart.Series>();  
		       
		        for(String key: intensities){
		        	XYChart.Series seriesX = new XYChart.Series();
		        	seriesX.setName(key);
		        	seriesX.getData().add(new XYChart.Data(key,(gvalues.get(key)*100)));  	
		        	barcharts.get(counter).getData().add(seriesX);
		        	
		        }	
		        counter++;
			}
		 }
		 
		 public void initMaintenances(){
			 int counter=0;
				
				for(Integer sortedID : sortedIDs){
					HashMap<String,Double> mvalues = histograms.get(sortedID).getMaintenance();
	        
			       ArrayList<XYChart.Series> series = new ArrayList<XYChart.Series>();  
			       
			        for(String key: intensities){
			        	XYChart.Series seriesX = new XYChart.Series();
			        	seriesX.setName(key);
			        	seriesX.getData().add(new XYChart.Data(key,(mvalues.get(key)*100)));  	
			        	barcharts.get(counter+6).getData().add(seriesX);
			        	
			        }	
			        counter++;
				}
		 }
		 
		 @Override
		 public void initialize(URL location, ResourceBundle resources){}
	
}
