package ru.iisuslik.cli

import java.io.File
import java.io.IOException

/**
 * Returns content of files
 *
 * @param fileNames list of files
 */
fun catFiles(fileNames: List<String>): String {
    val stringBuilder = StringBuilder()
    for (fileName in fileNames) {
        val file = createFile(fileName)
        if (!file.exists()) {
            println("File \"$fileName\" doesn't exists")
            continue
        }
        stringBuilder.append(file.readText())
    }
    return stringBuilder.toString()
}

/**
 * Returns current directory
 */
fun pwd(): String {
    return System.getProperty("user.dir")
}

/**
 * Returns count of lines, words and symbols in string
 *
 * @param input string to have statistic
 */
fun wcInput(input: String): Triple<Int, Int, Int> {
    val linesCount = input.split(System.lineSeparator()).size
    val wordsCount = input.split("[\\s]+".toRegex()).size
    val symbolsCount = input.length
    return Triple(linesCount, wordsCount, symbolsCount)
}


/**
 * Returns counts of lines, words and symbols in all files from list, last line - total information
 *
 * @param fileNames list of files
 */
fun wcFiles(fileNames: List<String>): String {
    val stringBuilder = StringBuilder()
    var totalLinesCount = 0
    var totalWordsCount = 0
    var totalSymbolsCount = 0
    for (fileName in fileNames) {
        val file = createFile(fileName)
        if (!file.exists()) {
            throw ErrorInCommandException("File \"$fileName\" doesn't exists")
        }
        val fileText = file.readText()
        val (linesCount, wordsCount, symbolsCount) = wcInput(fileText)
        stringBuilder.append("$fileName: $linesCount $wordsCount $symbolsCount")
        if (fileNames.size > 1) {
            stringBuilder.append(System.lineSeparator())
        }
        totalLinesCount += linesCount
        totalWordsCount += wordsCount
        totalSymbolsCount += symbolsCount

    }
    if (fileNames.size > 1) {
        stringBuilder.append("total: $totalLinesCount $totalWordsCount $totalSymbolsCount")
    }
    return stringBuilder.toString()
}

/**
 * Throws if there is no such command in OS
 */
class CommandNotFoundException(message: String): Exception(message)

/**
 * Throws if external command returns error
 */
class ErrorInCommandException(message: String): Exception(message)

/**
 * Executes external command
 * @param name command name
 * @param args arguments
 * @param input input
 */
fun executeCommand(name: String, args: List<String>, input: String): String {
    val process = try {
        Runtime.getRuntime().exec("$name ${args.joinToString(separator = " ")}", emptyArray(), File(pwd()))
    } catch (e: IOException) {
        throw CommandNotFoundException(name)
    }
    process.outputStream.bufferedWriter().write(input)
    process.outputStream.close()
    val errorCode = process.waitFor()
    if (errorCode != 0) {
        throw ErrorInCommandException(process.errorStream.bufferedReader().readLine() ?: "")
    }
    return process.inputStream.bufferedReader().readText()
}

// Changes current directory (ignores all arguments but first)
fun cd(fileNames: List<String>): String {
    val dir = createFile(fileNames[0])
    return if (dir.exists() && dir.isDirectory) {
        System.setProperty("user.dir", dir.canonicalPath)
        ""
    } else {
        "Directory does not exists!"
    }
}

// Returns content list of current directory (ignores all arguments but first)
fun ls(fileNames: List<String>): String {
    val dir = createFile(fileNames[0])
    return if (dir.exists() && dir.isDirectory) {
        return dir.listFiles().map { it.name }.sorted().joinToString("\n")
    } else {
        "Directory does not exists!"
    }
}

fun createFile(filename: String): File {
    return File(filename).absoluteFile
}
