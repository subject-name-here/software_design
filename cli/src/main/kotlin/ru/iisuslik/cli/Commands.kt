package ru.iisuslik.cli

interface Statement {
    fun execute(varsContainer: VarsContainer)
    fun status(): Executor.Status
}

class Assignment(val name: String, val value: String) : Statement {
    override fun status() = Executor.Status.CONTINUE

    override fun execute(varsContainer: VarsContainer) {
        varsContainer.add(name, value)
    }
}

class Commands(val commands: List<Command>) : Statement {
    override fun status() = if (commands.size == 1 && commands.first() is Exit)
        Executor.Status.EXIT
    else Executor.Status.CONTINUE

    override fun execute(varsContainer: VarsContainer) {
        var result = ""
        for (command in commands) {
            result = command.execute(result)
        }
        println(result)
    }
}

abstract class Command(protected val name: String, protected val args: List<String>) {
    companion object {
        @JvmStatic fun build(name: String, args: List<String>): Command {
            return when (name) {
                "echo" -> Echo(args)
                "wc" -> Wc(args)
                "cat" -> Cat(args)
                // Real bash doesn't care too if we pass any args to pwd or exit
                "pwd" -> Pwd()
                "exit" -> Exit()
                else -> External(name, args)
            }
        }
    }

    abstract fun execute(input: String): String
}

class Echo(args: List<String>) : Command("echo", args) {
    override fun execute(input: String): String {
        return args.joinToString(" ")
    }
}

class Wc(args: List<String>) : Command("wc", args) {
    override fun execute(input: String): String {
        if (args.isEmpty()) {
            return input
        } else {
            return wcFiles(args)
        }
    }
}

class Cat(args: List<String>) : Command("cat", args) {
    override fun execute(input: String): String {
        if (args.isEmpty()) {
            return input
        } else {
            return catFiles(args)
        }
    }
}

class External(name: String, args: List<String>) : Command(name, args) {
    override fun execute(input: String): String {
        return executeCommand(name, args)
    }
}

class Exit : Command("exit", emptyList()) {
    override fun execute(input: String): String {
        return ""
    }
}

class Pwd : Command("pwd", emptyList()) {
    override fun execute(input: String): String {
        return pwd()
    }
}
