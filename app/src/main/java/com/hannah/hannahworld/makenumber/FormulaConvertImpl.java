package com.hannah.hannahworld.makenumber;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class FormulaConvertImpl implements FormulaConvert {
    private String inStr;
    private String outStr;
    private char[] ops = {'+', '-', '*', '/','{','}' };
    private int[] precedences = {1,1,2,2,-1, -1};
    public Map<Character, Integer> opPrec;
    private Stack<Character> opStack = new Stack<Character>();

    public FormulaConvertImpl(){
        init();

    }
    private void init(){
        opPrec = new HashMap<Character, Integer>();
        int length = ops.length;
        for(int i=0; i<length; i++){
            opPrec.put(ops[i],  precedences[i]);
        }
    }
     public int isValidateChar(char mChar){
         if(mChar==' ')
             return 0;
         else if(mChar >='0' && mChar <='9')
             return 1;
         else if(opPrec.get(mChar)!=null){
             return 2;
         }
         else
             return -1;
    }

    public void Infix2PostFix(String mStr) {
        int length = inStr.length();
        for (int i = 0; i < length; i++) {
            int valChar = isValidateChar(inStr.charAt(i));
            if (valChar == 1) {
                outStr += inStr.charAt(i);
            }
            if (valChar == 2) {
                handleOperator(inStr.charAt(i));
            }
        }
    }
    public String getOutput(){
        return outStr;
    }

    public void setString(String inStr){
        this.inStr = inStr;
    }

    private void handleOperator(char mOp){
        


    }



}
