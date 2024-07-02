module org.nome.pre_auto {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires java.management;
    requires graphviz.java;
    requires javafx.swing;

    opens org.nome.pre_auto to javafx.fxml;
    exports org.nome.pre_auto;
}