package com.example.android.mathorcs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mathorcs.resources.Orc;
import com.example.android.mathorcs.resources.highscores.HighScores;

import org.w3c.dom.Text;

public class PlayGame extends AppCompatActivity {

    public static long startingTimePerQuestion;
    public static long timePerQuestionToApproach;
    public static long timePerBonus;
    public static double timeDecreaseFactor;
    public static int numberOfLevels;
    public static int difficulty;
    public static boolean timerEnabled;
    public static boolean tipsEnabled;
    public static boolean timerBarEnabled;
    private static String name;
    private static boolean timerBackgroundNotSet;

    private View mContentView;
    private TextView mMainTextView;
    private TextView mSolutionText;
    private TextView mPreviousSolutionText;
    private Toast currentToast;
    private TextView mScoreCard;
    private TextView mLivesRemaining;
    private TextView mLevelIndicator;
    private Button mMoreTimeButton;
    private Button mApproxButton;
    private Button mUndoButton;
    private View mScoreView;
    private TextView mFinalScoreTextView;
    private TextView mFinalScoreTextView2;
    private LinearLayout mEndGamePane;
    private LinearLayout mBonusLayout;
    private TextView mFinalTimeTextView;
    private TextView mCorrectProblemsTextView;
    private TextView mTimerTextView;
    private LinearLayout mMoreTimeTipLayout;
    private LinearLayout mApproxTipLayout;
    private LinearLayout mRewardTipLayout;
    private FrameLayout mTimerBar;
    private Button mGoBackButton;
    private Button mGoBackButtonEndGame;

    //Keypad vars
    private GridLayout mSolveButtonGrid;
    private Button mSolveButtonZero;
    private Button mSolveButtonOne;
    private Button mSolveButtonTwo;
    private Button mSolveButtonThree;
    private Button mSolveButtonFour;
    private Button mSolveButtonFive;
    private Button mSolveButtonSix;
    private Button mSolveButtonSeven;
    private Button mSolveButtonEight;
    private Button mSolveButtonNine;

    private CountDownTimer mCountDownTimer;

    public int currentSolution;
    public int currentSolutionDigits;
    public int currentGuessDigits;

    public int score;
    public int lives;
    public int moreTimeTokens;
    public int approxTokens;
    public int numTimesExtraTimeUsed;
    public int correctAnswers;
    public static int level;
    private static int difficultyCounter;       //increments difficulty when you get 5 correct answers in a row
    private static final int difficultyCounterIncrement = 5;
    private boolean gameOver;
    private boolean firstMoreTimeTokenFound;
    private boolean firstApproxTokenFound;
    private boolean firstRewardFound;
    private int tipDialogsOpen;
    private long startTime;
    private long endTime;
    private long timePerQuestion;
    private long currentTimeInTimer;
    private boolean approxFlag;
    private Orc currentOrc;
    boolean currentOrcBossOrc;

