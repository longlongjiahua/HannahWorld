package com.hannah.hannahworld.makenumberalgorithm;

/**
 * Created by xuyong1 on 24/07/15.
http://web.cse.ohio-state.edu/software/2231/web-sw2/extras/slides/27.Recursive-Descent-Parsing.pdf
expr → term { add-op term }
term → factor { mult-op factor }
factor → ( expr ) | number
add-op → + | -
mult-op → * | DIV | REM
number → 0 | nz-digit { 0 | nz-digit }
nz-digit → 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
*/
public class FormulaParser {
    private final static char EOF = '$';
    private String input;
    private boolean validFormula = true;
    private String formulaWithoutExtraParentheses;
    private int currPos;

    public FormulaParser(String input) {
        this.input = input + EOF; // mark the end
        this.currPos = -1;
    }
    public void parse() {
        nextToken();
        Result result = expression();
        if(currToken() != EOF) {
            validFormula = false;
            return;
        }
        formulaWithoutExtraParentheses =result.getText();
    }
    public boolean isValidFormula(){
        return validFormula;
    }
    public String getFormulaWithoutExtraParentheses(){
        if(validFormula){
            return formulaWithoutExtraParentheses;
        }
        else {
            return "Not Valid";
        }
    }
 
    private Result expression() {
        if(!validFormula)
            return new Result("", ' ');
        Result leftArg = term();
        char operator = currToken();
        if (operator != '+' && operator != '-') {
            return leftArg;
        }
        nextToken();
        Result rightArg = term();
        if(operator == '-' && (rightArg.getOp() == '-' || rightArg.getOp() == '+')) {
            rightArg = addParentheses(rightArg);
        }

        return new Result(leftArg.getText() + " " + operator + " " + rightArg.getText(), operator);
    }

    private Result term() {
        if(!validFormula)
            return new Result("", ' ');
        Result leftArg = factor();
        char operator = currToken();
        if (operator != '*' && operator != '/') {
            return leftArg; 
        }
        nextToken();
        Result rightArg = factor();
        if(leftArg.getOp() == '+' || leftArg.getOp() == '-') {
            leftArg = addParentheses(leftArg);
        }
        if(rightArg.getOp() == '+' || rightArg.getOp() == '-' || (operator == '/' && (rightArg.getOp() == '/' || rightArg.getOp() == '*'))) {
            rightArg = addParentheses(rightArg);
        }
        return new Result(leftArg.getText() + " " + operator + " " + rightArg.getText(), operator);
    }
   // factor → ( expr ) | number
    private Result factor()  {
        if(!validFormula)
            return new Result("", ' ');
        Result result =new Result("", ' ');
        if(currToken() == '(') {
            result = paren();
        } else if(currToken()>='0' &&currToken()<='9'){
            result = variable();
        } else {
            validFormula = false;
         }
        return result;
    }

  //( expr )
    private Result paren()  {
        if(!validFormula)
            return new Result("", ' ');
        nextToken();
        Result result = expression();
        if(currToken() != ')') {
            validFormula = false;
            return result;
         }
        nextToken();
        return result;
    }
    private Result variable()  {
        if(!validFormula)
            return new Result("", ' ');
        Result result = new Result(Character.toString(currToken()), ' ');
        nextToken();
        return result;
    }

    private char currToken() {
        return input.charAt(currPos);
    }

    private void nextToken() {
        if(currPos >= input.length() - 1) {
            validFormula = false;
            return;
        }
        do {
            ++currPos;
        }
        while(currToken() != EOF && currToken() == ' ');
    }

    private static Result addParentheses(Result result) {
        return new Result("(" + result.getText() + ")", result.getOp());
    }

    private static class Result {
        private final String text;
        private final char op;

        private Result(String text, char op) {
            this.text = text;
            this.op = op;
        }

        public String getText() {
            return text;
        }

        public char getOp() {
            return op;
        }
    }
}