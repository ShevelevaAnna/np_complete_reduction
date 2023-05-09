package framework.kp;

import java.util.ArrayList;

import static framework.utils.IntegerDataUtils.*;
import static framework.utils.WriteDataUtils.*;

/**
 * Класс точного решения стандартной задачи о рюкзаке
 */
class KpExactSolution extends KpExactSolutionAbstract {
    /**
     * Конструктор создания класса
     * @param kp - объект класса задачи о рюкзаке
     */
    protected KpExactSolution(Kp kp) {
        super(kp, KpExactSolution.class);
    }

    /**
     * Точное решение задачи
     */
    protected void solve() {
        super.solve();
    }

    /**
     * Точное решение задачи c выводом результатов в файл
     */
    protected void solveWithWrite() {
        super.solveWithWrite();
    }

    /**
     * Точное решение задачи при сведении к задаче коммивояжера
     */
    protected void solveKpToTsp() {
        solveWithWrite();
        writeResultToTsp();
    }

    /**
     * Нахождение точного решения через рекурсию
     */
    protected void exactSolution() {
        for (int i = ZERO; i < kp.getNumberOfThings(); i++) {
            currentResult = new ArrayList<>();
            next(i, ZERO, ZERO);
        }
    }

    /**
     * Рекурсивный метод получения следующей вещи результирующего списка вещей
     * @param currentThing     - текущая вещь
     * @param currentMaxWeight - текущий вес всех вещей из результирующего списка
     * @param currentMaxCost   - текущая стоимость всех вещей из результирующего списка
     */
    private void next(int currentThing, int currentMaxWeight, double currentMaxCost) {
        currentMaxCost += kp.getCostThing(currentThing);
        currentMaxWeight += kp.getWeightThing(currentThing);
        setThing(currentThing, kp.getCountThing(currentThing) - 1);
        currentResult.add(kp.getThing(currentThing));
        if (ifReturn(currentThing, currentMaxWeight)) return;
        if (currentMaxCost > maxCost) {
            setNewResult(currentMaxWeight, currentMaxCost);
        }
        if (currentMaxWeight < kp.getMaxWeight()) {
            for (int i = currentThing; i < kp.getNumberOfThings(); i++) {
                next(i, currentMaxWeight, currentMaxCost);
                setThing(i, kp.getCountThing(i) + 1);
                currentResult.remove(currentResult.size() - 1);
            }
        }
    }

    /**
     * Записать результат задачи в файл в виде задачи о коммивояжере (при сведении к рюкзаку)
     */
    private void writeResultToTsp() {
        String path = String.format(getFilePath() + PATH, Kp.PROBLEM, EXACT_SOLUTION, getPathId());
        logInfo("Запись решения задачи о рюкзаке в виде решения задачи коммивояжера в файл %s", path);
        if (result != null) {
            double costNew = getCostNew();
            String weightAndCostText = "Максимальная ценность при пересчете на задачу о коммивояжере = %f" + DOUBLE_ENTER;
            write(path, String.format(weightAndCostText, costNew));
        }
        else {
            write(path, NO_PATH + DOUBLE_ENTER);
        }
    }

    /**
     * Получить минимальную стоимость пути задачи коммивояжера через полученную стоимость
     */
    private double getCostNew() {
        double costNew = 0;
        for (KpThing kpThing : result) {
            costNew += kpThing.weight() * kpThing.weight() / kpThing.cost();
        }
        return costNew;
    }
}
