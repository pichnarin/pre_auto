package org.nome.pre_auto;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
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
    private TextArea txtCheckFaResult;

    @FXML
    private ChoiceBox<String> chooseFa;

    @FXML
    private void initialize() {
        initializeSubmitDfaBtn();
        initializeResetButton();
        initialChooseFa();
    }

    private void initialChooseFa() {
        chooseFa.getItems().add("DFA");
        chooseFa.getItems().add("NFA");
        chooseFa.setOnAction(_ -> {
            if (chooseFa.getValue().equals("DFA")) {
                txtEpsilonS.setDisable(true);
                txtEpsilonT.setDisable(true);
            } else if (chooseFa.getValue().equals("NFA")) {
                txtEpsilonS.setDisable(false);
                txtEpsilonT.setDisable(false);
            }
        });
    }


    private void initializeResetButton() {
        resetBtn.setOnAction(_ -> {
            txtCheckFaResult.clear();
            txtStrAR.clear();
            faImageView.setImage(null);
        });
    }

    private void initializeSubmitDfaBtn() {
        SubmitDfaBtn.setOnAction(_ -> {
            // Handle the user's input here

            //the part of dfa is implemented here
            Set<String> state = new HashSet<>(Arrays.asList(txtState.getText().split(",")));
            Set<String> alphabet = new HashSet<>(Arrays.asList(txtAlphabet.getText().split(",")));
            String startState = txtStartState.getText();
            Set<String> finalState = new HashSet<>(Arrays.asList(txtFinalState.getText().split(",")));
            Set<String> transition = new HashSet<>(Arrays.asList(txtTransition.getText().split(",")));
            Set<String> initial_string = new HashSet<>(Arrays.asList(txtString.getText().split(",")));

            //the part of nfa is implemented here
            Set<String> epsilonAlphabet = new HashSet<>(Arrays.asList(txtEpsilonS.getText().split(",")));
            Set<String> epsilonTransition = new HashSet<>(Arrays.asList(txtEpsilonT.getText().split(",")));


            // Validate the input
            if (state.isEmpty() || alphabet.isEmpty() || startState.isEmpty() || finalState.isEmpty() || transition.isEmpty() || initial_string.isEmpty()) {
                txtStrAR.setText("Please fill all the fields");
                return;
            } else if (!state.contains(startState)) {
                txtStrAR.setText("Start state must be one of the states");
                return;
            } else if (!state.containsAll(finalState)) {
                txtStrAR.setText("Final state must be from the states");
                return;
            }

            //if the fa is dfa
            if (chooseFa.getValue().equals("DFA")){

                txtCheckFaResult.setText("This fa is dfa");

                // You can now use these values in your application
                PrimaryData primaryData = new PrimaryData(state, alphabet, startState, finalState, transition, initial_string);

                String outputPath = "dfa.png";
                String dotScript = primaryData.generateDotScript();
                try {
                    primaryData.GenerateImage(dotScript, outputPath);
                    Image image = new Image(new FileInputStream(outputPath));
                    faImageView.setPreserveRatio(true);
                    faImageView.setImage(image);
                    GridPane.setHalignment(faImageView, HPos.CENTER); // Center horizontally in the cell
                    GridPane.setValignment(faImageView, VPos.CENTER); // Center vertically in the cell
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // Test the input strings
                String[] testStrings = txtString.getText().split(",");
                txtStrAR.clear(); // Clear previous results
                int i = 0;
                for (String testString : testStrings) {
                    boolean isAccepted = primaryData.isStringAccepted(testString);
                    txtStrAR.appendText(i + " : " + "(" + testString + ")" + " = " + (isAccepted ? "Accepted" : "Rejected") + "\n");
                    i++;
                }

                //if the fa is nfa
            } else if (chooseFa.getValue().equals("NFA")){

                transition.addAll(epsilonTransition);
                alphabet.addAll(epsilonAlphabet);

                txtCheckFaResult.clear();
                txtCheckFaResult.setText("This fa is nfa");

                //concat the dfa's transition and nfa's transition and dfa's alphabet and nfa's alphabet
                Set<String> transitionNfa = new HashSet<>(transition);
                Set<String> alphabetNfa = new HashSet<>(alphabet);

                SecondaryData secondaryData = new SecondaryData(state, alphabetNfa, startState, finalState, transitionNfa, initial_string);

                String outputPath = "nfa.png";
                String dotScript = secondaryData.generateDotScript();
                try {
                    secondaryData.GenerateImage(dotScript, outputPath);
                    Image image = new Image(new FileInputStream(outputPath));
                    faImageView.setPreserveRatio(true);
                    faImageView.setImage(image);
                    GridPane.setHalignment(faImageView, HPos.CENTER); // Center horizontally in the cell
                    GridPane.setValignment(faImageView, VPos.CENTER); // Center vertically in the cell
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }

                //test the input strings
                String[] testStrings = txtString.getText().split(",");
                txtStrAR.clear(); // Clear previous results
                int i = 0;
                for (String testString : testStrings) {
                    boolean isAccepted = secondaryData.isStringAccepted(testString);
                    txtStrAR.appendText(i + " : " + "(" + testString + ")" + " = " + (isAccepted ? "Accepted" : "Rejected") + "\n");
                    i++;
                }
            }
        });
    }
}
