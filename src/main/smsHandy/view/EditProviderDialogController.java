package main.smsHandy.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.smsHandy.Main;
import main.smsHandy.model.Provider;

public class EditProviderDialogController {
    private Main main;
    private Stage stage;
    private Provider editedProvider;

    @FXML
    private TextField providerNameTextField;

    @FXML
    private void initialize() {
    }

    @FXML
    private void handleEditProviderButton() {
        String name = providerNameTextField.getText();
        if (checkProviderName(name).equals("")){
            editedProvider = Provider.providersList.get(Provider.providersList.indexOf(editedProvider));
            editedProvider.setName(name);
            Provider.providersList.set(Provider.providersList.indexOf(editedProvider),editedProvider);
            main.getProvidersData().set(Provider.providersList.indexOf(editedProvider),editedProvider);
            alert("Provider erfolgreich bearbeitet!");
            stage.close();
        }else {
            alert(checkProviderName(name));
        }
    }


    @FXML
    private void handleCancelButton(){
            stage.close();
    }

    /**
     * zeigt Fehler- oder Erfolgsfenster an
     * @param text- oder Erfolmeldung
     */
    private void alert(String text) {
        Alert alert = new Alert(
                Alert.AlertType.NONE,
                text,
                ButtonType.CLOSE
        );
        alert.showAndWait();
    }

    /**
     * prueft den Provider-Namen auf Eindeutigkeit
     * @param name Name von Provider
     * @return liefert eine Fehlermeldung
     */
    private String checkProviderName(String name) {
        String message ="";
        if (name.isBlank()) message = "Name kann nicht leer sein!";
        for (Provider p : Provider.providersList){
            if (p.getName().equals(name)) return "Dieser Name ist besetzt";
        }
        return message;
    }

    public void setMain(Main main, Provider provider) {
        this.main = main;
        this.editedProvider = provider;
        providerNameTextField.setText(provider.getName());

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
