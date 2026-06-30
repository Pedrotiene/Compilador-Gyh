grammar GyhLang;

// =====================================================================
// 1. REGRAS SINTÁTICAS
// =====================================================================

programa
    : PCDec Delim listaDeclaracoes
      PCProg Delim listaComandos
    ;

listaDeclaracoes
    : declaracao+
    ;

declaracao
    : Var Delim tipoVar
    ;

tipoVar
    : PCInt
    | PCReal
    ;

listaComandos
    : comando+
    ;

comando
    : comandoAtribuicao
    | comandoEntrada
    | comandoSaida
    | comandoCondicao
    | comandoRepeticao
    | subAlgoritmo
    ;

comandoAtribuicao
    : Var Atrib expressaoAritmetica
    ;

comandoEntrada
    : PCLer Var
    ;

comandoSaida
    : PCImprimir Var
    | PCImprimir Cadeia
    ;

comandoCondicao
    : PCSe expressaoRelacional
      PCEntao subAlgoritmo
      (PCSenao subAlgoritmo)?
    ;

comandoRepeticao
    : PCEnqto expressaoRelacional
      subAlgoritmo
    ;

subAlgoritmo
    : PCIni listaComandos PCFim
    ;

// =====================================================================
// EXPRESSÕES ARITMÉTICAS
// =====================================================================

expressaoAritmetica
    : termoAritmetico
      ((OpAritSoma | OpAritSub) termoAritmetico)*
    ;

termoAritmetico
    : fatorAritmetico
      ((OpAritMult | OpAritDiv) fatorAritmetico)*
    ;

fatorAritmetico
    : Var
    | NumInt
    | NumReal
    | AbrePar expressaoAritmetica FechaPar
    ;

// =====================================================================
// EXPRESSÕES RELACIONAIS
// =====================================================================

expressaoRelacional
    : expressaoBooleana
    ;

expressaoBooleana
    : fatorBooleano ((OpBoolE | OpBoolOu) fatorBooleano)*
    ;

fatorBooleano
    : AbrePar expressaoBooleana FechaPar
    | comparacao
    ;

comparacao
    : expressaoAritmetica opRelacional expressaoAritmetica
    ;

opRelacional
    : OpRelMenor
    | OpRelMenorIgual
    | OpRelMaior
    | OpRelMaiorIgual
    | OpRelIgual
    | OpRelDif
    ;

// =====================================================================
// 2. REGRAS LÉXICAS
// =====================================================================

// Palavras-chave

PCDec       : 'DEC';
PCProg      : 'PROG';
PCInt       : 'INT';
PCReal      : 'REAL';
PCLer       : 'LER';
PCImprimir  : 'IMPRIMIR';
PCSe        : 'SE';
PCSenao     : 'SENAO';
PCEntao     : 'ENTAO';
PCEnqto     : 'ENQTO';
PCIni       : 'INI';
PCFim       : 'FIM';

// Operadores aritméticos

OpAritMult  : '*';
OpAritDiv   : '/';
OpAritSoma  : '+';
OpAritSub   : '-';

// Operadores relacionais

OpRelMenorIgual : '<=';
OpRelMaiorIgual : '>=';
OpRelIgual      : '==';
OpRelDif        : '!=';
OpRelMenor      : '<';
OpRelMaior      : '>';

// Operadores booleanos

OpBoolE     : 'E';
OpBoolOu    : 'OU';

// Delimitadores

Atrib       : ':=';
Delim       : ':';

// Parênteses

AbrePar     : '(';
FechaPar    : ')';

// Identificadores e constantes

Var         : [a-z][a-zA-Z0-9]*;

NumReal
    : [0-9]+ '.' [0-9]+
    ;

NumInt
    : [0-9]+
    ;

Cadeia
    : '"' (~["\r\n])* '"'
    ;

// Espaços e comentários

WS
    : [ \t\r\n]+ -> skip
    ;

Comentario
    : '//' ~[\r\n]* -> skip
    ;