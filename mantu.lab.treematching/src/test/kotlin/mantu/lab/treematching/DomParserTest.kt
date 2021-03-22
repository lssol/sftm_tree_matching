package mantu.lab.treematching

import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Assertions.*

internal class DomParserTest {
    companion object {
        @Language("HTML")
        public val simpleHtml = """
        <!DOCTYPE html>
            <html>
                <head></head>
                <body>
                    <!-- A comment -->
                    <h1>A title</h1>
                    <div>
                        <p>p1</p>
                        <p>p2</p>
                    </div>
                </body>
            </html>
    """.trimIndent()
    }

    @org.junit.jupiter.api.Test
    fun webpageToTree() {
        val nodes = DomParser.webpageToTree(simpleHtml)
        assertEquals(5, nodes.count())
    }
}