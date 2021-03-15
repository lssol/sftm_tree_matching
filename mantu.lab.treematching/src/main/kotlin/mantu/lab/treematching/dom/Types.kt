package mantu.lab.treematching.dom

// Not a data class because I want comparison to be by reference (not by value)
public class Node(
    val value: List<String>,
    val signature: String,
    val parent: Node?,
    var children: MutableList<Node> = mutableListOf(),
)

public class Edge(
    val source: Node,
    val target: Node,
    val normalizedScore: Double,
    val score: Double
)