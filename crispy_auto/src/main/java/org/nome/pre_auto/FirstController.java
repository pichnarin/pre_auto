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
    private Button ConstructNfaBtn;

    @FXML
    private Button SubmitDfaBtn;

    @FXML
    private MenuItem aboutProgram;

    @FXML
    private ChoiceBox<String> chooseFa;

    @FXML
    private ImageView faImageView;

    @FXML
    private Button resetBtn;

    @FXML
    private MenuItem saveFile;

    @FXML
    private TextField txtAlphabet;

    @FXML
    private TextField txtCheckFaResult;

    @FXML
    private TextField txtEpsilonS;

    @FXML
    private TextArea txtEpsilonT;

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
    private MenuItem errorHelper;

    @FXML
    private MenuItem instructionHelper;


    @FXML
    private void initialize() {
        initializeSubmitDfaBtn();
        initializeConDfaBtn();
        initializeResetButton();
        initialChooseFa();
        initializeNewFile();
        initializeOpenFile();
        initializeSaveFile();
        initializeAboutProgram();
        initializeErrorHelper();
        initializeInstructionHelper();
    }

    //construct an dfa that equivalent to nfa
    private void initializeConDfaBtn() {
        ConstructNfaBtn.setOnAction(_->{
            if(chooseFa.getValue().equals("DFA")){
                txtCheckFaResult.clear();
                txtCheckFaResult.setText("This FA is DFA");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("This is not NFA");
                alert.setContentText("""
                        a. You cannot construct DFA from another DFA. \s
                        b. Please choose NFA to construct DFA. \s
                        """);
                alert.showAndWait();
            }
            else if(chooseFa.getValue().equals("NFA with Epsilon")) {
                txtCheckFaResult.clear();
                txtCheckFaResult.setText("This FA is NFA");

                Set<String> state = new HashSet<>(Arrays.asList(txtState.getText().split(",")));
                Set<String> alphabet = new HashSet<>(Arrays.asList(txtAlphabet.getText().split(",")));
                String startState = txtStartState.getText();
                Set<String> finalState = new HashSet<>(Arrays.asList(txtFinalState.getText().split(",")));
                Set<String> transition = new HashSet<>(Arrays.asList(txtTransition.getText().split(",")));
                Set<String> epsilonAlphabet = new HashSet<>(Arrays.asList(txtEpsilonS.getText().split(",")));
                Set<String> epsilonTransition = new HashSet<>(Arrays.asList(txtEpsilonT.getText().split(",")));

                transition.addAll(epsilonTransition);
                alphabet.addAll(epsilonAlphabet);

                txtStrAR.clear();
            }
        });
    }

    //alert about the program
    private void initializeAboutProgram() {
        aboutProgram.setOnAction(_ -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setWidth(400);
            alert.setHeight(300);
            alert.setTitle("About Program");
            alert.setHeaderText("Automaton Simulation");
            alert.setContentText("This program is a simulation of a Deterministic Finite Automaton (DFA) and a Non-Deterministic Finite Automaton (NFA).");
            alert.showAndWait();
        });
    }

    //user can read to know how to use the program
    private void initializeInstructionHelper(){
        instructionHelper.setOnAction(_->{
            Alert instruction = new Alert(Alert.AlertType.INFORMATION);
            instruction.setWidth(400);
            instruction.setHeight(300);
            instruction.setTitle("Instruction");
            instruction.setHeaderText("How to use this automaton simulation program?");
            instruction.setContentText("""
                    a. You need to choose the FA type either it is DFA or NFA.\s
                    b. You need to input all the data in each text field.\s
                    c. Click on submit button to complete the simulation.\s
                    """);
            instruction.showAndWait();
        });
    }

    //user can fix the program after finding a bug
    private void initializeErrorHelper(){
        errorHelper.setOnAction(_->{
            Alert helperAlert = new Alert(Alert.AlertType.INFORMATION);
            helperAlert.setWidth(400);
            helperAlert.setHeight(300);
            helperAlert.setTitle("Error Helper");
            helperAlert.setHeaderText("Problem: If you submitting and the program doesn't show the fa graph.");
            helperAlert.setContentText("Solution: Please download the graphviz from the official website here: https://graphviz.org/download/ and install it. after installed you need to copy address of the bin folder and paste it into the system environment variable.");
            helperAlert.showAndWait();
        });
    }

    //user can open the file that content the fa information
    private void initializeOpenFile() {
    }

    //user can create a new file
    private void initializeNewFile() {
    }
    
    //user can save the file that content the fa information
    private void initializeSaveFile() {
        saveFile.setOnAction(_ -> {
        });
    }


    //user can choose the fa type
    private void initialChooseFa() {
        chooseFa.getItems().add("DFA");
        chooseFa.getItems().add("NFA with Epsilon");
        chooseFa.getItems().add("NFA without Epsilon");
        chooseFa.setOnAction(_ -> {
            if (chooseFa.getValue().equals("DFA")) {
                txtEpsilonS.setDisable(true);
                txtEpsilonT.setDisable(true);
            } else if (chooseFa.getValue().equals("NFA with Epsilon")) {
                txtEpsilonS.setDisable(false);
                txtEpsilonT.setDisable(false);
            }else if(chooseFa.getValue().equals("NFA without Epsilon")){
                txtEpsilonS.setDisable(true);
                txtEpsilonT.setDisable(true);
            }
        });
    }

    //user can reset the fa information
    private void initializeResetButton() {
        resetBtn.setOnAction(_ -> {
            //clear all the text field
            txtState.clear();
            txtAlphabet.clear();
            txtStartState.clear();
            txtFinalState.clear();
            txtTransition.clear();
            txtString.clear();
            txtEpsilonS.clear();
            txtEpsilonT.clear();
            txtCheckFaResult.clear();
            txtStrAR.clear();
            faImageView.setImage(null);

        });
    }

    //user can submit the fa information
    private void initializeSubmitDfaBtn() {
        SubmitDfaBtn.setOnAction(_ -> {
            // Handle the user's input here

            //the part of dfa is implemented here
            if (chooseFa.getValue().equals("DFA") || chooseFa.getValue().equals("NFA without Epsilon")){

                txtCheckFaResult.clear();
                txtCheckFaResult.setText("This FA is DFA");

                Set<String> state = new HashSet<>(Arrays.asList(txtState.getText().split(",")));
                Set<String> alphabet = new HashSet<>(Arrays.asList(txtAlphabet.getText().split(",")));
                String startState = txtStartState.getText();
                Set<String> finalState = new HashSet<>(Arrays.asList(txtFinalState.getText().split(",")));
                Set<String> transition = new HashSet<>(Arrays.asList(txtTransition.getText().split(",")));
                Set<String> initial_string = new HashSet<>(Arrays.asList(txtString.getText().split(",")));

                System.out.printf("Alphabet: %s%n", alphabet);
                System.out.printf("Transition: %s%n", transition);

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
                    txtStrAR.appendText("%d : (%s) = %s\n".formatted(i, testString, isAccepted ? "Accepted" : "Rejected"));
                    i++;
                }

            }
            //if the part of nfa implemented here
            else if(chooseFa.getValue().equals("NFA with Epsilon")){
                Set<String> state = new HashSet<>(Arrays.asList(txtState.getText().split(",")));
                Set<String> alphabet = new HashSet<>(Arrays.asList(txtAlphabet.getText().split(",")));
                String startState = txtStartState.getText();
                Set<String> finalState = new HashSet<>(Arrays.asList(txtFinalState.getText().split(",")));
                Set<String> transition = new HashSet<>(Arrays.asList(txtTransition.getText().split(",")));
                Set<String> initial_string = new HashSet<>(Arrays.asList(txtString.getText().split(",")));
                Set<String> epsilonAlphabet = new HashSet<>(Arrays.asList(txtEpsilonS.getText().split(",")));
                Set<String> epsilonTransition = new HashSet<>(Arrays.asList(txtEpsilonT.getText().split(",")));

                //validate the input
                if (state.isEmpty() || alphabet.isEmpty() || startState.isEmpty() || finalState.isEmpty() || transition.isEmpty() || initial_string.isEmpty() || txtEpsilonS.getText().isEmpty() || txtEpsilonT.getText().isEmpty()) {
                    txtStrAR.setText("Please fill all the fields");
                    return;
                } else if (!state.contains(startState)) {
                    txtStrAR.setText("Start state must be one of the states");
                    return;
                } else if (!state.containsAll(finalState)) {
                    txtStrAR.setText("Final state must be from the states");
                    return;
                }

                txtCheckFaResult.clear();
                txtCheckFaResult.setText("This FA is NFA");

                transition.addAll(epsilonTransition);
                alphabet.addAll(epsilonAlphabet);

                System.out.printf("Alphabet: %s%n", alphabet);
                System.out.printf("Transition: %s%n", transition);


                PrimaryData secondaryData = new PrimaryData(state, alphabet, startState, finalState, transition, initial_string);

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
                    txtStrAR.appendText("%d : (%s) = %s\n".formatted(i, testString, isAccepted ? "Accepted" : "Rejected"));
                    i++;
                }

            }
        });
    }
}
