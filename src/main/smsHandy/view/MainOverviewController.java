package main.smsHandy.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.smsHandy.Main;
import main.smsHandy.exception.ProviderNotFoundException;
import main.smsHandy.model.Provider;
import main.smsHandy.model.SmsHandy;
import main.smsHandy.model.TariffPlanSmsHandy;
import main.smsHandy.view.smsHandy.SmsHandyController;

import java.io.IOException;

public class MainOverviewController {
    @FXML
    private TableView<Provider> providerTableView;

    @FXML
    private TableColumn<Provider, String> providerNameColumn;

    @FXML
    private TableView<SmsHandy> smsHandyTableView;

    @FXML
    private TableColumn<SmsHandy, String> smsHandyNameColumn;

    @FXML
    private TableColumn<SmsHandy, String> smsHandyArtColumn;

    @FXML
    private TableColumn<SmsHandy, String> smsHandyGuthabenColumn;

    @FXML
    private Button createOrEditSmsHandyButton;

    @FXML
    private Button createOrEditProviderButton;

    private Main main;

    @FXML
    private void initialize() {
        providerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        smsHandyNameColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        smsHandyArtColumn.setCellValueFactory(param -> {
            SmsHandy handy = param.getValue();
            return handy instanceof TariffPlanSmsHandy ?
                    new SimpleStringProperty("Tariff Plan") : new SimpleStringProperty("Prepaid");
        });
        smsHandyGuthabenColumn.setCellValueFactory(param -> {
            SmsHandy handy = param.getValue();
            if (handy instanceof TariffPlanSmsHandy) {
                return new SimpleStringProperty(((TariffPlanSmsHandy) handy).getRemainingFreeSms() + " SMS");
            }
            int credits = handy.getProvider().getCreditForSmsHandy(handy.getNumber());
            return new SimpleStringProperty(credits + " €");
        });

        smsHandyTableView.setRowFactory(tableView2 -> {
            final TableRow<SmsHandy> row = new TableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                final int index = row.getIndex();
                if (index >= 0 && index < smsHandyTableView.getItems().size() && smsHandyTableView.getSelectionModel().isSelected(index)  ) {
                    smsHandyTableView.getSelectionModel().clearSelection();
                    changeCreateOrEditSmsHandyButtonPlaceholder("Sms-Handy erstellen");
                    event.consume();
                } else {
                    changeCreateOrEditSmsHandyButtonPlaceholder("Sms-Handy bearbeiten");
                }
            });
            return row;
        });

        providerTableView.setRowFactory(providerTableView -> {
            final TableRow<Provider> row = new TableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                final int index = row.getIndex();
                if (index >= 0 && index < providerTableView.getItems().size() && providerTableView.getSelectionModel().isSelected(index)  ) {
                    providerTableView.getSelectionModel().clearSelection();
                    showProvidersSmsHandy(null);
                    changeCreateOrEditProviderButtonPlaceholder("Provider erstellen");
                    event.consume();
                } else {
                    changeCreateOrEditProviderButtonPlaceholder("Provider bearbeiten");
                }
            });
            return row;
        });

        providerTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            showProvidersSmsHandy(newValue);
            changeCreateOrEditSmsHandyButtonPlaceholder("Sms-Handy erstellen");
        });

        smsHandyTableView.setPlaceholder(new Label("Provider nicht ausgewählt"));
        providerTableView.setPlaceholder(new Label("Bitte, Provider erstellen"));
    }

    public void setMain(Main main) {
        this.main = main;

        providerTableView.setItems(main.getProvidersData());
    }

    private void showProvidersSmsHandy(Provider provider){
        if (provider != null){
            smsHandyTableView.setItems(main.getSmsHandyData().filtered(smsHandy -> smsHandy.getProvider().getName().equals(provider.getName())));
        }
        else {
            smsHandyTableView.setItems(null);
        }
    }


    /**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new provider.
     */
    @FXML
    private void handleshowMessagesForSmsHandy() {
        SmsHandy selectedHandy = smsHandyTableView.getSelectionModel().getSelectedItem();
        if (selectedHandy != null) {
            try {
//                showSmsHandyWindow(selectedHandy);
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("view/MessagesOverview.fxml"));
                AnchorPane page = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.setTitle(selectedHandy.getNumber()+"@"+selectedHandy.getClass().getSimpleName());
                dialogStage.initOwner(this.main.getPrimaryStage());
                dialogStage.setScene(new Scene(page));

                MessagesOverviewController controller = loader.getController();
                controller.setSelectedSmsHandy(selectedHandy);

                controller.setMain(this.main);
                controller.setDialogStage(dialogStage);
                dialogStage.showAndWait();
            } catch (IOException | ProviderNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            alert("Es ist kein SmsHandy ausgewählt! Bitte wählen Sie zuerst SmsHandy aus.");
        }
    }

    // Second variant of sms handy
    private void showSmsHandyWindow(SmsHandy handy) throws IOException {

        SmsHandyController controller = new SmsHandyController();
        FXMLLoader contactsLoader = new FXMLLoader(getClass().getResource("smsHandy/ContactsView.fxml"));
        FXMLLoader chatLoader = new FXMLLoader(getClass().getResource("smsHandy/ChatView.fxml"));

        contactsLoader.setController(controller);
        chatLoader.setController(controller);

        Scene scene = new Scene(contactsLoader.load());

        Stage stage = new Stage();
        stage.setTitle("HANDY - " + handy.getNumber());
        stage.initOwner(this.main.getPrimaryStage());
        stage.setScene(scene);
        stage.show();

        controller.setContactsScene(scene);
        controller.setChatScene(new Scene(chatLoader.load()));
        controller.setStage(stage);
        controller.setHandy(handy);
        controller.setMain(this.main);
        controller.constructSmsHandyWindow();
    }

    /**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new provider.
     */
    @FXML
    private void handleCreateOrEditProvider() {
        Provider selectedProvider = providerTableView.getSelectionModel().getSelectedItem();
        try {
            if (selectedProvider == null) {
                main.showProviderEditDialog();
            } else {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("view/EditProviderDialog.fxml"));
                AnchorPane page = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("SmsHandy bearbeiten");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(this.main.getPrimaryStage());
                dialogStage.setScene(new Scene(page));

                EditProviderDialogController controller = loader.getController();
                controller.setMain(this.main, selectedProvider);
                controller.setStage(dialogStage);
                dialogStage.showAndWait();
            }
        }catch (IOException e){
            alert("Etwas ist schief gelaufen.");
            e.printStackTrace();
        }

    }

    @FXML
    private void handleCreateOrEditSmsHandy(){
        SmsHandy selectedHandy = smsHandyTableView.getSelectionModel().getSelectedItem();
        try {
            if (selectedHandy == null) {
            if (main.getProvidersData().isEmpty()) {
                alert("Es gibt keine Provider, um ein SmsHandy zu erstellen! Bitte erstellen Sie zuerst einen Provider.");
                return;
            }

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("view/CreateSmsHandyDialog.fxml"));
                AnchorPane page = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("SmsHandy erstellen");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(this.main.getPrimaryStage());
                dialogStage.setScene(new Scene(page));

                CreateSmsHandyDialogController controller = loader.getController();
                controller.setMain(this.main);
                controller.setStage(dialogStage);
                dialogStage.showAndWait();

            } else {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("view/EditSmsHandyDialog.fxml"));
                AnchorPane page = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("SmsHandy bearbeiten");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(this.main.getPrimaryStage());
                dialogStage.setScene(new Scene(page));

                EditSmsHandyDialogController controller = loader.getController();
                controller.setMain(this.main, selectedHandy);
                controller.setStage(dialogStage);
                dialogStage.showAndWait();
            }
        } catch (IOException e) {
            alert("Etwas ist schief gelaufen.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteSmsHandy(){
        int selectedHandyIndex = smsHandyTableView.getSelectionModel().getSelectedIndex();
        if (selectedHandyIndex>=0){
            changeCreateOrEditSmsHandyButtonPlaceholder("Neues Handy anlegen");
            SmsHandy selectedHandy = smsHandyTableView.getItems().get(selectedHandyIndex);
            main.getSmsHandyData().remove(selectedHandy);
            smsHandyTableView.getSelectionModel().clearSelection();
            for (Provider provider: main.getProvidersData()) {
                if (provider.getName().equals(selectedHandy.getProvider().getName())) {
                    provider.getSubscriber().remove(selectedHandy.getNumber());
                    provider.getCredits().remove(selectedHandy.getNumber());
                    break;
                }
            }
        }
        else{
            alert("Bitte wählen Sie in der Tabelle ein SMS-Handy aus.");
        }
    }

    @FXML
    private void handleDeleteProvider(){
        int selectedProviderIndex = providerTableView.getSelectionModel().getSelectedIndex();
        if (selectedProviderIndex>=0){
            Provider providerInProviderTV = providerTableView.getItems().get(selectedProviderIndex);
            main.getSmsHandyData().removeIf(smsHandy -> smsHandy.getProvider().getName().equals(providerInProviderTV.getName()));
            main.getProvidersData().remove(providerInProviderTV);
            providerTableView.getSelectionModel().clearSelection();
        }
        else{
            alert("Bitte wählen Sie in der Tabelle einen Provider aus.");
        }
    }

    private void changeCreateOrEditProviderButtonPlaceholder(String buttonPlaceholder) {
        createOrEditProviderButton.setText(buttonPlaceholder);
    }

    private void changeCreateOrEditSmsHandyButtonPlaceholder(String buttonPlaceholder) {
        createOrEditSmsHandyButton.setText(buttonPlaceholder);
    }

    private void alert(String text) {
        Alert alert = new Alert(
                Alert.AlertType.WARNING,
                text,
                ButtonType.CLOSE
        );
        alert.initOwner(main.getPrimaryStage());
        alert.showAndWait();
    }
}
