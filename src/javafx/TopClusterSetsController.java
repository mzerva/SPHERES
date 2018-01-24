package javafx;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Set;

import dataManager.Release;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.util.Callback;

public class TopClusterSetsController implements Initializable{
	
	@FXML
	private Label firstLabel;
	
	@FXML
	private Label secondLabel;
	
	@FXML
	private Label thirdLabel;
	
	@FXML
	private Label datasetLabel;
	
	@FXML
    private CategoryAxis x1Axis = new CategoryAxis();
	@FXML
	private NumberAxis y1Axis = new NumberAxis();
	@FXML
    private BarChart firstBarchart = new BarChart(x1Axis, y1Axis);
	private XYChart.Series<Number, Number> series1;
	private String chartTitle1;
	private HashMap<Integer,ArrayList<Release>> values1;
	
	@FXML
    private CategoryAxis x2Axis = new CategoryAxis();
	@FXML
	private NumberAxis y2Axis = new NumberAxis();
	@FXML
    private BarChart secondBarchart = new BarChart(x2Axis, y2Axis);
	private XYChart.Series<Number, Number> series2;
	private String chartTitle2;
	private HashMap<Integer,ArrayList<Release>> values2;
	
	@FXML
    private CategoryAxis x3Axis = new CategoryAxis();
	@FXML
	private NumberAxis y3Axis = new NumberAxis();
	@FXML
    private BarChart thirdBarchart = new BarChart(x3Axis, y3Axis);
	private XYChart.Series<Number, Number> series3;
	private String chartTitle3;
	private HashMap<Integer,ArrayList<Release>> values3;
	
	private ArrayList<String> seriesColours = new ArrayList<String>();	
	private static double _ZERO_GRAPHICAL_OFFSET = 0.05;
	
	@FXML
	private TableView firstTable = new TableView();
	private TableViewSelectionModel firstTableModel;
	private ArrayList<ObservableList<String>> rowsFirst;
	private ArrayList<ArrayList<String>> allResults1;
	
	@FXML
	private TableView secondTable = new TableView();
	private TableViewSelectionModel secondTableModel;
	private ArrayList<ObservableList<String>> rowsSecond;
	private ArrayList<ArrayList<String>> allResults2;
	
