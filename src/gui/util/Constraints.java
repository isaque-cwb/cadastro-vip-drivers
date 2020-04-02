package gui.util;

import javafx.scene.control.TextField;

public class Constraints {



	public static void setTextFieldInteger(TextField txt) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
	        if (newValue != null && !newValue.matches("\\d*")) {
	        	txt.setText(oldValue);
	        }
	    });
	}

	public static void setTextFieldMaxLength(TextField txt, int max) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
	        if (newValue != null && newValue.length() > max) {
	        	txt.setText(oldValue);
	        }
	    });
	}

	public static void setTextFieldDouble(TextField txt) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
		    	if (newValue != null && !newValue.matches("\\d*([\\.]\\d*)?")) {
                    txt.setText(oldValue);
                }
		    });
	}
	
	public static void setTextFieldString(TextField txt) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
	        if (newValue != null && !newValue.matches("\\D*")) {
	        	txt.setText(oldValue);
	        }
	    });
	}
	
	
	public static void setTextFieldTelefone(TextField txt) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
	        if (newValue != null && !newValue.matches("\\d{2}\\s\\d{5}\\-\\d{4}")) {
	        	txt.setText(oldValue);
	        }
	        //Alerts.showAlert("Erro de formato!", null, "Erro no formato\n do Numero de telefone \n Digite (ddd) ", Alert.AlertType.ERROR);
	    });
	}

	
	
	
}