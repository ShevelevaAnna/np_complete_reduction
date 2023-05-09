package framework.pathproblem.cap;

import framework.exception.ProblemException;
import framework.exception.NumberException;
import framework.pathproblem.tsp.Tsp;
import framework.provider.LoggerProvider;
import framework.utils.RandomValueUtils;
import framework.utils.ReadDataUtils;

import java.util.ArrayList;
import java.util.List;

import static framework.utils.WriteDataUtils.*;

/**
 * Класс двухуровневой задачи о назначениях
 * <p>
 * Существует n задач и n работников.
 * Каждый работник получает определенную зарплату за определенную задачу.
 * Каждая задача имеет определенную стоимость при выполнении ее определенным работником.
 * <p>
 * Задача верхнего уровня состоит в минимизации стоимости задачи
 * <p>
 * Задача нижнего уровня состоит в минимизации заработной платы работников
 */
public class Cap extends LoggerProvider {
    protected static final String PROBLEM = "cap2";

    /**
     * Матрица стоимостей всех задач
     */
    private final ArrayList<ArrayList<Double>> costs;

    /**
     * Матрица зарплат работников по всем задачам
     */
    private final ArrayList<ArrayList<Double>> salaries;

    /**
     * Размер матриц задачи
     */
    private final int sizeProblem;

    /**
     * Создание объекта по переданным данным
     * @param costs    - стоимость заказа
     * @param salaries - зарплата работников
     */
    private Cap(ArrayList<ArrayList<Double>> costs, ArrayList<ArrayList<Double>> salaries) {
        super(Cap.class);
        logInfo("Заполнение переданными значениями объекта двухуровневой задачи о назначениях");
        this.costs = costs;
        this.salaries = salaries;
        sizeProblem = costs.size();
    }

    /**
     * Создание объекта с рандомными значениями в промежутке переданных значений максимума и минимума
     * @param sizeProblem - размер задачи (матриц)
     * @param minCost     - минимальная возможная стоимость
     * @param maxCost     - максимальная возможная стоимость
     * @param minSalary   - минимальная возможная зарплата
     * @param maxSalary   - максимальная возможная зарплата
     * @param isInteger   - целые/дробные числа (генерация)
     */
    private Cap(int sizeProblem, int minCost, int maxCost, int minSalary, int maxSalary, boolean isInteger) {
        super(Cap.class);
        logInfo("Заполнение рандомными значениями в промежутке переданных значений максимума и минимума " +
                "объекта двухуровневой задачи о назначениях");
        costs = new ArrayList<>();
        salaries = new ArrayList<>();
        this.sizeProblem = sizeProblem;

        for(int i = 0; i < sizeProblem; i++) {
            ArrayList<Double> cost = new ArrayList<>();
            ArrayList<Double> salary = new ArrayList<>();
            for(int j = 0; j < sizeProblem; j++) {
                double costValue = RandomValueUtils.getDoubleValue(minCost, maxCost);
                double salaryValue = RandomValueUtils.getDoubleValue(minSalary, maxSalary);
                if(isInteger) {
                    cost.add((double)RandomValueUtils.getIntegerValue(minCost, maxCost));
                    salary.add((double)RandomValueUtils.getIntegerValue(minSalary, maxSalary));
                }
                else {
                    cost.add(RandomValueUtils.getDoubleValue(minCost, maxCost));
                    salary.add(RandomValueUtils.getDoubleValue(minSalary, maxSalary));
                }
            }
            costs.add(cost);
            salaries.add(salary);
        }
    }

    /**
     * @return Матрицу стоимости заказа
     */
    public ArrayList<ArrayList<Double>> getCosts() {
        return costs;
    }

    /**
     * @return Матрицу заработной платы
     */
    public ArrayList<ArrayList<Double>> getSalaries() {
        return salaries;
    }

    /**
     * @return общий размер матриц
     */
    public int getSizeProblem() {
        return sizeProblem;
    }

    /**
     * Сведение задачи к задаче коммивояжера
     */
    public Tsp toTsp() {
        CapToProblem capToProblem = new CapToProblem(this);
        return capToProblem.toTsp();
    }

    /**
     * Точное решение задачи
     */
    public void solve() {
        solveProblem();
    }

    /**
     * Точное решение задачи с записью в файл
     */
    public void solveWithWriteFile() {
        CapExactSolution capExactSolution = solveProblem();
        capExactSolution.writeResult();
    }

    /**
     * Точное решение задачи после сведения из задачи коммивояжера
     */
    public void solveTspToCap() {
        CapExactSolution capExactSolution = solveProblem();
        capExactSolution.writeResult();
        capExactSolution.writeTspToCapResult();;
    }

    /**
     * Точное решение задачи
     */
    private CapExactSolution solveProblem() {
        CapExactSolution capExactSolution = new CapExactSolution(this);
        capExactSolution.exactSolution();
        return capExactSolution;
    }

