package ru.iisuslik.cli

import org.junit.Assert.*
import org.junit.Test

class VarsContainerTest {

    @Test
    fun addVar() {
        val container = VarsContainer()
        container.add("kek", "lol")
        assertEquals("lol", container.get("kek"))
    }


    @Test
    fun addTwoVars() {
        val container = VarsContainer()
        container.add("kek", "lol")
        container.add("kek2", "lol2")
        assertEquals("lol", container.get("kek"))
        assertEquals("lol2", container.get("kek2"))
    }


    @Test
    fun updateVar() {
        val container = VarsContainer()
        container.add("kek", "lol")
        container.add("kek", "superlol")
        assertEquals("superlol", container.get("kek"))
    }


    @Test
    fun notExist() {
        val container = VarsContainer()
        assertEquals("", container.get("kek"))
    }

}