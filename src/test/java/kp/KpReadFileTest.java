package kp;

import framework.kp.Kp;
import framework.utils.WriteDataUtils;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class KpReadFileTest {
    @Test
    public void correctDataTest() {
        test(0, "0");
    }

    @Test
    public void errorDataNoEnterTest() {
        errorTest(1);
    }

    @Test
    public void errorDataWeightThingTest() {
        errorTest(2);
    }

    @Test
    public void errorDataCostThingTest() {
        errorTest(3);
    }

    @Test
    public void errorDataCountThingTest() {
        errorTest(4);
    }

    @Test
    public void errorDataNumberThingTest() {
        errorTest(5);
    }

    @Test
    public void errorManyDataTest() {
        errorTest(6);
    }

    @Test
    public void errorLowDataTest() {
        errorTest(7);
    }

    @Test
    public void errorDataLowKpWeightTest() {
        errorTest(8);
    }

    @Test
    public void errorDataKpWeightTest() {
        errorTest(9);
    }

    private void errorTest(int pathId) {
        test(pathId, "1");
    }

    private void test(int pathId, String expected) {
        WriteDataUtils.setPath("src\\test\\java\\kp\\outputfile\\");
        new MockUp<System>() {
            @Mock
            public void exit(int value) {
                throw new RuntimeException(String.valueOf(value));
            }
        };
        WriteDataUtils.setPathId(pathId);
        try {
            Kp.byFileValues(getInputFile(pathId));
        } catch (RuntimeException e) {
            Assertions.assertEquals(expected, e.getMessage());
        }
    }

    private String getInputFile(int i) {
        return String.format("src\\test\\java\\kp\\inputfile\\input_%d.txt", i);
    }
}
