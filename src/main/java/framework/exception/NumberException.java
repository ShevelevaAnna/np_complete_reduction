package framework.exception;

/**
 * Класс ошибки, связанный с неправильным использованием чисел в программе
 */
public class NumberException extends FrameworkException {
    public static final String MESSAGE = "Ожидалось, что значение %s = %d будет больше значения %s = %d";
    public static final String MESSAGE_DOUBLE = "Ожидалось, что значение %s = %f будет больше значения %s = %d";

    /**
     * Ошибка связанная с ожиданием числа, большего полученного
     * @param actualValueName   - название текущего значения числа
     * @param expectedValueName - название ожидаемого значения числа
     * @param actualValue       - текущее значение числа
     * @param expectedValue     - ожидаемое значение числа
     */
    public NumberException(String actualValueName, String expectedValueName, int actualValue, int expectedValue) {
        super(
                NumberException.class,
                String.format(MESSAGE, actualValueName, actualValue, expectedValueName, expectedValue)
        );
    }

    public NumberException(String actualValueName, String expectedValueName, double actualValue, int expectedValue) {
        super(
                NumberException.class,
                String.format(MESSAGE_DOUBLE, actualValueName, actualValue, expectedValueName, expectedValue)
        );
    }
}
