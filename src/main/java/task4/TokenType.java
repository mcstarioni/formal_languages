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
    ADD,
    /**
     * Символ '-'
     */
    SUB,
    /**
     * Символ '*'
     */
    MUL,
    /**
     * Символ '/'
     */
    DIV,
    /**
     * Символ '!'
     */
    EXCLAM,
    /**
     * Символ '('
     */
    LPAR,
    /**
     * Символ ')'
     */
    RPAR,

    VAR,

    COMPARISON
}
