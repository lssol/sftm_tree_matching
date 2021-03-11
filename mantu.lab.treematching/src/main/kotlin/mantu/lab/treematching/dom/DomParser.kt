package mantu.lab.treematching.dom

import org.jsoup.Jsoup
import org.jsoup.nodes.Attribute
import org.jsoup.nodes.Element

public data class ParsingSettings(val signatureAttribute: String = "signature", val maxTokensPerValue: Int = 8)

public class DomParser(val settings: ParsingSettings) {

    private fun tokenizeValue(value: String): List<String> {
        val valueTokens = value
            .replace(Regex("""\W+"""), " ")
            .split(" ")
            .filter { it.isNotBlank() }

        return when (valueTokens.count()) {
            0                                -> emptyList()
            1                                -> valueTokens
            in 2..settings.maxTokensPerValue -> valueTokens + value
            else                             -> listOf(value)
        }
    }

    private fun tokenizeAttribute(attr: Attribute): List<String> {
        val isIgnored = attr.key == settings.signatureAttribute
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



}
