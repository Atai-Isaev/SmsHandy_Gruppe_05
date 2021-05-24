package main.smsHandy.view;

import javafx.beans.property.SimpleStringProperty;
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


public class SmsHandyEditDialogController {
    private Main main;
    private Stage stage;
    private SmsHandy editedSmsHandy;

    @FXML
    private TextField smsHandyNumberTextField;
    @FXML
    private ChoiceBox<SmsHandyEditDialogController.SmsHandyArt> smsHandyTypeChoiceBox;
    @FXML
    private ChoiceBox<Provider> smsHandyProviderChoiceBox;

    @FXML
    private void initialize() {
        smsHandyTypeChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(SmsHandyEditDialogController.SmsHandyArt object) {
                return object == SmsHandyEditDialogController.SmsHandyArt.PREPAID ? "Prepaid Sms Handy" : "Tariff Plan Sms Handy";
            }

            @Override
            public SmsHandyEditDialogController.SmsHandyArt fromString(String string) {
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
        SmsHandyEditDialogController.SmsHandyArt art = smsHandyTypeChoiceBox.getValue();
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
            SmsHandy handy = (art == SmsHandyEditDialogController.SmsHandyArt.PREPAID) ?
                    new PrepaidSmsHandy(number, provider)
                    :
                    new TariffPlanSmsHandy(number, provider);
//            System.out.println(main.getIndexOfSmsHandy(editedSmsHandy));
            main.getSmsHandyData().set(main.getIndexOfSmsHandy(editedSmsHandy),handy);
            alert("Phone successfully edited!");
            stage.close();
        } catch (ProviderNotFoundException | SmsHandyHaveProviderException e) {
            alert(e.getMessage());
        }
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
        if (number.isBlank()) message = "Number cannot be empty!";
        try {
            Integer.parseInt(number);
        } catch (NumberFormatException e) {
            message = "Number should be of type INTEGER!";
        }
        return message;
    }

    public void setMain(Main main, SmsHandy smsHandy) {
        this.main = main;
        this.editedSmsHandy = smsHandy;

        smsHandyNumberTextField.setText(smsHandy.getNumber());

        smsHandyTypeChoiceBox.getItems().add(SmsHandyEditDialogController.SmsHandyArt.PREPAID);
        smsHandyTypeChoiceBox.getItems().add(SmsHandyEditDialogController.SmsHandyArt.TARIFF_PLAN);
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
