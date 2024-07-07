package org.nome.pre_auto;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.Objects;

public class DesignFaController {

    @FXML
    private TextField inAlphabet;

    @FXML
    private TextField inEState;

    @FXML
    private TextArea inETransition;

    @FXML
    private TextField inFinalState;

    @FXML
    private TextField inStartState;

    @FXML
    private TextField inState;

    @FXML
    private TextField inString;

    @FXML
    private TextArea inTransitions;

    @FXML
    private ImageView outGraph;

    @FXML
    private TextArea outResult;

    @FXML
    private TextField outFaType;

    @FXML
    private Button resetBtn;

    @FXML
    private Button submitBtn;


    @FXML
    private Button backToOptBtn;

    private Stage stage;

    private Parent parent;
    //switch scene forwards and backwards
    @FXML
    public void btnOnClick(ActionEvent event){
        //switch to pre_auto_version_1.0.fxml
        try{
            if(event.getSource() == backToOptBtn){
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("optional.fxml")));
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
