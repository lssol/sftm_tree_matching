package mantu.lab.treematching

public class Neighbors {
    val value : HashMap<Node, HashMap<Node, Double>> = HashMap()

    public fun score(source: Node, target: Node) = value[target]?.get(source) ?: 0.0
    public fun toEdges(): List<Edge> {
        val edges = mutableListOf<Edge>()

        value.forEach { (target, hits) ->
            hits.forEach { (source, score) ->
                edges.add(Edge(source, target, score))
            }
        }

        return edges
    }
}