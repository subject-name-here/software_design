package ru.iisuslik.cli

// This class can execute commands
class Executor(val varsContainer: VarsContainer) {
    // EXIT status means that we have only 1 command - exit
    enum class Status {EXIT, CONTINUE}

    // Executes statement with context from varsContainer
    fun execute(statement: Statement): Pair<String, Status> {
        val result = statement.execute(varsContainer)
        return Pair(result, statement.status())
    }
}