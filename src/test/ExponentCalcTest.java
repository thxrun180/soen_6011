import org.example.ExponentCalc;
import org.junit.jupiter.api.Test;

public class ExponentCalcTest {
    private static final double EPS = 1e-6;

    @Test void computesBasicCase() throws Exception {
        assertEquals(18.0, ExponentCalc.compute(2.0, 3.0, 2.0), EPS);
    }

    @Test void identityWhenBIsOne() throws Exception {
        assertEquals(7.5, ExponentCalc.compute(7.5, 1.0, 123.0), EPS);
    }

    @Test void xZeroReturnsA() throws Exception {
        assertEquals(123.45, ExponentCalc.compute(123.45, 2.0, 0.0), EPS);
    }

    @Test void negativeExponent() throws Exception {
        assertEquals(0.625, ExponentCalc.compute(5.0, 2.0, -3.0), EPS);
    }

    @Test void fractionalExponent() throws Exception {
        assertEquals(3.0, ExponentCalc.compute(1.0, 9.0, 0.5), EPS);
    }

    @Test void throwsOnInvalidBase() {
        Assertions.assertThrows(InvalidBaseException.class, () -> ExponentCalc.compute(1.0, 0.0, 2.0));
        Assertions.assertThrows(InvalidBaseException.class, () -> ExponentCalc.compute(2.0, -5.0, 1.0));
    }

    @Test void lnAndExpSanity() {
        Assertions.assertEquals(0.0, ExponentCalc.computeLn(1.0), EPS);
        Assertions.assertEquals(1.0, ExponentCalc.computeExp(0.0), EPS);
        Assertions.assertEquals(1.0, ExponentCalc.computeLn(2.718281828), 1e-3);
    }
}
