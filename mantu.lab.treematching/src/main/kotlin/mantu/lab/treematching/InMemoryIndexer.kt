package mantu.lab.treematching

import kotlin.math.ln
import kotlin.math.truncate

data class InMemoryIndexerSettings(val maxNeighborsPerNode: Int = 100, val maxTokenAppearance: Int)

class InMemoryIndex(private val settings: InMemoryIndexerSettings) {
    private val index: HashMap<String, MutableList<Node>> = HashMap()
    private val nodes: HashSet<Node> = HashSet()
    private val removedTokens: HashSet<String> = HashSet()
    private var idfPrecomputation: Double = 0.0

    companion object InMemoryIndexer {
        public fun buildIndex(sourceNodes: List<Node>, settings: InMemoryIndexerSettings): InMemoryIndex {
            val index = InMemoryIndex(settings)
            sourceNodes.forEach { node -> node.value.forEach { value -> index.add(value, node) } }
            index.precomputeIdf()

            return index
        }
    }

    public fun findNeighbors(targets: List<Node>): Neighbors {
        val neighbors = Neighbors()
        targets.forEach { target ->
            val results = queryIndex(target.value)
            if (results.isNotEmpty())
                neighbors.value[target] = results
        }

        return neighbors
    }

    private fun add(token: String, node: Node) {
        nodes.add(node)
        val nbTokenAppearance = (index[token]?.count() ?: 0) + 1

        when {
            removedTokens.contains(token)                    -> Unit
            nbTokenAppearance >= settings.maxTokenAppearance -> removedTokens.add(token)
            index.containsKey(token)                         -> index[token]!!.add(node)
            else                                             -> index[token] = mutableListOf(node)
        }
    }

    private fun queryIndex(query: List<String>): HashMap<Node, Double> {
        val hits = HashMap<Node, Double>()

        query.forEach { token ->
            when {
                !index.containsKey(token) -> Unit
                else                      -> {
                    val nodesWithToken = index[token]
                    val idf = idfPrecomputation - ln(nodesWithToken!!.count().toDouble())
                    nodesWithToken.forEach { node ->
                        hits[node] = hits[node]?.plus(idf) ?: idf
                    }
                }
            }
        }

        return truncateResults(hits)
    }

    private fun precomputeIdf() { idfPrecomputation = ln(nodes.count().toDouble()) }

    private fun truncateResults(hits: HashMap<Node, Double>): HashMap<Node, Double> {
        val map = hits
            .toList()
            .sortedBy { it.second }
            .take(settings.maxNeighborsPerNode)
            .toMap()

        return HashMap(map)
    }
}

