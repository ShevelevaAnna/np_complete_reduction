package framework.kp;

import framework.exception.ProblemException;
import framework.exception.NumberException;
import framework.pathproblem.tsp.Tsp;
import framework.provider.LoggerProvider;
import framework.utils.RandomValueUtils;
import framework.utils.ReadDataUtils;

import java.util.*;

import static framework.utils.WriteDataUtils.*;

/**
 * Класс задачи о рюкзаке
 * <p>
 * В рюкзаке лежат вещи разной стоимости, веса, количества и обладающие уникальным номером.
 * Вещи могут иметь уникальный порядок упаковки, например динамическая упаковка по хрупкости.
 * <p>
 * Рюкзак имеет максимальный возможный вес.
 * <p>
 * Следует найти список вещей, которые будут иметь максимальную ценность и не выходить за рамки максимального веса.
 */
public class Kp extends LoggerProvider {
    protected static final String PROBLEM = "kp";

    /**
     * Максимальный вес
     */
    private final int maxWeight;

    /**
     * Количество вещей в рюкзаке
     */
    private final int numberOfThings;

    /**
     * Список вещей
     */
    private final List<KpThing> things;

    /**
     * Динамическая матрица хрупкости
     */
    private List<Integer> fragile;

    /**
     * Создание стандартного объекта задачи по переданным данным
     * @param maxWeight - максимальный вес рюкзака
     * @param things    - список вещей
     */
    private Kp(int maxWeight, List<KpThing> things, String problemCondition) {
        super(Kp.class);
        logInfo("Заполнение переданными значениями объекта задачи о рюкзаке " + problemCondition);
        this.maxWeight = maxWeight;
        this.numberOfThings = things.size();
        this.things = things;
    }

    /**
     * Создание объекта задачи по переданным данным с динамической матрицей хрупкости
     * @param maxWeight   - максимальный вес рюкзака
     * @param things      - список вещей с уникальными номерами типа i_j, i!=j
     * @param fragileSize - размер динамической матрицы хрупкости вида [-1, 1, ...]
     */
    private Kp(int maxWeight, List<KpThing> things, int fragileSize) {
        this(maxWeight, things, "(с динамической матрицей хрупкости)");
        fragile = new ArrayList<>(List.of(-1));
        for (int i = 1; i < fragileSize; i++) {
            fragile.add(1);
        }
    }

    /**
     * Создание стандартного объекта задачи по переданным данным
     * @param maxWeight - максимальный вес рюкзака
     * @param things    - список вещей
     */
    private Kp(int maxWeight, List<KpThing> things) {
        this(maxWeight, things, "(стандартная)");
        things.sort(new ThingsWeightComparator());
    }

    /**
     * Создание объекта с рандомными значениями в промежутке переданных значений максимума и минимума
     * @param maxKnapsackWeight - максимальный вес рюкзака
     * @param numberOfThings    - количество вещей
     * @param maxWeight         - максимальный вес вещи
     * @param minWeight         - минимальный вес вещи
     * @param maxCost           - максимальная ценность вещи
     * @param minCost           - минимальная ценность вещи
     * @param maxCount          - максимальное количество вещи
     * @param minCount          - минимальное количество вещи
     */
    private Kp(
            int maxKnapsackWeight,
            int numberOfThings,
            int maxWeight,
            int minWeight,
            int maxCost,
            int minCost,
            int maxCount,
            int minCount
    ) {
        super(Kp.class);
        logInfo("Заполнение рандомными значениями объекта задачи о рюкзаке ");
        this.maxWeight = maxKnapsackWeight;
        things = new ArrayList<>();
        for (int i = 0; i < numberOfThings;  i++) {
            int weight = RandomValueUtils.getIntegerValue(minWeight, maxWeight);
            double cost = RandomValueUtils.getDoubleValue(minCost, maxCost) * weight;
            int count = RandomValueUtils.getIntegerValue(minCount, maxCount);
            things.add(new KpThing(Integer.toString(i), weight, cost, count));
        }
        things.sort(new ThingsWeightComparator());
        int index = 1;
        while (getThings().size() != index) {
            if(getThings().get(index).weight() == getThings().get(index - 1).weight()) {
                if (getThings().get(index).cost() > getThings().get(index - 1).cost()) {
                    getThings().remove(index - 1);
                }
                else {
                    getThings().remove(index);
                }
            }
            else {
                index++;
            }
        }
        this.numberOfThings = getThings().size();
    }

