package javafx;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Set;

import javax.imageio.ImageIO;

import com.sun.javafx.charts.Legend;

import dataManager.Release;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;


public class BarChartMaker implements Initializable{
	@FXML
    private CategoryAxis xAxis = new CategoryAxis();
	@FXML
	private NumberAxis yAxis = new NumberAxis();
	@FXML
    private BarChart barChart = new BarChart(xAxis, yAxis);
	@FXML
	private Button saveChartButton = new Button();
	private XYChart.Series<Number, Number> series;
	private String chartTitle;
	private HashMap<Integer,ArrayList<Release>> values;
	private ArrayList<String> seriesColours = new ArrayList<String>();
	private boolean date=false;
	
	private static double _ZERO_GRAPHICAL_OFFSET = 0.05;
	

	 public void init(boolean date, String chartTitle, HashMap<Integer,ArrayList<Release>> values) { 
		 
		this.date=date;
		 
		this.chartTitle=chartTitle;
		this.values=values;
		CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        barChart.setTitle(chartTitle);
        //barChart.setBarGap(0);
        //barChart.setSnapToPixel(true);
        barChart.setCenterShape(true);
        
        //System.out.println(barChart.getBarGap());
        //barChart.setBarGap(2.0);
               
        //RAINBOW STYLE
        seriesColours.add("-fx-background-color: purple;");
        //seriesColours.add("-fx-background-color: slategray;");
        seriesColours.add("-fx-background-color: midnightblue;");
        //seriesColours.add("-fx-background-color: slategray;");
        seriesColours.add("-fx-background-color: seagreen;");
        //seriesColours.add("-fx-background-color: slategray;");
        seriesColours.add("-fx-background-color: crimson;");
        //seriesColours.add("-fx-background-color: slategray;");
      
        
        //STYLE 1
        /*seriesColours.add("-fx-background-color: orangered;");
        seriesColours.add("-fx-background-color: orange;"); 
        seriesColours.add("-fx-background-color: mediumseagreen;");
        seriesColours.add("-fx-background-color: cornflowerblue;");*/
        
        //STYLE 2
        /*seriesColours.add("-fx-background-color: orangered;");
        seriesColours.add("-fx-background-color: slategray;"); 
        seriesColours.add("-fx-background-color: orange;"); 
        seriesColours.add("-fx-background-color: slategray;");
        seriesColours.add("-fx-background-color: limegreen;");
        seriesColours.add("-fx-background-color: slategray;");
        seriesColours.add("-fx-background-color: deepskyblue;");
        seriesColours.add("-fx-background-color: slategray;");*/
        
        
        //STYLE 3
        /*seriesColours.add("-fx-background-color: mediumvioletred;");
        seriesColours.add("-fx-background-color: slategray;");
        seriesColours.add("-fx-background-color: purple;");
        seriesColours.add("-fx-background-color: slategray;"); */    
        
        seriesColours.add("-fx-background-color: slategray;");

        
        ArrayList<Integer> clusterId = new ArrayList<Integer>();
        HashMap<Integer,Integer> clusterMap = new HashMap<Integer,Integer>();
        HashMap<Integer,Integer> last = new HashMap<Integer,Integer>();
        
        for(Integer key: values.keySet()){
        	ArrayList<Integer> tmp = new ArrayList<Integer>();
        	for(int i=0;i<values.get(key).size();i++){
        		tmp.add(values.get(key).get(i).getId());
        	}
        	Collections.sort(tmp);
        	clusterId.add(tmp.get(0));
        	clusterMap.put(tmp.get(0), key);
        	last.put(key, tmp.get(tmp.size()-1));
        }
        
        Collections.sort(clusterId);
        
        ArrayList<XYChart.Series> series = new ArrayList<XYChart.Series>();
        
        ArrayList<Node> seriesNodes = new ArrayList<Node>();
        
        int counter=-1;
        int seriesCounter=0;
        
        for(Integer cluster: clusterId){
        	
        	if(counter<seriesColours.size()-2){        	
        		counter++;
        	}
        	else if(counter!=0){
        		counter=0;
        	}
        	
        	int key=clusterMap.get(cluster);
        	
        	XYChart.Series seriesX = new XYChart.Series();
        	seriesX.setName("G:"+cluster+"-"+last.get(key));
        	for(int i=0;i<values.get(key).size();i++){
        		if(date==false){
        			String nameFields[] = values.get(key).get(i).getName().split("/");
        			seriesX.getData().add(new XYChart.Data(nameFields[nameFields.length-1],(values.get(key).get(i).getNormGrowth() + _ZERO_GRAPHICAL_OFFSET)));
        		}
        		else{
        			seriesX.getData().add(new XYChart.Data(values.get(key).get(i).getDate(),(values.get(key).get(i).getNormGrowth() + _ZERO_GRAPHICAL_OFFSET)));
        		}
        	}
        	
        	barChart.getData().add(seriesX);
        	
        	String seriesStyle=".series";
			seriesStyle+=seriesCounter;
			Set<Node> nodes = barChart.lookupAll(seriesStyle); 
		    for (Node n : nodes) {
		        n.setStyle(seriesColours.get(counter));
		        seriesNodes.add(n);
		    }
        	
		    seriesCounter++;
        	
        	XYChart.Series seriesZ = new XYChart.Series();
        	seriesZ.setName("M:"+cluster+"-"+last.get(key));
        	
        	for(int i=0;i<values.get(key).size();i++){
        		if(date==false){
        			String nameFields[] = values.get(key).get(i).getName().split("/");
        			seriesZ.getData().add(new XYChart.Data(nameFields[nameFields.length-1],( (-1) * values.get(key).get(i).getNormMaintenance()  ) - _ZERO_GRAPHICAL_OFFSET ));
        		}
        		else{
        			seriesZ.getData().add(new XYChart.Data(values.get(key).get(i).getDate(),( (-1) * values.get(key).get(i).getNormMaintenance()  ) - _ZERO_GRAPHICAL_OFFSET ));
        		}
        	}
        	
        	barChart.getData().add(seriesZ);
        	
        	seriesStyle=".series";
			seriesStyle+=seriesCounter;
			nodes = barChart.lookupAll(seriesStyle); 
		    for (Node n : nodes) {
		        n.setStyle(seriesColours.get(counter));
		        seriesNodes.add(n);
		    }
        	
		    seriesCounter++;
        	        	       	
		   
		    
		    
        }
        
        barChart.setLegendVisible(false);
	 }
	 
	 
	 @Override
	 public void initialize(URL location, ResourceBundle resources){}
	 
	 
	 public void saveChart(ActionEvent event){
		  WritableImage image = barChart.snapshot(new SnapshotParameters(), null);
		  	String imgName= new String();
		  	if(!chartTitle.equals("")){
		  		imgName=chartTitle+".png";
		  	}
		  	else{
		  		imgName="BarChart.png";
		  	}
		  	String filePath="files/output/charts/"+imgName;
		    File file = new File(filePath);
		    try {
		        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
		    }catch (IOException e){}
	}

	
	
}