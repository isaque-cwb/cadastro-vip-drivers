package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Parceiro;
import model.services.DepartmentService;
import model.services.ParceiroService;

public class ParceiroListController implements Initializable, DataChangeListener {

	private ParceiroService service;

	@FXML
	private TableView<Parceiro> tableViewParceiro;

	@FXML
	private TableColumn<Parceiro, Integer> tableColumnId;

	@FXML
	private TableColumn<Parceiro, String> tableColumnNome;
	
	@FXML
	private TableColumn<Parceiro, Integer> tableColumnTelefone;
	
	@FXML
	private TableColumn<Parceiro, String> tableColumnEmail;
	
	@FXML
	private TableColumn<Parceiro, Date> tableColumnBirthDate;
	
	@FXML
	private TableColumn<Parceiro, String> tableColumnEndereco;
	

	@FXML
	private TableColumn<Parceiro, Parceiro> tableColumnEDIT;

	@FXML
	private TableColumn<Parceiro, Parceiro> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<Parceiro> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Parceiro obj = new Parceiro();
		createDialogForm(obj, "/gui/ParceiroForm.fxml", parentStage);
	}

	public void setParceiroService(ParceiroService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		Utils.formatTableColumnDate(tableColumnBirthDate, "dd/MM/yyyy");
		tableColumnEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));
	
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewParceiro.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Serviço estava vazio!");
		}
		List<Parceiro> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewParceiro.setItems(obsList);
		initEditButtons();
		

		initRemoveButtons();
	}

	private void createDialogForm(Parceiro obj, String absoluteName, Stage parenteStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			ParceiroFormController controller = loader.getController();
			controller.setParceiro(obj);
			controller.setServices(new ParceiroService(), new DepartmentService());
			controller.loadAssociatedObjects();
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();
			

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Entre com Dados do Parceiro(a)");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parenteStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", null, e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();

	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Parceiro, Parceiro>() {
			private final Button button = new Button("edit");
			

			@Override
			protected void updateItem(Parceiro obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/ParceiroForm.fxml", Utils.currentStage(event)));
				

			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Parceiro, Parceiro>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Parceiro obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return; 
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Parceiro obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("TEM CERTEZA!", "Você quer mesmo DELETAR Parceiro(a)?");
		
		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Serviço estava vazio!");
			}
			try {
				service.remove(obj);
				updateTableView();
				Alerts.showAlert("Pronto, Removido!", null, "Parceiro Removido com sucesso!", Alert.AlertType.CONFIRMATION);
			}
			catch(DbIntegrityException e) {
				Alerts.showAlert("ERRO ao remover parceiro", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

}
