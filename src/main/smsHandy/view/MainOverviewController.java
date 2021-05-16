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
import main.smsHandy.model.Provider;
import main.smsHandy.model.SmsHandy;
import main.smsHandy.model.TariffPlanSmsHandy;

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
                return new SimpleStringProperty(((TariffPlanSmsHandy) handy).getRemainingFreeSms() + " sms");
            }
            int credits = handy.getProvider().getCreditForSmsHandy(handy.getNumber());
            return new SimpleStringProperty(credits + " euro");
        });

        smsHandyTableView.setRowFactory(tableView2 -> {
            final TableRow<SmsHandy> row = new TableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                final int index = row.getIndex();
                if (index >= 0 && index < smsHandyTableView.getItems().size() && smsHandyTableView.getSelectionModel().isSelected(index)  ) {
                    smsHandyTableView.getSelectionModel().clearSelection();
                    changeCreateOrEditSmsHandyButtonPlaceholder("Neues Handy anlegen");
                    event.consume();
                } else {
                    changeCreateOrEditSmsHandyButtonPlaceholder("Ausgewähltes Handy bearbeiten");
                }
            });
            return row;
        });
    }

    public void setMain(Main main) {
        this.main = main;

        providerTableView.setItems(main.getProvidersData());
        smsHandyTableView.setItems(main.getSmsHandyData());
    }

    /**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new provider.
     */
    @FXML
    private void handleNewProvider() {
        Provider tempProvider = new Provider();
        boolean okClicked = main.showProviderEditDialog(tempProvider);
        if (okClicked) {
            main.getProvidersData().add(tempProvider);
        }
    }

    @FXML
    private void handleCreateOrEditSmsHandy() {
        SmsHandy selectedHandy = smsHandyTableView.getSelectionModel().getSelectedItem();
        if (selectedHandy == null) {
            if (main.getProvidersData().isEmpty()) {
                alert("There is no providers to create an SmsHandy! Please, create a provider first.");
                return;
            }
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("view/CreateSmsHandyDialog.fxml"));
                AnchorPane page = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Create New SmsHandy");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(this.main.getPrimaryStage());
                dialogStage.setScene(new Scene(page));

                CreateSmsHandyDialogController controller = loader.getController();
                controller.setMain(this.main);
                controller.setStage(dialogStage);
                dialogStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // edit selected sms handy
        }
    }

    private void changeCreateOrEditSmsHandyButtonPlaceholder(String buttonPlaceholder) {
        createOrEditSmsHandyButton.setText(buttonPlaceholder);
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
