package main.smsHandy.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import main.smsHandy.exception.ProviderNotFoundException;
import main.smsHandy.model.Message;
import main.smsHandy.model.Provider;
import main.smsHandy.model.SmsHandy;

import java.util.ArrayList;
import java.util.List;

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
    private void handleSendMessageButton() {
        Stage stage = new Stage();

        TextArea smsTextField = new TextArea();
        smsTextField.setPromptText("Message...");
        smsTextField.setPrefColumnCount(24);
        smsTextField.setPrefRowCount(3);

        CheckBox isDirectCheckBox = new CheckBox();
        isDirectCheckBox.setText("Direct Message");

        ObservableList<String> receivers = FXCollections.observableArrayList();
        receivers.add("*101#");
        main.getSmsHandyData().forEach(handy -> {
            if (!handy.getNumber().equals(this.selectedSmsHandy.getNumber()))
                receivers.add(handy.getNumber());
        });
        ChoiceBox<String> receiversChoiceBox = new ChoiceBox<>();
        receiversChoiceBox.setMinWidth(smsTextField.getMinWidth());
        receiversChoiceBox.setItems(receivers);

        Button sendButton = new Button();
        sendButton.setMinWidth(20);
        sendButton.setText("Send");

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.getChildren().add(isDirectCheckBox);
        hBox.getChildren().add(receiversChoiceBox);
        hBox.getChildren().add(sendButton);

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);
        vBox.getChildren().add(smsTextField);
        vBox.getChildren().add(hBox);

        sendButton.setOnAction(event -> {
            if (smsTextField.getText().isBlank() && (receiversChoiceBox.getSelectionModel().getSelectedItem() != null
                    && !receiversChoiceBox.getSelectionModel().getSelectedItem().equals("*101#"))) {
                alert("Please, write a message!");
            } else if (receiversChoiceBox.getSelectionModel().getSelectedItem() == null) {
                alert("Please, select a receiver!");
            } else if (isDirectCheckBox.isSelected() && (receiversChoiceBox.getSelectionModel().getSelectedItem() != null
                    && receiversChoiceBox.getSelectionModel().getSelectedItem().equals("*101#"))) {
                alert("Cannot send sms direct to *101#!");
            } else {
                if (sendMessage(
                        receiversChoiceBox.getSelectionModel().getSelectedItem(),
                        smsTextField.getText(),
                        isDirectCheckBox.isSelected()
                )) {
                    alert("Your sms successfully sent!");
                } else alert("Error!");
                stage.close();
            }
        });

        stage.setTitle("Send Sms");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(this.main.getPrimaryStage());
        stage.setScene(new Scene(vBox));
        stage.showAndWait();
    }

    private boolean sendMessage(String to, String content, boolean direct) {
        try {
            if (direct) {
                this.main.getSmsHandyData().forEach(handy -> {
                    if (handy.getNumber().equals(to)) {
                        this.selectedSmsHandy.sendSmsDirect(handy, content);
                    }
                });
            } else this.selectedSmsHandy.sendSms(to, content);
        } catch (ProviderNotFoundException e) {
            return false;
        }
        return true;
    }

    private void alert(String text) {
        Alert alert = new Alert(
                Alert.AlertType.NONE,
                text,
                ButtonType.CLOSE
        );
        alert.showAndWait();
    }
}
