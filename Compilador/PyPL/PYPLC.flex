import java_cup.runtime.*;
%%
%cup 
%% 
	"{"										{ return new Symbol(sym.ALL); }
	"}"										{ return new Symbol(sym.CLL); }
	"("										{ return new Symbol(sym.AP); }
	")"										{ return new Symbol(sym.CP); }
	"["										{ return new Symbol(sym.AC); }
	"]"										{ return new Symbol(sym.CC); }
	","										{ return new Symbol(sym.COMA); }
	";"										{ return new Symbol(sym.PYC); }
	":"										{ return new Symbol(sym.DP); }
	\"										{ return new Symbol(sym.CD); }
	"#"[^\n]\n								{}
	"'''"[^]"'''"							{}
	
	"="										{ return new Symbol(sym.ASIG); }
	"+"										{ return new Symbol(sym.MAS); }
	"-"										{ return new Symbol(sym.MENOS); }
	"*"										{ return new Symbol(sym.POR); }
	"/"										{ return new Symbol(sym.DIV); }
	
	"=="									{ return new Symbol(sym.IGUAL); }
	"!="									{ return new Symbol(sym.DIST); }
	"!"										{ return new Symbol(sym.NEG); }
	"<"										{ return new Symbol(sym.MENOR); }
	">"										{ return new Symbol(sym.MAYOR); }
	"<="									{ return new Symbol(sym.MENOREQ); }
	">="									{ return new Symbol(sym.MAYOREQ); }
	"&&"									{ return new Symbol(sym.AND); }
	"||"									{ return new Symbol(sym.OR); }
	
	"if"									{ return new Symbol(sym.IF); }
	"else"									{ return new Symbol(sym.ELSE); }
	"while"									{ return new Symbol(sym.WHILE); }
	"do"									{ return new Symbol(sym.DO); }
	"for"									{ return new Symbol(sym.FOR); }
	"print"									{ return new Symbol(sym.PRINT); }
	
	0|[1-9][0-9]*							{ return new Symbol(sym.ENTERO, yytext()); }
	[a-zA-Z_][a-zA-Z0-9]*					{ return new Symbol(sym.IDENT, yytext()); }
	
	\t										{ return new Symbol(sym.TAB); }
	\n										{ return new Symbol(sym.EOL); }
	[^]										{}
