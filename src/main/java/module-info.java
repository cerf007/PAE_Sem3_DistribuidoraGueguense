module ni.edu.uam.distribuidoragueguense {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;


    opens ni.edu.uam.distribuidoragueguense to javafx.fxml;
    exports ni.edu.uam.distribuidoragueguense;
    exports ni.edu.uam.distribuidoragueguense.controller;
    opens ni.edu.uam.distribuidoragueguense.controller to javafx.fxml;
}