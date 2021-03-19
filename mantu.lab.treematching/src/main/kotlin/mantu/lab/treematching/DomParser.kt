package mantu.lab.treematching

import org.jsoup.Jsoup
import org.jsoup.nodes.Attribute
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element


public class DomParser(val params: Parameters = Parameters()) {
    companion object { public fun webpageToTree(source: String) = DomParser().webpageToTree(source) }
    public data class Parameters(val signatureAttribute: String = "signature", val maxTokensPerValue: Int = 8)
    public fun webpageToTree(source: String) : List<Node> {
        val doc = Jsoup.parse(source)
        return domToTree(doc)
    }

    private fun tokenizeValue(value: String): List<String> {
        val valueTokens = value
            .replace(Regex("""\W+"""), " ")
            .split(" ")
            .filter { it.isNotBlank() }

        return when (valueTokens.count()) {
            0                                -> emptyList()
            1                                -> valueTokens
            in 2..params.maxTokensPerValue -> valueTokens + value
            else                             -> listOf(value)
        }
    }

    private fun tokenizeAttribute(attr: Attribute): List<String> {
        val isIgnored = attr.key == params.signatureAttribute
                        || attr.key.startsWith("data")

        return when {
            isIgnored            -> emptyList()
            attr.value.isBlank() -> listOf(attr.key)
            else                 -> {
                val tokens = tokenizeValue(attr.value).map { token -> "${attr.key}:$token" }
                tokens + attr.key
            }
        }
    }

    private fun tokenizeNode(el: Element): List<String> =
        el.attributes().flatMap(::tokenizeAttribute) + el.tagName()


    private fun getNewXPath(el: Element, parent: Element?, partialXPath: String): String {
        val indexCurrentElement = parent
                                      ?.children()
                                      ?.filter { it.tagName() == el.tagName() }
                                      ?.indexOf(el) ?: -1

        val brackets = if (indexCurrentElement != -1) "[$indexCurrentElement]" else ""

        return partialXPath + "/${el.tagName()}" + brackets
    }

    private fun domToTree(doc: Document) : List<Node> {
        val nodes = mutableListOf<Node>()
        fun copy(el: Element, parent: Element?, parentNode: Node?, partialXPath: String) : Node {
            val newXPath = getNewXPath(el, parent, partialXPath)
            val tokenizedNode = tokenizeNode(el) + newXPath
            val node = Node(
                value = tokenizedNode,
                signature = el.attr(params.signatureAttribute),
                parent = parentNode
            )
            nodes.add(node)
            el.children().forEach { child ->
                val childNode = copy(child, el, node, newXPath)
                node.children.add(childNode)
            }

            return node
        }
        copy(doc.body(), null, null, "")

        return nodes
    }
}
