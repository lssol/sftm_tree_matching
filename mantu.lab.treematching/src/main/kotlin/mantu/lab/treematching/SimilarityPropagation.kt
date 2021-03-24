package mantu.lab.treematching

import kotlin.math.abs

internal class SimilarityPropagation(val params: Parameters) {
    public data class Parameters(
        val parent: Double = 0.25,
        val parentInv: Double = 0.7,
        val envelop: List<Double> = listOf(0.8, 0.1, 0.01)
    )

    companion object {
        internal fun propagateSimilarity(neighbors: Neighbors, params: Parameters): Neighbors {
            val propagation = SimilarityPropagation(params)
            var n = neighbors
            params.envelop.forEach {
                n = propagation.propagateOnce(n, it)
            }

            return n
        }
    }

    private fun propagateOnce(neighbors: Neighbors, currentEnvelop: Double): Neighbors {
        val newSimilarity = Neighbors()
        neighbors.forEachPair { source, target, score ->
            increaseScore(newSimilarity, source, target, score)
            val pSource = source.parent
            val pTarget = target.parent

            if (pSource == null || pTarget == null)
                return@forEachPair

            val parentScore = neighbors.score(pSource, pTarget)
            increaseScore(newSimilarity, source, target, currentEnvelop * parentScore * params.parent)
            increaseScore(newSimilarity, pSource, pTarget, currentEnvelop * score * params.parentInv)
        }

        return newSimilarity
    }

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
}

