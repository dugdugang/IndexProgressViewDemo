package com.wislint.downloaddemo;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        int i=1;
        int j=100;
        float r= (float) (i*1.0/j);
        System.out.println(r);
    }
}