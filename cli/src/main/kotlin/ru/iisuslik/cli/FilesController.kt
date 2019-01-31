package ru.iisuslik.cli

fun catFiles(fileNames: List<String>): String {
    return "FILES CONTENT"
}

fun pwd(): String {
    return "kek/lol/go/fuck/yourself"
}

fun wcFiles(fileNames: List<String>): String {
    return "WC FILES $fileNames"
}

fun executeCommand(name: String, args: List<String>, input: String): String {
    return "UNKNOW COMMAND $name"
}