	@FXML
	private TableView thirdTable = new TableView();
	private TableViewSelectionModel thirdTableModel;
	private ArrayList<ObservableList<String>> rowsThird;
	private ArrayList<ArrayList<String>> allResults3;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		
	}
	
	public void init(String dataset, String[] chartTitles, ArrayList<HashMap<Integer,ArrayList<Release>>> values, ArrayList<ArrayList<ArrayList<String>>> allResults) { 
		firstLabel.setText("#1 solution:");
		secondLabel.setText("#2 solution:");
		thirdLabel.setText("#3 solution:");
		datasetLabel.setText(dataset.toUpperCase());
		
		allResults1=allResults.get(0);
		allResults2=allResults.get(1);
		allResults3=allResults.get(2);
		
		chartTitle1=chartTitles[0];
		chartTitle2=chartTitles[1];
		chartTitle3=chartTitles[2];
		
		values1=values.get(0);
		values2=values.get(1);
		values3=values.get(2);
		
		CategoryAxis x1Axis = new CategoryAxis();
        NumberAxis y1Axis = new NumberAxis();
        CategoryAxis x2Axis = new CategoryAxis();
        NumberAxis y2Axis = new NumberAxis();
        CategoryAxis x3Axis = new CategoryAxis();
        NumberAxis y3Axis = new NumberAxis();
        
        firstBarchart.setTitle(chartTitle1);
        firstBarchart.setCenterShape(true);
        secondBarchart.setTitle(chartTitle2);
        secondBarchart.setCenterShape(true);
        thirdBarchart.setTitle(chartTitle3);
        thirdBarchart.setCenterShape(true);
        
               
        //RAINBOW STYLE
        seriesColours.add("-fx-background-color: purple;");
        seriesColours.add("-fx-background-color: midnightblue;");
        seriesColours.add("-fx-background-color: seagreen;");
        seriesColours.add("-fx-background-color: crimson;");        
        seriesColours.add("-fx-background-color: slategray;");
        
        initializeBarCharts();
        fillTables();
        
	}
	
	public void fillTables(){
		String columnNames[]={"phaseID","firstRelease","lastRelease","zeroG","lowG","mediumG","highG","zeroM","lowM","mediumM","highM"
				,"growthWinner","maintenanceWinner","classification"};
		
		//fillFirstTable
		
		rowsFirst = new ArrayList<ObservableList<String>>();
		
		firstTable.getItems().clear();
		firstTable.getColumns().clear();
		for(int i=0;i<allResults1.size();i++){
			TableColumn [] tableColumns = new TableColumn[5];     
	        int columnIndex = 0;
            final int m = i;
            TableColumn col = new TableColumn(columnNames[i]);
            col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                   
               public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                             
                    return new SimpleStringProperty(param.getValue().get(m).toString());                       
                }                   
            });
            col.setSortable(false);
            firstTable.getColumns().addAll(col);
	        if(i==0){ 
				for(int j=0;j<allResults1.get(i).size();j++){
					ObservableList<String> row = FXCollections.observableArrayList();
					row.addAll(allResults1.get(i).get(j));
					rowsFirst.add(row);
				}
	        }
	        else{
	        	for(int j=0;j<allResults1.get(i).size();j++){
					rowsFirst.get(j).addAll(allResults1.get(i).get(j));
				}
	        }
		}
		for(int l=0;l<rowsFirst.size();l++){
			firstTable.getItems().add(rowsFirst.get(l));
		}
		
		//fillSecondTable
		
		rowsSecond = new ArrayList<ObservableList<String>>();
		
		secondTable.getItems().clear();
		secondTable.getColumns().clear();
		for(int i=0;i<allResults2.size();i++){
			TableColumn [] tableColumns = new TableColumn[5];     
	        int columnIndex = 0;
            final int m = i;
            TableColumn col = new TableColumn(columnNames[i]);
            col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                   
               public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                             
                    return new SimpleStringProperty(param.getValue().get(m).toString());                       
                }                   
            });
            col.setSortable(false);
            secondTable.getColumns().addAll(col);
	        if(i==0){ 
				for(int j=0;j<allResults2.get(i).size();j++){
					ObservableList<String> row = FXCollections.observableArrayList();
					row.addAll(allResults2.get(i).get(j));
					rowsSecond.add(row);
				}
	        }
	        else{
	        	for(int j=0;j<allResults2.get(i).size();j++){
					rowsSecond.get(j).addAll(allResults2.get(i).get(j));
				}
	        }
		}
		for(int l=0;l<rowsSecond.size();l++){
			secondTable.getItems().add(rowsSecond.get(l));
		}
		
		//fillThirdTable
		
		rowsThird = new ArrayList<ObservableList<String>>();
		
		thirdTable.getItems().clear();
		thirdTable.getColumns().clear();
		for(int i=0;i<allResults3.size();i++){
			TableColumn [] tableColumns = new TableColumn[5];     
	        int columnIndex = 0;
            final int m = i;
            TableColumn col = new TableColumn(columnNames[i]);
            col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                   
               public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                             
                    return new SimpleStringProperty(param.getValue().get(m).toString());                       
                }                   
            });
            col.setSortable(false);
            thirdTable.getColumns().addAll(col);
	        if(i==0){ 
				for(int j=0;j<allResults3.get(i).size();j++){
					ObservableList<String> row = FXCollections.observableArrayList();
					row.addAll(allResults3.get(i).get(j));
					rowsThird.add(row);
				}
	        }
	        else{
	        	for(int j=0;j<allResults3.get(i).size();j++){
					rowsThird.get(j).addAll(allResults3.get(i).get(j));
				}
	        }
		}
		for(int l=0;l<rowsThird.size();l++){
			thirdTable.getItems().add(rowsThird.get(l));
		}
		
	}
	
	public void initializeBarCharts(){ 
        ArrayList<Integer> clusterId1 = new ArrayList<Integer>();
        HashMap<Integer,Integer> clusterMap1 = new HashMap<Integer,Integer>();
        HashMap<Integer,Integer> last1 = new HashMap<Integer,Integer>();
        ArrayList<Integer> clusterId2 = new ArrayList<Integer>();
        HashMap<Integer,Integer> clusterMap2 = new HashMap<Integer,Integer>();
        HashMap<Integer,Integer> last2 = new HashMap<Integer,Integer>();
        ArrayList<Integer> clusterId3 = new ArrayList<Integer>();
        HashMap<Integer,Integer> clusterMap3 = new HashMap<Integer,Integer>();
        HashMap<Integer,Integer> last3 = new HashMap<Integer,Integer>();
        
        for(Integer key: values1.keySet()){
        	ArrayList<Integer> tmp = new ArrayList<Integer>();
        	for(int i=0;i<values1.get(key).size();i++){
        		tmp.add(values1.get(key).get(i).getId());
        	}
        	Collections.sort(tmp);
        	clusterId1.add(tmp.get(0));
        	clusterMap1.put(tmp.get(0), key);
        	last1.put(key, tmp.get(tmp.size()-1));
        }
        
        for(Integer key: values2.keySet()){
        	ArrayList<Integer> tmp = new ArrayList<Integer>();
        	for(int i=0;i<values2.get(key).size();i++){
        		tmp.add(values2.get(key).get(i).getId());
        	}
        	Collections.sort(tmp);
        	clusterId2.add(tmp.get(0));
        	clusterMap2.put(tmp.get(0), key);
        	last2.put(key, tmp.get(tmp.size()-1));
        }
        
        for(Integer key: values3.keySet()){
        	ArrayList<Integer> tmp = new ArrayList<Integer>();
        	for(int i=0;i<values3.get(key).size();i++){
        		tmp.add(values3.get(key).get(i).getId());
        	}
        	Collections.sort(tmp);
        	clusterId3.add(tmp.get(0));
        	clusterMap3.put(tmp.get(0), key);
        	last3.put(key, tmp.get(tmp.size()-1));
        }
        
        Collections.sort(clusterId1);
        Collections.sort(clusterId2);
        Collections.sort(clusterId3);
        
        ArrayList<XYChart.Series> series1 = new ArrayList<XYChart.Series>();
        ArrayList<Node> seriesNodes1 = new ArrayList<Node>(); 
        int counter1=-1;
        int seriesCounter1=0;
        
        ArrayList<XYChart.Series> series2 = new ArrayList<XYChart.Series>();
        ArrayList<Node> seriesNodes2 = new ArrayList<Node>(); 
        int counter2=-1;
        int seriesCounter2=0;
        
        ArrayList<XYChart.Series> series3 = new ArrayList<XYChart.Series>();
        ArrayList<Node> seriesNodes3 = new ArrayList<Node>(); 
        int counter3=-1;
        int seriesCounter3=0;
        
        //fill first barchart
        
        for(Integer cluster: clusterId1){
        	if(counter1<seriesColours.size()-2){        	
        		counter1++;
        	}
        	else if(counter1!=0){
        		counter1=0;
        	}	
        	int key=clusterMap1.get(cluster);
        	XYChart.Series seriesX = new XYChart.Series();
        	seriesX.setName("G:"+cluster+"-"+last1.get(key));
        	for(int i=0;i<values1.get(key).size();i++){
        		seriesX.getData().add(new XYChart.Data(values1.get(key).get(i).getDate(),(values1.get(key).get(i).getNormGrowth() + _ZERO_GRAPHICAL_OFFSET)));
        	}
        	firstBarchart.getData().add(seriesX);
        	String seriesStyle=".series";
			seriesStyle+=seriesCounter1;
			Set<Node> nodes = firstBarchart.lookupAll(seriesStyle); 
		    for (Node n : nodes) {
		        n.setStyle(seriesColours.get(counter1));
		        seriesNodes1.add(n);
		    }
		    seriesCounter1++;
        	XYChart.Series seriesZ = new XYChart.Series();
        	seriesZ.setName("M:"+cluster+"-"+last1.get(key));	
        	for(int i=0;i<values1.get(key).size();i++){
        		seriesZ.getData().add(new XYChart.Data(values1.get(key).get(i).getDate(),( (-1) * values1.get(key).get(i).getNormMaintenance()  ) - _ZERO_GRAPHICAL_OFFSET ));
        	}
        	firstBarchart.getData().add(seriesZ);
        	seriesStyle=".series";
			seriesStyle+=seriesCounter1;
			nodes = firstBarchart.lookupAll(seriesStyle); 
		    for (Node n : nodes) {
		        n.setStyle(seriesColours.get(counter1));
		        seriesNodes1.add(n);
		    }
		    seriesCounter1++;        	       	 
        }
        
        //fill second barchart
        
        for(Integer cluster: clusterId2){
        	if(counter2<seriesColours.size()-2){        	
        		counter2++;
        	}
        	else if(counter2!=0){
        		counter2=0;
        	}	
        	int key=clusterMap2.get(cluster);
        	XYChart.Series seriesX = new XYChart.Series();
        	seriesX.setName("G:"+cluster+"-"+last2.get(key));
        	for(int i=0;i<values2.get(key).size();i++){
        		seriesX.getData().add(new XYChart.Data(values2.get(key).get(i).getDate(),(values2.get(key).get(i).getNormGrowth() + _ZERO_GRAPHICAL_OFFSET)));
        	}
        	secondBarchart.getData().add(seriesX);
        	String seriesStyle=".series";
			seriesStyle+=seriesCounter2;
			Set<Node> nodes = secondBarchart.lookupAll(seriesStyle); 
		    for (Node n : nodes) {
		        n.setStyle(seriesColours.get(counter2));
		        seriesNodes2.add(n);
		    }
		    seriesCounter2++;
        	XYChart.Series seriesZ = new XYChart.Series();
        	seriesZ.setName("M:"+cluster+"-"+last2.get(key));	
        	for(int i=0;i<values2.get(key).size();i++){
        		seriesZ.getData().add(new XYChart.Data(values2.get(key).get(i).getDate(),( (-1) * values2.get(key).get(i).getNormMaintenance()  ) - _ZERO_GRAPHICAL_OFFSET ));
        	}
        	secondBarchart.getData().add(seriesZ);
        	seriesStyle=".series";
			seriesStyle+=seriesCounter2;
			nodes = secondBarchart.lookupAll(seriesStyle); 
		    for (Node n : nodes) {
		        n.setStyle(seriesColours.get(counter2));
		        seriesNodes2.add(n);
		    }
		    seriesCounter2++;        	       	 
        }
        
        //fill third barchart
        
        for(Integer cluster: clusterId3){
        	if(counter3<seriesColours.size()-2){        	
        		counter3++;
        	}
        	else if(counter3!=0){
        		counter3=0;
        	}	
        	int key=clusterMap3.get(cluster);
        	XYChart.Series seriesX = new XYChart.Series();
        	seriesX.setName("G:"+cluster+"-"+last3.get(key));
        	for(int i=0;i<values3.get(key).size();i++){
        		seriesX.getData().add(new XYChart.Data(values3.get(key).get(i).getDate(),(values3.get(key).get(i).getNormGrowth() + _ZERO_GRAPHICAL_OFFSET)));
        	}
        	thirdBarchart.getData().add(seriesX);
        	String seriesStyle=".series";
			seriesStyle+=seriesCounter3;
			Set<Node> nodes = thirdBarchart.lookupAll(seriesStyle); 
		    for (Node n : nodes) {
		        n.setStyle(seriesColours.get(counter3));
		        seriesNodes3.add(n);
		    }
		    seriesCounter3++;
        	XYChart.Series seriesZ = new XYChart.Series();
        	seriesZ.setName("M:"+cluster+"-"+last3.get(key));	
        	for(int i=0;i<values3.get(key).size();i++){
        		seriesZ.getData().add(new XYChart.Data(values3.get(key).get(i).getDate(),( (-1) * values3.get(key).get(i).getNormMaintenance()  ) - _ZERO_GRAPHICAL_OFFSET ));
        	}
        	thirdBarchart.getData().add(seriesZ);
        	seriesStyle=".series";
			seriesStyle+=seriesCounter3;
			nodes = thirdBarchart.lookupAll(seriesStyle); 
		    for (Node n : nodes) {
		        n.setStyle(seriesColours.get(counter3));
		        seriesNodes3.add(n);
		    }
		    seriesCounter3++;        	       	 
        }
        
        firstBarchart.setLegendVisible(false);
        secondBarchart.setLegendVisible(false);
        thirdBarchart.setLegendVisible(false);
	 }

}
