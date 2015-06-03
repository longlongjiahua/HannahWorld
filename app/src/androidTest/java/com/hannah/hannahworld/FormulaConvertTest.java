package com.hannah.hannahworld;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import com.hannah.hannahworld.makenumber.FormulaConvertImpl;


public class FormulaConvertTest extends AndroidTestCase {


    @SmallTest
    public void testCase1() {
        FormulaConvertImpl fConvert = new FormulaConvertImpl();
        fConvert.setString("3*6+4*2");
        assertEquals(fConvert.getOutput(),"3 6 * 4 2 * +");
    }
    @SmallTest
    public void testCase2() {
        FormulaConvertImpl fConvert = new FormulaConvertImpl();
        fConvert.setString("(3+2)*5");
        assertEquals(fConvert.getOutput(),"3 2 + 5 *");
    }
}