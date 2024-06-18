module at.fhtw.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;


    opens at.fhtw.demo to javafx.fxml;
    exports at.fhtw.demo;
}