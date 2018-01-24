package javafx;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import clusterManager.ClusterSet;
import clusterManager.ClusteringManager;
import dataManager.ClusterSetExporter;
import dataManager.ClusteredDataHandler;
import dataManager.HistogramComputer;
import dataManager.Release;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener.Change;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import phaseManager.DissimilarityManager;
import phaseManager.Histogram;
import phaseManager.Phase;
import phaseManager.PhaseExporter;
import phaseManager.PhaseSet;
import phaseManager.TopClusterSetsManager;

public class MainGUIController implements Initializable {
		
	@FXML
	private Button clusterButton;
	
	@FXML
	private Button chartButton;
	
	@FXML
	private Button extractPhases;
	
	@FXML
	private Button topClusterButton;
	
	@FXML
	private ChoiceBox datasetChoiceBox= new ChoiceBox();
	private ObservableList<String> datasetItems = FXCollections.observableArrayList();
	private SingleSelectionModel<String> model;
	
	@FXML
	private TableView levelTableView = new TableView();
	private TableViewSelectionModel tableModel;
	private ArrayList<Integer> Columns = new ArrayList<Integer>();
	private ArrayList<Integer> Rows = new ArrayList<Integer>();
	private ArrayList<ObservableList<String>> rows;
	private int currentRow;
	
	private HashMap<Integer,ClusterSet> clusterSets = new HashMap<Integer,ClusterSet>();
	private HashMap<Integer, Release> releases = new HashMap<Integer, Release>();
	private HashMap<Integer,HashMap<Integer,ArrayList<Release>>> releaseMap;
	
	private String currentDataset="";
	private HashMap<String,String> levelMap = new HashMap<String,String>();
	
