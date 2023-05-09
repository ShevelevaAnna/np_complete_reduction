package framework.utils;

import java.util.ArrayList;

import static framework.utils.IntegerDataUtils.INF;

/**
 * Класс работы со списками
 */
public class ArrayListUtils {
    /**
     * Привести числовые значения переданного списка к типу Double
     * @param list - список числовых значений
     */
    public static <T extends Number> ArrayList<ArrayList<Double>> toDouble(ArrayList<ArrayList<T>> list) {
        ArrayList<ArrayList<Double>> doubleList = new ArrayList<>();
        for (ArrayList<T> lineList: list) {
            ArrayList<Double> doubleLineList = new ArrayList<>();
            for (T cellList: lineList) {
                doubleLineList.add((Double) cellList);
            }
            doubleList.add(doubleLineList);
        }
        return doubleList;
    }

    /**
     * Получить значение суммы чисел переданного списка, не равных бесконечности
     * @param list - список числовых значений
     */
    public static double getSumNotInf(ArrayList<ArrayList<Double>> list) {
        double sum = 0;
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                if (list.get(i).get(j) != INF) {
                    sum += list.get(i).get(j);
                }
            }
        }
        return sum;
    }
}
