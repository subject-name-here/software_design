package ru.iisuslik.cli

import java.io.File
import java.io.IOException

// Returns content of all files in list
fun catFiles(fileNames: List<String>): String {
    val stringBuilder = StringBuilder()
    for (fileName in fileNames) {
        val file = File(fileName)
        if (!file.exists()) {
            println("File \"$fileName\" doesn't exists")
            continue
        }
        stringBuilder.append(file.readText())
    }
    return stringBuilder.toString()
}

// Returns current directory
fun pwd(): String {
    return System.getProperty("user.dir")
}

// Returns count of lines, words and symbols in string
fun wcInput(input: String): Triple<Int, Int, Int> {
    val linesCount = input.split(System.lineSeparator()).size
    val wordsCount = input.split("[\\s]+".toRegex()).size
    val symbolsCount = input.length
    return Triple(linesCount, wordsCount, symbolsCount)
}

// Returns counts of lines, words and symbols in all files from list, last line - total information
fun wcFiles(fileNames: List<String>): String {
    val stringBuilder = StringBuilder()
    var totalLinesCount = 0
    var totalWordsCount = 0
    var totalSymbolsCount = 0
    for (fileName in fileNames) {
        val file = File(fileName)
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

class CommandNotFoundException(message: String): Exception(message)
class ErrorInCommandException(message: String): Exception(message)

// Executes external command
fun executeCommand(name: String, args: List<String>, input: String): String {
    val process = try {
        Runtime.getRuntime().exec("$name ${args.joinToString(separator = " ")}")
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