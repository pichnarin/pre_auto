module org.nome.pre_auto {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    opens org.nome.pre_auto to javafx.fxml;
    exports org.nome.pre_auto.automata_sim;
    opens org.nome.pre_auto.automata_sim to javafx.fxml;
    exports org.nome.pre_auto;
}