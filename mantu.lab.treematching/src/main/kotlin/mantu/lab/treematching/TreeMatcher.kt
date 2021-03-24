package mantu.lab.treematching

import mantu.lab.utils.toMap
import kotlin.math.*

internal class TreeMatcher(val params: Parameters = Parameters()) {
    public data class Parameters(
            val propagationParameters: SimilarityPropagation.Parameters = SimilarityPropagation.Parameters(),
            val metropolisParameters: Metropolis.Parameters = Metropolis.Parameters(),
            val noMatchCost: Double = 1.2,
            val limitNeighbors: Int = 100,
            val maxTokenAppearance: (nbNodes: Int) -> Int = { sqrt(it.toDouble()).roundToInt() },
            val maxPenalizationChildren: Double = 0.4,
            val maxPenalizationParentsChildren: Double = 0.2
    )

    companion object {
        /**
         * Compute a node-by-node matching between two trees
         * @param sourceNodes the list of nodes in the first tree
         * @param targetNodes the list of nodes in the second tree
         * @param params the parameters of the matching
         * @return the result of the matching. In particular, the "edges" member contains the list of individual node-to-node matches. If a node was not match, it's corresponding match will be null
         */
        @JvmStatic
        public fun matchTrees(sourceNodes: List<Node>, targetNodes: List<Node>, params: Parameters = Parameters()): TreeMatcherResponse {
            return TreeMatcher(params).matchTrees(sourceNodes, targetNodes)
        }

        /**
         * Compute a node-by-node matching between two webpages
         * @param source the HTML of the first webpage
         * @param target the HTML of the second webpage
         * @param params the parameters of the matching
         * @return the result of the matching. In particular, the "edges" member contains the list of individual node-to-node matches. If a node was not match, it's corresponding match will be null
         */
        @JvmStatic
        public fun matchTrees(source: String, target: String, params: Parameters = Parameters()): TreeMatcherResponse {
            val sourceNodes = DomParser.webpageToTree(source)
            val targetNodes = DomParser.webpageToTree(target)

            return matchTrees(sourceNodes, targetNodes, params)
        }
    }

    /**
     * Compute a node-by-node matching between two trees
     * @param sourceNodes the list of nodes in the first tree
     * @param targetNodes the list of nodes in the second tree
     * @return the result of the matching. In particular, the "edges" member contains the list of individual node-to-node matches. If a node was not match, it's corresponding match will be null
     */
    public fun matchTrees(sourceNodes: List<Node>, targetNodes: List<Node>): TreeMatcherResponse {
        val tStart = System.currentTimeMillis()

        val indexerParameters = InMemoryIndex.Parameters(params.limitNeighbors, params.maxTokenAppearance(sourceNodes.count()))
        var index = InMemoryIndex.buildIndex(sourceNodes, indexerParameters)
        val targetIndex = InMemoryIndex.buildIndex(sourceNodes, indexerParameters)

        addParentToken(sourceNodes, index.index)
        addParentToken(targetNodes, targetIndex.index)

        // Need to rebuild the index because we added some tokens
        index = InMemoryIndex.buildIndex(sourceNodes, indexerParameters)

        var neighbors = index.findNeighbors(targetNodes)
        neighbors = SimilarityPropagation.propagateSimilarity(neighbors, params.propagationParameters)

        val noMatchEdges: List<Edge> = getNoMatchEdges(sourceNodes, targetNodes)
        val edges = neighbors.toEdges() + noMatchEdges

        val metropolis = Metropolis(edges, sourceNodes.count() + targetNodes.count(), params.limitNeighbors, params.metropolisParameters)
        val matchingEdges = metropolis.run()

        val tEnd = System.currentTimeMillis()

        return TreeMatcherResponse(matchingEdges, tEnd - tStart, metropolis.maxObjective)
    }

    private fun getNoMatchEdges(sourceNodes: List<Node>, targetNodes: List<Node>): List<Edge> {
        val noMatchScore = 1 / params.noMatchCost
        val edgesFromSource = sourceNodes.map { Edge(it, null, noMatchScore) }
        val edgesFromTarget = targetNodes.map { Edge(null, it, noMatchScore) }

        return edgesFromSource + edgesFromTarget
    }

    private fun addParentToken(nodes: List<Node>, index: HashMap<String, HashSet<Node>>) {
        val rarestToken = nodes
                .toMap({ it }, { node ->
                    node.value
                            .filter { !it.startsWith("/BODY") }
                            .minByOrNull { index[it]?.count() ?: nodes.count() }
                })
        nodes.forEach {
            if (it.parent != null && rarestToken.containsKey(it.parent))
                it.value.add("#C_${rarestToken[it.parent]}")
        }
    }
}