package ru.iisuslik.cli

interface Statement {
    fun execute(varsContainer: VarsContainer): String
    fun status(): Executor.Status
}

data class Assignment(val name: String, val value: String) : Statement {
    override fun status() = Executor.Status.CONTINUE

    override fun execute(varsContainer: VarsContainer): String {
        varsContainer.add(name, value)
        return ""
    }
}

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
        @JvmStatic
        fun build(name: String, args: List<String>): Command {
            return when (name) {
                "echo" -> Echo(args)
                "wc" -> Wc(args)
                "cat" -> Cat(args)
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
        if (args.isEmpty()) {
            return input
        } else {
            return wcFiles(args)
        }
    }
}

data class Cat(val args: List<String>) : Command {
    override fun execute(input: String): String {
        if (args.isEmpty()) {
            return input
        } else {
            return catFiles(args)
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
