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
import javafx.scene.control.TextInputDialog;
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
    private TextField 				joName;
    @FXML
    private Button 					joChooseBtn;
    @FXML
    private Button 					joImportBtn;
    @FXML
    private Button 					joExportBtn;
    @FXML
    private TextField 				joFilePath;
    @FXML
    private Button 					joResetBtn;
    @FXML
    private Button 					joRegisterBtn;
    
    //JediKnight
    @FXML
    private ListView<String> 		jkKnightsView;
    private ObservableList<String> 	jkKnights;
    @FXML
    private TextField 				jkName;
    @FXML
    private ChoiceBox<String> 		jkSaber;
    @FXML	
    private Button 					jediKnightImportBtn;
    @FXML
    private Button 					jediKnightExportBtn;
    @FXML
    private TextField 				jediKnightsFilePath;
    @FXML
    private Button 					jediKnightResetBtn;
    @FXML
    private Button 					jkRegisterBtn;
    @FXML
    private Slider 					jediKnightPower;
    @FXML
    private RadioButton 			rbLight;
    @FXML
    private RadioButton 			rbDark;
    private ToggleGroup 			forceSide;
	
	
    @FXML
    public void initialize() throws SQLException {
    	
    	jkKnights = FXCollections.observableArrayList();
    	jkKnightsView.setItems(jkKnights);
    	
    	joKnights = FXCollections.observableArrayList();
    	joOrders = FXCollections.observableArrayList();
    	joKnightsView.setItems(joKnights);
    	joOrdersView.setItems(joOrders);

    	dbConnector = new DBConnector(this, "5433", "JediKnights");
		dbConnector.createTable("jedi_knights");
		dbConnector.createTable("jedi_orders");
		dbConnector.createTable("knights_orders");
    	
    	jkSaber.getItems().add("niebieski");
    	jkSaber.getItems().add("zielony");
    	jkSaber.getItems().add("¿ó³ty");
    	jkSaber.getItems().add("fioletowy");
    	jkSaber.getItems().add("czerwony");
    	jkSaber.getItems().add("pomarañczowy");
    	jkSaber.getItems().add("ró¿owy");
    	jkSaber.getItems().add("bia³y");
    
    	forceSide = new ToggleGroup();   	
    	rbLight.setToggleGroup(forceSide);
    	rbDark.setToggleGroup(forceSide);
    	
    }
    
    @FXML
    void jkRegBtnOnAction(ActionEvent event) {
    	
    	String name = jkName.getText();
    	String side = ((RadioButton)forceSide.getSelectedToggle()).getText();
    	String saber = jkSaber.getValue();
    	int    power = (int) jediKnightPower.getValue();
    	
    	JediKnight jedi = new JediKnight(name, side, saber, power);
    	addKnightOrOrder(jedi);  	
    	dbConnector.insertInto(jedi);
    }
    
    @FXML
    void joRegBtnOnAction(ActionEvent event) {
    	
    	String name = joName.getText();
    	
    	JediOrder order = new JediOrder(name);
    	addKnightOrOrder(order);
    	dbConnector.insertInto(order);
    }
    
    @FXML
	void joChooseBtnOnAction(ActionEvent event) {

		String knight = joKnightsView.getSelectionModel().getSelectedItem();
		int indexOfKnight = joKnightsView.getSelectionModel().getSelectedIndex();
		joKnights.remove(indexOfKnight);
		
		String order = joOrdersView.getSelectionModel().getSelectedItem();
		int indexOfOrder = joOrdersView.getSelectionModel().getSelectedIndex();

		try {
			dbConnector.insertInto(order.split(" ")[0], knight);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (!order.contains(" [ "))
			order = order + " [ ";
		order = order.replace(" ]", "; ");
		order = order + knight + " ]";
		joOrders.set(indexOfOrder, order);
	}
    
    public void addKnightOrOrder(Object knightOrOrder) {
    	if(knightOrOrder instanceof JediKnight) {
    		jkKnights.add(knightOrOrder.toString());
    		joKnights.add(((JediKnight)knightOrOrder).getName());
    	}else if(knightOrOrder instanceof JediOrder)
    		joOrders.add(knightOrOrder.toString());
    }
    
    public ObservableList<String> getJoOrders(){
    	return joOrders;
    }
    
    public ObservableList<String> getJoKnights(){
    	return joKnights;
    }

}
