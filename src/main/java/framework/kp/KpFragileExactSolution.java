package framework.kp;

import java.util.ArrayList;
import java.util.List;

import static framework.utils.IntegerDataUtils.ZERO;

/**
 * Класс точного решения задачи о рюкзаке с динамической матрицей хрупкости
 * <p>
 * Работа динамической матрицы хрупкости: [-1, 1, 1, ...]
 * <p>
 * Начальное положение:
 * <p>
 * Изменение матрицы при номере вещи i_j: [..., i, ..., j, ...], где
 * i = i + 1 (будет равняться 2),
 * j = j - 2 (будет равняться 0)
 * <p>
 * Финальное положение: [0, 2, 2, ...] (если путь существует)
 */
class KpFragileExactSolution extends KpExactSolutionAbstract {

    /**
     * Конструктор создания класса
     * @param kp - объект класса задачи о рюкзаке
     */
    protected KpFragileExactSolution(Kp kp) {
        super(kp, KpFragileExactSolution.class);
    }

    /**
     * Точное решение задачи о рюкзаке после сведения из задачи коммивояжера
     */
    protected void solveTspToKp() {
        solveWithWrite();
    }

    /**
     * Нахождение точного решения через рекурсию
     */
    protected void exactSolution() {
        for (int i = ZERO; i < kp.getNumberOfThings(); i++) {
            int startI = getStartNumber(i);
            if(startI == 0) {
                currentResult = new ArrayList<>();
                List<Integer> fragile = new ArrayList<>();
                fragile.add(-1);
                for (int j = 1; j < kp.getMaxWeight(); j++) {
                    fragile.add(1);
                }
                kp.setFragile(fragile);
                next(i, ZERO, ZERO);
            }
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
        setFragile(getStartNumber(currentThing), 2, getFinishNumber(currentThing), -1);
        if (ifReturnFragile(currentThing)) return;
        if (ifReturn(currentThing, currentMaxWeight)) return;
        if (currentMaxCost > maxCost) {
            if (ifResultFragile()) {
                setNewResult(currentMaxWeight, currentMaxCost);
            }
        }
        if (currentMaxWeight < kp.getMaxWeight()) {
            for (int i = 0; i < kp.getNumberOfThings(); i++) {
                int startNumberNext = getStartNumber(i);
                if (getFinishNumber(currentThing) == startNumberNext) {
                    next(i, currentMaxWeight, currentMaxCost);
                    setThing(i, kp.getCountThing(i) + 1);
                    currentResult.remove(currentResult.size() - 1);
                    setFragile(startNumberNext, -2, getFinishNumber(i), 1);
                }
            }
        }
    }

    /**
     * Условие записи результата по динамической матрице в рекурсии
     */
    private boolean ifResultFragile() {
        int flagSecond = 0;
        for (int i = 0; i < kp.getFragile().size(); i++) {
            if (kp.getFragile().get(i) == 2) {
                flagSecond += 1;
            }
        }
        return kp.getFragile().get(0) == 0 && flagSecond == kp.getFragile().size() - 1;
    }

    /**
     * Условие возврата рекурсии при выходе за рамки в динамической матрице
     * @param currentThing - текущая вещь
     */
    private boolean ifReturnFragile(int currentThing) {
        return kp.getFragile().get(getStartNumber(currentThing)) > 2 &&
                kp.getFragile().get(getFinishNumber(currentThing)) < 0;
    }

    /**
     * Установить новое значение динамической матрицы
     * @param startNumber  - стартовое значение в номере вещи
     * @param shiftStart   - сдвиг по стартовому значению
     * @param finishNumber - конечное значение в номере вещи
     * @param shiftFinish  - сдвиг по конечному значению
     */
    private void setFragile(int startNumber, int shiftStart, int finishNumber, int shiftFinish) {
        kp.getFragile().set(startNumber, kp.getFragile().get(startNumber) + shiftStart);
        kp.getFragile().set(finishNumber, kp.getFragile().get(finishNumber) + shiftFinish);
    }

    /**
     * Получить стартовый номер вещи
     * @param i - интдекс вещи
     */
    private int getStartNumber(int i) {
        return Integer.parseInt(kp.getNumberThing(i).split("_")[0]);
    }

    /**
     * Получить конечный номер вещи
     * @param i - интдекс вещи
     */
    private int getFinishNumber(int i) {
        int indexNext = kp.getThings().get(i).number().indexOf("_");
        return Integer.parseInt(kp.getNumberThing(i).substring(indexNext + 1));
    }
}
