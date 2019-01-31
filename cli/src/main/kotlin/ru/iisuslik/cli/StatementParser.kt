package ru.iisuslik.cli
import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams

class StatementParser(val varsContainer: VarsContainer) {
    fun parse(statement: String): Statement {
        val substituted = substitution(statement)
        val statementLexer = CliLexer(CharStreams.fromString(substituted))
        val statementParser = CliParser(BufferedTokenStream(statementLexer))
        return statementParser.statement().value
    }

    fun substitution(statement: String): String {
        return statement.replace(Regex("\\$[A-Za-z0-9]+")) { it: MatchResult -> varsContainer.get(it.value)}
    }
}