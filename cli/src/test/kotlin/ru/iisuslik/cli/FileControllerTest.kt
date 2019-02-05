package ru.iisuslik.cli

import org.junit.Assert.*
import org.junit.Test
import java.io.File


fun getRealFileName(name: String) = "src${File.separator}test${File.separator}resources${File.separator}$name"

class FileControllerTest {

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