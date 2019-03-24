package ru.iisuslik.cli

/**
 * Interface for statement in cli
 */
interface Statement {
    /**
     * Executes statement in context
     *
     * @param varsContainer context(variables values)
     * @return execution result
     */
    fun execute(varsContainer: VarsContainer): String


    /**
     * Returns statement status
     *
     * @return exit or continue status
     */
    fun status(): Executor.Status
}


/**
 * Representation of assignment command (a="kek")
 *
 * @param name variable name (a)
 * @param value variable value (kek)
 */
data class Assignment(val name: String, val value: String) : Statement {
    override fun status() = Executor.Status.CONTINUE

    override fun execute(varsContainer: VarsContainer): String {
        varsContainer.add(name, value)
        return ""
    }
}

/**
 * Contains list of commands, divided by pipes
 *
 * @param commands list of commands
 */
data class Commands(val commands: List<Command>) : Statement {
    override fun status() = if (commands.size == 1 && commands.first() is Exit)
        Executor.Status.EXIT
    else Executor.Status.CONTINUE

    override fun execute(varsContainer: VarsContainer): String {
        var result = ""
        for (command in commands) {
            result = command.execute(result)
        }
        return result
    }
}

/**
 * Interface for bash command (commandName arg1 arg2 arg3)
 */
interface Command {
    /**
     * Executes command
     *
     * @param input string (e.g. "echo kek | cat", "kek" is input for cat)
     * @return execution result
     */
    fun execute(input: String): String
}

/**
 * Representation of echo command
 *
 * Execution just returns concatenation of args
 * @param args command arguments
 */
data class Echo(val args: List<String>) : Command {
    override fun execute(input: String): String {
        return args.joinToString(" ")
    }
}

/**
 * Representation of wc command
 *
 * Execution returns statistics of words count, lines count and symbol count in file
 *
 * @param args command arguments
 */
data class Wc(val args: List<String>) : Command {
    override fun execute(input: String): String {
        // ignoring input if args are not empty
        return if (args.isEmpty()) {
            val (linesCount, wordsCount, symbolsCount) = wcInput(input)
            "$linesCount $wordsCount $symbolsCount"
        } else {
            wcFiles(args)
        }
    }
}

/**
 * Representation of cat command
 *
 * Execution returns content of file
 *
 * @param args command arguments
 */
data class Cat(val args: List<String>) : Command {
    override fun execute(input: String): String {
        // ignoring input if args are not empty
        return if (args.isEmpty()) {
            input
        } else {
            catFiles(args)
        }
    }
}

/**
 * Representation of external command
 *
 * Execution does whatever real command does
 *
 * @param args command arguments
 */
data class External(val name: String, val args: List<String>) : Command {
    override fun execute(input: String): String {
        return executeCommand(name, args, input)
    }
}

/**
 * Representation of exit command
 *
 * Execution triggers exit from cli
 */
object Exit : Command {
    override fun execute(input: String): String {
        return ""
    }
}

/**
 * Representation of pwd command
 *
 * Execution returns current pwd
 */
object Pwd : Command {
    override fun execute(input: String): String {
        return pwd()
    }
}

data class Cd(val args: List<String>) : Command {
    override fun execute(input: String): String {
        return if(args.isEmpty()) {
            cd(listOf(System.getProperty("user.home")))
        } else {
            cd(args)
        }
    }
}
data class Ls(val args: List<String>) : Command {
    override fun execute(input: String): String {
        return if(args.isEmpty()) {
            ls(listOf(pwd()))
        } else {
            ls(args)
        }
    }
}
