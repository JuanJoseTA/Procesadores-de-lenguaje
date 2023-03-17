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
	"++"									{ return new Symbol(sym.INC); }
	"--"									{ return new Symbol(sym.DEC); }
	
	"="										{ return new Symbol(sym.ASIG); }
	"+"										{ return new Symbol(sym.MAS); }
	"-"										{ return new Symbol(sym.MENOS); }
	"*"										{ return new Symbol(sym.POR); }
	"/"										{ return new Symbol(sym.DIV); }
	"%"										{ return new Symbol(sym.MOD); }
	"~"										{ return new Symbol(sym.CIRCUN); }
	
	"=="									{ return new Symbol(sym.IGUAL); }
	"!="									{ return new Symbol(sym.DIST); }
	"!"										{ return new Symbol(sym.NEG); }
	"<"										{ return new Symbol(sym.MENOR); }
	">"										{ return new Symbol(sym.MAYOR); }
	"<="									{ return new Symbol(sym.MENOREQ); }
	">="									{ return new Symbol(sym.MAYOREQ); }
	"&&"									{ return new Symbol(sym.AND); }
	"||"									{ return new Symbol(sym.OR); }
	"<=="									{ return new Symbol(sym.ADDC); }
	"==>"									{ return new Symbol(sym.DELC); }
	
	"if"									{ return new Symbol(sym.IF); }
	"else"									{ return new Symbol(sym.ELSE); }
	"while"									{ return new Symbol(sym.WHILE); }
	"do"									{ return new Symbol(sym.DO); }
	"for"									{ return new Symbol(sym.FOR); }
	"print"									{ return new Symbol(sym.PRINT); }
	"to"									{ return new Symbol(sym.TO); }
	"downto"								{ return new Symbol(sym.DOWNTO); }
	"step"									{ return new Symbol(sym.STEP); }
	"in"									{ return new Symbol(sym.IN); }
	".length"								{ return new Symbol(sym.LENGTH); }
	"set"									{ return new Symbol(sym.SET); }
	
	"int"									{ return new Symbol(sym.INT); }
	"float"									{ return new Symbol(sym.FLOAT); }
	"char"									{ return new Symbol(sym.CHAR); }
	"string"								{ return new Symbol(sym.STRING); }
	
	0|[1-9][0-9]*							{ return new Symbol(sym.ENTERO, yytext()); }
	0|[1-9][0-9]*\.[0-9]+					{ return new Symbol(sym.REAL, yytext()); }
	0|[1-9][0-9]*\.[0-9]*E-[0-9]+			{ return new Symbol(sym.REAL, yytext()); }
	'(\\([\"'\\bfnrt]|u[0-9A-Fa-f]{4}))'	{ return new Symbol(sym.CHARESP, yytext()); }
	'[^\\']'								{ return new Symbol(sym.CHARN, yytext()); }
	\"([^\\\"]|\\.)*\"						{ return new Symbol(sym.STRINGVAL, yytext()); }
	[a-zA-Z_][a-zA-Z0-9]*					{ return new Symbol(sym.IDENT, yytext()); }
	[^]										{}
