package framework.algorithm;

import framework.kp.KpThing;
import framework.provider.LoggerProvider;
import framework.utils.ArrayListUtils;
import framework.utils.WriteDataUtils;

import java.util.ArrayList;
import java.util.List;

import static framework.utils.IntegerDataUtils.*;
import static framework.utils.WriteDataUtils.*;

/**
 * Жадный алгоритм для поиска кратчайший пути в графе
 */
public class GreedyAlgorithm extends LoggerProvider {
    /**
     * Время выполнения задачи
     */
    protected long time;

    /**
     * Матрица графа
     */
    private ArrayList<ArrayList<Double>> matrix;
    private List<KpThing> matrixKp;

    /**
     * Кратчайший путь
     */
    private final ArrayList<Integer> minPath;

    /**
     * Минимальный вес пути
     */
    private double minWeight;

    /**
     * Конструктор жадного алгоритма
     * @param matrix - матрица графа
     * @param <T>    - значения матрицы (Double, Integer)
     */
    public <T extends Number> GreedyAlgorithm(ArrayList<ArrayList<T>> matrix) {
        super(GreedyAlgorithm.class);
        this.matrix = ArrayListUtils.toDouble(matrix);
        minPath = new ArrayList<>();
        minPath.add(ZERO);
        minWeight = ZERO;
    }

    /**
     * Конструктор жадного алгоритма
     */
    public GreedyAlgorithm(List<KpThing> matrixKp) {
        super(GreedyAlgorithm.class);
        this.matrixKp = matrixKp;
        minPath = new ArrayList<>();
        minPath.add(ZERO);
        minWeight = ZERO;
    }

    /**
     * @return кратчайшего пути
     */
    public ArrayList<Integer> getMinPath() {
        return minPath;
    }

    /**
     * @return минимального веса пути
     */
    public double getMinWeight() {
        return minWeight;
    }

    /**
     * Поиск кратчайшего пути жадным алгоритмом
     */
    public void findKpPath(int maxWeight) {
        logInfo("Поиск жадного алгоритма для переданной матрицы");
        long startTime = System.nanoTime();
        ArrayList<Integer> openVertex = new ArrayList<>();
        ArrayList<Integer> closeVertex = new ArrayList<>();
        ArrayList<String> result = new ArrayList<>();
        double maxVertex = 0;
        double maxCost = 0;
        for(int i = 0; i < matrixKp.size(); i++) {
            openVertex.add(i);
        }

        while(minWeight < maxWeight) {
            int  minVertexIndex = matrixKp.size();
            maxVertex = 0;
            for (int i = 0; i < openVertex.size(); i++) {
                if (matrixKp.get(openVertex.get(i)).cost() / matrixKp.get(openVertex.get(i)).weight() >= maxVertex &&
                        matrixKp.get(openVertex.get(i)).count() != 0 &&
                        matrixKp.get(openVertex.get(i)).weight() + minWeight <= maxWeight
                ) {
                    maxVertex = matrixKp.get(openVertex.get(i)).cost() / matrixKp.get(openVertex.get(i)).weight();
                    minVertexIndex = i;
                }
            }
            if (minVertexIndex == matrixKp.size()) break;
            minWeight += matrixKp.get(openVertex.get(minVertexIndex)).weight();
            maxCost += matrixKp.get(openVertex.get(minVertexIndex)).cost();
            closeVertex.add(openVertex.get(minVertexIndex));
            result.add(matrixKp.get(openVertex.get(minVertexIndex)).number());
        }
        time = System.nanoTime() - startTime;

        String resultKp = String.format("Результат рюкзака: %s", result);
        String costKp = String.format("Максимальная стоимость: %f", maxCost);
        String timeResult = String.format(TIME, time/NANO);
        logInfo(timeResult);
        logInfo(resultKp);
        logInfo(costKp);

        String path = String.format(getFilePath() + PATH, "GreedyKp", "Heuristic", getPathId());
        write(path, timeResult + ENTER, resultKp + ENTER, costKp + DOUBLE_ENTER);
    }

