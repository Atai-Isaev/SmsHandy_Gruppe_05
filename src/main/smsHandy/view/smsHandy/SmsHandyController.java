package main.smsHandy.view.smsHandy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import main.smsHandy.Main;
import main.smsHandy.model.SmsHandy;
import main.smsHandy.model.TariffPlanSmsHandy;

public class SmsHandyController {

    private Scene contactsScene;
    private Scene chatScene;

    private Stage stage;
    private Main main;
    private SmsHandy handy;

    private SmsHandy chatHandy;

    private final ObservableList<SmsHandy> contacts = FXCollections.observableArrayList();

    @FXML
    private ListView<SmsHandy> contactsListView;

    @FXML
    private Label balanceLabel;

    @FXML
    private Label chatHandyLabel;

    @FXML
    private void initialize() {
        contactsListView.setItems(contacts);

        contactsListView.setCellFactory(param -> new ListCell<SmsHandy>() {
            @Override
            protected void updateItem(SmsHandy cellHandy, boolean empty) {
                super.updateItem(cellHandy, empty);
                if (empty || cellHandy == null) {
                    setText(null);
                } else {
                    setText(cellHandy.getNumber() + " | " + cellHandy.getProvider().getName() + " | " + cellHandy.getClass().getSimpleName());
                }
            }
        });

        contactsListView.setOnMouseClicked(event -> {
//            contactsListView.getSelectionModel().setSelectionMode();
            // select clicked contact item
            if (event.getClickCount() == 2) {
                chatHandy = contactsListView.getSelectionModel().getSelectedItem();
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
            balance = ((TariffPlanSmsHandy) handy).getRemainingFreeSms() + " SMS";
        else
            balance = handy.getProvider().getCreditForSmsHandy(handy.getNumber()) + " €";
        balanceLabel.setText(balance);
    }

    @FXML
    private void switchToChat() {
        stage.setScene(this.chatScene);
    }

    @FXML
    private void switchToContacts() {
        stage.setScene(this.contactsScene);
        chatHandyLabel.setText(chatHandy.getNumber() + "@" + chatHandy.getProvider().getName());
    }

    public void setContactsScene(Scene contactsScene) {
        this.contactsScene = contactsScene;
    }

    public void setChatScene(Scene chatScene) {
        this.chatScene = chatScene;
    }
}
