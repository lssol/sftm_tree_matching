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

package mantu.lab.treematching

import org.junit.jupiter.api.Test

import java.io.File

internal class TreeMatcherTest {

    @Test
    fun matchSimpleTrees() {
        val source = DomParserTest.simpleHtml
        val matching = TreeMatcher.matchWebpages(source, source)
    }

    private fun analyzeResults(sourceNodes: List<Node>, targetNodes: List<Node>, matching: List<Edge>): Double {
        val sourceSignatures = sourceNodes.map { it.signature }
        val targetSignatures = targetNodes.map { it.signature }
        val commonSignatures = sourceSignatures.intersect(targetSignatures)

        val edges = matching.distinct()
        val goodMatches = edges.count {
            it.source != null && it.target != null && it.source!!.signature == it.target!!.signature
        }

        return goodMatches.toDouble() / commonSignatures.count()
    }

    @Test
    fun matchTrees() {
        val websiteFolder = "src/test/kotlin/mantu/lab/treematching/websites/"

        val source = File(websiteFolder + "linkedin.html").readText()
        val target = File(websiteFolder + "linkedin_mutant.html").readText()

        val sourceNodes = DomParser.webpageToTree(source)
        val targetNodes = DomParser.webpageToTree(target)
        TreeMatcher().matchTrees(sourceNodes, targetNodes)

        val tStart = System.currentTimeMillis()
        val matching = TreeMatcher().matchTrees(sourceNodes, targetNodes)
        val tEnd = System.currentTimeMillis()

        println("The pages were matched in ${tEnd - tStart}ms")

        val ratioGoodMatches = analyzeResults(sourceNodes, targetNodes, matching.edges)
        println("The ratio of good matches is: $ratioGoodMatches")
    }
}