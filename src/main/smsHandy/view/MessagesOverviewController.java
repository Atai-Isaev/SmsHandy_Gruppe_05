package main.smsHandy.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.smsHandy.Main;
import main.smsHandy.exception.ProviderNotFoundException;
import main.smsHandy.model.Message;
import main.smsHandy.model.Provider;
import main.smsHandy.model.SmsHandy;

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

    private Main main;
    private SmsHandy smsHandy;
    private Stage dialogStage;

    private SmsHandy selectedSmsHandy;
    private ObservableList<Message> sentMessageData = FXCollections.observableArrayList();
    private ObservableList<Message> receivedMessageData = FXCollections.observableArrayList();

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
        sentMessageData.addAll(selectedSmsHandy.getSent());
        sentMessageTableView.setItems(sentMessageData);
        receivedMessageData.addAll(selectedSmsHandy.getReceived());
        receivedMessageTableView.setItems(receivedMessageData);
//        receivedMessageTableView.setItems(sentMessageData);
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
}
