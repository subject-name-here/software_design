package ru.iisuslik.cli

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import com.xenomachina.argparser.mainBody

interface Statement {
    // Execute command with context
    fun execute(varsContainer: VarsContainer): String

    // Get command status
    fun status(): Executor.Status
}

// Representation of assignment command (a="kek")
data class Assignment(val name: String, val value: String) : Statement {
    override fun status() = Executor.Status.CONTINUE

    override fun execute(varsContainer: VarsContainer): String {
        varsContainer.add(name, value)
        return ""
    }
}

// Contains list of commands, divided by pipes
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

interface Command {
    companion object {
        // Uses standard commands if we can, otherwise use external command
        @JvmStatic
        fun build(name: String, args: List<String>): Command {
            return when (name) {
                "echo" -> Echo(args)
                "wc" -> Wc(args)
                "cat" -> Cat(args)
                "grep" -> Grep(args)
                // Real bash doesn't care too if we pass any args to pwd or exit
                "pwd" -> Pwd
                "exit" -> Exit
                else -> External(name, args)
            }
        }
    }

    fun execute(input: String): String
}

data class Echo(val args: List<String>) : Command {
    override fun execute(input: String): String {
        return args.joinToString(" ")
    }
}

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

data class Grep(val args: List<String>) : Command {
    override fun execute(input: String): String {
        val parsedArgs = GrepArgsParser(ArgParser(args.toTypedArray()))
        val regex = getRegex(parsedArgs)
        // ignoring input if args are not empty
        return if (parsedArgs.files.isEmpty()) {
            grepInput(input, regex, parsedArgs.linesCount)
        } else {
            grepFiles(parsedArgs.files, regex, parsedArgs.linesCount)
        }
    }

    class GrepArgsParser(parser: ArgParser) {
        val ignoringCaseSensivity by parser.flagging("-i", help = "Ignoring case sensitivity")
        val searchingWords by parser.flagging("-w", help = "Searching full words")
        val linesCount by parser.storing("-n", help = "Printing n lines") { toInt() }.default { 0 }
        val regex by parser.positional("REGEX", help = "Regex or just a string to find")
        val files by parser.positionalList("FILE", help = "Source files"). default { emptyList()}
    }

    private fun getRegex(parsedArgs: GrepArgsParser): Regex {
        val regexString = if (parsedArgs.searchingWords) {
            "(\\b|^)${parsedArgs.regex}(\\b|$)"
        } else {
            parsedArgs.regex
        }
        return if (parsedArgs.ignoringCaseSensivity) {
            regexString.toRegex(RegexOption.IGNORE_CASE)
        } else {
            regexString.toRegex()
        }

    }
}

data class External(val name: String, val args: List<String>) : Command {
    override fun execute(input: String): String {
        return executeCommand(name, args, input)
    }
}

object Exit : Command {
    override fun execute(input: String): String {
        return ""
    }
}

object Pwd : Command {
    override fun execute(input: String): String {
        return pwd()
    }
}
