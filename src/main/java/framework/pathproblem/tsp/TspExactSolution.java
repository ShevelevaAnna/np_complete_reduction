package framework.pathproblem.tsp;

import framework.pathproblem.ExactSolution;
import framework.utils.ArrayListUtils;

import java.util.ArrayList;
import java.util.List;

import static framework.utils.IntegerDataUtils.INF;
import static framework.utils.IntegerDataUtils.NANO;
import static framework.utils.WriteDataUtils.*;

/**
 * Класс решения задачи о коммивояжере
 */
class TspExactSolution extends ExactSolution {
    private final Tsp tsp;

    /**
     * Конструктор класса решения задачи о коммивояжере
     */
    protected TspExactSolution(Tsp tsp) {
        super(tsp.getPaths(), TspExactSolution.class);
        this.tsp = tsp;
    }

    /**
     * Нахождение точного пути задачи о коммивояжере
     */
    protected void solve() {
        solve("задачи о коммивояжере");
        if (minCost > INF) {
            logInfo(NO_PATH);
        }
        else {
            logInfo("Минимальный вес: %s", minCost);
            logInfo("Кратчайший путь: %s", minPath.toString());
        }
    }

    /**
     * Получение пути файла
     */
    private static String getPath() {
        return String.format(getFilePath() + PATH, Tsp.PROBLEM, EXACT_SOLUTION, getPathId());
    }

    /**
     * Записать результат задачи в файл
     */
    protected void writeResult() {
        logInfo("Запись решения задачи о коммивояжере в файл %s", getPath());
        if (minCost > INF) {
            write(getPath(), String.format(TIME + ENTER, time/NANO), NO_PATH + DOUBLE_ENTER);
        }
        else {
            write(
                    getPath(),
                    String.format(TIME + ENTER, time/NANO),
                    String.format(WEIGHT + ENTER, minCost),
                    String.format(RESULT + DOUBLE_ENTER, minPath.toString())
            );
        }
    }

    /**
     * Записать результат задачи в файл в виде задачи о рюкзаке (при сведении к рюкзаку)
     */
    protected void writeTspToKpResult() {
        logInfo("Запись решения задачи о коммивояжере в виде задачи о рюкзаке в файл %s", getPath());
        if (minCost > INF) {
            write(getPath(), "-- В виде задачи о рюкзаке" + ENTER, NO_THINGS + DOUBLE_ENTER);
        }
        else {
            write(
                    getPath(),
                    "-- В виде задачи о рюкзаке" + ENTER,
                    String.format(WEIGHT + ENTER, getWeightToKp()),
                    String.format(RESULT + DOUBLE_ENTER, getPathToKp())
            );
        }
    }

    /**
     * Записать результат задачи в файл в виде решения двухуровневой задачи о назначениях (при сведении из 2ЗОН)
     */
    protected void writeCapToTspResult() {
        if (minCost < INF) {
            logInfo(
                    "Запись решения задачи о коммивояжере в виде решения двухуровневой задачи о назначениях в файл %s",
                    getPath()
            );
            String stepMessage = "Преобразование пути в вид решения двухуровневой задачи о назначениях ";
            ArrayList<Integer> minPathFromCapIntermediateValue = getMinPathFromCapIntermediateValue();
            String pathFromCapText = stepMessage + "(Шаг 1) = %s" + ENTER + stepMessage + "(Шаг 2) = %s" + DOUBLE_ENTER;
            write(
                    getPath(),
                    "-- В виде двухуровневой задачи о назначених" + ENTER,
                    String.format(
                            pathFromCapText,
                            minPathFromCapIntermediateValue,
                            getMinPathFromCap(minPathFromCapIntermediateValue)
                    )
            );
        }
    }

    /**
     * Записать результат задачи в файл в виде решения задачи о рюкзаке (при сведении из рюкзака)
     */
    protected void writeKpToTspResult() {
        logInfo(
                "Запись решения задачи о коммивояжере в виде решения задачи о рюкзаке в файл %s",
                getPath()
        );
        if (minCost > INF) {
            write(getPath(), "-- В виде задачи о рюкзаке", NO_THINGS + DOUBLE_ENTER);
        }
        else {
            ArrayList<Integer> minPathFromKp = getMinPathFromKp();
            String pathFromCapText = "Веса вещей: %s" + DOUBLE_ENTER;
            write(getPath(), "-- В виде задачи о рюкзаке", String.format(pathFromCapText, minPathFromKp));
        }
    }

