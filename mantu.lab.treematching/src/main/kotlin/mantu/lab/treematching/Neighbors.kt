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