module at.fhtw.communication {
    requires javafx.controls;
    requires javafx.fxml;


    opens at.fhtw.communication to javafx.fxml;
    exports at.fhtw.communication;
}