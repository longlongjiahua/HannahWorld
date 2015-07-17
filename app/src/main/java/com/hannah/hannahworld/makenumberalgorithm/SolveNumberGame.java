package com.hannah.hannahworld.makenumberalgorithm;

import java.util.ArrayList;
import java.util.List;

public class SolveNumberGame{
    /*
    get all possible formula by applying backtrack algorithm.
    Possible get imrovement by using binary search  tree
     */

    private char[] mNums;
    private int length;
    private List<String> mPermutations = new ArrayList<String>();
    private List<char[]> numPermutations;
    private List<String> opAll;
    private static char[] ops ={'+','-','*','/'};

    public void setNums(char[] nums){
        this.mNums = nums;
        this.length = nums.length;
        opAll = new ArrayList<String>();
        numPermutations = new ArrayList<char[]>();
        backtrackForOperators("", 0);
        backtrackForNumPermutation(mNums,0);
    }
    private String array2String(char[] nums){
        String str = "";
        int length = nums.length;
        for(int i=0; i<length; i++){
            str += nums[i];
        }
//        System.out.println(str);
        return str;

    }

    private void backtrackForOperators(String inString, int start){
        if(start ==length-1){
            opAll.add(inString);
            return;
        }
        else {
            for(int i=0; i<ops.length; i++){
                backtrackForOperators(inString + ops[i], start + 1);
                //  nums = Arrays.copyOf(tmp, length);
            }
        }
    }

    private void backtrackForNumPermutation(char[] nums, int start){
        int length = nums.length;
        if(start ==length){
            addOperator(nums);
            //System.out.println(array2String(nums));
            return;
        }
        else {
            for(int i=start; i<length; i++){
                if(i<length-1 && nums[i] == nums[i+1]) continue;  // first need sort
                swap(nums, start, i);
                backtrackForNumPermutation(nums, start+1);
                swap(nums, i, start);//backtrack. also work if int[] tmp = Arrays.copyOf(nums, length); and
                //  nums = Arrays.copyOf(tmp, length);
            }
        }
    }
    private void swap(char[] nums, int i, int j) {
        char x = nums[i];
        nums[i] = nums[j];
        nums[j] = x;
    }
    private void addOperator(char[] nums){
        for(String mOps: opAll){
            String formula = "";
            int i=0;
            for(i=0; i<length-1; i++) {
                formula += ("" + nums[i] + mOps.charAt(i));
            }
            formula+=nums[i];
            System.out.println(formula);
        }
    }
/*
    public static void main(String[] args){
        SolveNumberGame s = new SolveNumberGame();
        s.setNums(new char[] {'1','2','3'});
        // s.setNums(new int[] {1,1,2,2});
    }
    */

}
