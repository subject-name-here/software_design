package ru.iisuslik.cli

/**
 * Class that contains context for command execution, such as current directory and variables.
 */
class Context() {
    /**
     * Container of variables used in this context.
     */
    val varsContainer = VarsContainer()

    /**
     * Current directory of this context.
     */
    var currentDirectory: String = System.getProperty("user.dir")

    /**
     * Home directory of this context.
     */
    var userHome: String = System.getProperty("user.home")

    /**
     * Copy constructor of context.
     *
     * @param context context from which all fields are copied
     */
    constructor(context: Context) : this() {
        context.varsContainer.copyTo(varsContainer)
        currentDirectory = context.currentDirectory
        userHome = context.userHome
    }
}