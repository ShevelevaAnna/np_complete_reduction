import framework.kp.Kp;
import framework.pathproblem.cap.Cap;
import framework.pathproblem.tsp.Tsp;
import framework.utils.WriteDataUtils;

public class Main {

    public static void main(String[] args) {
        WriteDataUtils.setPathId(0);
        Cap cap = Cap.byRandomDoubleValues(4, 0, 100, 0, 1);
        cap.solveWithWriteFile();
        Tsp tsp = cap.toTsp();
        tsp.solveCapToTsp();

        WriteDataUtils.setPathId(1);
        tsp = Tsp.byRandomDoubleValues(4, 0, 1, 100);
        tsp.solveWithWrite();
        cap = tsp.toCap();
        cap.solveTspToCap();

        WriteDataUtils.setPathId(2);
        tsp = Tsp.byRandomDoubleValues(4, 0, 1, 100);
        tsp.solveTspToKp();
        Kp kp = tsp.toKp();
        kp.solveTspToKp();

        WriteDataUtils.setPathId(3);
        kp = Kp.byRandomValues(8, 10, 4, 1, 7, 3, 100, 1);
        kp.solveKpToTsp();
        tsp = kp.toTSP();
        tsp.solveKpToTsp();
    }
}
