package mantu.lab.treematching

import mantu.lab.utils.LinkedList
import mantu.lab.utils.LinkedListNode
import mantu.lab.utils.average
import mantu.lab.utils.pushAt
import kotlin.random.Random


public class Metropolis(val edges: List<Edge>, val nbNodes: Int, val maxNeighbors: Int, val parameters: Parameters) {
    public data class Parameters(
            val gamma: Double = 1.0,
            val lambda: Double = 2.5,
            val nbIterations: Int = 1,
            val metropolisNormalisation: Boolean = true
    )

    val nodeToEdges: HashMap<Node, HashSet<Edge>> = hashMapOf()
    val linkedListNodes = HashMap<Edge, LinkedListNode<Edge>>(edges.count())
    val adjacentEdges = HashSet<Edge>(maxNeighbors * 2)
    val newMatching = ArrayList<Edge>(nbNodes + 10)
    var linkedEdges = LinkedList<Edge>()

    init { computeNodeToEdgesDic() }

    public fun run(): ArrayList<Edge> {
        var currentMatching = suggestMatching(listOf())
        var currentObjective = computeObjective(currentMatching)

        var maxObjective = currentObjective
        var bestMatching = currentMatching

        for (i in 0..parameters.nbIterations) {
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
        edges.forEach {
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
            adjacentEdges.addAll(nodeToEdges[edge.source]!!)
        if (edge.target != null)
            adjacentEdges.addAll(nodeToEdges[edge.target]!!)
        adjacentEdges.remove(edge)
        return adjacentEdges
    }

    private fun computeObjective(matching: List<Edge>): Double =
            matching.average { it.score }

    private fun keepEdge(edge: Edge) {
        newMatching.add(edge)
        val adjacentEdges = getAdjacentEdges(edge)
        adjacentEdges.forEach { linkedEdges.remove(linkedListNodes[it]) }
        linkedEdges.remove(linkedListNodes[edge])
    }

    private fun suggestMatching(previousMatching: List<Edge>): ArrayList<Edge> {
        newMatching.clear()
        linkedEdges = LinkedList<Edge>()
        linkedListNodes.clear()
        edges.forEach {  linkedListNodes[it] = linkedEdges.add(it) }

        val p = Random.nextInt(0, previousMatching.count())
        (0..p).forEach { keepEdge(previousMatching[it]) }

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