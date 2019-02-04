package ru.iisuslik.cli

import org.junit.AfterClass
import org.junit.Assert.*
import org.junit.BeforeClass
import org.junit.Test

class ExecutorTest {

    private val executor = Executor(VarsContainer())

    private fun Executor.runCommands(commands: List<Command>): String {
        val (result, status) = executor.execute(Commands(commands))
        assertEquals(Executor.Status.CONTINUE, status)
        return result
    }

    companion object {
        @BeforeClass
        @JvmStatic
        fun createFiles() {
            createTestFile("file1", "content1")
            createTestFile("file2", "content2")
        }

        @AfterClass
        @JvmStatic
        fun deleteFiles() {
            deleteTestsFile("file1")
            deleteTestsFile("file2")
        }
    }

    @Test
    fun echoSimple() {
        assertEquals("kek", executor.runCommands(listOf(Echo(listOf("kek")))))
    }

    @Test
    fun echoTwoArgs() {
        assertEquals("kek lol", executor.runCommands(listOf(Echo(listOf("kek", "lol")))))
    }

    @Test
    fun echoInput() {
        assertEquals("lol", executor.runCommands(listOf(Echo(listOf("kek")), Echo(listOf("lol")))))
    }
}