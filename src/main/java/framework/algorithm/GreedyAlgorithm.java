package framework.algorithm;

import framework.utils.ArrayListUtils;
import framework.provider.LoggerProvider;
import framework.utils.WriteDataUtils;

import java.util.ArrayList;

import static framework.utils.IntegerDataUtils.INF;
import static framework.utils.IntegerDataUtils.ZERO;

/**
 * Жадный алгоритм для поиска кратчайший пути в графе
 */
public class GreedyAlgorithm extends LoggerProvider {
    /**
     * Матрица графа
     */
    private final ArrayList<ArrayList<Double>> matrix;

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
    public void findPath() {
        logInfo("Поиск жадного алгоритма для переданной матрицы");
        ArrayList<Integer> openVertex = new ArrayList<>();
        int currentVertex = ZERO;
        for(int i = 1; i < matrix.size(); i++) {
            openVertex.add(i);
        }
        for(int i = 0; i < matrix.size() - 1; i++) {
            double currentMinVertex = INF;
            for(int j = 0; j < openVertex.size(); j++) {
                if (getWeightEdgeMinPath(openVertex.get(j)) <= currentMinVertex) {
                    currentVertex = j;
                    currentMinVertex = getWeightEdgeMinPath(openVertex.get(j));
                }
            }
            minPath.add(openVertex.get(currentVertex));
            openVertex.remove(currentVertex);
            minWeight += currentMinVertex;
        }
        minWeight += getWeightEdgeMinPath(ZERO);
        logInfo("Кратчайший путь: %s", minPath.toString());
        logInfo(WriteDataUtils.WEIGHT, minWeight);
    }

    /**
     * Получить вес ребра из списка кратчайшего пути
     */
    private double getWeightEdgeMinPath(int endVertex) {
        return matrix.get(minPath.get(minPath.size() - 1)).get(endVertex);
    }
}
