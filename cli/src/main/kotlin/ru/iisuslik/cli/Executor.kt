package ru.iisuslik.cli

class Executor(val varsContainer: VarsContainer) {
    enum class Status {EXIT, CONTINUE}

    fun execute(statement: Statement): Status {
        statement.execute(varsContainer)
        return statement.status()
    }
}