package main.smsHandy.view;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import main.smsHandy.model.Provider;
import main.smsHandy.model.SmsHandy;

public class MainOverviewController {
    @FXML
    private TableView<Provider> providerTableView;

    @FXML
    private TableColumn<Provider, String> providerNameColumn;

    @FXML
    private TableColumn<SmsHandy, String> smsHandyNameColumn;

    @FXML
    private TableColumn<SmsHandy, Object> smsHandyArtColumn;

    @FXML
    private TableColumn<SmsHandy, Integer> smsHandyGuthabenColumn;
}
