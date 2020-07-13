package jedi;

import java.sql.SQLException;
import java.util.Optional;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

public class MainWindowController {

	private DBConnector 			dbConnector;
    private Dialog<String[]> 		dialog;
    
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
    private TextField 				jkFilePath;
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
    public void initialize() {
    	
    	jkKnights = FXCollections.observableArrayList();
    	jkKnightsView.setItems(jkKnights);
    	
    	joKnights = FXCollections.observableArrayList();
    	joOrders = FXCollections.observableArrayList();
    	joKnightsView.setItems(joKnights);
    	joOrdersView.setItems(joOrders);
    	
    	jkSaber.getItems().add("niebieski");
    	jkSaber.getItems().add("zielony");
    	jkSaber.getItems().add("¿ó³ty");
    	jkSaber.getItems().add("fioletowy");
    	jkSaber.getItems().add("czerwony");
    	jkSaber.getItems().add("pomarañczowy");
    	jkSaber.getItems().add("ró¿owy");
    	jkSaber.getItems().add("bia³y");
    	
    	jkFilePath.setFocusTraversable(true);
    	
    	forceSide = new ToggleGroup();   	
    	rbLight.setToggleGroup(forceSide);
    	rbDark.setToggleGroup(forceSide);
    	dialog = new Dialog<>();
    	
//    	setDialog();
//    	Optional<String[]> result = dialog.showAndWait();
//    	result.ifPresent(connVals -> {
//			try {
//				establishConnection(connVals);
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		});
//    		
//    	if(result.isEmpty()) {
//    		Platform.exit();
//    	}
    	
    	try {
			establishConnection(new String[] {"5433","JediKnights","Delfin8765"});
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void setDialog() {
		ButtonType connectBtn = new ButtonType("Po³¹cz", ButtonData.OK_DONE);
		dialog.setTitle("Po³¹czenie z baz¹ danych");
		dialog.setHeaderText("Wype³nij rekordy wymagane do po³¹czenia z baz¹ danych:\n nazwê, numer portu oraz has³o.");
		dialog.getDialogPane().getButtonTypes().addAll(connectBtn, ButtonType.CANCEL);
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20,150,10,10));
		TextField dbName = new TextField();
		TextField portNum = new TextField();
		PasswordField password = new PasswordField();
		dbName.setPromptText("nazwa");
		portNum.setPromptText("5432");
		grid.add(new Label("Baza danych:"), 0, 0);
		grid.add(dbName, 1, 0);
		grid.add(new Label("Numer portu:"), 0, 1);
		grid.add(portNum, 1, 1);
		grid.add(new Label("Has³o:"), 0, 2);
		grid.add(password, 1, 2);
		
		dialog.getDialogPane().setContent(grid);
		
		dialog.setResultConverter(dialogButton ->{
			if(dialogButton == connectBtn) {
				String[] ret = { portNum.getText(), dbName.getText(), password.getText() };
				return ret;
			}
			return null;
		});
	}
    
    public void establishConnection(String[] connVals) throws SQLException {
    	dbConnector = new DBConnector(this, connVals[0], connVals[1] , connVals[2]);
		dbConnector.createTable("jedi_knights");
		dbConnector.createTable("jedi_orders");
		dbConnector.createTable("knights_orders");
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
    void jkFilePathOnMClicked(MouseEvent event) {
    	System.out.println("ok");
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
