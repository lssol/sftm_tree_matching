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

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.fail

internal class InMemoryIndexerTest {

    @Test
    fun findNeighbors() {
        val sourceList = listOf(
            "a node",
            "another node",
            "node",
            "a fox",
            "I love fox",
            "fox"
        )

        val targetList = listOf(
            "node",
            "fox"
        )

        val sourceNodes = sourceList.map { Node(it.split(" ").toMutableList()) }
        val targetNodes = targetList.map { Node(it.split(" ").toMutableList()) }

        val neighbors = InMemoryIndex
            .buildIndex(sourceNodes, InMemoryIndex.Parameters(10, 10))
            .findNeighbors(targetNodes)

        if (neighbors.value.isEmpty())
            assert(false)

        val exampleTarget = targetNodes[0]
        val associatedNodes = neighbors.value[exampleTarget]?.keys

        assert(associatedNodes?.contains(sourceNodes[0]) ?: false)
        assert(associatedNodes?.contains(sourceNodes[1]) ?: false)
        assert(associatedNodes?.contains(sourceNodes[2]) ?: false)
    }
}