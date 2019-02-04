package ru.iisuslik.cli

import org.junit.AfterClass
import org.junit.Assert.*
import org.junit.BeforeClass
import org.junit.Test

class ExecutorTest {

    private val executor = Executor(VarsContainer())

    private fun Executor.runCommands(commands: List<Command>): String {
        val (result, status) = this.execute(Commands(commands))
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

    @Test
    fun catSimple() {
        assertEquals("content1", executor.runCommands(listOf(Cat(listOf(getRealFileName("file1"))))))
    }

    @Test
    fun catInput() {
        assertEquals("kek", executor.runCommands(listOf(Echo(listOf("kek")),
            Cat(emptyList()))))
    }

    @Test
    fun catInputIgnore() {
        assertEquals("content1", executor.runCommands(listOf(Echo(listOf("kek")),
            Cat(listOf(getRealFileName("file1"))))))
    }

    @Test
    fun wcSimple() {
        assertEquals("${getRealFileName("file1")}: 1 1 8",
            executor.runCommands(listOf(Wc(listOf(getRealFileName("file1"))))))
    }

    @Test
    fun wcInput() {
        assertEquals("1 1 3", executor.runCommands(listOf(Echo(listOf("kek")),
            Wc(emptyList()))))
    }

    @Test
    fun wcInputIgnore() {
        assertEquals("${getRealFileName("file1")}: 1 1 8",
            executor.runCommands(listOf(Echo(listOf("kek")),
            Wc(listOf(getRealFileName("file1"))))))
    }
}