package main.smsHandy;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.smsHandy.exception.ProviderNotFoundException;
import main.smsHandy.model.PrepaidSmsHandy;
import main.smsHandy.model.Provider;
import main.smsHandy.model.SmsHandy;
import main.smsHandy.model.TariffPlanSmsHandy;
import main.smsHandy.view.MainOverviewController;
import main.smsHandy.view.ProviderEditDialogController;

import java.io.IOException;

public class Main extends Application {
    private Stage primaryStage;

    private ObservableList<Provider> providersData = FXCollections.observableArrayList();
    private ObservableList<SmsHandy> smsHandyData = FXCollections.observableArrayList();

    public Main() throws ProviderNotFoundException {
        Provider provider1 = new Provider();
        Provider provider2 = new Provider();
        Provider provider3 = new Provider();

        provider1.setName("Vodafon");
        provider2.setName("O2");
        provider3.setName("Telecom");

        providersData.add(provider1);
        providersData.add(provider2);
        providersData.add(provider3);

        PrepaidSmsHandy prepaidSmsHandyO = new PrepaidSmsHandy("123", provider1);
        PrepaidSmsHandy prepaidSmsHandy1 = new PrepaidSmsHandy("234", provider2);
        PrepaidSmsHandy prepaidSmsHandy2 = new PrepaidSmsHandy("345", provider3);

        TariffPlanSmsHandy handy1 = new TariffPlanSmsHandy("456", provider1);
        TariffPlanSmsHandy handy2 = new TariffPlanSmsHandy("567", provider2);
        TariffPlanSmsHandy handy3 = new TariffPlanSmsHandy("678", provider3);
        prepaidSmsHandyO.sendSms(prepaidSmsHandy1.getNumber(),"Test");

        smsHandyData.add(prepaidSmsHandyO);
        smsHandyData.add(prepaidSmsHandy1);
        smsHandyData.add(prepaidSmsHandy2);
        smsHandyData.add(handy1);
        smsHandyData.add(handy2);
        smsHandyData.add(handy3);
    }

    public ObservableList<Provider> getProvidersData() {
        return providersData;
    }

    public ObservableList<SmsHandy> getSmsHandyData() {
        return smsHandyData;
    }

    @Override
    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("SMS-Handy Anwendung");

        showMainOverview();
    }

    public void showMainOverview(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/MainOverview.fxml"));
            Parent parent = loader.load();

            Scene scene = new Scene(parent);
            primaryStage.setScene(scene);
            primaryStage.show();
            MainOverviewController moc = loader.getController();
            moc.setMain(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a dialog to edit details for the specified provider. If the user
     * clicks OK, the changes are saved into the provided provider object and true
     * is returned.
     *
     * @param provider the provider object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showProviderEditDialog(Provider provider) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/ProviderEditDialog.fxml"));
//            Parent parent = loader.load();
//            Scene scene=new Scene(parent);
//            primaryStage.setScene(scene);
//            primaryStage.show();
//            ProviderEditDialogController providerEditDialogController = loader.getController();
//            providerEditDialogController.setDialogStage(this);
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Provider anlegen");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the provider into the controller.
            ProviderEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setProvider(provider);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
