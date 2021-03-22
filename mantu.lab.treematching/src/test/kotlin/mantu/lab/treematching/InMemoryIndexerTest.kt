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