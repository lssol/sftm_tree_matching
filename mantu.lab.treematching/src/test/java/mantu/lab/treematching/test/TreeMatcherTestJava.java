package mantu.lab.treematching.test;

import java.util.List;

import mantu.lab.treematching.*;
public class TreeMatcherTestJava {
    void MatchTrees() {
        String oldHtml = "<html>....</html>";
        String newHtml = "<html>....</html>";

        // The matching
        TreeMatcherResponse response = TreeMatcher.matchWebpages(oldHtml, newHtml);

        // Using the result
        List<Edge> edges = response.getEdges();
        edges.forEach(edge -> {
            String oldXPath = edge.getSource().getXPath();
            String newXPath = edge.getTarget().getXPath();
            // Warning: getSource() returns null when nothing has been matched
        });
    }
}
