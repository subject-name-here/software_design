package ru.iisuslik.cli

class Executor(val varsContainer: VarsContainer) {
    enum class Status {EXIT, CONTINUE}

    fun execute(statement: Statement): Pair<String, Status> {
        val result = statement.execute(varsContainer)
        return Pair(result, statement.status())
    }
}