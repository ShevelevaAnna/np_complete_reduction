package framework.exception;

import org.slf4j.LoggerFactory;

/**
 * Ошибки библиотеки
 */
public class FrameworkException extends Exception {
    /**
     * Конструктор класса ошибки с логгированием
     * @param objectClass - класс ошибки
     * @param message     - сообщение ошибки
     */
    protected <T extends FrameworkException> FrameworkException(Class<T> objectClass, String message) {
        super(message);
        LoggerFactory.getLogger(objectClass).error(message, objectClass);
        System.exit(1);
    }
}
