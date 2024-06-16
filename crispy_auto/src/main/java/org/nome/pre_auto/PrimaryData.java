package org.nome.pre_auto;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

public class PrimaryData {
        private final Set<String> state;
        private Set<String> alphabet;
        private String startState;
        private Set<String> finalState;
        private Set<String> transition;
        private Set<String> initial_string;

        public PrimaryData(Set<String> state, Set<String> alphabet, String startState, Set<String> finalState, Set<String> transition, Set<String> initial_string) {
            this.state = state;
            this.alphabet = alphabet;
            this.startState = startState;
            this.finalState = finalState;
            this.transition = transition;
            this.initial_string = initial_string;
        }

    public boolean isStringAccepted(String testString) {
        String currentState = startState;
        for (char c : testString.toCharArray()) {
            boolean transitionFound = false;
            for (String t : transition) {
                String[] parts = t.split("->");
                if (parts[0].trim().equals(currentState) && parts[1].trim().equals(String.valueOf(c))) {
                    currentState = parts[2].trim();
                    transitionFound = true;
                    break;
                }
            }
            if (!transitionFound) {
                return false;
            }
        }
        return finalState.contains(currentState);
    }

    public String generateDotScript() {
        StringBuilder dotScript = new StringBuilder("digraph G {\n");
        dotScript.append("rankdir=LR;\n");
        // Add states to the DOT script
        for (String state : this.state) {
            dotScript.append(state).append(" [shape=circle];\n");
        }

        // Mark the initial state
        dotScript.append(this.startState).append(" [shape=circle, color=red];\n"); // color=red to highlight the initial state

        // Mark the final states
        for (String finalState : this.finalState) {
            dotScript.append(finalState).append(" [shape=doublecircle, color=green];\n");
        }

        // Add transitions to the DOT script
        for (String t : this.transition) {
            String[] parts = t.split("->");
            if (parts.length == 3) {
                dotScript.append(parts[0].trim())
                        .append(" -> ")
                        .append(parts[2].trim())
                        .append(" [ label = \"")
                        .append(parts[1].trim())
                        .append("\" ];\n");
            }
        }
        dotScript.append("}");
        return dotScript.toString();
    }

    public void GenerateImage(String dotScript, String outputPath) throws IOException, InterruptedException {
        Path tempDotFile = Files.createTempFile("graph", ".dot");
        Files.writeString(tempDotFile, dotScript);
        ProcessBuilder pb = new ProcessBuilder("dot", "-Tpng", "-o", outputPath, tempDotFile.toString());
        Process process = pb.start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Graphviz exited with error code " + exitCode);
        }
    }
}

