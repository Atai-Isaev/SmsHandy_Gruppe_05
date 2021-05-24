package main.smsHandy.view.smsHandy;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.smsHandy.Main;
import main.smsHandy.model.SmsHandy;
import main.smsHandy.model.TariffPlanSmsHandy;

import java.io.IOException;

public class SmsHandyController {

    private Scene contactsScene;
    private Scene chatScene;

    private Stage stage;
    private Main main;
    private SmsHandy handy;

    private SmsHandy selectedContactHandy;

    private final ObservableList<SmsHandy> contacts = FXCollections.observableArrayList();

    @FXML
    private ListView<SmsHandy> contactsListView;

    @FXML
    private Label balanceLabel;

    @FXML
    private void initialize() {
        contactsListView.setItems(contacts);

        contactsListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                selectedContactHandy = contactsListView.getSelectionModel().getSelectedItem();
                switchToChat();
            }
        });
    }

    public void setHandy(SmsHandy handy) {
        this.handy = handy;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void constructSmsHandyWindow() {
        updateContactsListView();
        updateBalanceLabel();
    }

    private void updateContactsListView() {
        contacts.addAll(this.main.getSmsHandyData());
    }

    private void updateBalanceLabel() {
        String balance = "";
        if (handy instanceof TariffPlanSmsHandy)
            balance = ((TariffPlanSmsHandy) handy).getRemainingFreeSms() + " sms";
        else
            balance = handy.getProvider().getCreditForSmsHandy(handy.getNumber()) + " euro";
        balanceLabel.setText(balance);
    }

    @FXML
    private void switchToChat() {
        stage.setScene(this.chatScene);
        System.out.println(selectedContactHandy.getNumber());
    }

    @FXML
    private void switchToContacts() {
        stage.setScene(this.contactsScene);
    }

    public void setContactsScene(Scene contactsScene) {
        this.contactsScene = contactsScene;
    }

    public void setChatScene(Scene chatScene) {
        this.chatScene = chatScene;
    }
}
