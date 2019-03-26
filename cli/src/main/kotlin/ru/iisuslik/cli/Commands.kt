package ru.iisuslik.cli

/**
 * Interface for statement in cli
 */
interface Statement {
    /**
     * Executes statement in context
     *
     * @param context context(variables values, current directory)
     * @return execution result
     */
    fun execute(context: Context): String


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

    override fun execute(context: Context): String {
        context.varsContainer.add(name, value)
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

    override fun execute(context: Context): String {
        var result = ""

        if (commands.size == 1) {
            result = commands[0].execute(result, context)
        } else {
            for (command in commands) {
                result = command.execute(result, Context(context))
            }
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
     * @param context context in which command is exectued
     * @return execution result
     */
    fun execute(input: String, context: Context): String
}

/**
 * Representation of echo command
 *
 * Execution just returns concatenation of args
 * @param args command arguments
 */
data class Echo(val args: List<String>) : Command {
    override fun execute(input: String, context: Context): String {
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
    override fun execute(input: String, context: Context): String {
        // ignoring input if args are not empty
        return if (args.isEmpty()) {
            val (linesCount, wordsCount, symbolsCount) = wcInput(input)
            "$linesCount $wordsCount $symbolsCount"
        } else {
            wcFiles(args, context)
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
    override fun execute(input: String, context: Context): String {
        // ignoring input if args are not empty
        return if (args.isEmpty()) {
            input
        } else {
            catFiles(args, context)
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
    override fun execute(input: String, context: Context): String {
        return executeCommand(name, args, input, context)
    }
}

/**
 * Representation of exit command
 *
 * Execution triggers exit from cli
 */
object Exit : Command {
    override fun execute(input: String, context: Context): String {
        return ""
    }
}

/**
 * Representation of pwd command
 *
 * Execution returns current pwd
 */
object Pwd : Command {
    override fun execute(input: String, context: Context): String {
        return pwd(context)
    }
}

data class Cd(val args: List<String>) : Command {
    override fun execute(input: String, context: Context): String {
        return cd(args, context)
    }
}
data class Ls(val args: List<String>) : Command {
    override fun execute(input: String, context: Context): String {
        return ls(args, context)
    }
}
