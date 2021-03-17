package mantu.lab.utils

class LinkedListNode<T>(
    val value: T,
    var prev: LinkedListNode<T>? = null,
    var next: LinkedListNode<T>? = null,
)

class LinkedList<T> {
    private var head: LinkedListNode<T>? = null
    private var tail: LinkedListNode<T>? = null
    public var count: Int = 0

    public fun add(value: T): LinkedListNode<T> {
        count++
        val newNode = LinkedListNode(value)
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
        if (listNode == null)
            return

        count--

        if (listNode == head)
            head = head?.next

        if (listNode == tail)
            tail = tail?.prev

        if (listNode.prev != null)
            listNode.prev!!.next = listNode.next

        if (listNode.next != null)
            listNode.next!!.prev = listNode.prev
    }

    public fun forEach(f: ((T) -> Unit)) {
        var cur : LinkedListNode<T>? = head
        while (cur != null) {
            f(cur.value)
            cur = cur.next
        }
    }
}
