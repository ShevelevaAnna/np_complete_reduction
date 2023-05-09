package framework.kp;

/**
 * Класс вещи в задаче о рюкзаке
 * @param number - уникальный номер вещи
 * @param weight - вес вещи
 * @param cost   - стоимость вещи
 * @param count  - количество вещей данного веса и стоимости
 */
public record KpThing(String number, int weight, double cost, int count) {
}
