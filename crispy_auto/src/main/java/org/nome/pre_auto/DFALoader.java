package org.nome.pre_auto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DFALoader {

    // Method to load DFA from file
    public static void loadDFA(String fileName) {
        Set<Set<String>> states = new HashSet<>();
        Set<String> alphabet = new HashSet<>();
        Map<Set<String>, Map<String, Set<String>>> transitions = new HashMap<>();
        Set<Set<String>> finalStates = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            String section = "";
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (line.startsWith("States:")) {
                    section = "states";
                } else if (line.startsWith("Alphabet:")) {
                    section = "alphabet";
                } else if (line.startsWith("Transitions:")) {
                    section = "transitions";
                } else if (line.startsWith("Final States:")) {
                    section = "finalStates";
                } else {
                    switch (section) {
                        case "states":
                            // Extract states from line
                            Set<String> state = parseState(line);
                            states.add(state);
                            break;
                        case "alphabet":
                            // Extract alphabet from line
                            alphabet.addAll(parseAlphabet(line));
                            break;
                        case "transitions":
                            // Extract transitions from line
                            parseTransition(line, transitions);
                            break;
                        case "finalStates":
                            // Extract final states from line
                            Set<String> finalState = parseState(line);
                            finalStates.add(finalState);
                            break;
                        default:
                            break;
                    }
                }
            }

            // Print loaded DFA
            System.out.println("States: " + states);
            System.out.println("Alphabet: " + alphabet);
            System.out.println("Transitions: " + transitions);
            System.out.println("Final States: " + finalStates);

        } catch (IOException e) {
            System.err.println("Error loading DFA from file: " + e.getMessage());
        }
    }

    // Helper method to parse states from line
    private static Set<String> parseState(String line) {
        String[] parts = line.split(":");
        if (parts.length < 2) {
            return Collections.emptySet();
        }
        String stateList = parts[1].trim();
        stateList = stateList.substring(1, stateList.length() - 1); // Remove brackets
        return new HashSet<>(Arrays.asList(stateList.split(", ")));
    }

    // Helper method to parse alphabet from line
    private static Set<String> parseAlphabet(String line) {
        String[] parts = line.split(":");
        if (parts.length < 2) {
            return Collections.emptySet();
        }
        String alphabetList = parts[1].trim();
        alphabetList = alphabetList.substring(1, alphabetList.length() - 1); // Remove brackets
        return new HashSet<>(Arrays.asList(alphabetList.split(", ")));
    }

    // Helper method to parse transitions from line
    private static void parseTransition(String line, Map<Set<String>, Map<String, Set<String>>> transitions) {
        String[] parts = line.split(" -> ");
        if (parts.length < 3) {
            return;
        }
        Set<String> fromState = Collections.singleton(parseState(parts[0].trim()).iterator().next()); // Only one state expected
        String[] transParts = parts[1].split(" -> ");
        String symbol = transParts[0].trim();
        Set<String> toState = parseState(transParts[1].trim());

        transitions.putIfAbsent(fromState, new HashMap<>());
        transitions.get(fromState).put(symbol, toState);
    }

    public static void main(String[] args) {
        loadDFA("dfa_output.txt");
    }
}
