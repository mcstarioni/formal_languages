package task4;

/**
 * Ошибка разбора
 */
public class ParseException extends RuntimeException {

    public final int index;

    public ParseException(String message, int index) {
        super(message);
        this.index = index;
    }
}
