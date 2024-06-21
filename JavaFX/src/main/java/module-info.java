module at.fhtw.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires java.desktop;


    opens at.fhtw.javafx to javafx.fxml;
    exports at.fhtw.javafx;
    exports at.fhtw.javafx.controller;
    opens at.fhtw.javafx.controller to javafx.fxml;
}