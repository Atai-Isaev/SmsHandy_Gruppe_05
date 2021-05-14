package main.smsHandy.view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.smsHandy.Main;
import main.smsHandy.model.Provider;
import main.smsHandy.model.SmsHandy;
import main.smsHandy.model.TariffPlanSmsHandy;

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
    }

    public void setMain(Main main) {
        this.main = main;

        providerTableView.setItems(main.getProvidersData());
        smsHandyTableView.setItems(main.getSmsHandyData());
    }
}
