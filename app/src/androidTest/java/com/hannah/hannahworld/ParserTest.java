package com.hannah.hannahworld;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.hannah.hannahworld.makenumberalgorithm.FormulaConvert;
import com.hannah.hannahworld.makenumberalgorithm.FormulaParser;

/**
 * Created by xuyong1 on 24/07/15.
 */
public class ParserTest  extends AndroidTestCase {

    @SmallTest
    public void testCase1() {
        FormulaParser p = new FormulaParser("(( 1 + 1) * (( 2 + 3 )))");
        p.parse();
        assertEquals(p.getFormulaWithoutExtraParentheses(), "(1 + 1) * (2 + 3)");

    }
    @SmallTest
    public void testCase2() {
        FormulaParser p = new FormulaParser("(( 1 + 1) * (( 4 + *4)))");
        p.parse();
        System.out.println(p.getFormulaWithoutExtraParentheses());
        assertEquals(p.getFormulaWithoutExtraParentheses(), "Not Valid");
    }
}
