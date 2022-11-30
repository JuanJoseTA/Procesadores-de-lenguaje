import java_cup.runtime.*;

%%

%cup
%%

[0-9]+ {
    return new Symbol(sym.NUMBER, Integer.parseInt(yytext()));
}

"+" {
    return new Symbol(sym.SUM);
}

"-" {
    return new Symbol(sym.RES);    
}

"*" {
    return new Symbol(sym.MUL);
}

"/" {
    return new Symbol(sym.DIV);
}

"(" {
    return new Symbol(sym.LPAREN);
}

")" {
    return new Symbol(sym.RPAREN);
}

\n {
    return new Symbol(sym.EOL);
}

[^] {}
