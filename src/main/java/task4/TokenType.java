package task4;

/**
 * Тип лексемы
 */
public enum TokenType {
    /**
     * Пробелы
     */
    SPACES,
    /**
     * Целое число
     */
    NUMBER,
    /**
     * Символ '+'
     */
    ADD
    {
        @Override
        public double binaryOperator(double x, double y) {
            return x+y;
        }
    },
    /**
     * Символ '-'
     */
    SUB
            {
                @Override
                public double binaryOperator(double x, double y) {
                    return x-y;
                }

                @Override
                public double unaryOperator(double x) {
                    return -x;
                }
            },

    /**
     * Символ '*'
     */
    MUL{
        @Override
        public double binaryOperator(double x, double y) {
            return x*y;
        }
    },
    /**
     * Символ '/'
     */
    DIV{
        @Override
        public double binaryOperator(double x, double y) {
            return x/y;
        }
    },
    /**
     * Символ '!'
     */
    EXCLAM
            {
                @Override
                public double unaryOperator(double x) {
                    return factorial((long) x);
                }
            },
    /**
     * Символ '('
     */
    LPAR,
    /**
     * Символ ')'
     */
    RPAR,
    /**
     * Variable 'blah-blah'
     * */
    VAR,
    /**
     * Symbol '='
     * */
    ASSIGN,
    POW
            {
                @Override
                public double binaryOperator(double x, double y)
                {
                    return Math.pow(x,y);
                }
            },
    SIN
            {
                @Override
                public double unaryOperator(double x) {
                    return Math.sin(x);
                }
            },
    COS
            {
                @Override
                public double unaryOperator(double x) {
                    return Math.cos(x);
                }
            },
    EXP
            {
                @Override
                public double unaryOperator(double x) {
                    return Math.exp(x);
                }
            },
    /*
    * Separator ';'
    * */
    SEP,
    EQ
            {
                @Override
                public double binaryOperator(double x, double y) {
                    return x==y? 0: 1;
                }
            },
    FUNC_DEC,
    FUNC_EXE,

    /*
    * Comparision symbols like '<=', '==' and so on
    * */
    COM;
    public double binaryOperator(double x, double y)
    {
        throw new UnsupportedOperationException();
    }
    public double unaryOperator(double x)
    {
        throw new UnsupportedOperationException();
    }
    private static long factorial(long n) {
        long result = 1;
        for (long i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
}
