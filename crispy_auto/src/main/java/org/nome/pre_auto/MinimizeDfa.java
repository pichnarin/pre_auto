package org.nome.pre_auto;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;
//I'm used chatgpt 👌👌👌👌👌

public class MinimizeDfa {
    Set<String> state;
    Set<String> alphabet;
    String startState;
    Set<String> finalState;
    Set<String> transitions;
    Map<String, Map<String, String>> transitionMap;

    public MinimizeDfa(Set<String> state, Set<String> alphabet, String startState, Set<String> finalState, Set<String> transitions) {
        this.state = state;
        this.alphabet = alphabet;
        this.startState = startState;
        this.finalState = finalState;
        this.transitions = transitions;
        this.transitionMap = new HashMap<>();
        parseTransitions();
    }

    private void parseTransitions() {
        for (String transition : transitions) {
            String[] parts = transition.split(" ");
            String fromState = parts[0];
            String input = parts[1];
            String toState = parts[2];

            transitionMap.putIfAbsent(fromState, new HashMap<>());
            transitionMap.get(fromState).put(input, toState);
        }
    }

    public MinimizeDfa minimize() {
        // Step 1: Remove unreachable states
        Set<String> reachableStates = getReachableStates();
        state.retainAll(reachableStates);
        finalState.retainAll(reachableStates);
        transitionMap.keySet().retainAll(reachableStates);

        // Step 2: Identify and merge equivalent states
        Map<String, String> equivalentStates = findEquivalentStates();

        // Build the minimized DFA
        Set<String> newStates = new HashSet<>();
        Set<String> newFinalStates = new HashSet<>();
        Set<String> newTransitions = new HashSet<>();
        String newStartState = equivalentStates.get(startState);

        for (String oldState : state) {
            String newState = equivalentStates.get(oldState);
            newStates.add(newState);
            if (finalState.contains(oldState)) {
                newFinalStates.add(newState);
            }
            if (transitionMap.containsKey(oldState)) {
                for (String input : transitionMap.get(oldState).keySet()) {
                    String oldNextState = transitionMap.get(oldState).get(input);
                    String newNextState = equivalentStates.get(oldNextState);
                    newTransitions.add("%s %s %s".formatted(newState, input, newNextState));
                }
            }
        }

        return new MinimizeDfa(newStates, alphabet, newStartState, newFinalStates, newTransitions);
    }

    Set<String> getReachableStates() {
        Set<String> reachable = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(startState);
        reachable.add(startState);

        while (!queue.isEmpty()) {
            String currentState = queue.poll();
            if (transitionMap.containsKey(currentState)) {
                for (String input : transitionMap.get(currentState).keySet()) {
                    String nextState = transitionMap.get(currentState).get(input);
                    if (reachable.add(nextState)) {
                        queue.add(nextState);
                    }
                }
            }
        }

        return reachable;
    }

    Map<String, String> findEquivalentStates() {
        Map<String, String> equivalent = new HashMap<>();
        for (String s : state) {
            equivalent.put(s, s);
        }

        boolean[][] distinguishable = new boolean[state.size()][state.size()];
        List<String> stateList = new ArrayList<>(state);
        Map<String, Integer> stateIndex = new HashMap<>();
        for (int i = 0; i < stateList.size(); i++) {
            stateIndex.put(stateList.get(i), i);
        }

        for (int i = 0; i < stateList.size(); i++) {
            for (int j = 0; j < stateList.size(); j++) {
                if (finalState.contains(stateList.get(i)) != finalState.contains(stateList.get(j))) {
                    distinguishable[i][j] = true;
                }
            }
        }

        boolean changed;
        do {
            changed = false;
            for (int i = 0; i < stateList.size(); i++) {
                for (int j = 0; j < stateList.size(); j++) {
                    if (!distinguishable[i][j]) {
                        for (String input : alphabet) {
                            String nextI = transitionMap.getOrDefault(stateList.get(i), Collections.emptyMap()).get(input);
                            String nextJ = transitionMap.getOrDefault(stateList.get(j), Collections.emptyMap()).get(input);
                            if (nextI != null && nextJ != null && distinguishable[stateIndex.get(nextI)][stateIndex.get(nextJ)]) {
                                distinguishable[i][j] = true;
                                changed = true;
                                break;
                            }
                        }
                    }
                }
            }
        } while (changed);

        for (int i = 0; i < stateList.size(); i++) {
            for (int j = i + 1; j < stateList.size(); j++) {
                if (!distinguishable[i][j]) {
                    String stateI = stateList.get(i);
                    String stateJ = stateList.get(j);
                    String representative = equivalent.get(stateI);
                    for (Map.Entry<String, String> entry : equivalent.entrySet()) {
                        if (entry.getValue().equals(stateJ)) {
                            entry.setValue(representative);
                        }
                    }
                }
            }
        }

        return equivalent;
    }

