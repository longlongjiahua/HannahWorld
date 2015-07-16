package com.hannah.hannahworld.makenumber;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Created by yongxu on 15-7-15.
 * give n number, generate all possible formulas with four operators" +. - * /;
 * two step: first generate full binary tree( this is the catalan number) 1/(n+1) * C 2n n;
 * then add four operators
 * for example, if the input are  numbers, then there are 5 * 4^3 = 320 formulas
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
   
}