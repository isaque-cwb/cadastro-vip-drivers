package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.TextFieldFormatter;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Parceiro;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.ParceiroService;

public class ParceiroFormController implements Initializable {

	private Parceiro entity;

	private ParceiroService service;

	private DepartmentService departmentService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNome;
	
	@FXML
	private TextField txtTelefone;

	@FXML
	private TextField txtEmail;

	@FXML
	private DatePicker dpBirthDate;

	@FXML
	private TextField txtEndereco;
	

	

	@FXML
	private ComboBox<Department> comboBoxDepartment;

	@FXML
	private Label labelErrorNome;
	
	@FXML
	private Label labelErrorTelefone;

	@FXML
	private Label labelErrorEmail;

	@FXML
	private Label labelErrorBirthDate;

	@FXML
	private Label labelErrorEndereco;
	

	
	@FXML
	private Label labelErrorContatoFamiliar;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	private ObservableList<Department> obsList;

	public void setParceiro(Parceiro entity) {
		this.entity = entity;
	}

	public void setServices(ParceiroService service, DepartmentService departmentService) {
		this.service = service;
		this.departmentService = departmentService;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormaData();
			service.saveOrUpdate(entity);
			notifyDataChangeListener();
			Alerts.showAlert("Cadastrado!", null, "Tudo certo! Parceiro Cadastrado ou Alterado com sucesso!", AlertType.INFORMATION);
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Erro ao cadastrar!", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	@FXML
	private void fomartNumCel() {
		TextFieldFormatter numero = new TextFieldFormatter();
		numero.setMask("(##)#####-####");
		numero.setCaracteresValidos("0123456789");
		numero.setTf(txtTelefone);
		numero.formatter();
		
	}
	
	

	private void notifyDataChangeListener() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}

	}

	private Parceiro getFormaData() {
		Parceiro obj = new Parceiro();

		ValidationException exception = new ValidationException("Validation ERROR");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			exception.addError("nome", "Campo não pode ficar vazio!");
		}
		obj.setNome(txtNome.getText());

		if (txtTelefone.getText() == null || txtTelefone.getText().trim().equals("")) {
			exception.addError("telefone", "Campo não pode ficar vazio!");
		}
		obj.setTelefone(txtTelefone.getText());
		

		if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
			exception.addError("email", "Campo não pode ficar vazio!");
		}
		obj.setEmail(txtEmail.getText());
		
		if(dpBirthDate.getValue() == null) {
			exception.addError("birthDate", "Campo não pode ficar vazio!");
		}
		else {
			Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setBirthDate(Date.from(instant));
		}
			
		if (txtEndereco.getText() == null || txtEndereco.getText().trim().equals("")) {
			exception.addError("endereco", "Campo não pode ficar vazio!");
		}
		obj.setEndereco(txtEndereco.getText());
		


		obj.setDepartment(comboBoxDepartment.getValue());
		if (exception.getErrors().size() > 0) {
			throw exception;
		}

		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();

	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldString(txtNome);
		Constraints.setTextFieldMaxLength(txtTelefone, 50);
		Constraints.setTextFieldMaxLength(txtEmail, 80);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
		Constraints.setTextFieldMaxLength(txtEndereco, 80);

		
		
		initializeComboBoxDepartment();
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtNome.setText(entity.getNome());
		txtTelefone.setText(String.valueOf(entity.getTelefone()));
		txtEmail.setText(entity.getEmail());
		txtEndereco.setText(entity.getEndereco());

		
		if (entity.getBirthDate() != null) {
			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
		if (entity.getDepartment() == null) {
			comboBoxDepartment.getSelectionModel().selectFirst();
		} else {
			comboBoxDepartment.setValue(entity.getDepartment());
		}
	}

	public void loadAssociatedObjects() {
		if (departmentService == null) {
			throw new IllegalStateException("Department was null");
		}
		List<Department> list = departmentService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxDepartment.setItems(obsList);
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		labelErrorNome.setText((fields.contains("nome") ? errors.get("nome") : ""));
		labelErrorTelefone.setText((fields.contains("telefone") ? errors.get("telefone") : ""));
		labelErrorEmail.setText((fields.contains("email") ? errors.get("email") : ""));
		labelErrorBirthDate.setText((fields.contains("birthDate") ? errors.get("birthDate") : ""));
		labelErrorEndereco.setText((fields.contains("endereco") ? errors.get("endereco") : ""));


	}

	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}

}
