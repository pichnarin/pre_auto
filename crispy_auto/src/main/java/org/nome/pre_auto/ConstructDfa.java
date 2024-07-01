package org.nome.pre_auto;

import java.util.*;

public class ConstructDfa {
    private final Set<String> state;
    private final String startState;
    private final Set<String> finalState;
    private final Set<String> transition;
    private final Set<String> alphabet;
    private final String eAlphabet;
    private final Set<String> eTransition;

    // Constructor
    public ConstructDfa(Set<String> state, Set<String> alphabet, String startState, Set<String> finalState, Set<String> transition, String eAlphabet, Set<String> eTransition) {
        this.state = state;
        this.alphabet = alphabet;
        this.startState = startState;
        this.finalState = finalState;
        this.transition = transition;
        this.eAlphabet = eAlphabet;
        this.eTransition = eTransition;
    }

    // Getters
    public Set<String> getState() {
        return state;
    }

    public Set<String> getAlphabet() {
        return alphabet;
    }

    public String getStartState() {
        return startState;
    }

    public Set<String> getFinalState() {
        return finalState;
    }

    public Set<String> getTransition() {
        return transition;
    }

    // Compute Epsilon Closure for a single state
    public Set<String> computeEpsilonClosure(String state) {
        Set<String> epsilonClosure = new HashSet<>();
        epsilonClosure.add(state);
        boolean added = true;
        while (added) {
            added = false;
            for (String t : eTransition) {
                String[] parts = t.split(" "); // Split on space
                if (parts.length < 3) {
                    System.out.println("Invalid transition format from primaryDataClass: " + t);
                    continue;
                }
                if (epsilonClosure.contains(parts[0].trim()) && parts[1].trim().equals(eAlphabet)) {
                    if (!epsilonClosure.contains(parts[2].trim())) {
                        epsilonClosure.add(parts[2].trim());
                        added = true;
                    }
                }
            }
        }
        return epsilonClosure;
    }

    // Compute Move
    public Set<String> computeMove(Set<String> states, String symbol) {
        Set<String> move = new HashSet<>();
        for (String s : states) {
            for (String t : transition) {
                String[] parts = t.split(" "); // Split on space
                if (parts.length < 3) {
                    System.out.println("Invalid transition format from primaryDataClass: " + t);
                    continue;
                }
                if (s.equals(parts[0].trim()) && parts[1].trim().equals(symbol)) {
                    move.add(parts[2].trim());
                }
            }
        }
        return move;
    }

    // Find DFA start state
    public Set<String> findDfaStartState() {
        Set<String> startStateSet = new HashSet<>();
        startStateSet.add(this.startState);
        return computeEpsilonClosure(this.startState);
    }

    // Find DFA final states
    public Set<Set<String>> findDfaFinalStates() {
        Set<Set<String>> dfaFinalStates = new HashSet<>();
        for (String s : state) {
            Set<String> closure = computeEpsilonClosure(s);
            for (String c : closure) {
                if (finalState.contains(c)) {
                    dfaFinalStates.add(closure);
                    break;
                }
            }
        }
        return dfaFinalStates;
    }

    // Compute DFA transitions
    public Map<Set<String>, Map<String, Set<String>>> computeDfaTransitions() {
        Map<Set<String>, Map<String, Set<String>>> dfaTransitions = new HashMap<>();
        Set<Set<String>> visited = new HashSet<>();
        Queue<Set<String>> queue = new LinkedList<>();
        Set<String> startState = findDfaStartState();
        queue.add(startState);
        visited.add(startState);

        while (!queue.isEmpty()) {
            Set<String> currentState = queue.poll();
            dfaTransitions.putIfAbsent(currentState, new HashMap<>());
            for (String a : alphabet) {
                Set<String> moveResult = computeMove(currentState, a);
                Set<String> epsilonClosureResult = new HashSet<>();
                for (String state : moveResult) {
                    epsilonClosureResult.addAll(computeEpsilonClosure(state));
                }
                if (!epsilonClosureResult.isEmpty()) {
                    dfaTransitions.get(currentState).put(a, epsilonClosureResult);
                    if (!visited.contains(epsilonClosureResult)) {
                        queue.add(epsilonClosureResult);
                        visited.add(epsilonClosureResult);
                    }
                }
            }
        }
        return dfaTransitions;
    }

    // Main method to construct the DFA
    public void constructDfa() {
        Set<String> startState = findDfaStartState();
        Set<Set<String>> dfaFinalStates = findDfaFinalStates();
        Map<Set<String>, Map<String, Set<String>>> dfaTransitions = computeDfaTransitions();

        System.out.println("DFA Start State: " + startState);
        System.out.println("DFA Final States: " + dfaFinalStates);
        System.out.println("DFA Transitions: " + dfaTransitions);
    }

    public static void main(String[] args) {
        // Example NFA input
        Set<String> state = new HashSet<>(Arrays.asList("q0", "q1", "q2", "q3"));
        Set<String> alphabet = new HashSet<>(Arrays.asList("a", "b"));
        String startState = "q0";
        Set<String> finalState = new HashSet<>(Collections.singletonList("q3"));
        Set<String> transition = new HashSet<>(Arrays.asList(
                "q0 a q0",
                "q0 b q0",
                "q0 b q1",
                "q1 a q2",
                "q1 b q2",
                "q2 a q3",
                "q2 b q3"
        ));
        Set<String> eTransition = new HashSet<>(Collections.singletonList("q1 e q2")); // Assuming eTransition is epsilon transitions

        ConstructDfa constructDfa = new ConstructDfa(state, alphabet, startState, finalState, transition, "e", eTransition);
        constructDfa.constructDfa();
    }
}
