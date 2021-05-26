package main.smsHandy.view;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.ButtonSkin;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.smsHandy.Main;
import main.smsHandy.exception.InvalidNumberException;
import main.smsHandy.exception.ProviderNotFoundException;
import main.smsHandy.exception.SmsHandyNotFoundException;
import main.smsHandy.model.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MessagesOverviewController {
    @FXML
    private TableView<Message> receivedMessageTableView;

    @FXML
    private TableView<Message> sentMessageTableView;

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

    //Guthabenaufladung Eleements
    @FXML
    private TextField aufladenBetrag;

    //    Send Sms Elements
    @FXML
    private Label infoLabel;
    @FXML
    private TextArea smsTextArea;
    @FXML
    private TextField receiverTextField;
    @FXML
    private CheckBox isDirectCheckBox;
    @FXML
    private Button sendSmsButton;

    @FXML
    private Label numberLabel;

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
        sentMessageTableView.setPlaceholder(new Label("Keine gesendeten Nachrichten"));
        receivedMessageTableView.setPlaceholder(new Label("Keine empfangenen Nachrichten"));
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
        numberLabel.setText(selectedSmsHandy.getNumber()+" | "+selectedSmsHandy.getClass().getSimpleName()+" | "+selectedSmsHandy.getProvider().getName());
    }

    @FXML
    private void handleSendSmsButtonClicked() {
        String to = receiverTextField.getText();
        String content = smsTextArea.getText();
        boolean direct = isDirectCheckBox.isSelected();
        if (to.isBlank()) {
            infoLabel.setText("✕ Give a receiver number first!");
            return;
        } else {
            if (content.isBlank() && !to.equals("*101#")) {
                infoLabel.setText("✕ You cannot send empty message!");
                return;
            }
        }

        Task<String> task = new Task<String>() {
            @Override
            public String call() {
                try {
                    Boolean result = sendMessage(to, content, direct);
                    if (to.equals("*101#")) {
                        if (direct) return "Direktnachricht an *101# ist überflüssig!";
                        else return "✓ Empfangene Nachrichten ablesen!";
                    } else return "✓ Ihre Nachricht wurde erfolgreich an " + to + " gesendet!";
                } catch (ProviderNotFoundException e) {
                    return "✕ Leider wurde der Provider für diese Nummer nicht gefunden!";
                } catch (InvalidNumberException | SmsHandyNotFoundException e) {
                    return "✕ Leider ist diese Nummer ungültig!";
                }
            }
        };

        Timeline timelineLoading = new Timeline(
                new KeyFrame(Duration.seconds(0.1), ev -> sendSmsButton.setText("◷")),
                new KeyFrame(Duration.seconds(0.2), ev -> sendSmsButton.setText("◶")),
                new KeyFrame(Duration.seconds(0.3), ev -> sendSmsButton.setText("◵")),
                new KeyFrame(Duration.seconds(0.4), ev -> sendSmsButton.setText("◴"))
        );
        timelineLoading.setCycleCount(Animation.INDEFINITE);
        timelineLoading.play();

        task.setOnSucceeded(e -> {
            String info = task.getValue();
            infoLabel.setText(info);
            timelineLoading.stop();
            sendSmsButton.setText("SMS SENDEN");
        });

        new Thread(task).start();
    }

    private boolean sendMessage(String to, String content, boolean direct) throws
            ProviderNotFoundException,
            InvalidNumberException,
            SmsHandyNotFoundException {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (direct) {
            this.main.getSmsHandyData().forEach(handy -> {
                if (handy.getNumber().equals(to)) {
                    this.selectedSmsHandy.sendSmsDirect(handy, content);
                }
            });
        } else this.selectedSmsHandy.sendSms(to, content);
        return true;
    }

    @FXML
    private void handleAufladenButton() {
        try {
            int guthaben = Integer.parseInt(aufladenBetrag.getText());
            selectedSmsHandy.getProvider().deposit(selectedSmsHandy.getNumber(), guthaben);
            main.getSmsHandyData().set(main.getIndexOfSmsHandy(selectedSmsHandy), selectedSmsHandy);
            alert("✓ Der Betragaufladung erfolgt");
        } catch (NumberFormatException e) {
            alert("✕ Bitte die Zahlen eingeben");
        }
    }

    @FXML
    private void handlePlusTenButton() {
        setGuthaben("10");
    }

    @FXML
    private void handlePlusTwentyButton() {
        setGuthaben("20");
    }

    @FXML
    private void handlePlusFiftyButton() {
        setGuthaben("50");
    }

    @FXML
    private void handlePlusHundredButton() {
        setGuthaben("100");
    }


    private void setGuthaben(String s) {
        if (!aufladenBetrag.getText().equals("")) {
            try {
                int count = Integer.parseInt(s) + Integer.parseInt(aufladenBetrag.getText());
                aufladenBetrag.setText(String.valueOf(count));
            } catch (NumberFormatException e) {
                aufladenBetrag.setText(s);
            }
        } else {
            aufladenBetrag.setText(s);
        }
    }

    /**
     * zeigt Fehler- oder Erfolgsfenster an
     *
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
}
