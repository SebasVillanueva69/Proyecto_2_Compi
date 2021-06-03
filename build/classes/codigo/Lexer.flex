package codigo;
import static codigo.Tokens.*;
%%
%class Lexer
%type Tokens
L=[a-zA-Z_]+
D=[0-9]+
O=[0-7]+
H=[0-9a-fA-F]+
espacio=[ ,\t,\r]+
%{
    public String lexeme;
%}
%%
auto |
break |
case |
char |
const |
continue |
default |
do |
double |
else |
enum |
extern |
float |
for |
goto |
if |
int |
long |
register |
return |
short |
signed |
sizeof |
static |
struct |
switch |
typedef |
union |
unsigned |
void |
volatile |
while {lexeme=yytext(); return Reservadas;}
{espacio} {/*Ignore*/}
"//".* {/*Ignore*/}
"\n" {return Linea;}
"=" |
"+" |
"-" |
"*" |
"/" |
"++" |
"--" |
"==" |
">=" |
">" |
"?" |
"<=" |
"<" |
"!=" |
"||" |
"&&" |
"!" |
"%" |
"." |
"+=" |
"-=" |
"*=" |
"/=" |
"&" |
"^" |
"|" |
">>" |
"<<" |
"~" |
"%=" |
"&=" |
"^=" |
"|=" |
"<<=" |
">>=" |
"->" |
"#" {lexeme=yytext(); return Operador;}
"(" |
")" |
"[" |
"]" |
"{" |
"}" |
"," |
";" |
"..." |
":" {lexeme=yytext(); return Delimitador;}
{L}({L}|{D})* {lexeme=yytext(); return Identificador;}
\".*\" | \'.\' {lexeme=yytext(); return Literal;}
{D}+ | {D}+"."{D}* | "-"{D}+ | "-"{D}+"."{D}* {lexeme=yytext(); return Literal;}
0[xX]{H}+ | 0[xX]{H}+"."{H}* {lexeme=yytext(); return Literal;}
 . {return ERROR;}
