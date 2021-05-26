package main.smsHandy.view;

import javafx.animation.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.skin.ButtonSkin;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class MyButtonSkin extends ButtonSkin {

    public MyButtonSkin(Button control) {
        super(control);


//
//        final FadeTransition fadeIn = new FadeTransition(Duration.millis(100));
//        fadeIn.setNode(control);
//        fadeIn.setToValue(1);
//        control.setOnMouseEntered(e -> fadeIn.playFromStart());
//
//        final FadeTransition fadeOut = new FadeTransition(Duration.millis(100));
//        fadeOut.setNode(control);
//        fadeOut.setToValue(0.5);
//        control.setOnMouseExited(e -> fadeOut.playFromStart());
//
//        control.setOpacity(0.5);


        final Color startColor = Color.web("#e08090");
        final Color endColor = Color.web("#80e090");

        final ObjectProperty<Color> color = new SimpleObjectProperty<Color>(startColor);

        // String that represents the color above as a JavaFX CSS function:
        // -fx-body-color: rgb(r, g, b);
        // with r, g, b integers between 0 and 255
        final StringBinding cssColorSpec = Bindings.createStringBinding(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return String.format("-fx-body-color: rgb(%d, %d, %d);",
                        (int) (256*color.get().getRed()),
                        (int) (256*color.get().getGreen()),
                        (int) (256*color.get().getBlue()));
            }
        }, color);

        // bind the button's style property
        control.styleProperty().bind(cssColorSpec);

        final Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(color, startColor)),
                new KeyFrame(Duration.seconds(1), new KeyValue(color, endColor)));

        control.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                timeline.play();
            }
        });

        // Create a rotating rectangle and set it as the graphic for the button
        final Rectangle rotatingRect = new Rectangle(5,5,10,6);
        rotatingRect.setFill(Color.CORNFLOWERBLUE);
        final Pane rectHolder = new Pane();
        rectHolder.setMinSize(20, 16);
        rectHolder.setPrefSize(20, 16);
        rectHolder.setMaxSize(20, 16);
        rectHolder.getChildren().add(rotatingRect);
        final RotateTransition rotate = new RotateTransition(Duration.seconds(1), rotatingRect);
        rotate.setByAngle(360);
        rotate.setCycleCount(5);
        rotate.setInterpolator(Interpolator.LINEAR);
        control.setGraphic(rectHolder);
        rotate.play();
    }
}