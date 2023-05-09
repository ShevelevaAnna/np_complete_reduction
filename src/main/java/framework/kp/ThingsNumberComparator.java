package framework.kp;

import java.util.Comparator;

/**
 * Обратная сортировка всех вещей по номерам
 */
class ThingsNumberComparator implements Comparator<KpThing> {
    public int compare(KpThing things1, KpThing things2){
        return things1.number().compareTo(things2.number());
    }
}
