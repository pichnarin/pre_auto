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


public class OptionalScene {
    @FXML
    private Button optConDfaBtn;

    @FXML
    private Button optDesDfaBtn;

    @FXML
    private Button optMiniDfaBtn;

    private Stage stage;

    private Parent parent;
    //switch scene forwards and backwards
    @FXML
    public void onBtnClick(ActionEvent event){
        try{
            if(event.getSource() == optDesDfaBtn) {
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("designFa.fxml")));
            }else if(event.getSource() == optConDfaBtn) {
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("constructDfa.fxml")));
            }else if(event.getSource() == optMiniDfaBtn) {
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("minimizeDfa.fxml")));
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

    @FXML
    public void initialize(){

    }

}
