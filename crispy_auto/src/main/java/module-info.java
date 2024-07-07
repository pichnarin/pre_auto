module org.nome.pre_auto {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires java.management;

    exports org.nome.pre_auto;
    opens org.nome.pre_auto to javafx.fxml;

}