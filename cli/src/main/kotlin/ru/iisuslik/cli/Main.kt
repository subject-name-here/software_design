package ru.iisuslik.cli

// main function
fun main() {
    val varsContainer = VarsContainer()
    val executor = Executor(varsContainer)
    val parser = StatementParser(varsContainer)
    while (true) {
        print(":~$ ")
        val nextLine = readLine() ?: return
        if (nextLine == "") {
            continue
        }
        val statement = parser.parse(nextLine)
        val (result, status) = executor.execute(statement)
        if (status == Executor.Status.EXIT) {
            break
        } else {
            println(result)
        }
    }
}