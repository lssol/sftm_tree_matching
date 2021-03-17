package mantu.lab.treematching

import mantu.lab.utils.average
import mantu.lab.utils.toMap
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sqrt

public class TreeMatcher(val params: Parameters = Parameters()) {
    public data class Parameters(
            val propagationParameters: SimilarityPropagation.Parameters = SimilarityPropagation.Parameters(),
            val metropolisParameters: Metropolis.Parameters = Metropolis.Parameters(),
            val noMatchCost: Double = 4.5,
            val limitNeighbors: Int = 100,
            val maxTokenAppearance: (nbNodes: Int) -> Int = { sqrt(it.toDouble()).roundToInt() },
            val maxPenalizationChildren: Double = 0.4,
            val maxPenalizationParentsChildren: Double = 0.2
    )


    private fun computeChildrenPenalization(neighbors: Neighbors, nodes: List<Node>) {
        val edges = neighbors.toEdges()
        val averageChildrenCount = nodes.average { it.children.count() }

        fun computeRatioChildren(cT: Int, cS: Int, maxPenalization: Int) {
            val ratioChildren = abs(cS - cT) / averageChildrenCount
        }

    }
    private fun addParentToken(nodes: List<Node>, index: HashMap<String, List<Node>>) {
        val rarestToken = nodes
                .toMap({ it }, { node ->
                    node.value
                            .filter { !it.startsWith("/BODY") }
                            .minByOrNull { index[it]?.count() ?: nodes.count() }
                })
        nodes.forEach {
            if (it.parent != null && rarestToken.containsKey(it.parent))
                it.value.add("#C_{${rarestToken[it.parent]}")
        }
    }

}