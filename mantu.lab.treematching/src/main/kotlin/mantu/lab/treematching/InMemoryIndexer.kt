package mantu.lab.treematching

import kotlin.math.ln
import kotlin.math.log


class InMemoryIndex(private val params: Parameters) {
    data class Parameters(val maxNeighborsPerNode: Int = 100, val maxTokenAppearance: Int)

    public val index: HashMap<String, HashSet<Node>> = HashMap()
    private val nodes: HashSet<Node> = HashSet()
    private val removedTokens: HashSet<String> = HashSet()
    private var idfPrecomputation: Double = 0.0

    companion object InMemoryIndexer {
        public fun buildIndex(sourceNodes: List<Node>, params: Parameters): InMemoryIndex {
            val inMemoryIndex = InMemoryIndex(params)
            sourceNodes.forEach { node -> node.value.forEach { value -> inMemoryIndex.add(value, node) } }
            inMemoryIndex.precomputeIdf()

            return inMemoryIndex
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
            removedTokens.contains(token)                 -> Unit
            nbTokenAppearance > params.maxTokenAppearance -> {
                removedTokens.add(token)
                index.remove(token)
            }
            index.containsKey(token)                      -> index[token]!!.add(node)
            else                                          -> index[token] = HashSet(mutableSetOf(node))
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

    private fun precomputeIdf() {
        idfPrecomputation = ln(nodes.count().toDouble())
    }

    private fun truncateResults(hits: HashMap<Node, Double>): HashMap<Node, Double> {
        val map = hits
            .toList()
            .sortedBy { it.second }
            .take(params.maxNeighborsPerNode)
            .toMap()

        return HashMap(map)
    }
}

