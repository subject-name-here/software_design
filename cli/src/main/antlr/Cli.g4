grammar Cli;

statement returns [ru.iisuslik.cli.Statement value]
    :   a=assignment  {$value = $a.value;}
        | c=commands   {$value = new ru.iisuslik.cli.Commands($c.list);}
    ;

assignment returns [ru.iisuslik.cli.Assignment value]
    :   w=word '=' a=arg {$value = new ru.iisuslik.cli.Assignment($w.text, $a.value);}
    ;

commands returns [java.util.List<ru.iisuslik.cli.Command> list]
    :   {$list = new java.util.ArrayList<>();}
        c=command {$list.add($c.value);}
        (' | ' c=command {$list.add($c.value);})*
    ;

command returns [ru.iisuslik.cli.Command value]
    :   'echo' a=args {$value = new ru.iisuslik.cli.Echo($a.list);}
        | 'wc' a=args {$value = new ru.iisuslik.cli.Wc($a.list);}
        | 'cat' a=args {$value = new ru.iisuslik.cli.Cat($a.list);}
        | 'exit' {$value = new ru.iisuslik.cli.Exit();}
        | 'pwd' {$value = new ru.iisuslik.cli.Pwd();}
        | w=word a=args {$value = new ru.iisuslik.cli.Unknown($w.text, $a.list);}
    ;

args returns [java.util.List<String> list]
    :   {$list = new java.util.ArrayList<>();}
        (' ' w=word {$list.add($w.text);})*

    ;


arg returns [String value]
    :   w=word {$value = $w.text;}
        | '"' s=string '"' {$value = $s.text;}
        | '\'' s=string '\'' {$value = $s.text;}
    ;

word
    :   (LETTER | DIGIT | '.' | '-')+
    ;

string
    : (LETTER | DIGIT | '.' | '-' | ' ' | '\t' | '|')+
    ;

DIGIT
    : '0'..'9'
    ;
LETTER: ('a'..'z') | ('A'..'Z');
