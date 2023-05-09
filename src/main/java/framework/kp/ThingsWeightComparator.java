package framework.kp;

import java.util.Comparator;

/**
 * Обратная сортировка всех вещей по весам
 */
class ThingsWeightComparator implements Comparator<KpThing> {
    public int compare(KpThing things1, KpThing things2){
        return Double.compare(things2.weight(), things1.weight());
    }
}
