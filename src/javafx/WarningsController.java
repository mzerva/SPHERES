package javafx;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class WarningsController implements Initializable{
	@FXML
	private Label warningLabel = new Label();
	@FXML
	private Button okButton = new Button();
	private String warning = new String();
	
	public void setWarning(String warning){
		this.warning=warning;
		warningLabel.setText(warning);	
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {}
	public void okPressed(ActionEvent event){
		((Node) event.getSource()).getScene().getWindow().hide();
	}
}
