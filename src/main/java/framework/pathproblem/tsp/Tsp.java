package framework.pathproblem.tsp;

import framework.exception.NumberException;
import framework.exception.ProblemException;
import framework.pathproblem.cap.Cap;
import framework.kp.Kp;
import framework.provider.LoggerProvider;
import framework.utils.RandomValueUtils;
import framework.utils.ReadDataUtils;

import java.util.ArrayList;
import java.util.List;

import static framework.utils.WriteDataUtils.*;
import static framework.utils.IntegerDataUtils.*;

/**
 * Класс задачи о коммивояжере
 * <p>
 * Существует n городов, путь из одного города в другой имеет вес w. Значение w > 0.
 * <p>
 * Задача, найти гамильтонов цикл минимального веса.
 */
public class Tsp extends LoggerProvider {
    protected static final String PROBLEM = "tsp";

    /**
     * Количество вершин в графе (городов)
     */
    private final int countVertexes;

    /**
     * Матрица всех путей графа
     */
    private final ArrayList<ArrayList<Double>> paths;

    /**
     * Создание объекта с рандомными значениями в промежутке переданных значений максимума и минимума
     * @param countVertexes     - количество вершин
     * @param minValue          - минимальное значение веса
     * @param maxValue          - максимальное значение веса
     * @param percentFullMatrix - процент заполненности матрицы (100 - полная матрица)
     * @param isInteger         - целые/дробные числа (генерация)
     */
    private Tsp(int countVertexes, int minValue, int maxValue, int percentFullMatrix, boolean isInteger) {
        super(Tsp.class);
        logInfo(
                String.format(
                        "Заполнение рандомными значениями матрицы объекта задачи о коммивояжере размера %d",
                        countVertexes
                )
        );
        this.countVertexes = countVertexes;
        paths = new ArrayList<>();
        for(int i = 0; i < countVertexes; i++) {
            ArrayList<Double> strPaths = new ArrayList<>();
            for(int j = 0; j < countVertexes; j++) {
                if (i == j) {
                    strPaths.add(INF);
                }
                else {
                    if (RandomValueUtils.getIntegerValue(0, 100) < percentFullMatrix) {
                        if (isInteger) {
                            strPaths.add((double)RandomValueUtils.getIntegerValue(minValue, maxValue));
                        }
                        else {
                            strPaths.add(RandomValueUtils.getDoubleValue(minValue, maxValue));
                        }
                    }
                    else {
                        strPaths.add(INF);
                    }
                }
            }
            paths.add(strPaths);
        }
    }

    /**
     * Создание объекта задачи по переданным данным
     * @param countVertexes - количество вершин
     * @param paths         - матрица пути
     */
    private Tsp(int countVertexes, ArrayList<ArrayList<Double>> paths) {
        super(Tsp.class);
        logInfo(
                String.format(
                        "Заполнение переданными значениями объекта задачи о коммивояжере размера %d",
                        countVertexes
                )
        );
        this.countVertexes = countVertexes;
        this.paths = paths;
    }

    /**
     * @return матрица весов (путей)
     */
    public ArrayList<ArrayList<Double>> getPaths() {
        return paths;
    }

    /**
     * @return количество вершин
     */
    public int getCountVertexes() {
        return countVertexes;
    }

    /**
     * Сведение задачи к двухуровневой задаче о назначениях
     */
    public Cap toCap() {
        TspToProblem tspToProblem = new TspToProblem(this);
        return tspToProblem.toCap();
    }

    /**
     * Сведение задачи к задаче о рюкзаке
     */
    public Kp toKp() {
        TspToProblem tspToProblem = new TspToProblem(this);
        return tspToProblem.toKp();
    }

    /**
     * Точное решение задачи
     */
    public void solve() {
        baseSolve();
    }

    /**
     * Точное решение задачи с записью в файл
     */
    public void solveWithWrite() {
        baseSolveWithWrite();
    }

    /**
     * Точное решение задачи после сведения из задачи о рюкзаке с записью в файл
     */
    public void solveKpToTsp() {
        TspExactSolution tspExactSolution = baseSolveWithWrite();
        tspExactSolution.writeKpToTspResult();
    }

    /**
     * Точное решение задачи после сведения из двухуровневой задачи о назначении с записью в файл
     */
    public void solveCapToTsp() {
        TspExactSolution tspExactSolution = baseSolveWithWrite();
        tspExactSolution.writeCapToTspResult();
    }

    /**
     * Точное решение задачи до сведения к задаче о рюкзаке с записью в файл
     */
    public void solveTspToKp() {
        TspExactSolution tspExactSolution = baseSolveWithWrite();
        tspExactSolution.writeTspToKpResult();
    }

    /**
     * Точное решение задачи о коммивояжере
     */
    private TspExactSolution baseSolve() {
        TspExactSolution tspExactSolution = new TspExactSolution(this);
        tspExactSolution.solve();
        return tspExactSolution;
    }

