package ru.iisuslik.cli

import java.io.File
import java.io.IOException

/**
 * Returns content of files
 *
 * @param fileNames list of files
 */
fun catFiles(fileNames: List<String>, context: Context): String {
    val stringBuilder = StringBuilder()
    for (fileName in fileNames) {
        val file = createFile(fileName, context)
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
fun pwd(context: Context): String {
    return context.currentDirectory
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
fun wcFiles(fileNames: List<String>, context: Context): String {
    val stringBuilder = StringBuilder()
    var totalLinesCount = 0
    var totalWordsCount = 0
    var totalSymbolsCount = 0
    for (fileName in fileNames) {
        val file = createFile(fileName, context)
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
fun executeCommand(name: String, args: List<String>, input: String, context: Context): String {
    val process = try {
        Runtime.getRuntime().exec("$name ${args.joinToString(separator = " ")}", emptyArray(), File(context.currentDirectory))
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

/**
 * Changes current directory
 * @param fileNames directory to list files in or nothing if we want to list files in home directory
 * @param context context in which command is executed
 * @throws ErrorInCommandException if more than one argument given
 */
fun cd(fileNames: List<String>, context: Context): String {
    if (fileNames.size > 1) {
        throw ErrorInCommandException("More than one argument given to cd!")
    }

    val directory = fileNames.getOrElse(0) { context.userHome }
    val file = createFile(directory, context)

    return if (file.exists() && file.isDirectory) {
        context.currentDirectory = file.canonicalPath
        ""
    } else {
        throw ErrorInCommandException("Directory \"$directory\" doesn't exists")
    }
}

/**
 * Returns content list of directory
 * @param fileNames directory to list files in or nothing if we want to list files in current directory
 * @param context context in which command is executed
 * @throws ErrorInCommandException if more than one argument given
 */
fun ls(fileNames: List<String>, context: Context): String {
    if (fileNames.size > 1) {
        throw ErrorInCommandException("More than one argument given to ls!")
    }

    val directory = fileNames.getOrElse(0) { context.currentDirectory }
    val file = createFile(directory, context)
    return if (file.exists() && file.isDirectory) {
        file.listFiles().map { it.name }.sorted().joinToString(System.lineSeparator())
    } else {
        throw ErrorInCommandException("Directory \"$directory\" doesn't exists")
    }
}

/**
 * Creates File by given filename. If filename is relative, it is resolved based on context current directory
 * @param fileName name of file we want to create
 * @param context context from which we want to use current directory
 */
fun createFile(filename: String, context: Context): File {
    return File(context.currentDirectory).resolve(filename)
}
