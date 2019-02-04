package ru.iisuslik.cli

import java.io.File

fun createTestFile(name: String, content: String) {
    val file = File(getRealFileName(name))
    if (!file.exists()) {
        file.createNewFile()
    }
    file.writeText(content)
}

fun deleteTestsFile(name: String) {
    val file = File(getRealFileName(name))
    if (file.exists()) {
        file.delete()
    }
}


fun getRealFileName(name: String) = "src${File.separator}test${File.separator}resources${File.separator}$name"