package ru.iisuslik.cli

import org.junit.Assert.*
import org.junit.Test
import java.io.File


fun getRealFileName(name: String) = "src${File.separator}test${File.separator}resources${File.separator}$name"

class FileControllerTest {

    @Test
    fun pwdWorks() {
        assertTrue(pwd(Context()).endsWith("${File.separator}software_design${File.separator}cli"))
    }

    @Test
    fun catSimple() {
        assertEquals("content1", catFiles(listOf(getRealFileName("file1")), Context()))
    }

    @Test
    fun catTwoFiles() {
        assertEquals(
            "content1content2",
            catFiles(listOf(getRealFileName("file1"), getRealFileName("file2")), Context())
        )
    }

    @Test
    fun wc() {
        assertEquals(
            "${getRealFileName("file3")}: 1 2 9",
            wcFiles(listOf(getRealFileName("file3")), Context())
        )
    }

    @Test
    fun wcTwoFiles() {
        assertEquals(
            "${getRealFileName("file3")}: 1 2 9${System.lineSeparator()}" +
                    "${getRealFileName("file1")}: 1 1 8${System.lineSeparator()}total: 2 3 17",
            wcFiles(listOf(getRealFileName("file3"), getRealFileName("file1")), Context())
        )
    }
}