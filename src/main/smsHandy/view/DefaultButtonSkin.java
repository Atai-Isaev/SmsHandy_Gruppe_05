package main.smsHandy.view;

import javafx.scene.control.Button;
import javafx.scene.control.skin.ButtonSkin;

public class DefaultButtonSkin extends ButtonSkin {

    public DefaultButtonSkin(Button control) {
        super(control);
        control.setText("SUCCESS!");
    }

}
