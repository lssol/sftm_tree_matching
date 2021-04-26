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