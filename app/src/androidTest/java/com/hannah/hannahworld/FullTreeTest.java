package com.hannah.hannahworld;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.hannah.hannahworld.makenumberalgorithm.FormulaConvertImpl;
import com.hannah.hannahworld.makenumberalgorithm.FullTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by yongxu on 15-7-16.
 */
public class FullTreeTest extends AndroidTestCase {
    @SmallTest
    public void testCase1() {
        FullTree fullTree = new FullTree();
        List<String> t1 = new ArrayList<String>(Arrays.asList("1", "2", "3", "4"));
        //System.out.println(t1.toString());
        for (String s : fullTree.makeTree(t1)) {
            FormulaConvertImpl t = new FormulaConvertImpl();
            t.setString(s);
            String out = t.getOutput();
            System.out.println(out);
            t.calResult();
            FormulaConvertImpl fConvert = new FormulaConvertImpl();
            fConvert.setString("3*6+4*2");
            assertEquals(fConvert.getOutput(), "3 6 * 4 2 * +");
        }
    }
    @SmallTest
    public void testCase2() {
        FormulaConvertImpl fConvert = new FormulaConvertImpl();
        fConvert.setString("(3+2)*5");
        assertEquals(fConvert.getOutput(),"3 2 + 5 *");
    }
    @SmallTest
    public void testCase3() {
        FormulaConvertImpl fConvert = new FormulaConvertImpl();
        fConvert.setString("1+2(1+3) + 2/1");
        assertEquals(fConvert.getOutput(), "3 2 + 5 *");
    }
}
