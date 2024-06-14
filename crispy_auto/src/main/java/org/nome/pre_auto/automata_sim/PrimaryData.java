package org.nome.pre_auto.automata_sim;


import java.util.Set;

public class PrimaryData {
        private Set<String> state;
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
    }

