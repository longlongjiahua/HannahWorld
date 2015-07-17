package com.hannah.hannahworld.makenumberalgorithm;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
/*
Implement Reverse Polish notation(http://en.wikipedia.org/wiki/Reverse_Polish_notation)
to convert input formulat

 */

public class FormulaConvertImpl  {
    private String inStr;
    public String outStr="";
    private char[] ops = {'+', '-', '*', '/', '(', ')'};
    private int[] precedences = {1, 1, 2, 2, -1, -1};
    public Map<Character, Integer> opPrec;
    private Stack<Character> opStack = new Stack<Character>();
    public int result;
    private double eps = 0.00001;

    public FormulaConvertImpl() {
        init();
    }

    private void init() {
        opPrec = new HashMap<Character, Integer>();
        int length = ops.length;
        for (int i = 0; i < length; i++) {
            opPrec.put(ops[i], precedences[i]);
        }
    }

    public int isValidateChar(char mChar) {
        if (mChar == ' ')
            return 0;
        else if (mChar >= '0' && mChar <= '9')
            return 1;
        else if (opPrec.get(mChar) != null) {
            return 2;
        } else
            return -1;
    }

    public void infix2PostFix() {
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
        while (!opStack.empty()) {
            if (opStack.peek() != '(' && opStack.peek() != ')') {
                outStr += opStack.peek();
            }
            opStack.pop();
        }
    }

    public String getOutput() {
        return outStr;
    }

    public void setString(String inStr) {
        this.inStr = inStr;
        infix2PostFix();
    }

    private void handleOperator(char mOp) {

        if (opStack.empty() ||mOp=='(') {
            opStack.push(mOp);
        }
        else if (mOp == ')') {
            while (opStack.peek() != '(') {
                outStr += opStack.pop();
            }
            opStack.pop();//get rid of (;
        }
        else {
            char top = opStack.peek();
            if (opPrec.get(mOp) <= opPrec.get(top)) { //if current operator not greater than the top one in the stack, pop the opStack until the the top of
                // stack is strict  less than the current;
                while (!opStack.empty() && opStack.peek() != '(' && opPrec.get(opStack.peek())>=opPrec.get(mOp)) {
                    System.out.println(opStack.peek() + "peek");
                    outStr += opStack.pop();
                }
                opStack.push(mOp);

            }
            else {
                opStack.push(mOp);
            }
        }
    }
    public boolean valideInputForm(){
        //TODO
        return true;
    }

    public double calResult(){
        int length = outStr.length();
        Stack<Double> numStack= new Stack<Double>();
        for(int i=0; i<length; i++){
            char curr = outStr.charAt(i);
            if(opPrec.get(curr)==null && curr>='0' && curr<='9'){
                double x = (double) Character.digit(curr, 10);
                numStack.push(x);
            }
            else {
                double a = numStack.pop(); double b = numStack.pop();
                switch(curr){
                    case '+':
                        numStack.push(a+b);
                        break;
                    case '-':
                        numStack.push(b-a);
                        break;
                    case '*':
                        numStack.push(a*b);
                        break;
                    case '/':
                        numStack.push(b/a);
                    default:
                        break;
                }
            }
        }
        double outResults = numStack.pop();
        return outResults;
        //System.out.println("result: " + outResults);
    }

    public  boolean almostEqual(double a, int c){
        double b = (double) ((int) (a+0.5));
        if(Math.abs(a-b)<eps){
            int around = (int) (a+0.5);
            if(around ==c){
                return true;
            }
            return false;
        }
        return false;
    }

/*
    public static void main(String[] Args) {
        //String input = Args[0];

        test("1+2*(3+5)+3/5");
    }
    */
    static private void test(String  input){

        System.out.println(input);
        FormulaConvertImpl t = new FormulaConvertImpl();
        t.setString(input);

        String out = t.getOutput();
        System.out.println(out);
        t.calResult();
    }
}
