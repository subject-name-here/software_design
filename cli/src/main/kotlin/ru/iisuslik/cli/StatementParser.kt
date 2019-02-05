package ru.iisuslik.cli

import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams

// This class uses antlr to parse commands
class StatementParser(val varsContainer: VarsContainer) {
    fun parse(statement: String): Statement {
        // Some preprocessing before parsing
        val substituted = substitution(statement)
        val statementLexer = CliLexer(CharStreams.fromString(substituted))
        val statementParser = CliParser(BufferedTokenStream(statementLexer))
        return statementParser.statement().value
    }

    // Replaces all \\s symbols into one " " and substitutes all vars from varsContainer
    fun substitution(statement: String): String {
        val oneSpace = statement.replace("\\s+".toRegex(), " ")
        return oneSpace.replace("\\$[A-Za-z0-9]+".toRegex()) {
            val varName = it.value
            varsContainer.get(varName.subSequence(1 until varName.length).toString())
        }
    }
}