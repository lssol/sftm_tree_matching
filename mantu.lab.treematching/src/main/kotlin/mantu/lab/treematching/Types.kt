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

// Not a data class because I want comparison to be by reference (not by value)
public class Node(
    val value: MutableList<String>,
    val xPath: String = "",
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