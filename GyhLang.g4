grammar GyhLang;

@header{
    import java.util.ArrayList;
    // Lembre-se de importar aqui suas classes de AST, Tabela de Símbolos, etc.
}

@members{
    private String _varName;
    private int _varTipo;
    private String _varValue;
    private Symbol _varSymbol;
    private SymbolTable symbolTable = new SymbolTable();

    private String _expVar;
    private String _expContent;
    private String _condition;
    private ArrayList<Command> _condTrue = new ArrayList<Command>();
    private ArrayList<Command> _condFalse = new ArrayList<Command>();

    private String _writeVar;
    private String _readVar;
    private ArrayList<Command> listCmd = new ArrayList<Command>();
    private ArrayList<Command> listAux = new ArrayList<Command>();
    private GyhProgram program = new GyhProgram();

    public void generateCommand(){
        program.generatetarget();
    }

    public void verificarVar(String nomeVar){
        if(!symbolTable.exists(nomeVar)){
            System.out.println("Erro Semântico: Variável não declarada - " + nomeVar);
        }
    }
}

programa: Delim 'DEC' listaDeclaracoes Delim 'PROG' listaComandos EOF
            {
                program.setVarTable(symbolTable);
                program.setCommand(listCmd);
            };

listaComandos: (comando
                    {
                        listCmd.addAll(listAux);
                        listAux.clear();
                    }
               )+ ;

comando: comandoAtribuicao
         | comandoEntrada
         | comandoSaida
         | comandoCondicao
         | subAlgoritmo;

subAlgoritmo: 'INI' (comando)+ 'FIM';

comandoCondicao: 'SE' {_condition += " (";}
                 expressaoRelacional 'ENTAO'
                 comando
                 {
                      _condTrue.addAll(listAux);
                      listAux.clear();
                 }
                 ('SENAO' comando
                       {
                            _condFalse.addAll(listAux);
                            listAux.clear();
                       }
                 )?
                 {
                     CommandCondicao cmd = new CommandCondicao(_condition, _condTrue, _condFalse);
                     listAux.add(cmd);
                 };

expressaoRelacional: {_expContent = "";}
                     expressaoRelacional
                     OpBool     {_condition += " "; _condition += _input.LT(-1).getText(); }
                     termoRelacional
                     | termoRelacional
                     {_condition += " "; _condition += _input.LT(-1).getText();}
                     ;

termoRelacional: {_expContent = "";}
                 expressaoAritmetica
                 OpRel          {_condition += " "; _condition += _input.LT(-1).getText(); }
                 expressaoAritmetica
                 |
                 '('            {_condition += " (";}
                 expressaoRelacional
                 ')'            {_condition += " )";}
                 ;

comandoEntrada : 'LER' Var
                 {
                    verificarVar(_input.LT(-1).getText());
                    _readVar = _input.LT(-1).getText();
                    CommandLeitura cmd = new CommandLeitura(_readVar);
                    listAux.add(cmd); // Mudei para listAux para manter a coerência com listaComandos
                    System.out.println(cmd.toString());
                 };

comandoSaida : 'IMPRIMIR' Var
               {
                    verificarVar(_input.LT(-1).getText());
                    _writeVar = _input.LT(-1).getText();
                    CommandEscrita cmd = new CommandEscrita(_writeVar);
                    listAux.add(cmd); // Mudei para listAux
                    System.out.println(cmd.toString());
               }
               | 'IMPRIMIR' Cadeia;

comandoAtribuicao: Var
                   {
                        verificarVar(_input.LT(-1).getText());
                        _expVar = _input.LT(-1).getText();
                   }
                   ':=' {_expContent = "";}
                   expressaoAritmetica
                   ;

expressaoAritmetica: termoAritmetico
                     '+'             {_expContent += "+";}
                     termoAritmetico
                     |
                     termoAritmetico
                     '-'             {_expContent += "-";}
                     termoAritmetico
                     |
                     termoAritmetico;

termoAritmetico: NumInt     {_expContent += _input.LT(-1).getText();}
                 | NumReal  {_expContent += _input.LT(-1).getText();}
                 | Var      {_expContent += _input.LT(-1).getText();}
                 ;

listaDeclaracoes returns [int qtdeCmd] :
                    {$qtdeCmd = 0;}
                    (declaracao {$qtdeCmd++;})+
                    {System.out.println("qtdeCmd = " + $qtdeCmd);};

declaracao: Var Delim
            (
              'INT'       {_varTipo = 1;}
            | 'REAL'    {_varTipo = 0;}
            )
            {
                _varName = _input.LT(-3).getText();
                _varValue = null;
                _varSymbol = new Symbol(_varName, _varTipo, _varValue);
                if(!symbolTable.exists(_varName)){
                    symbolTable.add(_varSymbol);
                } else {
                    System.out.println("Erro Semântico: Variável já declarada - " + _varName);
                }
            }
            ;

// === REGRAS LÉXICAS ===

PC: 'DEC' | 'PROG' | 'INT' | 'REAL' | 'LER' | 'IMPRIMIR';
Coment: '#' ~[\r\n]* -> skip;
Delim: ':';

WS:  (' ' | '\n' | '\r' | '\t') -> skip;

OpRel: '<' | '<=' | '>' | '>=' | '==' | '!=';
OpBool: 'E' | 'OU';

Var: [a-z] ([a-z] | [A-Z] | [0-9])*;
NumInt: [0-9]+;
NumReal: [0-9]+'.'[0-9]* ;
Cadeia: '"' ~'"'* '"';