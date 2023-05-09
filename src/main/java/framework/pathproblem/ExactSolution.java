package framework.pathproblem;

import framework.algorithm.GreedyAlgorithm;
import framework.provider.LoggerProvider;

import java.util.ArrayList;
import java.util.function.Supplier;

import static framework.utils.IntegerDataUtils.*;
import static framework.utils.WriteDataUtils.TIME;

/**
 * Вспомогательный класс нахождения решения задачи с минимальным путем в матрице nxn
 */
public abstract class ExactSolution extends LoggerProvider {
    /**
     * Матрица, где ищется минимальный путь
     */
    protected final ArrayList<ArrayList<Double>> matrix;

    /**
     * Матрица всех открытых вершин
     */
    protected ArrayList<Integer> openVertex;

    /**
     * Матрица всех закрытых вершин
     */
    protected ArrayList<Integer> closeVertex;

    /**
     * Найденный минимальный путь
     */
    protected ArrayList<Integer> minPath;

    /**
     * Все минимальные пути одной стоимости
     */
    protected ArrayList<ArrayList<Integer>> allMinPath;

    /**
     * Минимальный путь с учетом зависимой матрицы
     */
    protected ArrayList<Integer> subMinPath;

    /**
     * Минимальная стоимость пути
     */
    protected double minCost;

    /**
     * Время работы алгоритма
     */
    protected long time;

    /**
     * Конструктор класса решения
     */
    protected <T extends LoggerProvider> ExactSolution(ArrayList<ArrayList<Double>> matrix, Class<T> objectClass) {
        super(objectClass);
        this.matrix = matrix;
        minPath = new ArrayList<>(ZERO);
        allMinPath = new ArrayList<>();
        subMinPath = new ArrayList<>();
        minCost = ZERO;
    }

    /**
     * Нахождение точного пути без дополнительной матрицы с 1 начальной вершиной
     * @param problem - название решаемой проблемы
     */
    protected void solve(String problem) {
        solve(ZERO, problem, () -> null, () -> null, () -> null);
    }

    /**
     * Нахождение точного пути с дополнительной матрицей и несколькими начальными вершинами
     * Жадным алгоритмом ищем границу стоимости
     * @param maxStartVertex        - максималльно возможная стартовая вершина
     * @param problem               - название решаемой проблемы
     * @param solveSubProblem       - нахождение минимального пути с учетом зависимой матрицы
     * @param noEqualFinishSubSolve - действия при нахождении пути при равенстве текущего минимального пути и
     *                                сохраненного минимального пути
     * @param equalFinishSubSolve   - действия при нахождении пути, когда текущий минимальный путь меньше
     *                                сохраненного минимального пути
     */
    protected void solve(
        int maxStartVertex,
        String problem,
        Supplier<ArrayList<Integer>> solveSubProblem,
        Supplier<ArrayList<ArrayList<Integer>>> noEqualFinishSubSolve,
        Supplier<ArrayList<ArrayList<Integer>>> equalFinishSubSolve
    ) {
        logInfo("Точное решение " + problem);
        GreedyAlgorithm greedyAlgorithm = new GreedyAlgorithm(matrix);
        greedyAlgorithm.findPath();
        minPath = greedyAlgorithm.getMinPath();
        allMinPath = new ArrayList<>();
        allMinPath.add(minPath);
        minCost = greedyAlgorithm.getMinWeight();
        long startTime = System.currentTimeMillis();
        initSolve(maxStartVertex, noEqualFinishSubSolve, equalFinishSubSolve);
        solveSubProblem.get();
        time = System.currentTimeMillis() - startTime;
        logInfo(TIME, time/NANO);
    }

    /**
     * Заполнение начальных данных для старта рекурсивного алгоритма
     */
    private void initSolve(
        int maxStartVertex,
        Supplier<ArrayList<ArrayList<Integer>>> noEqualFinishSubSolve,
        Supplier<ArrayList<ArrayList<Integer>>> equalFinishSubSolve
    ) {
        for (int i = 0; i <= maxStartVertex; i++) {
            closeVertex = new ArrayList<>();
            openVertex = new ArrayList<>();
            closeVertex.add(i);
            for(int j = 0; j < matrix.size(); ++j) {
                if (j != i) {
                    openVertex.add(j);
                }
            }
            exactAlgorithm(ZERO, noEqualFinishSubSolve, equalFinishSubSolve);
        }
    }

    /**
     * Рекурсивный алгоритм точного решения
     */
    private void exactAlgorithm(
        double minCurrentCount,
        Supplier<ArrayList<ArrayList<Integer>>> noEqualFinishSubSolve,
        Supplier<ArrayList<ArrayList<Integer>>> equalFinishSubSolve
    ) {
        if (openVertex.size() == ZERO) {
            double currentCount = getWeightEdgeClose(ZERO);
            if (minCurrentCount + currentCount <= minCost) {
                minCost = minCurrentCount + currentCount;
                minPath = new ArrayList<>();
                minPath.addAll(closeVertex);
                if (minCurrentCount + currentCount == minCost) {
                    equalFinishSubSolve.get();
                }
                else {
                    noEqualFinishSubSolve.get();
                }
            }
        }
        for(int i = 0; i < openVertex.size() && minCurrentCount < minCost; ++i) {
            double currentCount = getWeightEdgeClose(openVertex.get(i));
            if(currentCount == INF) {
                continue;
            }
            minCurrentCount += currentCount;
            closeVertex.add(openVertex.get(i));
            openVertex.remove(i);
            exactAlgorithm(minCurrentCount, noEqualFinishSubSolve, equalFinishSubSolve);
            minCurrentCount -= currentCount;
            openVertex.add(i, closeVertex.get(getLastCloseId()));
            closeVertex.remove(getLastCloseId());
        }
    }

    /**
     * Получить вес ребра последней вершины в закрытом списке
     */
    private double getWeightEdgeClose(int endVertex) {
        return matrix.get(closeVertex.get(getLastCloseId())).get(endVertex);
    }

    /**
     * Получить индекс последней вершины в закрытом списке
     */
    private int getLastCloseId() {
        return closeVertex.size() - 1;
    }
}