    private Runnable mDoProblemRunnable = new Runnable() {
        @Override
        public void run() {
            Orc thisOrc;

            if(difficultyCounter==difficultyCounterIncrement-1){
                currentOrcBossOrc = true;
            }else{
                currentOrcBossOrc = false;
            }

            if(!currentOrcBossOrc){
                thisOrc = new Orc(level);
            }else{
                thisOrc = new Orc(level, true);
            }
            currentOrc = thisOrc;
            mMainTextView.setText(thisOrc.queryString);
            PlayGame.this.currentSolution = thisOrc.solution;

            currentSolutionDigits = 1;
            if(currentSolution>=10){
                currentSolutionDigits = 2;
            }if(currentSolution>=100){
                currentSolutionDigits = 3;
            }if(currentSolutionDigits >=1000){
                currentSolutionDigits = 4;
            }
        }
    };
/*
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);
        MainActivity.currentActivity = this;

        mContentView = findViewById(R.id.fullscreen_content);
        mMainTextView = (TextView) findViewById(R.id.main_display_board);
        mSolutionText = (TextView) findViewById(R.id.solution_edit_text);
        mPreviousSolutionText = (TextView) findViewById(R.id.previous_question_view);
        mScoreCard = (TextView) findViewById(R.id.scorecard);
        mLivesRemaining = (TextView) findViewById(R.id.lives_remaining);
        mLevelIndicator = (TextView) findViewById(R.id.level_indicator);
        mMoreTimeButton = (Button) findViewById(R.id.more_time_button);
        mApproxButton = (Button) findViewById(R.id.approx_button);
        mUndoButton = (Button) findViewById(R.id.undo_button);
        mScoreView = findViewById(R.id.scores_pane);
        mFinalScoreTextView = (TextView) findViewById(R.id.final_score_textview);
        mFinalScoreTextView2 = (TextView) findViewById(R.id.final_score_textview_2);
        mEndGamePane = (LinearLayout) findViewById(R.id.end_game_pane);
        mCorrectProblemsTextView = (TextView) findViewById(R.id.correct_problems_textview);
        mFinalTimeTextView = (TextView) findViewById(R.id.total_time_textview);
        mTimerTextView = (TextView) findViewById(R.id.timer_textview);
        mBonusLayout = (LinearLayout) findViewById(R.id.bonus_button_grid);
        mMoreTimeTipLayout = (LinearLayout) findViewById(R.id.more_time_tip_layout);
        mApproxTipLayout = (LinearLayout) findViewById(R.id.approx_tip_layout);
        mRewardTipLayout = (LinearLayout) findViewById(R.id.reward_tip_layout);
        mTimerBar = (FrameLayout) findViewById(R.id.timer_bar);
        mGoBackButton = (Button) findViewById(R.id.exit_play_button);
        mGoBackButtonEndGame = (Button) findViewById(R.id.final_go_back_button);

        //Keypad vars
        mSolveButtonGrid = (GridLayout) findViewById(R.id.solve_button_grid);
        mSolveButtonZero = (Button) findViewById(R.id.solution_button_zero);
        mSolveButtonOne = (Button) findViewById(R.id.solution_button_one);
        mSolveButtonTwo = (Button) findViewById(R.id.solution_button_two);
        mSolveButtonThree = (Button) findViewById(R.id.solution_button_three);
        mSolveButtonFour = (Button) findViewById(R.id.solution_button_four);
        mSolveButtonFive = (Button) findViewById(R.id.solution_button_five);
        mSolveButtonSix = (Button) findViewById(R.id.solution_button_six);
        mSolveButtonSeven = (Button) findViewById(R.id.solution_button_seven);
        mSolveButtonEight = (Button) findViewById(R.id.solution_button_eight);
        mSolveButtonNine = (Button) findViewById(R.id.solution_button_nine);

        if(!timerEnabled){
            mTimerTextView.setVisibility(View.INVISIBLE);
            mTimerBar.setVisibility(View.INVISIBLE);
        }
        if(!timerBarEnabled){
            mTimerBar.setVisibility(View.INVISIBLE);
        }

        gameOver = false;
        firstApproxTokenFound=false;
        firstMoreTimeTokenFound=false;
        firstRewardFound=false;
        tipDialogsOpen=0;
        level = 1;
        difficultyCounter = 0;
        lives = 3;
        moreTimeTokens = 0;
        approxTokens = 0;
        approxFlag = false;
        numTimesExtraTimeUsed = 0;
        currentOrcBossOrc = false;
        correctAnswers = 0;
        timePerQuestion = startingTimePerQuestion;
        startTime = System.currentTimeMillis();
        newProblem();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        mHidePart2Runnable.run();
    }

    private void newProblem(){
        mSolutionText.setText("");
        currentGuessDigits = 0;
        numTimesExtraTimeUsed = 0;
        mTimerBar.setBackgroundResource(R.color.problem_color);
        timerBackgroundNotSet = true;

        mUndoButton.setVisibility(View.INVISIBLE);
        mDoProblemRunnable.run();
        if(timerEnabled) {
            startCountDownTimer(timePerQuestion);
        }
    }

    private void startCountDownTimer(long millis) {
        if(mCountDownTimer != null){
            mCountDownTimer.cancel();
        }
        if(currentOrcBossOrc) millis *= 2;

        mCountDownTimer = new CountDownTimer(millis,50) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimerTextView.setText(String.valueOf((double)Math.round(millisUntilFinished/100)/10));

                currentTimeInTimer = millisUntilFinished;

                updateTimerBar();
            }

            @Override
            public void onFinish() {
                if(gameOver){
                    mTimerTextView.setText("Time's up");
                    mPreviousSolutionText.setText(getQuestionSolutionText());
                    mPreviousSolutionText.setTextColor(Color.RED);
                }else{
                    makeToast("out of time");
                    mPreviousSolutionText.setText(getQuestionSolutionText());
                    decrementLives();
                    mPreviousSolutionText.setTextColor(Color.RED);
                }
            }
        }.start();
    }

    public void isAnswerRight(){

        mPreviousSolutionText.setText(getQuestionSolutionText());

        if(approxFlag){
            approxFlag = false;

            int answerGiven = Integer.parseInt(mSolutionText.getText().toString());

            if((double)Math.abs(answerGiven-currentSolution) / (answerGiven + currentSolution) < .1 || Math.abs(answerGiven - currentSolution) < 3){
                incrementScore();       //close enough!
                mPreviousSolutionText.setTextColor(Color.GREEN);
            }
            else{
                decrementLives();
                mPreviousSolutionText.setTextColor(Color.RED);
            }

        }else {
            if (mSolutionText.getText().toString().contentEquals(Integer.toString(currentSolution))) {
                incrementScore();       //correct answer!
                mPreviousSolutionText.setTextColor(Color.GREEN);
            } else {
                decrementLives();
                mPreviousSolutionText.setTextColor(Color.RED);
            }
        }
    }

    public String getQuestionSolutionText(){
        if (mSolutionText.getText().toString().contentEquals(Integer.toString(currentSolution))){
            return currentOrc.queryString + " = " + mSolutionText.getText().toString();

        }else if(!mSolutionText.getText().toString().equals("")){
            return currentOrc.queryString + " = " + currentOrc.solution + ", not " + mSolutionText.getText().toString();
        }else{
            return currentOrc.queryString + " = " + currentOrc.solution;
        }
    }

    public void handleButton(View v){

        switch (v.getId()){
            case R.id.exit_play_button:
                gameOver = true;
                NavUtils.navigateUpFromSameTask(this);
                break;

            case R.id.final_go_back_button:
                NavUtils.navigateUpFromSameTask(this);
                break;

            case R.id.solution_button_nine:
                mSolutionText.append("9");
                currentGuessDigits++;
                if(currentGuessDigits==currentSolutionDigits){
                    isAnswerRight();
                }else{
                    mUndoButton.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.solution_button_eight:
                mSolutionText.append("8");
                currentGuessDigits++;
                if(currentGuessDigits==currentSolutionDigits){
                    isAnswerRight();
                }else{
                    mUndoButton.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.solution_button_zero:
                mSolutionText.append("0");
                currentGuessDigits++;
                if(currentGuessDigits==currentSolutionDigits){
                    isAnswerRight();
                }else{
                    mUndoButton.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.solution_button_one:
                mSolutionText.append("1");
                currentGuessDigits++;
                if(currentGuessDigits==currentSolutionDigits){
                    isAnswerRight();
                }else{
                    mUndoButton.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.solution_button_two:
                mSolutionText.append("2");
                currentGuessDigits++;
                if(currentGuessDigits==currentSolutionDigits){
                    isAnswerRight();
                }else{
                    mUndoButton.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.solution_button_three:
                mSolutionText.append("3");
                currentGuessDigits++;
                if(currentGuessDigits==currentSolutionDigits){
                    isAnswerRight();
                }else{
                    mUndoButton.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.solution_button_four:
                mSolutionText.append("4");
                currentGuessDigits++;
                if(currentGuessDigits==currentSolutionDigits){
                    isAnswerRight();
                }else{
                    mUndoButton.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.solution_button_five:
                mSolutionText.append("5");
                currentGuessDigits++;
                if(currentGuessDigits==currentSolutionDigits){
                    isAnswerRight();
                }else{
                    mUndoButton.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.solution_button_six:
                mSolutionText.append("6");
                currentGuessDigits++;
                if(currentGuessDigits==currentSolutionDigits){
                    isAnswerRight();
                }else{
                    mUndoButton.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.solution_button_seven:
                mSolutionText.append("7");
                currentGuessDigits++;
                if(currentGuessDigits==currentSolutionDigits){
                    isAnswerRight();
                }else{
                    mUndoButton.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.undo_button:
                mSolutionText.setText("");
                currentGuessDigits = 0;
                mUndoButton.setVisibility(View.INVISIBLE);
                break;

            case R.id.more_time_button:
                if(moreTimeTokens>0){
                    extendCountdownTimer(timePerBonus);
                    moreTimeTokens--;
                    numTimesExtraTimeUsed++;
                    setMoreTimeTokensText();
                }else{
                    makeToast("no time tokens");
                }
                break;

            case R.id.approx_button:
                if(approxTokens>0){
                    approxTokens--;
                    setApproxTokensText();
                    approxFlag = true;
                }else{
                    makeToast("no approx tokens");
                }
                break;

            case R.id.more_time_tip_button:
                mMoreTimeTipLayout.setVisibility(View.INVISIBLE);
                tipDialogsOpen --;
                if(tipDialogsOpen == 0) startCountDownTimer(timePerQuestion);
                break;

            case R.id.approx_tip_button:
                mApproxTipLayout.setVisibility(View.INVISIBLE);
                tipDialogsOpen --;
                if(tipDialogsOpen == 0) startCountDownTimer(timePerQuestion);
                break;

            case R.id.reward_tip_button:
                mRewardTipLayout.setVisibility(View.INVISIBLE);
                tipDialogsOpen --;
                if(tipDialogsOpen ==0) startCountDownTimer(timePerQuestion);
                break;

        }
    }

    public void makeToast(String text){
        if(currentToast != null) {
            currentToast.cancel();
        }
        currentToast = Toast.makeText(PlayGame.this, text, Toast.LENGTH_SHORT);
        currentToast.show();
    }

    public void extendCountdownTimer(long millisToExtend){
        startCountDownTimer(currentTimeInTimer+millisToExtend);
    }

    public void incrementScore(){
        score += level;
        correctAnswers++;
        mScoreCard.setText("score: "+score);
        difficultyCounter++;

        if(difficultyCounter>=difficultyCounterIncrement){
            incrementLevel();
        }
        double speedRatio = (double)(currentTimeInTimer - (numTimesExtraTimeUsed*timePerBonus))/timePerQuestion;

        newProblem();
        getRandomReward(speedRatio);
    }

    public void incrementLevel(){
        level++;
        difficultyCounter=0;
        mLevelIndicator.setText("level: "+level);

        timePerQuestion  = timePerQuestionToApproach + (Math.round(timeDecreaseFactor * (timePerQuestion-timePerQuestionToApproach)));

        if(level>numberOfLevels){
            //*!*//
            victory();
            //*!*//
        }
    }

    public void incrementLives(){
        lives++;
        mLivesRemaining.setText("lives: " + lives);
    }

    public void decrementLives(){
        score -= level;
        lives--;
        mScoreCard.setText("score: " + score);
        mLivesRemaining.setText("lives: " + lives);
        difficultyCounter = 0;

        if(lives<=0){
            // :(
            lose();
            // :(
            return;
        }

        newProblem();
    }

    public void setMoreTimeTokensText(){
        mMoreTimeButton.setText("x" + String.valueOf(moreTimeTokens));
    }

    public void setApproxTokensText(){
        mApproxButton.setText("x" + String.valueOf(approxTokens));
    }

    public void updateTimerBar(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                double ratio = (double)currentTimeInTimer/timePerQuestion;
                if(currentOrcBossOrc) ratio /= 2;
                if(timerBackgroundNotSet && ratio < .65){
                    timerBackgroundNotSet = false;
                    mTimerBar.setBackgroundResource(R.color.problem_color_dark);
                }
                mTimerBar.setLayoutParams(new LinearLayout.LayoutParams((int)Math.round(ratio*600), 15));
            }
        });
    }

    public void getRandomReward(double speedRatio){
        if(speedRatio > .65) {
            if(Math.random() > .9){
                incrementLives();
                makeToast("extra life!");
                checkIfFirstRewardAndGiveTip();
            }
            if(Math.random() > .8){
                moreTimeTokens++;
                makeToast("found more time token!");
                setMoreTimeTokensText();
                if(!firstMoreTimeTokenFound && tipsEnabled){
                    //give more time token tip
                    mCountDownTimer.cancel();
                    mMoreTimeTipLayout.setVisibility(View.VISIBLE);
                    tipDialogsOpen ++;
                }
                firstMoreTimeTokenFound = true;
                checkIfFirstRewardAndGiveTip();
            }
            if(Math.random() > .8){
                approxTokens++;
                makeToast("found approx token!");
                setApproxTokensText();
                if(!firstApproxTokenFound && tipsEnabled){
                    //give approx token tip
                    mCountDownTimer.cancel();
                    mApproxTipLayout.setVisibility(View.VISIBLE);
                    tipDialogsOpen ++;
                }
                firstApproxTokenFound = true;
                checkIfFirstRewardAndGiveTip();
            }

        }
    }

    public void checkIfFirstRewardAndGiveTip(){
        if(!firstRewardFound && tipsEnabled){
            //give reward tip
            mRewardTipLayout.setVisibility(View.VISIBLE);
            mCountDownTimer.cancel();
            tipDialogsOpen++;

        }
        firstRewardFound = true;
    }

    public void victory(){

        endTime = System.currentTimeMillis();
        double totalTime = (double) Math.round((endTime - startTime)/10) / 100;
        mMainTextView.setText("WOW!\nYou Did It!");
        initializeFinalScreen(totalTime);

        int difficultyBonus = (int) Math.round(difficulty * .33333 * score);
        if(difficultyBonus < 0) difficultyBonus = 0;
        int megaModeBonus = (int) Math.round(Orc.megaModifier * .2 * score);
        if(megaModeBonus < 0) megaModeBonus = 0;
        final int totalScore = score + difficultyBonus + megaModeBonus + 1000;
        animateFinalScreen(difficultyBonus, megaModeBonus, totalScore, true);

        storeGameInfoInSharedPrefs((int)Math.round(totalTime), correctAnswers);
        incrementGamesWonInSharedPrefs();
    }

    public void lose(){

        endTime = System.currentTimeMillis();
        double totalTime = (double) Math.round((endTime - startTime)/10) / 100;
        mMainTextView.setText("NICE\nTRY!");
        initializeFinalScreen(totalTime);

        int difficultyBonus = (int) Math.round(difficulty * .33333 * score);
        if(difficultyBonus < 0) difficultyBonus = 0;
        int megaModeBonus = (int) Math.round(Orc.megaModifier * .2 * score);
        if(megaModeBonus < 0) megaModeBonus = 0;
        final int totalScore = score + difficultyBonus + megaModeBonus;

        storeGameInfoInSharedPrefs((int)Math.round(totalTime), correctAnswers);

        animateFinalScreen(difficultyBonus, megaModeBonus, totalScore, false);
    }

    public void initializeFinalScreen(double totalTime){

        mFinalTimeTextView.setText("in " + totalTime + " seconds!");
        mCorrectProblemsTextView.setText("You got " + correctAnswers + " problems right");
        mEndGamePane.setVisibility(View.VISIBLE);
        mGoBackButtonEndGame.setVisibility(View.INVISIBLE);

        mSolutionText.setVisibility(View.INVISIBLE);
        mTimerTextView.setVisibility(View.INVISIBLE);
        mTimerBar.setVisibility(View.INVISIBLE);
        mSolveButtonGrid.setVisibility(View.INVISIBLE);
        mBonusLayout.setVisibility(View.INVISIBLE);
        mUndoButton.setVisibility(View.INVISIBLE);
        mGoBackButton.setVisibility(View.INVISIBLE);

        gameOver = true;
    }

    public void animateFinalScreen(final int difficultyBonus, final int megaModeBonus, final int totalScore, final boolean victory){

        Handler handler = new Handler();
        Runnable firstR = new Runnable() {
            @Override
            public void run() {

                mFinalScoreTextView.setText("score: " + score);

            }
        };
        Runnable thirdR = new Runnable() {
            @Override
            public void run() {

                mFinalScoreTextView2.setText(mFinalScoreTextView.getText().toString());

                mFinalScoreTextView.setText("diff bonus: " + difficultyBonus);

                mGoBackButtonEndGame.setVisibility(View.VISIBLE);

            }
        };
        Runnable secondR = new Runnable() {
            @Override
            public void run() {

                mFinalScoreTextView2.setText(mFinalScoreTextView.getText().toString());

                mFinalScoreTextView.setText("mm bonus: " + megaModeBonus);

            }
        };

        Runnable fourthR = new Runnable() {
            @Override
            public void run() {
                mFinalScoreTextView2.setText(mFinalScoreTextView.getText().toString());

                mFinalScoreTextView.setText("victory bonus: 1000");

            }
        };

        Runnable lastR = new Runnable() {
            @Override
            public void run() {

                mFinalScoreTextView2.setText("Total Score:");

                if(!victory){
                    mFinalScoreTextView.setText(score + " + " + megaModeBonus + " + " + difficultyBonus + " = " + totalScore);
                }else{
                    mFinalScoreTextView.setText(score + " + " + megaModeBonus + " + " + difficultyBonus + " + 1000 = " + totalScore);
                }

            }
        };


        Runnable finalR = new Runnable() {
            @Override
            public void run() {
                buildHighScoresDialog(totalScore);
            }
        };

        handler.postDelayed(firstR, 0);
        handler.postDelayed(secondR, 1000);
        handler.postDelayed(thirdR, 2000);
        if(!victory){
            handler.postDelayed(lastR, 3000);
        }else{
            handler.postDelayed(fourthR, 3000);
            handler.postDelayed(lastR, 4000);
        }

        if(totalScore > HighScores.getScoreToBeat()) handler.postDelayed(finalR, 5000);

    }

    public void buildHighScoresDialog(final int totalScore){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.currentActivity);
        builder.setTitle("Incredible! High Score!  " + String.valueOf(totalScore) + " points!");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Enter your name:");
        builder.setView(input);

        builder.setPositiveButton("Great!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name = input.getText().toString();
                HighScores.addToHighScores(
                        name,
                        -1 * totalScore,
                        Orc.megaModifier,
                        MainActivity.difficulty,
                        level);
            }

        });
        builder.setNegativeButton("Nope", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void storeGameInfoInSharedPrefs(int totalTime, int totalCorrect){

        SharedPreferences sharedP = getSharedPreferences(getString(R.string.shared_prefs_key), Activity.MODE_PRIVATE);

        int allTimeCorrect = sharedP.getInt(getString(R.string.all_time_kills_key), 0);
        int allTimeTime = sharedP.getInt(getString(R.string.all_time_time_key), 0);
        int highestLevelReached = 0;
        int gamesFinished = sharedP.getInt(getString(R.string.all_time_games_finised_key),0) + 1;

        if(difficulty == 1){
            highestLevelReached = sharedP.getInt(getString(R.string.highest_level_reached_easy_key), 0);
        } else if(difficulty == 2){
            highestLevelReached = sharedP.getInt(getString(R.string.highest_level_reached_medium_key), 0);
        }else if(difficulty == 3){
            highestLevelReached = sharedP.getInt(getString(R.string.highest_level_reached_hard_key), 0);
        }

        SharedPreferences.Editor spEdit = sharedP.edit();

        spEdit.putInt(getString(R.string.all_time_kills_key), (allTimeCorrect + totalCorrect));
        spEdit.putInt(getString(R.string.all_time_time_key), (allTimeTime + totalTime));

        if(difficulty == 1){
            if(level > highestLevelReached){
                spEdit.putInt(getString(R.string.highest_level_reached_easy_key), level);
            }
        } else if(difficulty == 2){
            if(level > highestLevelReached) {
                spEdit.putInt(getString(R.string.highest_level_reached_medium_key), level);
            }
        }else if(difficulty == 3){
            if(level>highestLevelReached){
                spEdit.putInt(getString(R.string.highest_level_reached_hard_key), level);
            }
        }

        spEdit.putInt(getString(R.string.all_time_games_finised_key), gamesFinished);

        spEdit.apply();

    }

    public void incrementGamesWonInSharedPrefs(){

        SharedPreferences sharedP = getSharedPreferences(getString(R.string.shared_prefs_key), Activity.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sharedP.edit();
        spEdit.putInt(getString(R.string.all_time_games_won_key), sharedP.getInt(getString(R.string.all_time_games_won_key),0) + 1);
        spEdit.apply();

    }

}
