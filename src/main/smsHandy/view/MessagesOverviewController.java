package main.smsHandy.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.smsHandy.Main;
import main.smsHandy.exception.InvalidNumberException;
import main.smsHandy.exception.ProviderNotFoundException;
import main.smsHandy.exception.SmsHandyNotFoundException;
import main.smsHandy.model.Message;
import main.smsHandy.model.Provider;
import main.smsHandy.model.SmsHandy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MessagesOverviewController {
    @FXML
    private TableView<Message> receivedMessageTableView;

    @FXML
    private TableView<Message> sentMessageTableView;

    @FXML
    private Tab receivedTab;

    @FXML
    private Tab sentTab;
    @FXML
    private TableColumn<Message, String> fromColumnInReceived;

    @FXML
    private TableColumn<Message, String> toColumn;

    @FXML
    private TableColumn<Message, String> contentColumn;

    @FXML
    private TableColumn<Message, String> contentColumnInReceived;

    @FXML
    private TableColumn<Message, String> dateColumn;

    @FXML
    private TableColumn<Message, String> dateColumnInReceived;

//    Send Sms Elements
    @FXML private Label infoLabel;
    @FXML private TextArea smsTextArea;
    @FXML private TextField receiverTextField;
    @FXML private CheckBox isDirectCheckBox;
    @FXML private Button sendSmsButton;

    private Main main;
    private SmsHandy smsHandy;
    private Stage dialogStage;

    private SmsHandy selectedSmsHandy;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        fromColumnInReceived.setCellValueFactory(new PropertyValueFactory<>("From"));
        toColumn.setCellValueFactory(new PropertyValueFactory<>("To"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("Content"));
        contentColumnInReceived.setCellValueFactory(new PropertyValueFactory<>("Content"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));
        dateColumnInReceived.setCellValueFactory(new PropertyValueFactory<>("Date"));

    }

    public void setMain(Main main) throws ProviderNotFoundException {
        this.main = main;
        sentMessageTableView.setItems(selectedSmsHandy.getSent());
        receivedMessageTableView.setItems(selectedSmsHandy.getReceived());
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setSelectedSmsHandy(SmsHandy selectedSmsHandy) {
        this.selectedSmsHandy = selectedSmsHandy;
    }

    @FXML
    private void handleSendSmsButtonClicked() {
        String to = receiverTextField.getText();
        String content = smsTextArea.getText();
        boolean direct = isDirectCheckBox.isSelected();
        if (to.isBlank()) {
            infoLabel.setText("Give a receiver number first!");
            return;
        } else {
            if (content.isBlank() && !to.equals("*101#")) {
                infoLabel.setText("You cannot send empty message!");
                return;
            }
        }


        try {

            sendMessage(to, content, direct);

            if (to.equals("*101#")) {
                if (direct) infoLabel.setText("Direct message to *101# is redundant!");
                else infoLabel.setText("Check received messages!");
            }
            else infoLabel.setText("Your message successfully sent to " + to + "!");
        } catch (ProviderNotFoundException e) {
            infoLabel.setText("Sorry, provider for this number not found!");
        } catch (InvalidNumberException | SmsHandyNotFoundException e) {
            infoLabel.setText("Sorry, this number is invalid!");
        }
    }

    private boolean sendMessage(String to, String content, boolean direct) throws ProviderNotFoundException, InvalidNumberException, SmsHandyNotFoundException {
        if (direct) {
            this.main.getSmsHandyData().forEach(handy -> {
                if (handy.getNumber().equals(to)) {
                    this.selectedSmsHandy.sendSmsDirect(handy, content);
                }
            });
        } else this.selectedSmsHandy.sendSms(to, content);
        return true;
    }
}