    /**
     * Получение кратчайшего пути в виде решения задачи о рюкзаке (при сведении к рюкзаку)
     * Путь [0, 1, 2, 3] -> [0_1, 1_2, 2_3, 3_1]
     */
    private List<String> getPathToKp() {
        List<String> minPathKp = new ArrayList<>();
        for (int i = 0; i < minPath.size() - 1; i++) {
            minPathKp.add(minPath.get(i) + "_" + minPath.get(i + 1));
        }
        minPathKp.add(minPath.get(minPath.size() - 1) + "_0");
        return minPathKp;
    }

    /**
     * Получение минимального веса в виде решения задачи о рюкзаке (при сведении к рюкзаку)
     * sum[W] -> sum[1 - (W / s)], где
     * s - сумма всех весов ребер (исключая бесконечные),
     * W - вес ребра из кратчайшего пути
     */
    private double getWeightToKp() {
        double perWeight = ArrayListUtils.getSumNotInf(tsp.getPaths());
        double weightKp = tsp.getCountVertexes();
        for (int i = 0; i < minPath.size() - 1; i++) {
            weightKp -= tsp.getPaths().get(minPath.get(i)).get(minPath.get(i + 1)) / perWeight;
        }
        weightKp -= tsp.getPaths().get(minPath.get(minPath.size() - 1)).get(minPath.get(0)) / perWeight;
        return weightKp;
    }

    /**
     * Получение кратчайшего пути в виде решения двухуровневой задачи о назначении (при сведении из 2ЗОН)
     * Путь [minPath.size() / 2 + 1, minPath.size()] -> [0, minPath.size() / 2]
     * Пример: [1, 5, 2, 6, 3, 4] -> [1, 2, 2, 3, 3, 1]
     */
    private ArrayList<Integer> getMinPathFromCapIntermediateValue() {
        ArrayList<Integer> minPathFromCapIntermediateValue = new ArrayList<>();
        for (int i = 0; i < minPath.size(); i++) {
            if (minPath.get(i) < minPath.size() / 2) {
                minPathFromCapIntermediateValue.add(minPath.get(i));
            }
            else {
                minPathFromCapIntermediateValue.add(minPath.get(i) - minPath.size() / 2);
            }
        }
        return minPathFromCapIntermediateValue;
    }

    /**
     * Получение кратчайшего пути в виде решения двухуровневой задачи о назначении (при сведении из 2ЗОН)
     * Удаление повторяющихся вершин.
     * Пример: [1, 2, 2, 3, 3, 1] -> [1, 2, 3]
     */
    private ArrayList<Integer> getMinPathFromCap(ArrayList<Integer> minPathFromCapIntermediateValue) {
        ArrayList<Integer> minPathFromCap = new ArrayList<>();
        minPathFromCap.add(minPathFromCapIntermediateValue.get(0));
        for (int j = 1; j < minPath.size() - 1; j += 2) {
            minPathFromCap.add(minPathFromCapIntermediateValue.get(j));
        }
        return minPathFromCap;
    }

    /**
     * Получение кратчайшего пути в виде решения задачи о рюкзаке (при сведении из рюкзака)
     * Взятие вершин до значения (minPath.size() + 1) / 2 - 1. Получения расстояния между ними.
     * Пример: [1, 3, 5, 4, 6, 7, 2] -> [1, 3, 5] -> [2, 2]
     */
    private ArrayList<Integer> getMinPathFromKp() {
        ArrayList<Integer> minPathFromKp = new ArrayList<>();
        for (int i = 0; i < (minPath.size() + 1) / 2; i++) {
            int value = minPath.get(i + 1) - minPath.get(i);
            if (value > 0) {
                minPathFromKp.add(value);
            }
            else break;
        }
        return minPathFromKp;
    }
}
