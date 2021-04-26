/*
 * Tree Matching library based on the SFTM algorithm.
 *
 * Copyright (C) 2021  Mantu, Sacha Brisset.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see https://www.gnu.org/licenses/.
 */

package mantu.lab.treematching

import kotlin.math.ln


public class InMemoryIndex(private val params: Parameters) {
    /**
     * @param maxNeighborsPerNode the maximum number of neighbors allowed per node. Beyond this value, only the closest neighbors are kept. This helps with computation times.
     * @param maxTokenAppearance tokens appearingn more than this value are removed. Tokens that appear too many times carry very few information yet cause high overhead.
     */
    public data class Parameters(val maxNeighborsPerNode: Int = 100, val maxTokenAppearance: Int)

    internal val index: HashMap<String, HashSet<Node>> = HashMap()
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

    internal fun findNeighbors(targets: List<Node>): Neighbors {
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

