package framework.kp;

import framework.pathproblem.tsp.Tsp;
import framework.provider.LoggerProvider;

import java.util.ArrayList;

import static framework.utils.IntegerDataUtils.*;

/**
 * Класс сведения задачи о рюкзаке к другой возможной задаче
 */
class KpToProblem extends LoggerProvider {
    private final Kp kp;

    /**
     * Конструктор создания класса по переданному классу задачи о рюкзаке
     * @param kp - объект задачи о рюкзаке
     */
    protected KpToProblem(Kp kp) {
        super(KpToProblem.class);
        this.kp = kp;
    }

    /**
     * <b>Алгоритм сведения задачи о рюкзаке к задаче коммивояжера</b>
     * <p>
     * Шаги алгоритма:
     * <p>
     * n = <максимальный_вес_рюкзака>
     * <p>
     * Шаг 1. Создать объект задачи о коммивояжере размером (2n - 1);
     * <p>
     * Шаг 2. Добавить ребра на отрезке вершин от 0 до (n + 1) с длиной ребра
     * (разность значения конечной вершины и начальной) равной весу вещи и ценностью ребра равной квадрату веса вещи
     * деленного на ценность вещи
     * <p>
     * Шаг 3. Для вершин от (n + 1) вершины до конечной добавить "треугольные ребра" нулевого веса.
     * Текущая вершина a соединяется ребром с последующей вершиной b (последняя соединяется с нулевой вершиной)
     * и с вершиной с, рассчитанной по формуле (2n + 1 - a)
     * Вершина c соединяется ребром с вершиной b.
     * @return объект задачи о коммивояжере
     */
    protected Tsp toTsp() {
        logInfo("Создание объекта задачи коммивояжера через сведение задачи о рюкзаке");
        int countVertexes = 2 * kp.getMaxWeight() - 1;
        ArrayList<ArrayList<Double>> paths = createInfPath(countVertexes);
        int index = 1;
        while (kp.getThings().size() != index) {
            if(kp.getThings().get(index).weight() == kp.getThings().get(index - 1).weight()) {
                if (kp.getThings().get(index).cost() > kp.getThings().get(index - 1).cost()) {
                    kp.getThings().remove(index - 1);
                }
                else {
                    kp.getThings().remove(index);
                }
            }
            else {
                index++;
            }
        }

        for(int j = 0; j < kp.getMaxWeight(); j++) {
            for(int i = 0; i < kp.getThings().size(); i++) {
                if(j + kp.getWeightThing(i) < kp.getMaxWeight() + 1) {
                    paths
                        .get(j)
                        .set(j + kp.getWeightThing(i), (kp.getWeightThing(i) * kp.getWeightThing(i) / kp.getCostThing(i)));
                }
            }
        }
        for(int j = kp.getMaxWeight(); j < countVertexes; j++) {
            paths.get(j).set(countVertexes - j, ZERO_DOUBLE);
            if(j == countVertexes - 1) paths.get(j).set(ZERO, ZERO_DOUBLE);
            else paths.get(j).set(j + 1, ZERO_DOUBLE);
        }
        for(int j = kp.getMaxWeight() - 1; j > 0; j--) {
            if(j == 1) paths.get(j).set(ZERO, ZERO_DOUBLE);
            else paths.get(j).set(2 * kp.getMaxWeight() - j, ZERO_DOUBLE);
        }
        return Tsp.byValues(countVertexes, paths);
    }

    /**
     * Создание пути с бесконечными значениями
     */
    private static ArrayList<ArrayList<Double>> createInfPath(int countVertexes) {
        ArrayList<ArrayList<Double>> paths = new ArrayList<>();
        for(int i = 0; i < countVertexes; i++) {
            ArrayList<Double> strPaths = new ArrayList<>();
            for(int j = 0; j < countVertexes; j++) {
                strPaths.add(INF);
            }
            paths.add(strPaths);
        }
        return paths;
    }
}
