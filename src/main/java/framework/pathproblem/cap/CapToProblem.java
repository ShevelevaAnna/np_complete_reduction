package framework.pathproblem.cap;

import framework.pathproblem.tsp.Tsp;
import framework.provider.LoggerProvider;

import java.util.ArrayList;
import static framework.utils.IntegerDataUtils.*;

/**
 * Класс сведения двухуровневой задачи о назначениях к другой возможной задаче
 */
class CapToProblem extends LoggerProvider {
    private final Cap cap;

    /**
     * Конструктор создания класса по переданному классу двухуровневой задачи о назначениях
     * @param cap - объект двухуровневой задачи о назначениях
     */
    protected CapToProblem(Cap cap) {
        super(CapToProblem.class);
        this.cap = cap;
    }

    /**
     * <b>Алгоритм сведения двухуровневой задачи о назначениях к задаче коммивояжера</b>
     * <p>
     * Шаги алгоритма:
     * <p>
     * n = <размер матрицы стоимости/зарплаты>
     * <p>
     * Шаг 1. Создать объект задачи о коммивояжере размером (2n);
     * <p>
     * Шаг 2. Добавить ребра из каждой вершины [0, n] к каждой вершине [n+1, 2n] с весом (зарплата - 0.1 * стоимости)
     * <p>
     * Шаг 3. Добавить ребра из каждой вершины [n+1, 2n] к параллельной вершине [0, n] с весом 0
     * @return объект задачи о коммивояжере
     */
    protected Tsp toTsp() {
        logInfo("Создание объекта задачи о коммивояжере через сведение двухуровневой задачи о назначениях");
        ArrayList<ArrayList<Double>> paths = new ArrayList<>();
        int countVertex = cap.getSizeProblem();

        for(int i = 0; i < 2 * countVertex; ++i) {
            ArrayList<Double> strToTSP = new ArrayList<>(countVertex);
            if (i < countVertex) {
                for (int j = 0; j < 2 * countVertex; ++j) {
                    if (j < countVertex) {
                        strToTSP.add(INF);
                    } else {
                        strToTSP.add(getCell(cap.getSalaries(), i, j) - 0.1 / getCell(cap.getCosts(), i, j));
                    }
                }
            } else {
                for (int j = 0; j < 2 * countVertex; ++j) {
                    if (j == i - countVertex) {
                        strToTSP.add(ZERO_DOUBLE);
                    } else {
                        strToTSP.add(INF);
                    }
                }
            }
            paths.add(strToTSP);
        }
        return Tsp.byValues(2 * countVertex, paths);
    }

    private double getCell(ArrayList<ArrayList<Double>> matrix, int i, int j) {
        return matrix.get(i).get(j - matrix.size());
    }
}
