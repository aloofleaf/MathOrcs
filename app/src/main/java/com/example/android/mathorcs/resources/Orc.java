package com.example.android.mathorcs.resources;

/**
 * Created by kskotheim on 6/16/17.
 */

public class Orc {

    public static int megaModifier;         //dramatically increases difficulty incrementing - also affects time decrease factor between levels
    public int arg1;
    public int arg2;
    public int solution;
    public int operator;    //1: +  2: -  3: x  4: ⁄
    public int difficulty;  // 1-10

    public String queryString;

    private int[] nums;

    boolean bossOrc;


    public Orc(int difficulty){
        this.difficulty = difficulty;
        bossOrc = false;
        nums = new int[4];
        for (int i=0;i<nums.length; i++){
            nums[i] = getRandInt(2 + difficulty, 5 + difficulty + megaModifier);
        }

        arg1 = nums[0];
        arg2 = nums[1];

        double determiner = Math.random();

            if(determiner < .5 - .05*difficulty){
                operator = 1;
            }else if(determiner < 1 - .1*difficulty){
                operator = 2;
            }else if(determiner < 1 - .05*difficulty){
                operator = 3;
            }else{
                operator = 4;
            }

        switch (operator) {
            case 1:
                solution = arg1 + arg2;
                queryString = String.valueOf(arg1) + " + " + String.valueOf(arg2);
                break;
            case 2:
                if(arg1<arg2){
                    int storage = arg2;
                    arg2 = arg1;
                    arg1 = storage;
                }

                solution = arg1 - arg2;
                queryString = String.valueOf(arg1) + " - " + String.valueOf(arg2);

                break;
            case 3:
                solution = arg1 * arg2;
                queryString = String.valueOf(arg1) + " x " + String.valueOf(arg2);

                break;
            case 4:
                solution = arg2;
                queryString = String.valueOf(arg1*arg2) + " / " + String.valueOf(arg1);

                break;
        }
    }

    public Orc(int difficulty, boolean boss){
        this.difficulty = difficulty;
        bossOrc = boss;
        if(!bossOrc){
            new Orc(difficulty);        //not sure what to do here ...
        }else {
            double determiner = Math.random();

            if (determiner < .5 - .05 * difficulty) {
                operator = 1;
            } else if (determiner < 1 - .1 * difficulty) {
                operator = 2;
            } else if (determiner < 1 - .05 * difficulty) {
                operator = 3;
            } else {
                operator = 4;
            }

            solution = 21 + 10 * difficulty;
            while (solution > 20 + 10 * difficulty) {

                Orc arg1 = new Orc(difficulty);
                Orc arg2 = new Orc(difficulty);

                switch (operator) {
                    case 1:
                        solution = arg1.solution + arg2.solution;
                        queryString = arg1.queryString + " + " + arg2.queryString;

                        break;
                    case 2:
                        if (arg1.solution < arg2.solution) {
                            Orc storage = arg1;
                            arg1 = arg2;
                            arg2 = storage;
                        }
                        solution = arg1.solution - arg2.solution;
                        queryString = "(" + arg1.queryString + ") - (" + arg2.queryString + ")";

                        break;
                    case 3:
                        while (arg1.solution == 0) {
                            arg1 = new Orc(difficulty);
                        }
                        while (arg2.solution == 0) {
                            arg2 = new Orc(difficulty);
                        }

                        solution = arg1.solution * arg2.solution;
                        queryString = "(" + arg1.queryString + ") * (" + arg2.queryString + ")";

                        break;
                    case 4:
                        while (arg1.solution == 0) {
                            arg1 = new Orc(difficulty);
                        }
                        while (arg2.solution == 0) {
                            arg2 = new Orc(difficulty);
                        }

                        if (arg1.solution > arg2.solution) {
                            Orc storage = arg1;
                            arg1 = arg2;
                            arg2 = storage;
                        }
                        solution = arg1.solution;
                        queryString = arg2.solution * arg1.solution + " / (" + arg2.queryString + ")";

                        break;
                }
            }
        }
    }


    public static int getRandInt(int low, int high){
        int returnMe = 0;
        double rand = Math.random();
        returnMe = (int) Math.round(rand*(high-low)) + low;
        return returnMe;
    }
}
