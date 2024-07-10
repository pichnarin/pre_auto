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

    // Compute Epsilon Closure for a start state
    public Set<String> computeEpsilonClosure(String state) {
        Set<String> epsilonClosure = new HashSet<>();
        epsilonClosure.add(state);
        boolean added = true;
        while (added) {
            added = false;
            for (String t : eTransition) {
                String[] parts = t.split(" "); // Split on space
                if (parts.length < 3) {
                    System.out.printf("Invalid transition format from primaryDataClass: %s%n", t);
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

    //Compute the transition function for the DFA from the start state
    public Set<String> computeTransitionFunction(String state, String input) {
        Set<String> transitionFunction = new HashSet<>();
        Set<String> epsilonClosure = computeEpsilonClosure(state);
        for (String s : epsilonClosure) {
            for (String t : transition) {
                String[] parts = t.split(" "); // Split on space
                if (parts.length < 3) {
                    System.out.printf("Invalid transition format from primaryDataClass: %s%n", t);
                    continue;
                }
                if (parts[0].trim().equals(s) && parts[1].trim().equals(input)) {
                    transitionFunction.add(parts[2].trim());
                }
            }
        }
        return transitionFunction;
    }

    //find the start state of the DFA
    public Set<String> findStartState() {
        return computeEpsilonClosure(startState);
    }

    //find the final state of the DFA
    public Set<String> findFinalState() {
        Set<String> finalStates = new HashSet<>();
        for (String s : state) {
            Set<String> epsilonClosure = computeEpsilonClosure(s);
            for (String f : finalState) {
                if (epsilonClosure.contains(f)) {
                    finalStates.add(s);
                    break;
                }
            }
        }
        return finalStates;
    }

    //find the transition function of the DFA
    public Set<String> findTransitionFunction() {
        Set<String> transitionFunction = new HashSet<>();
        Set<String> visited = new HashSet<>();
        Queue<Set<String>> queue = new LinkedList<>();
        queue.add(findStartState());
        while (!queue.isEmpty()) {
            Set<String> currentState = queue.poll();
            visited.add(currentState.toString());
            for (String a : alphabet) {
                Set<String> nextState = new HashSet<>();
                for (String s : currentState) {
                    nextState.addAll(computeTransitionFunction(s, a));
                }
                if (!nextState.isEmpty()) {
                    transitionFunction.add("%s %s %s".formatted(currentState.toString(), a, nextState.toString()));
                    if (!visited.contains(nextState.toString())) {
                        queue.add(nextState);
                    }
                }
            }
        }
        return transitionFunction;
    }



    public static void main() {
        // Example NFA input
        Set<String> state = new HashSet<>(Arrays.asList("1", "2", "3", "4", "5"));
        Set<String> alphabet = new HashSet<>(Arrays.asList("a", "b"));
        String startState = "1";
        Set<String> finalState = new HashSet<>(Collections.singletonList("5"));
        Set<String> transition = new HashSet<>(Arrays.asList(
                "1 a 3",
                "2 a 4",
                "2 a 5",
                "3 b 4",
                "4 a 5",
                "4 b 5"
        ));
        Set<String> eTransition = new HashSet<>(Collections.singletonList("1 e 2")); // Assuming eTransition is epsilon transitions

        ConstructDfa constructDfa = new ConstructDfa(state, alphabet, startState, finalState, transition, "e", eTransition);

        System.out.printf("Start State: %s%n", constructDfa.findStartState());
        System.out.printf("Final State: %s%n", constructDfa.findFinalState());
        System.out.printf("Transition Function: %s%n", constructDfa.findTransitionFunction());

        //format this output
        constructDfa.findTransitionFunction().forEach(System.out::println);

    }
}
