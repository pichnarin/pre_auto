package org.nome.pre_auto;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.util.Objects;


public class SwitchScene {
    @FXML
    private Button btnSwitch;

    private Stage stage;

    private Parent parent;
    //switch scene forwards and backwards
    @FXML
    public void onBtnClick(ActionEvent event){
        //switch to pre_auto_version_1.0.fxml
        try{
            if(event.getSource() == btnSwitch){
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("pre_auto_version_1.0.fxml")));
            }

            assert parent != null;
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
