package org.nome.pre_auto;

import java.io.IOException;
import java.util.Set;

//ths class is a subclass od primaryData that will be used to store the data of NFA
public class SecondaryData extends PrimaryData{

    public SecondaryData(Set<String> state, Set<String> alphabet, String startState, Set<String> finalState, Set<String> transition, Set<String> initial_string) {
        super(state, alphabet, startState, finalState, transition, initial_string);
    }

    @Override
    public boolean isStringAccepted(String testString) {
        return super.isStringAccepted(testString);
    }

    @Override
    public String generateDotScript() {
        return super.generateDotScript();
    }

    @Override
    public void GenerateImage(String dotScript, String outputPath) throws IOException, InterruptedException {
        super.GenerateImage(dotScript, outputPath);
    }
}