	public void initializeTable(){
		levelTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		levelTableView.getSelectionModel().setCellSelectionEnabled(true);
		tableModel=levelTableView.getSelectionModel();
		final ObservableList<TablePosition> selectedCells = levelTableView.getSelectionModel().getSelectedCells();
		selectedCells.addListener(new ListChangeListener<TablePosition>() {
		    @Override
		    public void onChanged(Change change) {
		    	Columns = new ArrayList<Integer>();
		    	Rows = new ArrayList<Integer>();
		        for (TablePosition pos : selectedCells) {
		        	Columns.add(pos.getColumn());
		        	Rows.add(pos.getRow());
		        	currentRow=pos.getRow();
		        }
		    }
		});
		rows = new ArrayList<ObservableList<String>>();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		model=datasetChoiceBox.getSelectionModel();	
		datasetItems.add("biosql");datasetItems.add("ensembl");datasetItems.add("mediawiki");
		datasetItems.add("opencart");datasetItems.add("phpbb");datasetItems.add("typo3");
		datasetChoiceBox.setItems(datasetItems);
		datasetChoiceBox.getSelectionModel().selectedIndexProperty()
        .addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue ov, Number value, Number new_value) {
            	try{
            		currentDataset=datasetItems.get(new_value.intValue());
            	}catch (Exception e) {}
            }
          });
		

		initializeTable();
		
	}
	
	public void fillTable(ArrayList<ArrayList<String>> allResults){
		initializeTable();
		levelTableView.getItems().clear();
		levelTableView.getColumns().clear();
		String columnNames[]={"level","numOfClusters","cohesion","separation","silhouette"};
		for(int i=0;i<allResults.size();i++){
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
            levelTableView.getColumns().addAll(col);
	        if(i==0){ 
				for(int j=0;j<allResults.get(i).size();j++){
					ObservableList<String> row = FXCollections.observableArrayList();
					row.addAll(allResults.get(i).get(j));
					rows.add(row);
				}
	        }
	        else{
	        	for(int j=0;j<allResults.get(i).size();j++){
					rows.get(j).addAll(allResults.get(i).get(j));
				}
	        }
		}
		for(int l=0;l<rows.size();l++){
			levelTableView.getItems().add(rows.get(l));
		}
	}
	
	public void performClustering(ActionEvent event){
		if(currentDataset.equals("")){
			showWarning("Warning","No dataset selected!");
		}
		else{
			ClusteringManager manager = new ClusteringManager(currentDataset);
			manager.process();
			clusterSets=manager.getClusterSets();
			releases=manager.getReleases();
			releaseMap = manager.matchReleases();
			updateListView(currentDataset);
			showWarning("","Clustering Completed!");
		}
	}
	
	public void updateListView(String dataset){		
		ArrayList<ArrayList<String>> allResults = new ArrayList<ArrayList<String>>();
		
		ArrayList<Integer> levels = new ArrayList<Integer>();
		for(Integer key : clusterSets.keySet()){
			levels.add(key);
		}
		Collections.sort(levels);
		
		ArrayList<String> level = new ArrayList<String>();
		ArrayList<String> numClusters = new ArrayList<String>();
		ArrayList<String> cohesion = new ArrayList<String>();
		ArrayList<String> separation = new ArrayList<String>();
		ArrayList<String> silhouette = new ArrayList<String>();
		for(int i=0;i<levels.size();i++){
			
			level.add(currentDataset+" : "+levels.get(i));
			numClusters.add(String.valueOf(clusterSets.get(levels.get(i)).getNumOfClusters()));
			cohesion.add(String.valueOf(clusterSets.get(levels.get(i)).getCohesion()));
			separation.add(String.valueOf(clusterSets.get(levels.get(i)).getSeparation()));
			silhouette.add(String.valueOf(clusterSets.get(levels.get(i)).getSilhouette()));
			
		}
		allResults.add(level);
		allResults.add(numClusters);
		allResults.add(cohesion);
		allResults.add(separation);
		allResults.add(silhouette);
		
		fillTable(allResults);
	}
	
	public void extractPhases(ActionEvent event){
		if(levelTableView.getSelectionModel().getSelectedItems().size()==0){
			showWarning("Warning","No level selected!");
		}
		else{
			int row=currentRow;
			String title=currentDataset+": "+row;
			System.out.println("*** Phase Characterization of: "+title+" ***");
			System.out.println();
			
			HashMap<Integer,ArrayList<Release>> currentReleases = releaseMap.get(row);
			ClusterSetExporter clusterExporter = new ClusterSetExporter(title,currentReleases);
			clusterExporter.process();
			
			PhaseSet phaseSet = new PhaseSet(title,currentReleases);
			ArrayList<Phase> phases=phaseSet.computePhases();
			
			PhaseExporter exporter = new PhaseExporter(title,phases);
			exporter.process();
			
			showWarning("","Phases Extracted!");
		}
	}
	
	
	private HashMap<Integer,Integer> sortPhaseIDs(HashMap<Integer,ArrayList<Release>> currentReleaseMap){
		HashMap<Integer,Integer> phaseIDsMap = new HashMap<Integer,Integer>();
		HashMap<Integer,Integer> tmpMap = new HashMap<Integer,Integer>();
		ArrayList<Integer> starters = new ArrayList<Integer>();
		
		for(Integer key : currentReleaseMap.keySet()){
			ArrayList<Integer> tmp = new ArrayList<Integer>();
			for(Release release : currentReleaseMap.get(key)){
				tmp.add(release.getId());
			}
			Collections.sort(tmp);
			starters.add(tmp.get(0));
			tmpMap.put(tmp.get(0), key);
		}
		Collections.sort(starters);
		
		int counter=0;
		for(Integer start : starters){
			phaseIDsMap.put(tmpMap.get(start), counter);
			counter++;
		}
		
		return phaseIDsMap;
	}
	
	public void seeChart(ActionEvent event){
		
		
		if(levelTableView.getSelectionModel().getSelectedItems().size()==0){
			showWarning("Warning","No level selected!");
		}
		else{
			int row=currentRow;
			String title=currentDataset+": "+row;
			HashMap<Integer,ArrayList<Release>> currentReleases = releaseMap.get(row);
			HashMap<Integer,Integer> phaseIDsMap = sortPhaseIDs(currentReleases);		
			
			try{
				FXMLLoader loader = new FXMLLoader();
		        loader.setLocation((getClass().getResource("BarChart.fxml")));
		        AnchorPane page = (AnchorPane) loader.load();
		        page.setStyle("-fx-background-color: transparent;");
		        Stage dialogStage = new Stage();
		        Scene scene = new Scene(page);
		        scene.getStylesheets().add((getClass().getResource("chartStyle.css").toExternalForm()));
		        dialogStage.setScene(scene);
		        dialogStage.setTitle("BarChart with Release Names");
		        BarChartMaker bc = loader.getController();
				bc.init(false,title,releaseMap.get(row));
		        dialogStage.show();
		        
		        FXMLLoader loader1 = new FXMLLoader();
		        loader1.setLocation((getClass().getResource("BarChart.fxml")));
		        AnchorPane page1 = (AnchorPane) loader1.load();
		        page1.setStyle("-fx-background-color: transparent;");
		        Stage dialogStage1 = new Stage();
		        Scene scene1 = new Scene(page1);
		        scene1.getStylesheets().add((getClass().getResource("chartStyle.css").toExternalForm()));
		        dialogStage1.setScene(scene1);
		        dialogStage1.setTitle("BarChart with Release Dates");
		        BarChartMaker bc1 = loader1.getController();
				bc1.init(true,title,releaseMap.get(row));
		        dialogStage1.show();
		        
		        
		    }catch (IOException e) {
		        e.printStackTrace();
		    }
			
			if(currentReleases.keySet().size()>6){
			
				/*for(Integer vkey: v.keySet()){
				
					HistogramComputer hComputer = new HistogramComputer(phaseIDsMap.get(vkey), v.get(vkey));
					hComputer.processGrowth();
					hComputer.processMaintenance();
					Histogram histogram = hComputer.getHistogram();
					
					HashMap<String,Double> growth = histogram.getGrowth();
					HashMap<String,Double> maintenance = histogram.getMaintenance();
					
					String gTitle="cluster: "+phaseIDsMap.get(vkey);
					try{
						FXMLLoader loader = new FXMLLoader();
				        loader.setLocation((getClass().getResource("Histogram.fxml")));
				        AnchorPane page = (AnchorPane) loader.load();
				        Stage dialogStage = new Stage();
				        Scene scene = new Scene(page);
				        dialogStage.setScene(scene);
				        dialogStage.setTitle("Histogram");
				        HistogramMaker hc = loader.getController();
						hc.init(gTitle,growth,maintenance);
						
				        dialogStage.show();
				    }catch (IOException e) {
				        e.printStackTrace();
				    }
				}*/
			}
			else{
				HashMap<Integer,Histogram> histograms = new HashMap<Integer,Histogram>();
				HashMap<Integer,String> titles = new HashMap<Integer,String>();
				ArrayList<Integer> sortedIDs = new ArrayList<Integer>();
				for(Integer vkey: currentReleases.keySet()){
					
					HistogramComputer hComputer = new HistogramComputer(phaseIDsMap.get(vkey), currentReleases.get(vkey));
					hComputer.processGrowth();
					hComputer.processMaintenance();
					Histogram histogram = hComputer.getHistogram();
					histograms.put(phaseIDsMap.get(vkey),histogram);
					titles.put(phaseIDsMap.get(vkey),"cluster: "+phaseIDsMap.get(vkey));
					sortedIDs.add(phaseIDsMap.get(vkey));
					
				}
				
				Collections.sort(sortedIDs);
				
				try{
					FXMLLoader loader = new FXMLLoader();
			        loader.setLocation((getClass().getResource("Histograms.fxml")));
			        AnchorPane page = (AnchorPane) loader.load();
			        page.setStyle("-fx-background-color: transparent;");
			        Stage dialogStage = new Stage();
			        Scene scene = new Scene(page);
			        scene.getStylesheets().add((getClass().getResource("chartStyle.css").toExternalForm()));
			        dialogStage.setScene(scene);
			        dialogStage.setTitle("Histograms");
			        HistogramsController hc = loader.getController();
					hc.init(sortedIDs,titles,histograms);
					
			        dialogStage.show();
			    }catch (IOException e) {
			        e.printStackTrace();
			    }
				
			}
			
		}	
	}
	
	
	public void showWarning(String title,String warning){
    	try{
			FXMLLoader loader = new FXMLLoader();
	        loader.setLocation((getClass().getResource("Warnings.fxml")));
	        AnchorPane page = (AnchorPane) loader.load();
	        Stage dialogStage = new Stage();
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        dialogStage.setTitle(title);
	        WarningsController wC = loader.getController();
	        wC.setWarning(warning);
	        dialogStage.showAndWait();	   
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
    }
	
	public void getTopClusters(ActionEvent event){
		if(currentDataset.equals("") || releaseMap==null){
			showWarning("Warning","No dataset selected for clustering!");
		}
		else{
			int[] top;
			TopClusterSetsManager manager = new TopClusterSetsManager(clusterSets);
			top=manager.getTopClusterSets();
		
			ArrayList<ArrayList<ArrayList<String>>> allResults=getAllResults(top);
			ArrayList<HashMap<Integer,ArrayList<Release>>> values = new ArrayList<HashMap<Integer,ArrayList<Release>>>();
			
			values.add(releaseMap.get(top[0]));values.add(releaseMap.get(top[1]));values.add(releaseMap.get(top[2]));
			
			String[] chartTitles={currentDataset+" [ "+"round: "+top[0]+", numOfClusters : "+clusterSets.get(top[0]).getNumOfClusters()+" ]",
					currentDataset+" [ "+"round: "+top[1]+", numOfClusters : "+clusterSets.get(top[1]).getNumOfClusters()+" ]",
					currentDataset+" [ "+"round: "+top[2]+", numOfClusters : "+clusterSets.get(top[2]).getNumOfClusters()+" ]"
			};
			
			try{
				FXMLLoader loader = new FXMLLoader();
		        loader.setLocation((getClass().getResource("TopClusterSets.fxml")));
		        AnchorPane page = (AnchorPane) loader.load();
		        page.setStyle("-fx-background-color: transparent;");
		        Stage dialogStage = new Stage();
		        Scene scene = new Scene(page);
		        scene.getStylesheets().add((getClass().getResource("chartStyle.css").toExternalForm()));
		        dialogStage.setScene(scene);
		        dialogStage.setTitle("Top Cluster Sets");
		        TopClusterSetsController tc = loader.getController();
		        tc.init(currentDataset,chartTitles,values,allResults);
		        dialogStage.show();
		    }catch (IOException e) {
		        e.printStackTrace();
		    }
		}
	}
	
	public ArrayList<ArrayList<ArrayList<String>>> getAllResults(int[] top){
		ArrayList<ArrayList<ArrayList<String>>> allResults = new ArrayList<ArrayList<ArrayList<String>>>();

		for(int i=0;i<top.length;i++){
			HashMap<Integer,ArrayList<Release>> currentReleases = releaseMap.get(top[i]);
			String title="";
			
			PhaseSet phaseSet = new PhaseSet(title,currentReleases);
			ArrayList<Phase> phases=phaseSet.computePhases();
			HashMap<Integer,Histogram> histograms = phaseSet.getHistograms();
			ArrayList<ArrayList<String>> tmpAllResults = new ArrayList<ArrayList<String>>();
			
			ArrayList<String> phaseID = new ArrayList<String>();
			ArrayList<String> firstRelease = new ArrayList<String>();
			ArrayList<String> lastRelease = new ArrayList<String>();
			ArrayList<String> zeroG = new ArrayList<String>();
			ArrayList<String> lowG = new ArrayList<String>();
			ArrayList<String> mediumG = new ArrayList<String>();
			ArrayList<String> highG = new ArrayList<String>();
			ArrayList<String> zeroM = new ArrayList<String>();
			ArrayList<String> lowM = new ArrayList<String>();
			ArrayList<String> mediumM = new ArrayList<String>();
			ArrayList<String> highM = new ArrayList<String>();
			ArrayList<String> growthWinner = new ArrayList<String>();
			ArrayList<String> maintenanceWinner = new ArrayList<String>();
			ArrayList<String> classification = new ArrayList<String>();
			
			ArrayList<Integer> ids = new ArrayList<Integer>();
			HashMap<Integer,Phase> phaseMap = new HashMap<Integer,Phase>();
			for(Phase phase : phases){
				ids.add(phase.getPhaseID());
				phaseMap.put(phase.getPhaseID(), phase);
			}
			Collections.sort(ids);
			
			for(Integer id : ids){
				Phase phase=phaseMap.get(id);
				
				phaseID.add(String.valueOf(phase.getPhaseID()));
				firstRelease.add(String.valueOf(phase.getFirstRelease().getId()));
				lastRelease.add(String.valueOf(phase.getLastRelease().getId()));
				zeroG.add(String.valueOf(histograms.get(phase.getPhaseID()).getGrowth().get("Zero")));
				lowG.add(String.valueOf(histograms.get(phase.getPhaseID()).getGrowth().get("Low")));
				mediumG.add(String.valueOf(histograms.get(phase.getPhaseID()).getGrowth().get("Medium")));
				highG.add(String.valueOf(histograms.get(phase.getPhaseID()).getGrowth().get("High")));
				zeroM.add(String.valueOf(histograms.get(phase.getPhaseID()).getMaintenance().get("Zero")));
				lowM.add(String.valueOf(histograms.get(phase.getPhaseID()).getMaintenance().get("Low")));
				mediumM.add(String.valueOf(histograms.get(phase.getPhaseID()).getMaintenance().get("Medium")));
				highM.add(String.valueOf(histograms.get(phase.getPhaseID()).getMaintenance().get("High")));
				growthWinner.add(phase.getWinnerGrowth());
				maintenanceWinner.add(phase.getWinnerMaintenance());
				classification.add(phase.getCharacterization());				
			}
			tmpAllResults.add(phaseID);
			tmpAllResults.add(firstRelease);
			tmpAllResults.add(lastRelease);
			tmpAllResults.add(zeroG);
			tmpAllResults.add(lowG);
			tmpAllResults.add(mediumG);
			tmpAllResults.add(highG);
			tmpAllResults.add(zeroM);
			tmpAllResults.add(lowM);
			tmpAllResults.add(mediumM);
			tmpAllResults.add(highM);
			tmpAllResults.add(growthWinner);
			tmpAllResults.add(maintenanceWinner);
			tmpAllResults.add(classification);
			
			allResults.add(tmpAllResults);
		
		}
			
		return allResults;
	}
	
	
}
