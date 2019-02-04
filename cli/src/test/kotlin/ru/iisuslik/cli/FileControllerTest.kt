package ru.iisuslik.cli

import org.junit.AfterClass
import org.junit.Assert.*
import org.junit.Test
import org.junit.BeforeClass
import java.io.File

class FileControllerTest {
    companion object {
        @BeforeClass
        @JvmStatic
        fun createFiles() {
            createTestFile("file1", "content1")
            createTestFile("file2", "content2")
            createTestFile("file3", "two words")
        }

        @AfterClass
        @JvmStatic
        fun deleteFiles() {
            deleteTestsFile("file1")
            deleteTestsFile("file2")
            deleteTestsFile("file3")
        }
    }

    @Test
    fun pwdWorks() {
        assertTrue(pwd().contains("${File.separator}software_design${File.separator}cli".toRegex()))
    }

    @Test
    fun catSimple() {
        assertEquals("content1", catFiles(listOf(getRealFileName("file1"))))
    }

    @Test
    fun catTwoFiles() {
        assertEquals(
            "content1content2",
            catFiles(listOf(getRealFileName("file1"), getRealFileName("file2")))
        )
    }

    @Test
    fun wc() {
        assertEquals(
            "${getRealFileName("file3")}: 1 2 9",
            wcFiles(listOf(getRealFileName("file3")))
        )
    }

    @Test
    fun wcTwoFiles() {
        assertEquals(
            "${getRealFileName("file3")}: 1 2 9\n${getRealFileName("file1")}: 1 1 8\n" +
            "total: 2 3 17",
            wcFiles(listOf(getRealFileName("file3"), getRealFileName("file1")))
        )
    }
}