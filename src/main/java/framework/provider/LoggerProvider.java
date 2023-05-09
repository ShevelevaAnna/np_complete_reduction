package framework.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Класс вызова записи логов
 */
public class LoggerProvider {
    private final Logger logger;

    /**
     * Конструктор класса
     * @param objectClass - класс откуда вызывается лог
     */
    protected <T extends LoggerProvider> LoggerProvider(Class<T> objectClass) {
        logger = LoggerFactory.getLogger(objectClass);
    }

    /**
     * Записать в лог переданную строку в виде информации
     * @param info - строка информации
     */
    protected void logInfo(String info) {
        logger.info(info);
    }

    /**
     * Записать в лог переданную строку в виде информации
     * @param info   - строка информации
     * @param number - числовое значение типа double
     */
    protected void logInfo(String info, double number) {
        logger.info(String.format(info, number));
    }

    /**
     * Записать в лог переданную строку в виде информации
     * @param info - строка информации
     * @param str  - строковое значение
     */
    protected void logInfo(String info, String str) {
        logger.info(String.format(info, str));
    }

    /**
     * Записать в лог переданную строку в виде ошибки
     * @param error  - строка ошибки
     */
    protected void logError(String error) {
        logger.error(error);
    }
}
