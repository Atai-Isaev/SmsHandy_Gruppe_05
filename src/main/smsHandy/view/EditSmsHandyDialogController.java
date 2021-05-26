package main.smsHandy.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import main.smsHandy.Main;
import main.smsHandy.exception.ProviderNotFoundException;
import main.smsHandy.exception.SmsHandyHaveProviderException;
import main.smsHandy.model.PrepaidSmsHandy;
import main.smsHandy.model.Provider;
import main.smsHandy.model.SmsHandy;
import main.smsHandy.model.TariffPlanSmsHandy;


public class EditSmsHandyDialogController {
    private Main main;
    private Stage stage;
    private SmsHandy editedSmsHandy;

    @FXML
    private TextField smsHandyNumberTextField;
    @FXML
    private ChoiceBox<EditSmsHandyDialogController.SmsHandyArt> smsHandyTypeChoiceBox;
    @FXML
    private ChoiceBox<Provider> smsHandyProviderChoiceBox;

    @FXML
    private void initialize() {
        smsHandyTypeChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(EditSmsHandyDialogController.SmsHandyArt object) {
                return object == EditSmsHandyDialogController.SmsHandyArt.PREPAID ? "Prepaid Sms Handy" : "Tariff Plan Sms Handy";
            }

            @Override
            public EditSmsHandyDialogController.SmsHandyArt fromString(String string) {
                return null;
            }
        });

        smsHandyProviderChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Provider object) {
                return object.getName();
            }

            @Override
            public Provider fromString(String string) {
                return null;
            }
        });
    }

    @FXML
    private void handleEditSmsHandyButton() {
        EditSmsHandyDialogController.SmsHandyArt art = smsHandyTypeChoiceBox.getValue();
        Provider provider = smsHandyProviderChoiceBox.getValue();
        String number = smsHandyNumberTextField.getText();

        if (!checkSmsHandyNumber(number).equals("")) {
            alert(checkSmsHandyNumber(number));
            return;
        }

        try {
            main.getSmsHandyData()
                    .get(main.getIndexOfSmsHandy(editedSmsHandy))
                    .getProvider()
                    .getSubscriber()
                    .remove(editedSmsHandy.getNumber());
            main.getSmsHandyData()
                    .get(main.getIndexOfSmsHandy(editedSmsHandy))
                    .getProvider()
                    .getCredits()
                    .remove(editedSmsHandy.getNumber());
            SmsHandy handy = (art == EditSmsHandyDialogController.SmsHandyArt.PREPAID) ?
                    new PrepaidSmsHandy(number, provider)
                    :
                    new TariffPlanSmsHandy(number, provider);
//            System.out.println(main.getIndexOfSmsHandy(editedSmsHandy));
            main.getSmsHandyData().set(main.getIndexOfSmsHandy(editedSmsHandy),handy);
            alert("Sms-Handy erfolgreich bearbeitet!");
            stage.close();
        } catch (ProviderNotFoundException | SmsHandyHaveProviderException e) {
            alert(e.getMessage());
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
     * prueft den Sms-Handy Nummer auf Eindeutigkeit
     * @param number Nummer von Sms-Handy
     * @return liefert eine Fehlermeldung
     */
    private String checkSmsHandyNumber(String number) {
        String message = "";
        if (number.isBlank()) message = "Nummer kann nicht leer sein!";
        try {
            Integer.parseInt(number);
        } catch (NumberFormatException e) {
            message = "Zahl sollte vom Typ INTEGER sein!";
        }
        return message;
    }

    /**
     * lädt die Klasse mit Daten und dem ausgewählten Sms-Handy in den Controller
     * @param main Klasse mit Daten
     * @param smsHandy ausgewähltete Sms-Handy
     */
    public void setMain(Main main, SmsHandy smsHandy) {
        this.main = main;
        this.editedSmsHandy = smsHandy;

        smsHandyNumberTextField.setText(smsHandy.getNumber());

        smsHandyTypeChoiceBox.getItems().add(EditSmsHandyDialogController.SmsHandyArt.PREPAID);
        smsHandyTypeChoiceBox.getItems().add(EditSmsHandyDialogController.SmsHandyArt.TARIFF_PLAN);
        smsHandyTypeChoiceBox.setValue(smsHandy instanceof TariffPlanSmsHandy ?
                SmsHandyArt.TARIFF_PLAN : SmsHandyArt.PREPAID);

        main.getProvidersData().forEach(provider -> smsHandyProviderChoiceBox.getItems().add(provider));
        smsHandyProviderChoiceBox.setValue(smsHandy.getProvider());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private enum SmsHandyArt {
        PREPAID, TARIFF_PLAN
    }
}
