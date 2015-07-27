package com.hannah.hannahworld.makenumberalgorithm;

import android.util.Log;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by yongxu on 15-7-16.
 */
public class QuesionAndAnswerUtils {

    public static boolean isCorrectAnswer(String str) {
        FormulaParser parser = new FormulaParser(str);
        parser.parse();
        Log.i("ParseValideFor", parser.getFormulaWithoutExtraParentheses());
        if(!parser.isValidFormula())
            return false;
        FormulaConvert t = new FormulaConvert();
        t.setString(str);
        String out = t.getOutput();
        if (t.isValidFormula()) {
            double result = t.calResult();
            if (t.isValidFormula()) {
                if (t.almostEqual(result, 24)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String giveAnswer(List<String> numberList) {
        FullTree fullTree = new FullTree();
        //System.out.println(t1.toString());
        for (String s : fullTree.makeTree(numberList)) {
            s = s.substring(1, s.length()-1);
            FormulaConvert t = new FormulaConvert();
            t.setString(s);
            double outputCalculate = t.calResult();
            if (t.almostEqual(outputCalculate, 24)) {
                FormulaParser parser = new FormulaParser(s);
                parser.parse();
                return parser.getFormulaWithoutExtraParentheses();

            }

        }
        return "No Answer";
    }

    /**
     *
     * @return four nummbers having solution
     */
    public static List<String> provide24GameQuestion(){
        while(true){
            List<String> numberList = fourRandomInt();
            if(!giveAnswer(numberList).equals("No Answer")){
                return numberList;
            }
        }
     }
    private static List<String> fourRandomInt() {
        List<String> numberList = new ArrayList<String>();
        for (int i = 0; i < 4; i++) {
            numberList.add("" + randInt(0, 9));
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
