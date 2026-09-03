package ni.edu.uam.distribuidoragueguense.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DistribuidoraController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
