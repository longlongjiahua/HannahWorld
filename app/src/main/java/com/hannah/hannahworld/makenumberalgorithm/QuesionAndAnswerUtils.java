package com.hannah.hannahworld.makenumberalgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by yongxu on 15-7-16.
 */
public class QuesionAndAnswerUtils {

    public static String giveAnswer(List<String> numberList) {
        FullTree fullTree = new FullTree();
        //System.out.println(t1.toString());
        for (String s : fullTree.makeTree(numberList)) {
            s = s.substring(1, s.length()-1);
            FormulaConvertImpl t = new FormulaConvertImpl();
            t.setString(s);
            double outputCalculate = t.calResult();
            if (t.almostEqual(outputCalculate, 24)) {
                return s;
            }

        }
        return "No Answer";
    }

    public static List<String> fourRandomInt(){
        List<String> numberList = new ArrayList<String>();
        for(int i=0; i<4; i++){
            numberList.add(""+randInt(0,9));
        }
        return numberList;
     }

      static int  randInt(int min, int max) {
        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

}