    /**
     * Получение объекта задачи с переданными параметрами
     * @param costs    - стоимость заказа
     * @param salaries - зарплата работников
     */
    public static Cap byValues(
            ArrayList<ArrayList<Double>> costs,
            ArrayList<ArrayList<Double>> salaries
    ) {
        checkingCorrectnessMatrix(salaries.size(), salaries, costs);
        Cap cap = new Cap(costs, salaries);
        cap.writeProblemInFile();
        return cap;
    }

    /**
     * Получение объекта задачи с переданными параметрами
     * @param fileName - стоимость заказа
     */
    public static Cap byFileValues(String fileName) {
        List<List<String>> file = ReadDataUtils.read(fileName);
        ArrayList<ArrayList<Double>> cost = ReadDataUtils.readDoubleMatrix(file, 0);
        ArrayList<ArrayList<Double>> salary = ReadDataUtils.readDoubleMatrix(file, cost.size() + 1);
        return byValues(cost, salary);
    }

    /**
     * Получение объекта задачи с целыми рандомными значениями
     * @param sizeProblem - размер задачи (матриц)
     * @param minCost     - минимальная возможная стоимость
     * @param maxCost     - максимальная возможная стоимость
     * @param minSalary   - минимальная возможная зарплата
     * @param maxSalary   - максимальная возможная зарплата
     */
    public static Cap byRandomIntegerValues(int sizeProblem, int minCost, int maxCost, int minSalary, int maxSalary) {
        checkingCorrectnessOfInput(sizeProblem, minCost, maxCost, minSalary, maxSalary);
        Cap cap = new Cap(sizeProblem, minCost, maxCost, minSalary, maxSalary, true);
        cap.writeProblemInFile();
        return cap;
    }

    /**
     * Получение объекта задачи с рациональными рандомными значениями
     * @param sizeProblem - размер задачи (матриц)
     * @param minCost     - минимальная возможная стоимость
     * @param maxCost     - максимальная возможная стоимость
     * @param minSalary   - минимальная возможная зарплата
     * @param maxSalary   - максимальная возможная зарплата
     */
    public static Cap byRandomDoubleValues(int sizeProblem, int minCost, int maxCost, int minSalary, int maxSalary) {
        checkingCorrectnessOfInput(sizeProblem, minCost, maxCost, minSalary, maxSalary);
        Cap cap = new Cap(sizeProblem, minCost, maxCost, minSalary, maxSalary, false);
        cap.writeProblemInFile();
        return cap;
    }

    /**
     * Запись данных задачи задачи в файл
     */
    private void writeProblemInFile() {
        String path = String.format(getFilePath() + PATH, PROBLEM, DATA, getPathId());
        logInfo("Запись данных двухуровневой задачи о назначениях в файл %s", path);
        writeArray(path, salaries, "Матрица зарплаты");
        writeArray(path, costs, "Матрица стоимости");
    }

    /**
     * Проверка корректности переданных данных для рандомного заполнения
     */
    private static void checkingCorrectnessOfInput(
            int sizeProblem,
            int minCost,
            int maxCost,
            int minSalary,
            int maxSalary
    ) {
        int minValue = 0;
        int minSizeMatrix = 2;
        try {
            if (sizeProblem < minSizeMatrix) {
                throw new NumberException("sizeProblem", "minSizeMatrix", sizeProblem, minSizeMatrix);
            }
            if (minSalary < minValue) {
                throw new NumberException("minSalary", "minValue", minSalary, minValue);
            }
            if (minCost < minValue) {
                throw new NumberException("minCost", "minValue", minCost, minValue);
            }
            if (minCost > maxCost) {
                throw new NumberException("maxCost", "minCost", maxCost, minCost);
            }
            if (minSalary > maxSalary) {
                throw new NumberException("maxSalary", "minSalary", maxSalary, minSalary);
            }
        }
        catch (NumberException ignored) {}
    }

    /**
     * Проверка корректности переданных матриц
     */
    private static void checkingCorrectnessMatrix(
            int sizeMatrix,
            ArrayList<ArrayList<Double>> salary,
            ArrayList<ArrayList<Double>> cost
    ) {
        try {
            if (cost.size() != sizeMatrix) {
                throw new ProblemException(sizeMatrix, cost.size());
            }
            for (int i = 0; i < salary.size(); i++) {
                if (salary.get(i).size() != sizeMatrix) {
                    throw new ProblemException(i, sizeMatrix, salary.get(i).size());
                }
                if (cost.get(i).size() != sizeMatrix) {
                    throw new ProblemException(i, sizeMatrix, cost.get(i).size());
                }
                for (int j = 0; j < salary.get(i).size(); j++) {
                    if (salary.get(i).get(j) < 0) {
                        throw new ProblemException(i, salary.get(i).get(j), "> 0");
                    }
                }
            }
        }
        catch (ProblemException ignored) {}
    }
}
