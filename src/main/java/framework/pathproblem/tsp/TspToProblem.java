package framework.pathproblem.tsp;

import framework.kp.Kp;
import framework.kp.KpThing;
import framework.pathproblem.cap.Cap;
import framework.utils.ArrayListUtils;
import framework.provider.LoggerProvider;

import java.util.ArrayList;
import java.util.List;

import static framework.utils.IntegerDataUtils.INF;

/**
 * Класс сведения задачи о коммивояжере к другой возможной задаче
 */
class TspToProblem extends LoggerProvider {
    private final Tsp tsp;

    /**
     * Конструктор создания класса по переданному классу задачи о коммивояжере
     * @param tsp - объект задачи о коммивояжере
     */
    protected TspToProblem(Tsp tsp) {
        super(TspToProblem.class);
        this.tsp = tsp;
    }

    /**
     * <b>Алгоритм сведения задачи о коммивояжере к двухуровневой задаче о назначениях</b>
     * <p>
     * Шаги алгоритма:
     * <p>
     * Шаг 1. Продублировать матрицу путей задачи о коммивояжере в матрицы стоимости заказа и зарплаты работникам
     * @return объект двухуровневой задачи о назначениях
     */
    protected Cap toCap() {
        logInfo("Создание объекта двухуровневой задачи о назначениях через сведение задачи о коммивояжере");
        return Cap.byValues(tsp.getPaths(), tsp.getPaths());
    }

    /**
     * <b>Алгоритм сведения задачи о коммивояжере к задаче о рюкзаке</b>
     * <p>
     * Шаги алгоритма:
     * <p>
     * Шаг 1. Создать динамическую матрицу хрупкости со значениями [-1, 1, ...], размера матрицы коммивояжера (n)
     * <p>
     * Шаг 2. Создать вещи по количеству не бесконечных ребер с нумерацией: ребро ij имеет нумерацию "i_j"
     * <p>
     * Шаг 3. Добавить вещам вес:
     * <p>
     * - Ребро 0j имеет вес 2(n - 2) + 1
     * <p>
     * - Ребро i1 имеет вес 1
     * <p>
     * - Ребро ij имеет вес 2
     * <p>
     * Шаг 4. Добавить вещам стоимость: 1 - (wij / s), где
     * wij - вес ребра задачи коммивоящера,
     * s - сумма всех весов ребер (исключая бесконечные)
     * @return объект задачи о рюкзаке
     */
    protected Kp toKp() {
        logInfo("Создание объекта задачи о рюкзаке через сведение задачи о коммивояжере");
        List<KpThing> things = new ArrayList<>();
        double weightSum = ArrayListUtils.getSumNotInf(tsp.getPaths());
        for (int i = 0; i < tsp.getCountVertexes(); i++) {
            for (int j = 0; j < tsp.getCountVertexes(); j++) {
                if(i != j && tsp.getPaths().get(i).get(j) != INF) {
                    KpThing kpThing =
                            new KpThing(
                                    i + "_" + j,
                                    1,
                                    1 - (tsp.getPaths().get(i).get(j) / weightSum),
                                    1
                            );
                    things.add(kpThing);
                }
            }
        }
        return Kp.byValuesWithFragile(tsp.getCountVertexes(), things, tsp.getCountVertexes());
    }
}
