package mantu.lab.treematching

import mantu.lab.utils.LinkedList
import mantu.lab.utils.LinkedListNode
import mantu.lab.utils.average
import mantu.lab.utils.pushAt
import kotlin.random.Random


class Metropolis(val edges: List<Edge>, nbNodes: Int, val maxNeighbors: Int, val parameters: Parameters) {
    public data class Parameters(
        val gamma: Double = 1.0,
        val lambda: Double = 0.7,
        val nbIterations: Int = 1,
    )

    var linkedEdges = LinkedList<Edge>()
    var maxObjective: Double = 0.0
    val adjacentEdges = HashSet<Edge>(maxNeighbors * 2)
    val nodeToEdges: HashMap<Node, HashSet<Edge>> = hashMapOf()

    private val linkedListNodes = HashMap<Edge, LinkedListNode<Edge>>(edges.count())
    private val newMatching = ArrayList<Edge>(nbNodes + 10)
    private val sortedEdges = edges.sortedByDescending { edge -> edge.score }

    init {
        computeNodeToEdgesDic()
    }

    public fun run(): ArrayList<Edge> {
        var currentMatching = suggestMatching(listOf())
        var currentObjective = computeObjective(currentMatching)

        maxObjective = currentObjective
        var bestMatching = currentMatching

        for (i in 1 until parameters.nbIterations) {
            val matching = suggestMatching(currentMatching)
            val objective = computeObjective(matching)
            val acceptanceRatio = objective / currentObjective
            if (Random.nextDouble() > acceptanceRatio)
                continue
            currentMatching = matching
            currentObjective = objective
            if (currentObjective <= maxObjective)
                continue
            maxObjective = currentObjective
            bestMatching = currentMatching
        }

        return bestMatching
    }

    private fun computeNodeToEdgesDic() {
        sortedEdges.forEach {
            when {
                it.source == null -> nodeToEdges.pushAt(it.target!!, it)
                it.target == null -> nodeToEdges.pushAt(it.source, it)
                else -> nodeToEdges.pushAt(it.source, it).pushAt(it.target, it)
            }
        }
    }

    private fun getAdjacentEdges(edge: Edge): HashSet<Edge> {
        adjacentEdges.clear()
        if (edge.source != null)
            adjacentEdges.addAll(nodeToEdges[edge.source] ?: emptyList())
        if (edge.target != null)
            adjacentEdges.addAll(nodeToEdges[edge.target] ?: emptyList())
        return adjacentEdges
    }

    private fun computeObjective(matching: List<Edge>): Double =
        matching.average { it.score }

    private fun keepEdge(edge: Edge) {
        newMatching.add(edge)
        val adjacentEdges = getAdjacentEdges(edge)
        adjacentEdges.forEach { linkedEdges.remove(linkedListNodes[it]) }
    }

    private fun suggestMatching(previousMatching: List<Edge>): ArrayList<Edge> {
        newMatching.clear()
        linkedEdges = LinkedList()
        linkedListNodes.clear()
        sortedEdges.forEach { linkedListNodes[it] = linkedEdges.add(it) }

        if (previousMatching.count() != 0) {
            val p = Random.nextInt(0, previousMatching.count())
            (0..p).forEach { keepEdge(previousMatching[it]) }
        }

        while (linkedEdges.count > 0) {
            for (edge in linkedEdges) {
                if (Random.nextDouble() > parameters.gamma)
                    continue
                keepEdge(edge)
                break
            }
        }

        return newMatching
    }
}