package com.hannah.hannahworld.makenumber;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Created by yongxu on 15-7-15.
 * give n number, generate all possible formulas with four operators" +. - * /;
 * two step: first generate full binary tree( this is the catalan number) 1/(n+1) * C 2n n;
 * then add four operators
 * for example, if input is for numbers, then there are 5 * 4^3 = 320 formulas
 */
public class FullTree {
    //private List<String> numbers;
    private List<String> mOperators = new ArrayList<String>(Arrays.asList("+", "-", "*","/"));
    public List<String> makeTree(List<String> list) {
        int n = list.size();
        if (n == 0) return new ArrayList<String>();
        else if (n == 1) {
            return new ArrayList<String>(Arrays.asList(""+list.get(0)));
        } else {
            List<String> tList = new ArrayList<String>();
            for (int i = 1; i < n ; i++) {
                List<String> leftList = new ArrayList<String>(list.subList(0, i));
                List<String> rightList = new ArrayList<String>(list.subList(i, n));
                //System.out.println("I:"+i+leftList.toString()+rightList.toString());
                for (String left : makeTree(leftList)) {
                    for (String right : makeTree(rightList)) {
                        for(String op : mOperators){
                            //System.out.println(left+right);
                            tList.add("("+left+op+right+")");
                        }
                    }
                }
            }
            return tList;
        }
    }
    public static void main(String args[]){
        FullTree fullTree = new FullTree();
        List<String> t1 = new ArrayList<String>(Arrays.asList("1","2","3","4"));
        //System.out.println(t1.toString());
        for(String s :  fullTree.makeTree(t1)){
            System.out.println(s);
            FormulaConvertImpl t = new FormulaConvertImpl();
            t.setString(s);

            String out = t.getOutput();
            System.out.println(out);
            t.calResult();

        }
    }
}