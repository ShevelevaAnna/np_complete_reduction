package framework.utils;

/**
 * Класс получения сгенерированных данных
 */
public class RandomValueUtils {
    /**
     * Генерация числа типа double в промежутке
     * @param min - минимальное целое значение
     * @param max - максимальное целое значение
     */
    public static double getDoubleValue(int min, int max) {
        return getRandomNumber(min, max);
    }

    /**
     * Генерация числа типа integer в промежутке
     * @param min - минимальное целое значение
     * @param max - максимальное целое значение
     */
    public static int getIntegerValue(int min, int max) {
        return (int)getRandomNumber(min, max);
    }

    /**
     * Генерация числа в промежутке
     * @param min  - минимальное целое значение
     * @param max  - максимальное целое значение
     */
    private static double getRandomNumber(int min, int max) {
        double rand = Math.random();
        return (rand * (max - min)) + min;
    }
}