    /**
     * Точное решение задачи о коммивояжере с записью в файл
     */
    private TspExactSolution baseSolveWithWrite() {
        TspExactSolution tspExactSolution = baseSolve();
        tspExactSolution.writeResult();
        return tspExactSolution;
    }

    /**
     * Получение объекта задачи с переданными значениями
     * @param countVertexes - количество вершин
     * @param paths         - значения матрицы пути
     */
    public static Tsp byValues(int countVertexes, ArrayList<ArrayList<Double>> paths) {
        checkingCorrectnessPath(countVertexes, paths);
        Tsp tsp = new Tsp(countVertexes, paths);
        tsp.writeProblemInFile();
        return tsp;
    }

    /**
     * Получение объекта задачи из файла
     * @param fileName - путь к файлу
     */
    public static Tsp byFileValues(String fileName) {
        List<List<String>> file = ReadDataUtils.read(fileName);
        ArrayList<ArrayList<Double>> paths =  ReadDataUtils.readDoubleMatrix(file, 0);
        return byValues(paths.size(), paths);
    }

    /**
     * Получение объекта задачи с целыми рандомными значениями
     * @param countVertexes     - количество вершин
     * @param minValue          - минимальное значение веса
     * @param maxValue          - максимальное значение веса
     * @param percentFullMatrix - процент заполненности матрицы (100 - полная матрица)
     */
    public static Tsp byRandomIntegerValues(int countVertexes, int minValue, int maxValue, int percentFullMatrix) {
        checkingCorrectnessOfInput(countVertexes, minValue, maxValue, percentFullMatrix);
        Tsp tsp = new Tsp(countVertexes, minValue, maxValue, percentFullMatrix, true);
        tsp.writeProblemInFile();
        return tsp;
    }

    /**
     * Получение объекта задачи с рациональными рандомными значениями
     * @param countVertexes     - количество вершин
     * @param minValue          - минимальное значение веса
     * @param maxValue          - максимальное значение веса
     * @param percentFullMatrix - процент заполненности матрицы (100 - полная матрица)
     */
    public static Tsp byRandomDoubleValues(int countVertexes, int minValue, int maxValue, int percentFullMatrix) {
        checkingCorrectnessOfInput(countVertexes, minValue, maxValue, percentFullMatrix);
        Tsp tsp = new Tsp(countVertexes, minValue, maxValue, percentFullMatrix, false);
        tsp.writeProblemInFile();
        return tsp;
    }

    /**
     * Запись данных задачи о коммивояжере в файл
     */
    private void writeProblemInFile() {
        String path = String.format(getFilePath() + PATH, PROBLEM, DATA, getPathId());
        logInfo("Запись данных задачи о коммивояжере в файл %s", path);
        writeArray(path, paths, "Весовая матрица (пути)");
    }

    /**
     * Проверка корректности переданных данных для рандомного заполнения
     */
    private static void checkingCorrectnessOfInput(
            int countVertexes,
            int minValue,
            int maxValue,
            int percentFullMatrix
    ) {
        int extraMinValue = 0;
        int minSizeMatrix = 2;
        int maxPercent = 100;
        try {
            if (countVertexes < minSizeMatrix) {
                throw new NumberException("countVertexes", "minSizeMatrix", countVertexes, minSizeMatrix);
            }
            if (minValue < extraMinValue) {
                throw new NumberException("minValue", "extraMinValue", minValue, extraMinValue);
            }
            if (minValue > maxValue) {
                throw new NumberException("maxValue", "minValue", maxValue, minValue);
            }
            if (percentFullMatrix < extraMinValue) {
                throw new NumberException("percentFullMatrix", "extraMinValue", percentFullMatrix, extraMinValue);
            }
            if (percentFullMatrix > maxPercent) {
                throw new NumberException("maxPercent", "percentFullMatrix", maxPercent, percentFullMatrix);
            }
        }
        catch (NumberException ignored) {}
    }

    /**
     * Проверка корректности переданного пути
     */
    private static void checkingCorrectnessPath(int countVertexes, ArrayList<ArrayList<Double>> paths) {
        try {
            for (int i = 0; i < paths.size(); i++) {
                if (paths.get(i).size() != countVertexes) {
                    throw new ProblemException(i, countVertexes, paths.get(i).size());
                }
                for (int j = 0; j < paths.get(i).size(); j++) {
                    if (i == j && paths.get(i).get(j) != INF) {
                        throw new ProblemException(i, paths.get(i).get(j), String.valueOf(INF));
                    }
                    if (paths.get(i).get(j) < 0) {
                        throw new ProblemException(i, paths.get(i).get(j), "> 0");
                    }
                }
            }
        }
        catch (ProblemException ignored) {}
    }
}
