package mantu.lab.treematching

import kotlin.math.abs

public data class PropagationParameters(
        val parent: Double = 0.4,
        val parentInv: Double = 0.8,
        val envelop: List<Double> = listOf(0.9, 0.1, 0.01)
)

private fun increaseScore(neighbors: Neighbors, sourceNode: Node, targetNode: Node, incr: Double) {
    if (abs(incr) < 0.001)
        return

    val hits = neighbors[targetNode] ?: hashMapOf()
    val hitScore = hits[sourceNode] ?: 0.0
    val newScore = hitScore + incr

    if (abs(newScore) < 0.001)
        return

    hits[sourceNode] = newScore
    if (!neighbors.value.containsKey(targetNode))
        neighbors[targetNode] = hits
}

public fun propagateSimilarity(neighbors: Neighbors, settings: PropagationParameters, currentEnvelop: Double) {
    val newSimilarity = Neighbors()
    neighbors.forEachPair { source, target, score ->
        increaseScore(newSimilarity, source, target, score)
        val pSource = source.parent
        val pTarget = target.parent

        if (pSource == null || pTarget == null)
            return@forEachPair

        val parentScore = neighbors.score(pSource, pTarget)
        increaseScore(newSimilarity, source, target, currentEnvelop * parentScore * settings.parent)
        increaseScore(newSimilarity, pSource, pTarget, currentEnvelop * score * settings.parentInv)
    }
}
