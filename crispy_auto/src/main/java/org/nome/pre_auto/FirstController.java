package org.nome.pre_auto;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FirstController {

    @FXML
    private Label AlphabetLabel;

    @FXML
    private Button ConstructNfaBtn;

    @FXML
    private Label FinalStateLabel;

    @FXML
    private MenuBar MenuBar;

    @FXML
    private Button MinimizeDfaBtn;

    @FXML
    private Label StartStateLabel;

    @FXML
    private Label StateLabel;

    @FXML
    private Label StringLabel;

    @FXML
    private Button SubmitDfaBtn;

    @FXML
    private Label TransitionLabel;

    @FXML
    private Button resetBtn;

    @FXML
    private TextField txtAlphabet;

    @FXML
    private TextField txtFinalState;

    @FXML
    private TextField txtStartState;

    @FXML
    private TextField txtState;

    @FXML
    private TextArea txtStrAR;

    @FXML
    private TextField txtString;

    @FXML
    private TextArea txtTransition;

    @FXML
    private ImageView faImageView;

    @FXML
    private GridPane imageGridpane;

    @FXML
    private TextArea txtEpsilonT;

    @FXML
    private TextField txtEpsilonS;

    @FXML
    private TextArea txtCheckFa;

    @FXML
    private Button CheckFaBtn;

    @FXML
    private void initialize() {
        initializeSubmitDfaBtn();
        initializeResetButton();
    }

    private void initializeResetButton() {
        resetBtn.setOnAction(_ -> {
            txtStrAR.clear();
            faImageView.setImage(null);
        });
    }

    private void initializeSubmitDfaBtn() {
        SubmitDfaBtn.setOnAction(_ -> {
            // Handle the user's input here
            Set<String> state = new HashSet<>(Arrays.asList(txtState.getText().split(",")));
            Set<String> alphabet = new HashSet<>(Arrays.asList(txtAlphabet.getText().split(",")));
            String startState = txtStartState.getText();
            Set<String> finalState = new HashSet<>(Arrays.asList(txtFinalState.getText().split(",")));
            Set<String> transition = new HashSet<>(Arrays.asList(txtTransition.getText().split(",")));
            Set<String> initial_string = new HashSet<>(Arrays.asList(txtString.getText().split(",")));

           if(state.isEmpty() || alphabet.isEmpty() || startState.isEmpty() || finalState.isEmpty() || transition.isEmpty() || initial_string.isEmpty()) {
               txtStrAR.setText("Please fill all the fields");
               return;
           }else if(!state.contains(startState)) {
               txtStrAR.setText("Start state must be one of the states");
               return;
           }else if(!state.containsAll(finalState)) {
               txtStrAR.setText("Final state must be from the states");
               return;
           }

            // You can now use these values in your application
            PrimaryData primaryData = new PrimaryData(state, alphabet, startState, finalState, transition, initial_string);

            String outputPath = "dfa.png";
            String dotScript = primaryData.generateDotScript();
            try {
                primaryData.GenerateImage(dotScript, outputPath);
                Image image = new Image(new FileInputStream(outputPath));
                faImageView.setPreserveRatio(true);
                faImageView.setImage(image);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }

            // Test the input strings
            String[] testStrings = txtString.getText().split(",");
            txtStrAR.clear(); // Clear previous results
            int i = 0;
            for (String testString : testStrings) {
                boolean isAccepted = primaryData.isStringAccepted(testString);
                txtStrAR.appendText(i + " : " + "("+ testString+")" + " = " + (isAccepted ? "Accepted" : "Rejected") + "\n");
                i++;
            }
        });
    }
}
