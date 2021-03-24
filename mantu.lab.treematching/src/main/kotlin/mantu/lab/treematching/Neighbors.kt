package mantu.lab.treematching

internal class Neighbors {
    val value : HashMap<Node, HashMap<Node, Double>> = HashMap()

    public fun score(source: Node, target: Node) = value[target]?.get(source) ?: 0.0
    operator fun get(node: Node) = value[node]
    operator fun set(node: Node, v: HashMap<Node, Double>) { value[node] = v }
    public fun forEachPair(f: (sourceNode: Node, targetNode: Node, score: Double) -> Unit) {
        value.forEach { (target, hits) ->
            hits.forEach { (source, score) ->
                f(source, target, score)
            }
        }
    }
    public fun toEdges(): List<Edge> {
        val edges = mutableListOf<Edge>()
        forEachPair { source, target, score ->
            edges.add(Edge(source, target, score))
        }

        return edges
    }
}