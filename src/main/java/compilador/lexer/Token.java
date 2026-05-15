package compilador.lexer;

public class Token {
        private TokenType type;
        private String lexeme;
        private int line;

        public Token(TokenType type, String lexeme, int line) {
            this.type = type;
            this.lexeme = lexeme;
            this.line = line;
        }

        private String formatType(TokenType type) {
            return switch (type) {

                case PC_DEC -> "PCDec";
                case PC_PROG -> "PCProg";
                case PC_INT -> "PCInt";
                case PC_LER -> "PCLer";
                case PC_REAL -> "PCReal";
                case PC_IMPRIMIR -> "PCImprimir";
                case PC_SE -> "PCSe";
                case PC_SENAO -> "PCSenao";
                case PC_ENTAO -> "PCEntao";
                case PC_ENQTO -> "PCEnqto";
                case PC_INI -> "PCIni";
                case PC_FIM -> "PCFim";
                case OP_SOMA -> "OpAritSoma";
                case OP_SUB -> "OpAritSub";
                case OP_MULT -> "OpAritMult";
                case OP_DIV -> "OpAritDiv";
                case OP_MENOR -> "OpRelMenor";
                case OP_MENOR_IGUAL -> "OpRelMenorIgual";
                case OP_MAIOR -> "OpRelMaior";
                case OP_MAIOR_IGUAL -> "OpRelMaiorIgual";
                case OP_IGUAL -> "OpRelIgual";
                case OP_DIF -> "OpRelDif";
                case OP_E -> "OpBoolE";
                case OP_OU -> "OpBoolOu";
                case ATRIB -> "Atrib";
                case DELIM -> "Delim";
                case ABRE_PAR -> "AbrePar";
                case FECHA_PAR -> "FechaPar";
                case VAR -> "Var";
                case NUM_INT -> "NumInt";
                case NUM_REAL -> "NumReal";
                case CADEIA -> "Cadeia";
                case EOF -> "EOF";
            };
        }

        public TokenType getType() {
            return type;
        }

        public String getLexeme() {
            return lexeme;
        }

        public int getLine() {
            return line;
        }

        @Override
        public String toString() {
            return "<" + formatType(type) + ",\"" + lexeme + "\"," + line + ">";
        }
}
