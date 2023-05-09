package framework.exception;

/**
 * Класс возможных ошибок при создании объектов задач
 */
public class ProblemException extends FrameworkException {
    public static final String LENGTH_LINE_MESSAGE =
            "Некорректный ввод данных в строке %d: Ожидалась строка длиной '%d', получена строка длиной '%d'";
    public static final String SIZE_TWO_MATRIX_MESSAGE =
            "Ожидалось, что размер одной матрицы (%d) равен размеру другой матрицы (%d)";
    public static final String DOUBLE_NUMBER_MESSAGE =
            "Некорректный ввод данных в строке %d: Ожидалось что число %f =%s";
    public static final String VALUE_MESSAGE = "Значение в строке %d задано некорректно.";
    public static final String INTEGER_NUMBER_MESSAGE = "Слово в строке %d не число и не 'inf'";
    public static final String SIZE_MATRIX_MESSAGE = "Размер матрицы неверный (не равен 4 элементам)";
    public static final String LOW_DATA_IN_FILE_MESSAGE = "В файле не хватает данных";
    public static final String MAX_WEIGHT_MESSAGE = "Максимальный вес задан неверно.";
    public static final String MISS_ENTER_MESSAGE =
            "Пропущен перенос на новую строку после заданного максимального веса.";


    /**
     * Конструктор класса ошибки с логгированием
     * @param message - сообщение об ошибке
     */
    public <T extends FrameworkException> ProblemException(String message) {
        super(ProblemException.class, message);
    }

    /**
     * Конструктор класса ошибки с логгированием {@link ProblemException#LENGTH_LINE_MESSAGE}
     * @param line           - индекс текущей строки
     * @param expectedLength - ожидаемая длина строки
     * @param actualLength   - полученная длина строки
     */
    public <T extends FrameworkException> ProblemException(int line, int expectedLength, int actualLength) {
        super(ProblemException.class, String.format(LENGTH_LINE_MESSAGE, line, expectedLength, actualLength));
    }

    /**
     * Конструктор класса ошибки с логгированием {@link ProblemException#SIZE_TWO_MATRIX_MESSAGE}
     * @param firstSize  - размер первой матрицы
     * @param secondSize - размер второй матрицы
     */
    public <T extends FrameworkException> ProblemException(int firstSize, int secondSize) {
        super(ProblemException.class, String.format(SIZE_TWO_MATRIX_MESSAGE, firstSize, secondSize));
    }

    /**
     * Конструктор класса ошибки с логгированием {@link ProblemException#VALUE_MESSAGE}
     * @param line           - индекс текущей строки
     * @param actualNumber   - полученное число
     * @param expectedNumber - ожидаемое число double
     */
    public <T extends FrameworkException> ProblemException(int line, double actualNumber, String expectedNumber) {
        super(ProblemException.class, String.format(VALUE_MESSAGE, line, actualNumber, expectedNumber));
    }

    /**
     * Конструктор класса ошибки с логгированием {@link ProblemException#INTEGER_NUMBER_MESSAGE}
     * @param line - индекс текущей строки
     */
    public <T extends FrameworkException> ProblemException(int line) {
        super(ProblemException.class, String.format(INTEGER_NUMBER_MESSAGE, line));
    }
}
