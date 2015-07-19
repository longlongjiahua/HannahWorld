package com.hannah.hannahworld;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import com.hannah.hannahworld.makenumberalgorithm.FormulaConvert;


public class FormulaConvertTest extends AndroidTestCase {


    @SmallTest
    public void testCase1() {
        FormulaConvert fConvert = new FormulaConvert();
        fConvert.setString("3*6+4*2");
        assertEquals(fConvert.getOutput(),"3 6 * 4 2 * +");
    }
    @SmallTest
    public void testCase2() {
        FormulaConvert fConvert = new FormulaConvert();
        fConvert.setString("(3+2)*5");
        assertEquals(fConvert.getOutput(),"3 2 + 5 *");
    }
    @SmallTest
    public void testCase3() {
        FormulaConvert fConvert = new FormulaConvert();
        fConvert.setString("1+2(1+3) + 2/1");
        assertEquals(fConvert.getOutput(), "3 2 + 5 *");
    }

}