    /**
     * Поиск кратчайшего пути жадным алгоритмом
     */
    public void findPath(int size) {
        logInfo("Поиск жадного алгоритма для переданной матрицы");
        long startTime = System.nanoTime();
        ArrayList<Integer> openVertex = new ArrayList<>();
        ArrayList<Integer> closeVertex = new ArrayList<>();
        ArrayList<ArrayList<Double>> matrix = new ArrayList<>();
        ArrayList<Double> matrixLine;
        for(int i = 0; i < size; i++) {
            matrixLine = new ArrayList<>();
            for(int j = 0; j < size; j++) {
                matrixLine.add(this.matrix.get(i).get(j));
            }
            matrix.add(matrixLine);
        }

        double minVertex = INF;
        for(int i = 0; i < matrix.size(); i++) {
            for(int j = 0; j < matrix.size(); j++) {
                if(matrix.get(i).get(j) < minVertex && matrix.get(i).get(j) != ZERO_DOUBLE) {
                    closeVertex = new ArrayList<>();
                    closeVertex.add(i);
                    closeVertex.add(j);
                    minWeight = matrix.get(i).get(j);
                    minVertex = matrix.get(i).get(j);
                }
            }
        }
        for(int i = 0; i < matrix.size(); i++) {
            if(closeVertex.get(0) != i && closeVertex.get(1) != i) {
                openVertex.add(i);
            }
        }
        while(openVertex.size() != 0) {
            int firstVertex = closeVertex.get(0);
            int lastVertex = closeVertex.get(closeVertex.size() - 1);
            int minVertex2 = matrix.size();
            int minVertexIndex = matrix.size();
            int openVertexCurrent = firstVertex;
            minVertex = INF;
            for (int i = 0; i < openVertex.size(); i++) {
                if(matrix.get(openVertex.get(i)).get(firstVertex) <= minVertex) {
                    minVertex2 = openVertex.get(i);
                    minVertexIndex = i;
                    minVertex = matrix.get(openVertex.get(i)).get(firstVertex);
                    openVertexCurrent = firstVertex;
                }
                if(matrix.get(lastVertex).get(openVertex.get(i)) <= minVertex) {
                    minVertex2 = openVertex.get(i);
                    minVertexIndex = i;
                    minVertex = matrix.get(lastVertex).get(openVertex.get(i));
                    openVertexCurrent = lastVertex;
                }
            }
            if (minVertex2 == matrix.size()) break;
            minWeight += minVertex;
            if (openVertexCurrent == firstVertex) {
                closeVertex.add(0, minVertex2);
            }
            else {
                closeVertex.add(minVertex2);
            }
            openVertex.remove(minVertexIndex);
        }
        minWeight += matrix.get(closeVertex.get(closeVertex.size() - 1)).get(closeVertex.get(0));
        minPath.addAll(closeVertex);
        time = System.nanoTime() - startTime;

        String resultTsp = String.format("Кратчайший путь: %s", minPath);
        String weightTsp = String.format(WriteDataUtils.WEIGHT, minWeight);
        String timeResult = String.format(TIME, time/NANO);
        String sizeTsp = String.format("Size = %d", size);
        logInfo(timeResult);
        logInfo(resultTsp);
        logInfo(weightTsp);

        String path = String.format(getFilePath() + PATH, "GreedyTsp", "Heuristic", getPathId());
        write(path, timeResult + ENTER, sizeTsp + ENTER, resultTsp + ENTER, weightTsp + DOUBLE_ENTER);
    }

    /**
     * Поиск кратчайшего пути жадным алгоритмом
     */
    public void findKpFragilePath(int maxWeight, List<Integer> kpFragile) {
        logInfo("Поиск жадного алгоритма для переданной матрицы");
        long startTime = System.nanoTime();
        ArrayList<Integer> openVertex = new ArrayList<>();
        ArrayList<Integer> closeVertex = new ArrayList<>();
        ArrayList<String> result = new ArrayList<>();
        List<Integer> fragile = new ArrayList<>(kpFragile);
        double maxVertex;
        double maxCost = 0;
        for(int i = 0; i < matrixKp.size(); i++) {
            openVertex.add(i);
        }
        int finishNumberVertex = 0;
        int flag = 0;
        while(minWeight < maxWeight && flag < fragile.size()) {
            int  minVertexIndex = matrixKp.size();
            maxVertex = 0;
            for (int i = 0; i < openVertex.size(); i++) {
                if (getFinishNumber(openVertex.get(i)) != 0 && flag == fragile.size() - 1) continue;
                if (getFinishNumber(openVertex.get(i)) == 0 && flag < fragile.size() - 1) continue;
                if (fragile.get(getFinishNumber(openVertex.get(i))) == 2 && flag < fragile.size() - 1) continue;
                int startNumberNext = getStartNumber(openVertex.get(i));
                if (finishNumberVertex == startNumberNext) {
                    if (matrixKp.get(openVertex.get(i)).cost() / matrixKp.get(openVertex.get(i)).weight() >= maxVertex &&
                            matrixKp.get(openVertex.get(i)).count() != 0 &&
                            matrixKp.get(openVertex.get(i)).weight() + minWeight <= maxWeight
                    ) {
                        maxVertex = matrixKp.get(openVertex.get(i)).cost() / matrixKp.get(openVertex.get(i)).weight();
                        minVertexIndex = i;
                    }
                }
            }
            if (minVertexIndex == matrixKp.size()) break;
            minWeight += matrixKp.get(openVertex.get(minVertexIndex)).weight();
            maxCost += matrixKp.get(openVertex.get(minVertexIndex)).cost();
            setFragile(fragile, getStartNumber(openVertex.get(minVertexIndex)), getFinishNumber(openVertex.get(minVertexIndex)));
            closeVertex.add(openVertex.get(minVertexIndex));
            result.add(matrixKp.get(openVertex.get(minVertexIndex)).number());
            if (matrixKp.get(openVertex.get(minVertexIndex)).count() == 0) {
                openVertex.remove(minVertexIndex);
            }
            flag++;
            finishNumberVertex = getFinishNumber(closeVertex.get(closeVertex.size() - 1));
        }
        time = System.nanoTime() - startTime;

        String resultKp = String.format("Результат рюкзака: %s", result);
        String costKp = String.format("Максимальная стоимость: %f", maxCost);
        String timeResult = String.format(TIME, time/NANO);
        logInfo(timeResult);
        logInfo(resultKp);
        logInfo(costKp);

        String path = String.format(getFilePath() + PATH, "GreedyKp", "Heuristic", getPathId());
        write(path, timeResult + ENTER, resultKp + ENTER, costKp + DOUBLE_ENTER);
    }

    private void setFragile(List<Integer> fragile, int startNumber, int finishNumber) {
        fragile.set(startNumber, fragile.get(startNumber) + 2);
        fragile.set(finishNumber, fragile.get(finishNumber) - 1);
    }

    private int getStartNumber(int i) {
        return Integer.parseInt(matrixKp.get(i).number().split("_")[0]);
    }

    private int getFinishNumber(int i) {
        int indexNext = matrixKp.get(i).number().indexOf("_");
        return Integer.parseInt(matrixKp.get(i).number().substring(indexNext + 1));
    }

    /**
     * Получить вес ребра из списка кратчайшего пути
     */
    private double getWeightEdgeMinPath(int endVertex) {
        return matrix.get(minPath.get(minPath.size() - 1)).get(endVertex);
    }
}
