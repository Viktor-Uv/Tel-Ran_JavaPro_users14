package com.example.users14;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CalcServiceSumTest {
    @Test
    public void testSum() {
        CalcService calcService = new CalcService();

        assertEquals(calcService.sum(5, 6), 11);
    }
}
