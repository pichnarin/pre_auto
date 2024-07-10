package org.nome.pre_auto;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class DesignFaController {

    @FXML
    private ChoiceBox<String> choiceBox;

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
    private TextArea inTransition;

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
        initializeChoiceBox();
        initializeSubmitBtn();
        initializeResetBtn();
    }

    private void initializeSubmitBtn() {
        //the part of dfa is implemented here
        submitBtn.setOnAction(_ -> {
            if (choiceBox.getValue().equals("FA WITHOUT Epsilon")){

                outFaType.clear();
                outFaType.setText("This FA is DFA");

                Set<String> state = new HashSet<>(Arrays.asList(inState.getText().split(",")));
                Set<String> alphabet = new HashSet<>(Arrays.asList(inAlphabet.getText().split(",")));
                String startState = inStartState.getText();
                Set<String> finalState = new HashSet<>(Arrays.asList(inFinalState.getText().split(",")));
                Set<String> transition = new HashSet<>(Arrays.asList(inTransition.getText().split(",\\s*")));
                Set<String> initial_string = new HashSet<>(Arrays.asList(inString.getText().split(",")));

                System.out.printf("Alphabet: %s%n", alphabet);
                System.out.printf("Transition: %s%n", transition);

                // Validate the input
                if (state.isEmpty() || alphabet.isEmpty() || startState.isEmpty() || finalState.isEmpty() || transition.isEmpty() || initial_string.isEmpty()) {
                    outResult.setText("Please fill all the fields");
                    return;
                } else if (!state.contains(startState)) {
                    outResult.setText("Start state must be one of the states");
                    return;
                } else if (!state.containsAll(finalState)) {
                    outResult.setText("Final state must be from the states");
                    return;
                }

                // You can now use these values in your application
                DesignFa primaryData = new DesignFa(state, alphabet, startState, finalState, transition, initial_string);

                String outputPath = "dfa.png";
                String dotScript = primaryData.generateDotScript();
                try {
                    primaryData.GenerateImage(dotScript, outputPath);
                    Image image = new Image(new FileInputStream(outputPath));
                    outGraph.setPreserveRatio(true);
                    outGraph.setImage(image);
                    GridPane.setHalignment(outGraph, HPos.CENTER); // Center horizontally in the cell
                    GridPane.setValignment(outGraph, VPos.CENTER); // Center vertically in the cell
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // Test the input strings
                String[] testStrings = inString.getText().split(",");
                outResult.clear(); // Clear previous results
                int i = 0;
                for (String testString : testStrings) {
                    boolean isAccepted = primaryData.isStringAccepted(testString);
                    outResult.appendText("%d : (%s) = %s\n".formatted(i, testString, isAccepted ? "Accepted" : "Rejected"));
                    i++;
                }

            }
            //if the part of nfa implemented here
            else if(choiceBox.getValue().equals("FA WITH Epsilon")){
                Set<String> state = new HashSet<>(Arrays.asList(inState.getText().split(",")));
                Set<String> alphabet = new HashSet<>(Arrays.asList(inAlphabet.getText().split(",")));
                String startState = inStartState.getText();
                Set<String> finalState = new HashSet<>(Arrays.asList(inFinalState.getText().split(",")));
                Set<String> transition = new HashSet<>(Arrays.asList(inTransition.getText().split(",\\s*")));
                Set<String> initial_string = new HashSet<>(Arrays.asList(inString.getText().split(",")));
                Set<String> epsilonAlphabet = new HashSet<>(Arrays.asList(inEState.getText().split(",")));
                Set<String> epsilonTransition = new HashSet<>(Arrays.asList(inETransition.getText().split(",\\s*")));

                //validate the input
                if (state.isEmpty() || alphabet.isEmpty() || startState.isEmpty() || finalState.isEmpty() || transition.isEmpty() || initial_string.isEmpty() || epsilonAlphabet.isEmpty() || epsilonTransition.isEmpty()) {
                    outResult.setText("Please fill all the fields");
                    return;
                } else if (!state.contains(startState)) {
                    outResult.setText("Start state must be one of the states");
                    return;
                } else if (!state.containsAll(finalState)) {
                    outResult.setText("Final state must be from the states");
                    return;
                }

                outFaType.clear();
                outFaType.setText("This FA is NFA");

                transition.addAll(epsilonTransition);
                alphabet.addAll(epsilonAlphabet);

                System.out.printf("Alphabet: %s%n", alphabet);
                System.out.printf("Transition: %s%n", transition);


                DesignFa secondaryData = new DesignFa(state, alphabet, startState, finalState, transition, initial_string);

                String outputPath = "nfa.png";
                String dotScript = secondaryData.generateDotScript();
                try {
                    secondaryData.GenerateImage(dotScript, outputPath);
                    Image image = new Image(new FileInputStream(outputPath));
                    outGraph.setPreserveRatio(true);
                    outGraph.setImage(image);
                    GridPane.setHalignment(outGraph, HPos.CENTER); // Center horizontally in the cell
                    GridPane.setValignment(outGraph, VPos.CENTER); // Center vertically in the cell
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }

                //test the input strings
                String[] testStrings = inString.getText().split(",");
                outResult.clear(); // Clear previous results
                int i = 0;
                for (String testString : testStrings) {
                    boolean isAccepted = secondaryData.isStringAccepted(testString);
                    outResult.appendText("%d : (%s) = %s\n".formatted(i, testString, isAccepted ? "Accepted" : "Rejected"));
                    i++;
                }

            }
        });
    }

    private void initializeResetBtn() {
        resetBtn.setOnAction(_ ->{
            inAlphabet.clear();
            inEState.clear();
            inETransition.clear();
            inFinalState.clear();
            inStartState.clear();
            inState.clear();
            inString.clear();
            inTransition.clear();
            outResult.clear();
            outFaType.clear();
            outGraph.setImage(null);
        });
    }

    //initialize the choice box for user to pickup dfa or nfa
    public void initializeChoiceBox(){
        //initialize the choice box
        choiceBox.getItems().add("FA WITH Epsilon");
        choiceBox.getItems().add("FA WITHOUT Epsilon");

        //set the default value
        choiceBox.setValue("FA WITHOUT Epsilon");

        if (choiceBox.getValue().equals("FA WITHOUT Epsilon")) {
            inEState.setDisable(true);
            inETransition.setDisable(true);
        }
        //the operation of choice box
        choiceBox.setOnAction(_ -> {
            if(choiceBox.getValue().equals("FA WITH Epsilon")){
                inEState.setDisable(false);
                inETransition.setDisable(false);
            }else if(choiceBox.getValue().equals("FA WITHOUT Epsilon")){
                inEState.setDisable(true);
                inETransition.setDisable(true);
            }
        });
    }
}
