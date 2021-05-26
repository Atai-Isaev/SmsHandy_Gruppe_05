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

import java.util.Random;

public class CreateSmsHandyDialogController {
    private Main main;
    private Stage stage;

    @FXML
    private TextField smsHandyNumberTextField;
    @FXML
    private ChoiceBox<SmsHandyArt> smsHandyTypeChoiceBox;
    @FXML
    private ChoiceBox<Provider> smsHandyProviderChoiceBox;

    @FXML
    private void initialize() {
        smsHandyTypeChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(SmsHandyArt object) {
                return object == SmsHandyArt.PREPAID ? "Prepaid Sms Handy" : "Tariff Plan Sms Handy";
            }

            @Override
            public SmsHandyArt fromString(String string) {
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
    private void handleCreateSmsHandyButton() {
        SmsHandyArt art = smsHandyTypeChoiceBox.getValue();
        Provider provider = smsHandyProviderChoiceBox.getValue();
        String number = smsHandyNumberTextField.getText();

        if (!checkSmsHandyNumber(number).equals("")) {
            alert(checkSmsHandyNumber(number));
            return;
        }

        try {
            SmsHandy handy = (art == SmsHandyArt.PREPAID) ?
                    new PrepaidSmsHandy(number, provider) : new TariffPlanSmsHandy(number, provider);
            main.getSmsHandyData().add(handy);
            alert("Sms-Handy erfolgreich angelegt!");
            stage.close();
        } catch (ProviderNotFoundException | SmsHandyHaveProviderException e) {
            alert(e.getMessage());
        }
    }

    @FXML
    private void handleCancelButton(){
        stage.close();
    }

    private void alert(String text) {
        Alert alert = new Alert(
                Alert.AlertType.NONE,
                text,
                ButtonType.CLOSE
        );
        alert.showAndWait();
    }

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

    private String generateSmsHandyNumber() {
        String number = "";
        boolean exists = false;
        Random random = new Random();
        do {
            number = String.valueOf(random.nextInt(10000) + 100);
            for (SmsHandy smsHandy : main.getSmsHandyData()) {
                if (smsHandy.getNumber().equals(number)) {
                    exists = true;
                    break;
                }
            }
        } while (exists);
        return number;
    }

    public void setMain(Main main) {
        this.main = main;

        smsHandyNumberTextField.setText(generateSmsHandyNumber());

        smsHandyTypeChoiceBox.getItems().add(SmsHandyArt.PREPAID);
        smsHandyTypeChoiceBox.getItems().add(SmsHandyArt.TARIFF_PLAN);
        smsHandyTypeChoiceBox.setValue(smsHandyTypeChoiceBox.getItems().get(0));

        main.getProvidersData().forEach(provider -> smsHandyProviderChoiceBox.getItems().add(provider));
        smsHandyProviderChoiceBox.setValue(smsHandyProviderChoiceBox.getItems().get(0));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private enum SmsHandyArt {
        PREPAID, TARIFF_PLAN
    }
}
