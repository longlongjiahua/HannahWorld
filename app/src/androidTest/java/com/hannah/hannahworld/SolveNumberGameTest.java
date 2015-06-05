package com.hannah.hannahworld;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import com.hannah.hannahworld.makenumber.SolveNumberGame;

import javax.xml.transform.SourceLocator;


public class SolveNumberGameTest extends AndroidTestCase {


    @SmallTest
    public void testCase1() {
        SolveNumberGame fConvert = new SolveNumberGame();
        fConvert.setNums(new char[]{'1', '2','3'});
        assertEquals(fConvert.getResult(),9);
    }


}