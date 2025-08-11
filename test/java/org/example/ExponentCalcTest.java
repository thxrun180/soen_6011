package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ExponentCalcTest {
    private static final double EPS = 1e-6;


    @Test
    void identityWhenBIsOne() {
        assertEquals(7.5, ExponentCalc.compute(7.5, 1.0, 123.0), EPS);
    }

    @Test
    void xZeroReturnsA() {
        assertEquals(123.45, ExponentCalc.compute(123.45, 2.0, 0.0), EPS);
    }



    @Test
    void lnAndExpSanity() {
        assertEquals(0.0, ExponentCalc.computeLn(1.0), EPS);
        assertEquals(1.0, ExponentCalc.computeExp(0.0), EPS);
        // ln(e) ≈ 1
        assertEquals(1.0, ExponentCalc.computeLn(2.718281828), 1e-3);
    }
}
