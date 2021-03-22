package mantu.lab.treematching

import org.junit.jupiter.api.Test

import java.io.File

internal class TreeMatcherTest {

    @Test
    fun matchSimpleTrees() {
        val source = DomParserTest.simpleHtml

        val sourceNode = DomParser.webpageToTree(source)
        val targetNode  = DomParser.webpageToTree(source)

        val matching = TreeMatcher.matchTrees(sourceNode, targetNode)
    }

    private fun analyzeResults(sourceNodes: List<Node>, targetNodes: List<Node>, matching: List<Edge>): Double {
        val sourceSignatures = sourceNodes.map { it.signature }
        val targetSignatures = targetNodes.map { it.signature }
        val commonSignatures = sourceSignatures.intersect(targetSignatures)

        val edges = matching.distinct()
        val nbNoMatch = edges.count {
            commonSignatures.contains(it.source?.signature ?: it.target?.signature)
                    && (it.source == null || it.target == null)
        }

        val nbMismatch = edges.count {
            it.source != null && it.target != null && it.source!!.signature != it.target!!.signature
        }
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
        TreeMatcher.matchTrees(sourceNodes, targetNodes)

        val tStart = System.currentTimeMillis()
        val matching = TreeMatcher.matchTrees(sourceNodes, targetNodes)
        val tEnd = System.currentTimeMillis()

        println("The pages were matched in ${tEnd - tStart}ms")

        val ratioGoodMatches = analyzeResults(sourceNodes, targetNodes, matching.edges)
        println("The ratio of good matches is: $ratioGoodMatches")
    }
}