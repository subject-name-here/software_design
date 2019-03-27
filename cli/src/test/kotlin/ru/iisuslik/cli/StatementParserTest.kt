package ru.iisuslik.cli

import org.junit.Test
import org.junit.Assert.*

class StatementParserTest {

    private val parser = StatementParser(Context())

    private fun getStatement(vararg commands: Command) = Commands(commands.toList())

    @Test
    fun parseExit() {
        assertEquals(getStatement(Exit), parser.parse("exit"))
    }

    @Test
    fun parseEcho() {
        assertEquals(getStatement(Echo(listOf("kek", "lol"))), parser.parse("echo kek lol"))
    }

    @Test
    fun parseWcEmpty() {
        assertEquals(getStatement(Wc(emptyList())), parser.parse("wc"))
    }

    @Test
    fun parseWcWithArgs() {
        assertEquals(getStatement(Wc(listOf("kekFile"))), parser.parse("wc kekFile"))
    }

    @Test
    fun parseCat() {
        assertEquals(getStatement(Cat(listOf("kek", "lol"))), parser.parse("cat kek lol"))
    }

    @Test
    fun parsePwd() {
        assertEquals(getStatement(Pwd), parser.parse("pwd"))
    }

    @Test
    fun parseUselessArgs() {
        assertEquals(getStatement(Pwd), parser.parse("pwd kek lol"))
        assertEquals(getStatement(Exit), parser.parse("exit kek lol"))
    }

    @Test
    fun tooMuchSpacesBetweenArgs() {
        assertEquals(getStatement(Echo(listOf("kek", "lol"))), parser.parse("echo   kek lol"))
    }

    @Test
    fun tooMuchSpacesEverywhere() {
        assertEquals(getStatement(Echo(listOf("kek"))), parser.parse("   echo    kek   "))
    }

    @Test
    fun quotes() {
        assertEquals(getStatement(Echo(listOf("kek | lol"))), parser.parse("echo \"kek | lol\""))
        assertEquals(getStatement(Echo(listOf("kek | lol"))), parser.parse("echo \'kek | lol\'"))
    }

    @Test
    fun assignment() {
        val context = Context()
        val parser = StatementParser(context)
        assertEquals(Assignment("kek", "lol"), parser.parse("kek=lol"))
    }

    @Test
    fun substitutionEasy() {
        val context = Context()
        context.varsContainer.add("kek", "lol")
        val parser = StatementParser(context)
        assertEquals("lol", parser.substitution("\$kek"))
    }

    @Test
    fun twoDifferentSubstitutions() {
        val context = Context()
        context.varsContainer.add("kek", "lol")
        context.varsContainer.add("such", "wow")
        val parser = StatementParser(context)
        assertEquals("lol wow wow", parser.substitution("\$kek \$such wow"))
    }

    @Test
    fun twoSimilarSubstitutions() {
        val context = Context()
        context.varsContainer.add("kek", "lol")
        val parser = StatementParser(context)
        assertEquals(
            "lol lol such text lol wow",
            parser.substitution("lol \$kek such text \$kek wow")
        )
    }

    @Test
    fun substitutionsNotExists() {
        val context = Context()
        val parser = StatementParser(context)
        assertEquals("", parser.substitution("\$kek"))
    }

    @Test
    fun unknownCommand() {
        assertEquals(
            getStatement(External("wtf", listOf("-flag", "kek"))),
            parser.parse("wtf -flag kek")
        )
    }

    @Test
    fun easyPipe() {
        assertEquals(
            getStatement(Echo(listOf("kek")), Cat(emptyList())),
            parser.parse("echo kek | cat")
        )
    }

    @Test
    fun bigPipe() {
        assertEquals(
            getStatement(Echo(listOf("kek", "lol")), Cat(emptyList()), External("head", emptyList())),
            parser.parse("echo kek lol | cat | head")
        )
    }

    @Test
    fun quotesInside() {
        assertEquals(
            getStatement(Echo(listOf("       \" x   "))),
            parser.parse("echo '       \" x   '")
        )
        assertEquals(
            getStatement(Echo(listOf("       ' x   "))),
            parser.parse("echo \"       ' x   \"")
        )
    }

    @Test
    fun hardEcho() {
        assertEquals(
            getStatement(Echo(listOf("       \" x", "'asd"))),
            parser.parse("echo '       \" x' \"'asd\"")
        )
    }

    @Test
    fun superHardEcho() {
        assertEquals(
            getStatement(Echo(listOf("       \" x", "wedsf     'cv"))),
            parser.parse("echo '       \" x' \"wedsf     'cv\"")
        )
    }

    @Test
    fun substitutionDoubleQuotes() {
        val context = Context()
        context.varsContainer.add("kek", "lol")
        val parser = StatementParser(context)
        assertEquals("\"lol\"", parser.substitution("\"\$kek\""))
    }

    @Test
    fun substitutionQuotesNotHappening() {
        val context = Context()
        context.varsContainer.add("kek", "lol")
        val parser = StatementParser(context)
        assertEquals("'\$kek'", parser.substitution("'\$kek'"))
    }

    @Test
    fun echoDollar() {
        assertEquals(
            getStatement(Echo(listOf("a\$a"))),
            parser.parse("echo 'a\$a'")
        )
    }
}