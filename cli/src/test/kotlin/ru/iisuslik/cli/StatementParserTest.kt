package ru.iisuslik.cli

import org.junit.Test

import org.junit.Assert.*

class StatementParserTest {

    @Test
    fun parse() {
    }

    @Test
    fun substitutionEasy() {
        val c = VarsContainer()
        c.add("kek", "lol")
        val parser = StatementParser(c)
        print(parser.substitution("\$kek"))
    }
}