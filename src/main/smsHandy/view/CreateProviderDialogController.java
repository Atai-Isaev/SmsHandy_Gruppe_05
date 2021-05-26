package main.smsHandy.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.smsHandy.Main;
import main.smsHandy.model.Provider;

public class CreateProviderDialogController {
    @FXML
    private TextField providerNameField;


    private Stage dialogStage;
    private Main main;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMain(Main main) {
        this.main = main;
    }


    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            Provider provider = new Provider();
            provider.setName(providerNameField.getText());
            main.getProvidersData().add(provider);
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (providerNameField.getText().isBlank()) errorMessage = "Name kann nicht leer sein!";
        else {
            for (Provider p : Provider.providersList) {
                if (p.getName() !=null && p.getName().equals(providerNameField.getText())) {
                    errorMessage = "Dieser Name ist besetzt";
                    break;
                }
            }
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Eingabe Fehler");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }

}