    /**
     * @return максимальный вес рюкзака
     */
    public int getMaxWeight() {
        return maxWeight;
    }

    /**
     * @return количество вещей
     */
    public int getNumberOfThings() {
        return numberOfThings;
    }

    /**
     * @return список вещей
     */
    public List<KpThing> getThings() {
        return things;
    }

    /**
     * @return динамическую матрицу хрупкости
     */
    public List<Integer> getFragile() {
        return fragile;
    }

    /**
     * Установить новое значение динамической матрицы хрупкости
     */
    public void setFragile(List<Integer> fragile) {
        this.fragile = fragile;
    }

    /**
     * @param thingId - индекс вещи
     * @return вещь из списка
     */
    public KpThing getThing(int thingId) {
        return getThings().get(thingId);
    }

    /**
     * @param thingId - индекс вещи
     * @return количество вещи из списка
     */
    public int getCountThing(int thingId) {
        return getThing(thingId).count();
    }

    /**
     * @param thingId - индекс вещи
     * @return вес вещи из списка
     */
    public int getWeightThing(int thingId) {
        return getThing(thingId).weight();
    }

    /**
     * @param thingId - индекс вещи
     * @return ценность вещи из списка
     */
    public double getCostThing(int thingId) {
        return getThing(thingId).cost();
    }

    /**
     * @param thingId - индекс вещи
     * @return уникальный номер вещи из списка
     */
    public String getNumberThing(int thingId) {
        return getThing(thingId).number();
    }

    /**
     * Сведение задачи к задаче коммивояжера
     */
    public Tsp toTSP() {
        KpToProblem kpToProblem = new KpToProblem(this);
        return kpToProblem.toTsp();
    }

    /**
     * Точное решение задачи
     */
    public void solve() {
        KpExactSolution kpExactSolution = new KpExactSolution(this);
        kpExactSolution.solve();
    }

    /**
     * Точное решение задачи с записью в файл
     */
    public void solveWithWrite() {
        KpExactSolution kpExactSolution = new KpExactSolution(this);
        kpExactSolution.solveWithWrite();
    }

    /**
     * Точное решение задачи до сведения к задаче коммивояжера с записью в файл
     */
    public void solveKpToTsp() {
        KpExactSolution kpExactSolution = new KpExactSolution(this);
        kpExactSolution.solveKpToTsp();
    }

    /**
     * Точное решение задачи после сведения из задачи коммивояжера с записью в файл
     */
    public void solveTspToKp() {
        KpFragileExactSolution kpExactSolution = new KpFragileExactSolution(this);
        kpExactSolution.solveTspToKp();
    }

    /**
     * Получение объекта задачи с переданными значениями
     * @param maxWeight - максимальный вес рюкзака
     * @param things    - список вещей рюкзака
     */
    public static Kp byValues(int maxWeight, List<KpThing> things) {
        checkingCorrectnessKpData(maxWeight, things);
        Kp kp = new Kp(maxWeight, things);
        kp.writeProblemInFile();
        return kp;
    }

    /**
     * Получение объекта задачи из файла
     * @param fileName - путь к файлу
     */
    public static Kp byFileValues(String fileName) {
        List<List<String>> file = ReadDataUtils.read(fileName);
        int maxWeight = checkingCorrectnessWeightFile(file);
        List<KpThing> things = checkingCorrectnessThingsFile(file);
        return byValues(maxWeight, things);
    }

    /**
     * Получение объекта задачи с переданными значениями с динамической матрицей хрупкости
     * @param maxWeight - максимальный вес рюкзака
     * @param things    - список вещей рюкзака с уникальными номерами вида i_j
     * @param fragileSize - размер динамической матрицы хрупкости, равный различным i, j
     */
    public static Kp byValuesWithFragile(int maxWeight, List<KpThing> things, int fragileSize) {
        checkingCorrectnessKpData(maxWeight, things);
        checkingCorrectnessKpFragileData(things, fragileSize);
        Kp kp = new Kp(maxWeight, things, fragileSize);
        kp.writeProblemInFile();
        return kp;
    }

