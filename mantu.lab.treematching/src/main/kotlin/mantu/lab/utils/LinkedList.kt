package mantu.lab.utils

import mantu.lab.treematching.Metropolis
import org.junit.jupiter.api.fail

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
            fail("Empty LinkedList Iterator")

        val res = current!!
        current = res.next

        return res.value
    }
}