    //to String
    public String toString() {
        return "MinimizeDfa{state=%s, alphabet=%s, startState='%s', finalState=%s, transitions=%s, transitionMap=%s}".formatted(state, alphabet, startState, finalState, transitions, transitionMap);
    }

    //generate dot script for the minimized DFA
    public String generateDotScript() {
        StringBuilder dotScript = new StringBuilder("digraph G {\n");
        // Add a title to the graph
        dotScript.append("label=\"Minimized DFA(Red = StartState, Green = FinalState)\";\n");
        // Set the title location to top
        dotScript.append("labelloc=t;\n");
        //set the direction of graph
        dotScript.append("rankdir=LR;\n");
        //set size of graphviz image
        dotScript.append("size=\"6.5,3.3\";\n");
        //set dpi(dots per inch) of image
        dotScript.append("dpi=100;\n");
        //set ratio to fill to preserve aspect ratio
        dotScript.append("ratio=\"fill\";\n");
        //set page size equal to graph size
        dotScript.append("page=\"6.5,3.3\";\n");
        //center the drawing on the page
        dotScript.append("center=true;\n");
        //set the margin of the graph
        dotScript.append("margin=\"0.1,0.1\";\n");
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
        for (String t : this.transitions) {
            String[] parts = t.split(" ");
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



    //generate image of the minimized DFA
    public void GenerateImage(String dotScript, String outputPath) throws IOException, InterruptedException, IOException {
        String dotPath = "dot";
        String format = "-Tpng";
        String outputType = "-o";
        String[] cmd = new String[]{dotPath, format, outputType, outputPath};
        Process p = Runtime.getRuntime().exec(cmd);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
        writer.write(dotScript);
        writer.close();
        p.waitFor();
    }

    public static void main() {
        Set<String> states = new HashSet<>(Arrays.asList("a", "b", "c", "d", "e"));
        Set<String> alphabet = new HashSet<>(Arrays.asList("0", "1"));
        String startState = "a";
        Set<String> finalStates = new HashSet<>(Collections.singletonList("e"));
        Set<String> transitions = new HashSet<>(Arrays.asList(
                "a 0 b",
                "a 1 c",
                "b 1 d",
                "b 0 b",
                "c 1 c",
                "c 0 b",
                "d 0 b",
                "d 1 e",
                "e 1 c",
                "e 0 b"
        ));

        MinimizeDfa dfa = new MinimizeDfa(states, alphabet, startState, finalStates, transitions);
        MinimizeDfa minimizedDfa = dfa.minimize();

        System.out.println("Minimized DFA:");
        System.out.printf("Reachable state: %s%n", dfa.getReachableStates());
        System.out.println("Equivalent states:");
        dfa.findEquivalentStates().forEach((k, v) -> System.out.printf("%s -> %s%n", k, v));
        System.out.printf("States: %s%n", minimizedDfa.state);
        System.out.printf("Alphabet: %s%n", minimizedDfa.alphabet);
        System.out.printf("Start State: %s%n", minimizedDfa.startState);
        System.out.printf("Final States: %s%n", minimizedDfa.finalState);
        System.out.printf("Transitions: %s%n", minimizedDfa.transitions);

    }
}
