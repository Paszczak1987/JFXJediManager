package jedi;

import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class MainWindowController {

	private DBConnector 			dbConnector;
    
	//JediOrder
    @FXML
    private ListView<String> 		joOrdersView;
    private ObservableList<String> 	joOrders;
    @FXML
    private ListView<String> 		joKnightsView;
    private ObservableList<String> 	joKnights;
    @FXML
    private TextField 				jediOrderName;
    @FXML
    private Button 					jediOrderImportBtn;
    @FXML
    private Button 					jediOrderExportBtn;
    @FXML
    private TextField 				jediOrdersFilePath;
    @FXML
    private Button 					jediOrderResetBtn;
    @FXML
    private Button 					jediOrderRegisterBtn;
    @FXML
    private Button 					jediOrderChooseBtn;
    
    //JediKnight
    @FXML
    private ListView<String> 		jkKnightsView;
    private ObservableList<String> 	jkKnights;
    @FXML
    private TextField 				jediKnightName;
    @FXML
    private ChoiceBox<String> 		jediKnightLightSaber;
    @FXML	
    private Button 					jediKnightImportBtn;
    @FXML
    private Button 					jediKnightExportBtn;
    @FXML
    private TextField 				jediKnightsFilePath;
    @FXML
    private Button 					jediKnightResetBtn;
    @FXML
    private Button 					jediKnightRegisterBtn;
    @FXML
    private Slider 					jediKnightPower;
    @FXML
    private RadioButton 			rbLight;
    @FXML
    private RadioButton 			rbDark;
    private ToggleGroup 			forceSide;
	
	
    @FXML
    public void initialize() throws SQLException {
    	
    	dbConnector = new DBConnector("5433", "JediKnights");
		dbConnector.createTableJediKnights();
    	
    	jediKnightLightSaber.getItems().add("Niebieski");
    	jediKnightLightSaber.getItems().add("Zielony");
    	jediKnightLightSaber.getItems().add("¯ó³ty");
    	jediKnightLightSaber.getItems().add("Fioletowy");
    	jediKnightLightSaber.getItems().add("Czerwony");
    	jediKnightLightSaber.getItems().add("Pomarañczowy");
    	jediKnightLightSaber.getItems().add("Ró¿owy");
    	jediKnightLightSaber.setValue("Niebieski");
    	
    	jkKnights = FXCollections.observableArrayList();
    	joKnights = FXCollections.observableArrayList();
    	jkKnightsView.setItems(jkKnights);
    	joKnightsView.setItems(joKnights);
    	
    	forceSide = new ToggleGroup();   	
    	rbLight.setToggleGroup(forceSide);
    	rbDark.setToggleGroup(forceSide);
    	rbLight.setSelected(true);
    	
    }
    
    @FXML
    void jkRegBtnOnAction(ActionEvent event) {
    	
    	String name = jediKnightName.getText();
    	String side = ((RadioButton)forceSide.getSelectedToggle()).getText();
    	String saber = jediKnightLightSaber.getValue();
    	int    power = (int) jediKnightPower.getValue();
    	
    	JediKnight jedi = new JediKnight(name, side, saber, power);
    	
    	jkKnights.add(jedi.toString());
    	joKnights.add(jedi.toString());
    	
    	dbConnector.pushJedi(jedi);
    }

}
