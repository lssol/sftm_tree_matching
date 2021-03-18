package mantu.lab.treematching

// Not a data class because I want comparison to be by reference (not by value)
public class Node(
    val value: MutableList<String>,
    val signature: String = "",
    val parent: Node? = null,
    var children: MutableList<Node> = mutableListOf(),
)

public class Edge(
    val source: Node?,
    val target: Node?,
    val score: Double,
)

public data class TreeMatcherResponse(
    val edges: List<Edge>,
    val computationTime: Long,
    val cost: Double
)