package main.smsHandy;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.smsHandy.exception.ProviderNotFoundException;
import main.smsHandy.model.PrepaidSmsHandy;
import main.smsHandy.model.Provider;
import main.smsHandy.model.SmsHandy;
import main.smsHandy.model.TariffPlanSmsHandy;

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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
