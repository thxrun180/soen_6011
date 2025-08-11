

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ExponentCalcTest {
    private static final double EPS = 1e-6;

    @Test
    void computesBasicCase() {
        assertEquals(18.0, ExponentCalc.compute(2.0, 3.0, 2.0), EPS);
    }

    @Test
    void identityWhenBIsOne() {
        assertEquals(7.5, ExponentCalc.compute(7.5, 1.0, 123.0), EPS);
    }

    @Test
    void xZeroReturnsA() {
        assertEquals(123.45, ExponentCalc.compute(123.45, 2.0, 0.0), EPS);
    }

    @Test
    void negativeExponent() {
        assertEquals(0.625, ExponentCalc.compute(5.0, 2.0, -3.0), EPS); // 5 * 2^-3 = 5/8
    }

    @Test
    void fractionalExponent() {
        assertEquals(3.0, ExponentCalc.compute(1.0, 9.0, 0.5), EPS); // sqrt(9)=3
    }


    @Test
    void lnAndExpSanity() {
        assertEquals(0.0, ExponentCalc.computeLn(1.0), EPS);
        assertEquals(1.0, ExponentCalc.computeExp(0.0), EPS);
        // ln(e) ≈ 1
        assertEquals(1.0, ExponentCalc.computeLn(2.718281828), 1e-3);
    }
}
