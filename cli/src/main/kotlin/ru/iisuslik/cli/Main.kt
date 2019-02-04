package ru.iisuslik.cli

fun main(args: Array<String>) {
    val varsContainer = VarsContainer()
    val executor = Executor(varsContainer)
    val parser = StatementParser(varsContainer)
    while (true) {
        print(":~$ ")
        val nextLine = readLine() ?: return
        val statement = parser.parse(nextLine)
        val (result, status) = executor.execute(statement)
        if (status == Executor.Status.EXIT) {
            break
        } else {
            println(result)
        }
    }
}