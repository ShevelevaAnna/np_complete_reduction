package framework.pathproblem.cap;

import framework.pathproblem.ExactSolution;

import java.util.ArrayList;

import static framework.utils.WriteDataUtils.*;
import static framework.utils.IntegerDataUtils.*;

/**
 * Класс решения двухуровневой задачи о назначениях
 */
class CapExactSolution extends ExactSolution {
    public static final String MIN_SALARY = "Минимальная зарплата: %s";
    public static final String MIN_COST = "Минимальная стоимость: %s";
    private final Cap cap;

    /**
     * Конструктор класса решения
     * @param cap - объект класса двухуровневой задачи о назначениях
     */
    protected CapExactSolution(Cap cap) {
        super(cap.getSalaries(), CapExactSolution.class);
        this.cap = cap;
    }

    /**
     * Нахождение точного пути двухуровневой задачи о назначениях
     */
    protected void exactSolution() {
        solve(
                cap.getSizeProblem() - 1,
                "двухуровневой задачи о назначениях",
                this::getMinCosts,
                () -> {
                    allMinPath = new ArrayList<>();
                    allMinPath.add(minPath);
                    return allMinPath;
                },
                () -> {
                    allMinPath.add(minPath);
                    return allMinPath;
                }
        );
        logInfo(MIN_SALARY, allMinPath.toString());
        logInfo(MIN_COST, subMinPath.toString());
    }

    /**
     * Выбор из полученных решений с заработной платой - минимальное решение со стоимостью
     */
    private ArrayList<Integer> getMinCosts() {
        double minCost = INF * INF;
        ArrayList<Integer> minCosts = new ArrayList<>();
        for (ArrayList<Integer> salary : allMinPath) {
            double cost = 0;
            for (int j = 1; j < salary.size(); j++) {
                cost += cap.getCosts().get(salary.get(j - 1)).get(salary.get(j));
            }
            cost += cap.getCosts().get(salary.get(salary.size() - 1)).get(salary.get(0));
            if (cost <= minCost) {
                minCost = cost;
                subMinPath = salary;
            }
        }
        return minCosts;
    }

    /**
     * Запись результата решения задачи в файл
     */
    protected void writeResult() {
        String path = String.format(getFilePath() + PATH, Cap.PROBLEM, EXACT_SOLUTION, getPathId());
        write(
                path,
                String.format(TIME + ENTER, time/NANO),
                String.format(MIN_SALARY + ENTER, allMinPath.toString()),
                String.format(MIN_COST + DOUBLE_ENTER, subMinPath.toString())
        );
    }

    /**
     * Записать результат задачи в файл в виде задачи коммивояжеа (при сведении к 2ЗОН)
     */
    protected void writeTspToCapResult() {
        String path = String.format(getFilePath() + PATH, Cap.PROBLEM, EXACT_SOLUTION, getPathId());
        String message = "-- В виде задачи коммивояжера" + ENTER;
        if (minCost > INF) {
            write(
                    path,
                    message,
                    String.format(NO_PATH + DOUBLE_ENTER)
            );
        }
        else  {
            double weight = 0;
            for (int i = 0; i < subMinPath.size() - 1; i++) {
                weight += cap.getCosts().get(subMinPath.get(i)).get(subMinPath.get(i + 1));
            }
            weight += cap.getSalaries().get(subMinPath.get(subMinPath.size() - 1)).get(subMinPath.get(0));
            write(
                    path,
                    message,
                    String.format(WEIGHT + DOUBLE_ENTER, weight)
            );
        }
    }
}
