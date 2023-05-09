package framework.utils;

import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Класс с данными по записи значений в файл
 */
public class WriteDataUtils {
    public static final char ENTER = '\n';
    public static final String DOUBLE_ENTER = "\n\n";
    public static final String PATH = "%s_%s_%d.txt";
    public static final String EXACT_SOLUTION = "exactSolution";
    public static final String DATA = "data";
    public static final String INF_STRING = "inf";
    public static final String TIME = "Время решения %f секунд";
    public static final String WEIGHT = "Полученный вес = %f";
    public static final String RESULT = "Результирующий список вещей: %s";
    public static final String NO_PATH = "Нет пути";
    public static final String NO_THINGS = "Нет вещей";
    /**
     * Дефолтный путь к файлу
     */
    private static final String DEFAULT_PATH_FILE = "src\\result\\";
    /**
     * Дефолтный индекс файла
     */
    private static final int DEFAULT_PATH_ID = 0;

    /**
     * Путь к файлу
     */
    private static String pathFile = DEFAULT_PATH_FILE;

    /**
     * Индекс файла
     */
    private static int pathId = DEFAULT_PATH_ID;

    /**
     * @return путь к файлу
     */
    public static String getFilePath() {
        return pathFile;
    }

    /**
     * @return индекс файла (номер)
     */
    public static int getPathId() {
        return pathId;
    }

    /**
     * Установить новый путь к файлу
     * @param path - путь к файлу
     */
    public static void setPath(String path) {
        WriteDataUtils.pathFile = path;
    }

    /**
     * Установить новый индекс файла (номер)
     * @param pathId - индекс файла (номер)
     */
    public static void setPathId(int pathId) {
        WriteDataUtils.pathId = pathId;
    }

    /**
     * Запись значений из массива в файл по переданному пути
     * @param path        - путь к файлу
     * @param listArray   - массив значений
     * @param textMessage - текст сообщения
     */
    public static void writeArray(
            String path,
            ArrayList<ArrayList<Double>> listArray,
            String textMessage
    ) {
            StringBuilder strArray = new StringBuilder(String.format("%s:\n", textMessage));
            for (ArrayList<Double> array : listArray) {
                strArray.append(array).append("\n");
            }
            strArray.append("\n");
            write(path, strArray.toString());
    }

    /**
     * Запись значений в файл по переданному пути
     * @param path          - путь к файлу
     * @param writeMessages - список текстов сообщений
     */
    public static void write(String path, String ... writeMessages) {
        try {
            FileWriter writer = new FileWriter(path, true);
            for (String writeMessage: writeMessages) {
                writer.write(writeMessage);
            }
            writer.flush();
            writer.close();
        }
        catch (IOException e) {
            LoggerFactory.getLogger(WriteDataUtils.class).error(String.format("Ошибка вывода данных. Путь %s", path), e);
            System.exit(1);
        }
    }
}
