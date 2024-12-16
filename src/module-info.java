module module_name {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.xerial.sqlitejdbc;
    requires java.desktop;

    opens gui to javafx.fxml;
    opens domain to javafx.fxml;

    exports gui;
    exports domain;
}