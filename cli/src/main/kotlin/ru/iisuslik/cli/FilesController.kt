package ru.iisuslik.cli

import java.io.File


fun catFiles(fileNames: List<String>): String {
    val stringBuilder = StringBuilder()
    for (fileName in fileNames) {
        stringBuilder.append(File(fileName).readText())
    }
    return stringBuilder.toString()
}

fun pwd(): String {
    return System.getProperty("user.dir")
}

fun wcFiles(fileNames: List<String>): String {
    return "WC FILES $fileNames"
}

fun executeCommand(name: String, args: List<String>, input: String): String {
    val process = Runtime.getRuntime().exec("$name ${args.joinToString(separator = " ")}")
    process.outputStream.bufferedWriter().write(input)
    process.outputStream.close()
    return process.inputStream.bufferedReader().readText()
}