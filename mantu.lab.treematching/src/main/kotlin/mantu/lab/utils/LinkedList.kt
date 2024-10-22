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

package mantu.lab.utils


class LinkedList<T> : Iterable<T> {
    public var head: LinkedListNode<T>? = null
    public var tail: LinkedListNode<T>? = null
    public var count: Int = 0
    public var nodes : HashSet<LinkedListNode<T>> = hashSetOf()

    public fun add(value: T): LinkedListNode<T> {
        count++
        val newNode = LinkedListNode(value)
        nodes.add(newNode)
        when (head) {
            null -> {
                head = newNode
                tail = newNode
            }
            else -> {
                tail!!.next = newNode
                newNode.prev = tail
                tail = newNode
            }
        }

        return newNode
    }

    public fun remove(listNode: LinkedListNode<T>?) {
        if (listNode == null || !nodes.contains(listNode))
            return

        count--
        nodes.remove(listNode)

        if (listNode == head)
            head = head?.next

        if (listNode == tail)
            tail = tail?.prev

        if (listNode.prev != null)
            listNode.prev!!.next = listNode.next

        if (listNode.next != null)
            listNode.next!!.prev = listNode.prev
    }

    override fun iterator(): Iterator<T> = LinkedListIterator(this)
}

class LinkedListNode<T>(
        val value: T,
        var prev: LinkedListNode<T>? = null,
        var next: LinkedListNode<T>? = null,
)

class LinkedListIterator<T> ( val list : LinkedList<T>, var current : LinkedListNode<T>? = list.head ) : Iterator<T> {
    override fun hasNext(): Boolean = current != null
    override fun next(): T {
        if (current == null)
            error("Empty LinkedList Iterator")

        val res = current!!
        current = res.next

        return res.value
    }
}

