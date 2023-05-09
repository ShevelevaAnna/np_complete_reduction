package framework.utils;

import framework.exception.ProblemException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static framework.utils.IntegerDataUtils.INF;
import static framework.utils.WriteDataUtils.ENTER;
import static framework.utils.WriteDataUtils.INF_STRING;

/**
 * Класс чтения из файла
 */
public class ReadDataUtils {
    /**
     * Чтение данных из файла и разбиение считанного по словам и строкам
     * @param file - путь к файлу
     */
    public static List<List<String>> read(String file) {
        List<List<String>> text = new ArrayList<>();
        List<String> line = new ArrayList<>();
        String word = "";
        int flagNewComponent = 0;
        try(FileReader reader = new FileReader(file))
        {
            int c;
            while((c = reader.read()) != -1) {
                if (c == ' ') {
                    line.add(word);
                    word = "";
                    continue;
                }
                if (c == ENTER) {
                    flagNewComponent++;
                    if (flagNewComponent == 2) {
                        text.add(new ArrayList<>(ENTER));
                        flagNewComponent = 0;
                    }
                    else {
                        line.add(word);
                        text.add(line);
                        line = new ArrayList<>();
                        word = "";
                    }
                    continue;
                }
                flagNewComponent = 0;
                word += ((char) c);
            }
            line.add(word);
            text.add(line);
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        return text;
    }

    /**
     * Чтение матрицы пути из переданного файла в виде списка из строк и слов
     * @param file  - список из слов и строк
     * @param index - индекс, с которого читать данные списка
     */
    public static ArrayList<ArrayList<Double>> readDoubleMatrix(List<List<String>> file, int index) {
        ArrayList<ArrayList<Double>> matrix = new ArrayList<>();
        try {
            for (; index < file.size(); index++) {
                if (file.get(index).size() == 0) {
                    return matrix;
                }
                ArrayList<Double> path = new ArrayList<>();
                for (int j = 0; j < file.get(index).size(); j++) {
                    if (Objects.equals(file.get(index).get(j), INF_STRING)) {
                        path.add(INF);
                    }
                    else {
                        path.add(Double.valueOf(file.get(index).get(j)));
                    }
                }
                matrix.add(path);
            }
        }
        catch (NumberFormatException e) {
            new ProblemException(index + 1);
        }
        return matrix;
    }
}