    /**
     * Создание объекта с рандомными значениями в промежутке переданных значений максимума и минимума
     * @param maxKnapsackWeight - максимальный вес рюкзака
     * @param numberOfThings    - количество вещей
     * @param maxWeight         - максимальный вес вещи
     * @param minWeight         - минимальный вес вещи
     * @param maxCost           - максимальная ценность вещи
     * @param minCost           - минимальная ценность вещи
     * @param maxCount          - максимальное количество вещи
     * @param minCount          - минимальное количество вещи
     */
    public static Kp byRandomValues(
            int maxKnapsackWeight,
            int numberOfThings,
            int maxWeight,
            int minWeight,
            int maxCost,
            int minCost,
            int maxCount,
            int minCount
    ) {
        checkingCorrectnessOfInput(
                maxKnapsackWeight, numberOfThings, maxWeight, minWeight, maxCost, minCost, maxCount, minCount
        );
        Kp kp = new Kp(maxKnapsackWeight, numberOfThings, maxWeight, minWeight, maxCost, minCost, maxCount, minCount);
        kp.writeProblemInFile();
        return kp;
    }

    /**
     * Запись данных задачи о коммивояжере в файл
     */
    private void writeProblemInFile() {
        String path = String.format(getFilePath() + PATH, PROBLEM, DATA, getPathId());
        logInfo("Запись данных задачи о рюкзаке в файл %s", path);
        String weightAndNumberText = "Максимальный вес рюкзака: %d, количество вещей: %d\n";
        String thingsListText = "Список вещей: %s\n\n";
        write(
                path,
                String.format(weightAndNumberText, maxWeight, numberOfThings),
                String.format(thingsListText, things.toString())
        );
    }

    /**
     * Проверка корректности переданных данных для рандомного заполнения
     */
    private static void checkingCorrectnessOfInput(
            int maxKnapsackWeight,
            int numberOfThings,
            int maxWeight,
            int minWeight,
            int maxCost,
            int minCost,
            int maxCount,
            int minCount
    ) {
        int minPossibleWeight = 1;
        int minPossibleCost = 0;
        int minPossibleCount = 1;
        try {
            if (maxKnapsackWeight < minPossibleWeight) {
                throw new NumberException("maxKnapsackWeight", "minPossibleWeight", maxKnapsackWeight, minPossibleWeight);
            }
            if (minWeight < minPossibleWeight) {
                throw new NumberException("minWeight", "minPossibleWeight", minWeight, minPossibleWeight);
            }
            if (minWeight > maxWeight) {
                throw new NumberException("maxWeight", "minWeight", maxWeight, minWeight);
            }
            if (minCost < minPossibleCost) {
                throw new NumberException("minCost", "minPossibleCost", minCost, minPossibleCost);
            }
            if (minCost > maxCost) {
                throw new NumberException("maxCost", "minCost", maxCost, minCost);
            }
            if (numberOfThings < minPossibleCount) {
                throw new NumberException("numberOfThings", "minPossibleCount", numberOfThings, minPossibleCount);
            }
            if (minCount < minPossibleCount) {
                throw new NumberException("minCount", "minPossibleCount", minCount, minPossibleCount);
            }
            if (minCount > maxCount) {
                throw new NumberException("maxCount", "minCount", maxCount, minCount);
            }
        }
        catch (NumberException ignored) {}
    }

    /**
     * Проверка получения вещей из файла
     */
    private static List<KpThing> checkingCorrectnessThingsFile(List<List<String>> file) {
        List<KpThing> things = new ArrayList<>();
        int i = 2;
        try {
            for (; i < file.size(); i++) {
                if (file.get(i).size() != 4) {
                    throw new ProblemException(ProblemException.MAX_WEIGHT_MESSAGE);
                }
                things.add(
                        new KpThing(
                                file.get(i).get(0),
                                Integer.parseInt(file.get(i).get(1)),
                                Double.parseDouble(file.get(i).get(2)),
                                Integer.parseInt(file.get(i).get(3))
                        )
                );
            }
        }
        catch (ProblemException ignored) {}
        catch (NumberFormatException e) {
            new ProblemException(String.format(ProblemException.VALUE_MESSAGE, i));
        }
        return things;
    }

