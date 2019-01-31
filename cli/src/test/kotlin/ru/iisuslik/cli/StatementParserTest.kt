package ru.iisuslik.cli

import org.junit.Test

import org.junit.Assert.*

class StatementParserTest {

    @Test
    fun parse() {
    }

    @Test
    fun substitution() {
        val c = VarsContainer()
        c.add("fuck", "you")
        val parser = StatementParser(c)
        print(parser.substitution("\$fuck"))
    }
}