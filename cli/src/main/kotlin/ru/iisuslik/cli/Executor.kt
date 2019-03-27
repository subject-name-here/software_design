package ru.iisuslik.cli

/**
 * Class that can execute commands in context
 *
 * @param context context in which executor works and executes commands
 */
class Executor(private val context: Context) {
    /**
     * EXIT status means that we have only 1 command - exit
     */
    enum class Status { EXIT, CONTINUE }

    /**
     * Executes statement
     *
     * @param statement statement to execute
     */
    fun execute(statement: Statement): Pair<String, Status> {
        val result = statement.execute(context)
        return Pair(result, statement.status())
    }
}