    /**
     * Проверка получения максимального веса из файла
     */
    private static int checkingCorrectnessWeightFile(List<List<String>> file) {
        int maxWeight = 0;
        try {
            if (file.size() < 3) {
                throw new ProblemException(ProblemException.MAX_WEIGHT_MESSAGE);
            }
            if (file.get(0).size() != 1 || Integer.parseInt(file.get(0).get(0)) < 1) {
                throw new ProblemException(ProblemException.MAX_WEIGHT_MESSAGE);
            }
            maxWeight = Integer.parseInt(file.get(0).get(0));
            if (file.get(1).size() != 0) {
                throw new ProblemException(ProblemException.MISS_ENTER_MESSAGE);
            }
        }
        catch (ProblemException ignored) {}
        catch (NumberFormatException e) {
            new ProblemException(String.format(ProblemException.VALUE_MESSAGE, 0));
        }
        return maxWeight;
    }

    /**
     * Проверка корректности ввода максимального веса рюкзака и списка вещей
     */
    private static void checkingCorrectnessKpData(int maxWeight, List<KpThing> things) {
        int minPossibleWeight = 1;
        try {
            if (maxWeight < minPossibleWeight) {
                throw new NumberException("maxWeight", "minPossibleWeight", maxWeight, minPossibleWeight);
            }
            if (things.size() < 1) {
                throw new NumberException("things.size()", "minPossibleSize", things.size(), 1);
            }
            things.sort(new ThingsNumberComparator());
            KpThing firstThing = things.get(0);
            int thingMinWeight = firstThing.weight();
            double thingMinCost = firstThing.cost();
            int thingMinCount = firstThing.count();
            String lastNumber = firstThing.number();

            for (int i = 1; i < things.size(); i++) {
                if (things.get(i).weight() < thingMinWeight) {
                    thingMinWeight = things.get(i).weight();
                }
                if (things.get(i).cost() < thingMinCost) {
                    thingMinCost = things.get(i).cost();
                }
                if (things.get(i).count() < thingMinCount) {
                    thingMinCount = things.get(i).count();
                }
                if (Objects.equals(things.get(i).number(), lastNumber)) {
                    throw new ProblemException("Номера не уникальны, есть совпадения");
                }
                lastNumber = things.get(i).number();
            }

            if (maxWeight < thingMinWeight) {
                throw new NumberException("maxWeight", "thingMinWeight", maxWeight, thingMinWeight);
            }
            if (thingMinWeight < minPossibleWeight) {
                throw new NumberException("thingMinWeight", "minPossibleWeight", thingMinWeight, minPossibleWeight);
            }
            if (thingMinCount < 1) {
                throw new NumberException("thingMinCount", "minPossibleCount", thingMinCount, 1);
            }
            if (thingMinCost < 0) {
                throw new NumberException("thingMinCost", "minPossibleCost", thingMinCost, 0);
            }
        }
        catch (NumberException | ProblemException ignored) {}
    }

    /**
     * Проверка значений задачи в динамической матрицей
     */
    private static void checkingCorrectnessKpFragileData(List<KpThing> things, int fragileSize) {
        String numberMessage = "Некорректный вид уникального номера. Должен быть вид 'int_int'";
        try {
            HashSet<Integer> integerHashSet = new HashSet<>();
            for (int i = 0; i < things.size(); i++) {
                if (things.get(i).number().indexOf('_') == -1) {
                    throw new ProblemException(
                            String.format(numberMessage)
                    );
                }
                integerHashSet.add(Integer.parseInt(things.get(i).number().split("_")[0]));
                int indexNext = things.get(i).number().indexOf("_");
                integerHashSet.add(Integer.parseInt(things.get(i).number().substring(indexNext + 1)));
            }
            if (integerHashSet.size() != fragileSize) {
                throw new ProblemException(String.format("Некорректный размер динамической матрицы"));
            }
        }
        catch (ProblemException ignored) {}
        catch (NumberFormatException e) {
            new ProblemException(String.format(numberMessage));
        }
    }
}