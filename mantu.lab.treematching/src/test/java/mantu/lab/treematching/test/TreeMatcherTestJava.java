/*
 * Tree Matching library based on the SFTM algorithm.
 *
 * Copyright (C) 2021  Mantu, Sacha Brisset.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see https://www.gnu.org/licenses/.
 */

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
