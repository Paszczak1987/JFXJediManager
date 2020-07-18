package jedi;

import java.io.File;
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
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

public class MainWindowController {

	private DBConnector 			dbConnector;
    private Dialog<String[]> 		inputDialog;
    
    @FXML
    private StackPane stackPane;
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
    private File 					joExpFile;
    
    //JediKnight
    @FXML
    private ListView<String> 		jkKnightsView;
    private ObservableList<String> 	jkKnights;
    @FXML
    private TextField 				jkName;
    @FXML
    private ChoiceBox<String> 		jkSaber;
    @FXML	
    private Button 					jkImportBtn;
    @FXML
    private Button 					jkExportBtn;
    @FXML
    private TextField 				jkFilePath;
    @FXML
    private Button 					jkResetBtn;
    @FXML
    private Button 					jkRegisterBtn;
    @FXML
    private Slider 					jediKnightPower;
    @FXML
    private RadioButton 			rbLight;
    @FXML
    private RadioButton 			rbDark;
    private ToggleGroup 			forceSide;
    private File 					jkExpFile;
	
	
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
    	
    	forceSide = new ToggleGroup();   	
    	rbLight.setToggleGroup(forceSide);
    	rbDark.setToggleGroup(forceSide);
    	inputDialog = new Dialog<>();
    	
//    	setInputDialog();
//    	Optional<String[]> result = inputDialog.showAndWait();
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
			e.printStackTrace();
		}
    }
    
    //JK
    
    @FXML
    void jkRegBtnOnAction(ActionEvent event) {
    	String name = jkName.getText();
    	String side = null;
    	if(forceSide.getSelectedToggle() != null)
    		side = ((RadioButton)forceSide.getSelectedToggle()).getText();
    	String saber = jkSaber.getValue();
    	int    power = (int) jediKnightPower.getValue(); 
    	if((!name.equals("") && side != null) && saber != null) {
    		boolean whetherThereIs = false;
    		for(JediKnight jk: JediKnight.knights) {
    			if(jk.getName().equals(name))
    				whetherThereIs = true;
    		}
    	
    		if(!whetherThereIs) {
    			JediKnight jedi = new JediKnight(name, side, saber, power);
    			addKnightOrOrder(jedi);  	
    			dbConnector.insertInto(jedi);    		
    		}else
    			System.out.println("Jedi o takim imieniu ju¿ istnieje");
    	
    	
    		//reset values
    		jkSaber.setValue(null);
    		jkName.setText("");
    		forceSide.getSelectedToggle().setSelected(false);
    		jediKnightPower.setValue(500);
    	}
    }
    
    @FXML
    void jkResetBtnOnClick(MouseEvent event) {
    	jkKnights.clear();
    	JediKnight.knights.clear();
    	jkFilePath.setText("");
    }
    
    @FXML
    void jkFilePathOnMClicked(MouseEvent event) {
    	pickFile(event);
    }
    
    @FXML
    void jkExpBtnOnClick(MouseEvent event) {
    	if(jkExpFile != null) {
    		FileCodder.encodeToFile(jkExpFile.getAbsolutePath(), jkKnights);
    	}else
    		System.out.println("wybierz pliku do exportu");
    }
    
    @FXML
    void jkImportBtnOnClick(MouseEvent event) throws SQLException {
    	if(dbConnector.isTableExist("jedi_knights"))
    		dbConnector.getData("jedi_knights");
    }
   
    //JO
    
    @FXML
    void joRegBtnOnAction(ActionEvent event) {
    	String name = joName.getText();
    	if(!name.equals("")) {
    		boolean whetherThereIs = false;
    		for(JediOrder jo: JediOrder.orders) {
    			if(jo.getName().equals(name))
    				whetherThereIs = true;
    		}
    	
    		if(!whetherThereIs && !name.equals(null)) {
    			JediOrder order = new JediOrder(name);
    			addKnightOrOrder(order);
    			dbConnector.insertInto(order);    		
    		}
    	
    		//reset values
    		joName.setText("");
    	}
    }
  
    @FXML
    void joResetBtnOnClick(MouseEvent event) {
    	joOrders.clear();
    	JediOrder.orders.clear();
    	joKnights.clear();
    	joFilePath.setText("");
    }
    
    @FXML
	void joChooseBtnOnAction(ActionEvent event) {
    	if(joKnightsView.getSelectionModel().getSelectedItem() != null && joKnightsView.getSelectionModel().getSelectedItem() != null) {
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
	}
    
    @FXML
    void joFilePathOnMClicked(MouseEvent event) {
    	pickFile(event);
    }
    
    @FXML
    void joExpBtnOnClick(MouseEvent event) {
    	if(joExpFile != null) {
    		FileCodder.encodeToFile(joExpFile.getAbsolutePath(), joOrders);
    	}else
    		System.out.println("wybierz pliku do exportu");
    }
    
    @FXML
    void joImportBtnOnClick(MouseEvent event) throws SQLException {
    	if(dbConnector.isTableExist("jedi_orders"))
    		dbConnector.getData("jedi_orders");
    	if(!jkKnights.isEmpty())
    		dbConnector.getData("knights_orders");
    }
    
    //PRIVATE
    
    private void establishConnection(String[] connVals) throws SQLException {
    	dbConnector = new DBConnector(this, connVals[0], connVals[1] , connVals[2]);
    	dbConnector.createTable("jedi_knights");
		dbConnector.createTable("jedi_orders");
		dbConnector.createTable("knights_orders");
    }
    
    private void setInputDialog() {
		ButtonType connectBtn = new ButtonType("Po³¹cz", ButtonData.OK_DONE);
		inputDialog.setTitle("Po³¹czenie z baz¹ danych");
		inputDialog.setHeaderText("Wype³nij rekordy wymagane do po³¹czenia z baz¹ danych:\n nazwê, numer portu oraz has³o.");
		inputDialog.getDialogPane().getButtonTypes().addAll(connectBtn, ButtonType.CANCEL);
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
		
		inputDialog.getDialogPane().setContent(grid);
		
		inputDialog.setResultConverter(dialogButton ->{
			if(dialogButton == connectBtn) {
				String[] ret = { portNum.getText(), dbName.getText(), password.getText() };
				return ret;
			}
			return null;
		});
	}
    
    private void pickFile(MouseEvent event) {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.getExtensionFilters().add(new ExtensionFilter("Pliki tekstowe", "*.txt"));
    	Stage stage = (Stage) stackPane.getScene().getWindow();
    	if(event.getSource().hashCode() == joFilePath.hashCode()) {
    		fileChooser.setTitle("Wybierz plik do ekspotru ZAKONÓW jedi");
    		joExpFile = fileChooser.showOpenDialog(stage);
        	if(joExpFile == null) {
        		joFilePath.setText("");
        		return;	
        	}else
        		joFilePath.setText(joExpFile.getAbsolutePath());
    	}else if(event.getSource().hashCode() == jkFilePath.hashCode()) {
    		fileChooser.setTitle("Wybierz plik do ekspotru RYCERZY jedi");
    		jkExpFile = fileChooser.showOpenDialog(stage);
        	if(jkExpFile == null) {
        		jkFilePath.setText("");
        		return;	
        	}else
        		jkFilePath.setText(jkExpFile.getAbsolutePath());
    	}
    }
    
    //PUBLIC
    
    public void addKnightOrOrder(Object knightOrOrder) {
    	if(knightOrOrder instanceof JediKnight) {
    		jkKnights.add(knightOrOrder.toString());
    		if(!((JediKnight)knightOrOrder).isAssigned())
    			if(!joKnights.contains(((JediKnight)knightOrOrder).getName()))
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
