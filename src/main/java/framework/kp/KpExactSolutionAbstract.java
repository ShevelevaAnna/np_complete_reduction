package framework.kp;

import framework.provider.LoggerProvider;

import java.util.ArrayList;
import java.util.List;

import static framework.utils.IntegerDataUtils.NANO;
import static framework.utils.IntegerDataUtils.ZERO;
import static framework.utils.WriteDataUtils.*;

/**
 * Абстрактный класс общих методов для решения различных задач о рюкзаке
 */
abstract class KpExactSolutionAbstract extends LoggerProvider {
    private static final String WEIGHT_AND_COST_TEXT = "Полученный максимальный вес = %d, максимальная ценность = %f";

    protected final Kp kp;
    /**
     * Текущий результат решения задачи
     */
    protected List<KpThing> currentResult;
    /**
     * Результат решения задачи
     */
    protected List<KpThing> result;
    /**
     * Максимальная стоимость
     */
    protected double maxCost;
    /**
     * Максимальный вес
     */
    protected int maxWeight;
    /**
     * Время выполнения задачи
     */
    protected long time;

    /**
     * Конструктор создания класса
     */
    protected <T extends LoggerProvider> KpExactSolutionAbstract(Kp kp, Class<T> objectClass) {
        super(objectClass);
        this.kp = kp;
    }

    /**
     * Точное решение задачи
     */
    protected void solve() {
        logInfo("Точное решение задачи о рюкзаке");
        long startTime = System.nanoTime();
        maxCost = ZERO;
        maxWeight = kp.getMaxWeight();
        exactSolution();
        time = System.nanoTime() - startTime;
        logInfo(TIME, time/NANO);
        if (result != null) {
            logInfo(RESULT, result.toString());
            logInfo(String.format(WEIGHT_AND_COST_TEXT, maxWeight, maxCost));
        }
        else {
            logInfo(NO_THINGS);
        }
    }

    /**
     * Точное решение задачи с записью в файл
     */
    protected void solveWithWrite() {
        solve();
        writeResult();
    }

    /**
     * Условие возврата рекурсии при выходе за рамки максимального веса или невозможности взять вещь текущего веса
     * @param currentThing     - текущая вещь
     * @param currentMaxWeight - текуший максимальный вес
     */
    protected boolean ifReturn(int currentThing, int currentMaxWeight) {
        if (kp.getCountThing(currentThing) == -1) {
            return true;
        }
        return currentMaxWeight > kp.getMaxWeight();
    }

    /**
     * Установить новое результирующее значение в рекурсии
     * @param currentMaxWeight - текуший максимальный вес
     * @param currentMaxCost   - текущая максимальная стоимость
     */
    protected void setNewResult(int currentMaxWeight, double currentMaxCost) {
        maxCost = currentMaxCost;
        maxWeight = currentMaxWeight;
        result = new ArrayList<>();
        result.addAll(currentResult);
    }

    /**
     * Получить вещь по переданному индексу
     */
    protected void setThing(int thingId, int newCount) {
        kp
            .getThings()
            .set(
                thingId,
                new KpThing(kp.getNumberThing(thingId), kp.getWeightThing(thingId), kp.getCostThing(thingId), newCount)
            );
    }

    /**
     * Записать результат задачи в файл
     */
    protected void writeResult() {
        String path = String.format(getFilePath() + PATH, Kp.PROBLEM, EXACT_SOLUTION, getPathId());
        logInfo("Запись результата точного решения задачи о рюкзаке в файл %s", path);
        if (result != null) {
            write(
                    path,
                    String.format(TIME + ENTER, time/NANO),
                    String.format(WEIGHT_AND_COST_TEXT + ENTER, maxWeight, maxCost),
                    String.format(RESULT + DOUBLE_ENTER, result)
            );
        }
        else {
            write(path, String.format(TIME + ENTER, time/NANO), NO_THINGS + DOUBLE_ENTER);
        }
    }

    /**
     * Нахождение точного решения через рекурсию
     */
    protected abstract void exactSolution();
}
