package ru.iisuslik.cli

class Context() {
    val varsContainer = VarsContainer()
    var currentDirectory: String = System.getProperty("user.dir")
    var userHome: String = System.getProperty("user.home")

    constructor(context: Context) : this() {
        context.varsContainer.copyTo(varsContainer)
        currentDirectory = context.currentDirectory
    }
}