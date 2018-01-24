package javafx;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import dataManager.Release;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class HistogramMaker implements Initializable{
	@FXML
    private CategoryAxis gxAxis = new CategoryAxis();
	@FXML
	private NumberAxis gyAxis = new NumberAxis();
	@FXML
    private BarChart growthChart = new BarChart(gxAxis, gyAxis);
	@FXML
    private CategoryAxis mxAxis = new CategoryAxis();
	@FXML
	private NumberAxis myAxis = new NumberAxis();
	@FXML
    private BarChart maintenanceChart = new BarChart(mxAxis, myAxis);
	@FXML
	private Button saveChartButton = new Button();
	private XYChart.Series<Number, Number> gseries;
	private XYChart.Series<Number, Number> mseries;
	private String chartTitle;
	private HashMap<String,Double> gvalues;
	private HashMap<String,Double> mvalues;
	
	private ArrayList<String> intensities = new ArrayList<String>();
	
	private static double _ZERO_GRAPHICAL_OFFSET = 0.05;
	

	 public void init(String chartTitle, HashMap<String,Double> gvalues, HashMap<String,Double> mvalues) {
		 
		 intensities.add("Zero");
		 intensities.add("Low");
		 intensities.add("Medium");
		 intensities.add("High");
		 
		 this.chartTitle=chartTitle;
		 
		 this.gvalues=gvalues;
		 CategoryAxis gxAxis = new CategoryAxis();
		 NumberAxis gyAxis = new NumberAxis();
		 growthChart.setTitle("growth_"+chartTitle);
		 growthChart.setCenterShape(true); 
		 

		 this.mvalues=mvalues;
		 CategoryAxis mxAxis = new CategoryAxis();
		 NumberAxis myAxis = new NumberAxis();
		 maintenanceChart.setTitle("maintenance_"+chartTitle);
		 maintenanceChart.setCenterShape(true);
		 
		 initGrowth();
		 initMaintenance();		 
	 }
	 
	 public void initGrowth(){
        
       ArrayList<XYChart.Series> series = new ArrayList<XYChart.Series>();  
       
        for(String key: intensities){
        	XYChart.Series seriesX = new XYChart.Series();
        	seriesX.setName(key);
        	seriesX.getData().add(new XYChart.Data(key,(gvalues.get(key)*100)));  	
        	growthChart.getData().add(seriesX);
        }	
	 }
	 
	 public void initMaintenance(){
		ArrayList<XYChart.Series> series = new ArrayList<XYChart.Series>();  
				
        for(String key: intensities){
        	XYChart.Series seriesX = new XYChart.Series();
        	seriesX.setName(key);
        	seriesX.getData().add(new XYChart.Data(key,(mvalues.get(key)*100)));  	
        	maintenanceChart.getData().add(seriesX);
        }	
	 }
	 
	 @Override
	 public void initialize(URL location, ResourceBundle resources){}
	 
	 
	 public void saveCharts(ActionEvent event){
		WritableImage image1 = growthChart.snapshot(new SnapshotParameters(), null);
		WritableImage image2 = maintenanceChart.snapshot(new SnapshotParameters(), null);
	  	String imgName1= new String();
		String imgName2= new String();
	  	if(!chartTitle.equals("")){
	  		imgName1=chartTitle+".png";
	  	}
	  	else{
	  		imgName1="growthHistogram.png";
	  	}
	  	if(!chartTitle.equals("")){
	  		imgName2=chartTitle+".png";
	  	}
	  	else{
	  		imgName2="mrowthHistogram.png";
	  	}
	  	String filePath1="files/output/charts/growth_"+imgName1;
	    File file1 = new File(filePath1);
	    try {
	        ImageIO.write(SwingFXUtils.fromFXImage(image1, null), "png", file1);
	    }catch (IOException e){}
		String filePath2="files/output/charts/maintenance_"+imgName1;
	    File file2 = new File(filePath2);
	    try {
	        ImageIO.write(SwingFXUtils.fromFXImage(image2, null), "png", file2);
	    }catch (IOException e){}
	}

	
	
}