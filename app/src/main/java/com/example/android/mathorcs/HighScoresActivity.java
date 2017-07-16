package com.example.android.mathorcs;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.database.Cursor;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.Touch;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.NavUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.mathorcs.resources.highscores.HighScores;
import com.example.android.mathorcs.resources.highscores.HighScoresContract;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HighScoresActivity extends AppCompatActivity {

    private static int highScoreItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
        MainActivity.currentActivity = this;

//        Toolbar theToolbar = (Toolbar) findViewById(R.id.the_highscores_toolbar);
//        setSupportActionBar(theToolbar);

        RecyclerView highScoresRecycler;

        highScoresRecycler = (RecyclerView) findViewById(R.id.high_scores_recycler_view);
        highScoresRecycler.setLayoutManager(new LinearLayoutManager(this));

        Cursor cursor = HighScores.theHighScores.getHighScores();


        HighScoresAdapter mAdapter = new HighScoresAdapter(this, cursor);

        highScoresRecycler.setAdapter(mAdapter);

        highScoreItemCount = 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {

            case R.id.go_back_toolbar_button:
                NavUtils.navigateUpFromSameTask(this);
                break;
        }

        return true;
    }

    public void handleButton(View v) {

        switch (v.getId()) {
            case R.id.hs_back_button:
                NavUtils.navigateUpFromSameTask(this);

                break;
        }
    }

    class HighScoresAdapter extends RecyclerView.Adapter<HighScoresAdapter.HighScoreHolder>{

        private Context mContext;
        private Cursor mCursor;

        HighScoresAdapter(Context context, Cursor cursor){
            this.mContext = context;
            this.mCursor = cursor;
        }

        @Override
        public HighScoreHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.high_scores_item, parent, false);

            return new HighScoreHolder(view);
        }

        @Override
        public void onBindViewHolder(HighScoresAdapter.HighScoreHolder holder, int position) {
            if(!mCursor.moveToPosition(position)){
                return;
            }

            highScoreItemCount ++;

            String name = mCursor.getString(mCursor.getColumnIndex(HighScoresContract.HighScoresEntry.PLAYER_NAME));
            int score = mCursor.getInt(mCursor.getColumnIndex(HighScoresContract.HighScoresEntry.HIGH_SCORE));
            String difficulty = mCursor.getString(mCursor.getColumnIndex(HighScoresContract.HighScoresEntry.DIFFICULTY));
            int level = mCursor.getInt(mCursor.getColumnIndex(HighScoresContract.HighScoresEntry.LEVEL));
            long id = mCursor.getLong(mCursor.getColumnIndex(HighScoresContract.HighScoresEntry._ID));

            holder.nameTextView.setText(name);
            holder.scoreTextView.setText(String.valueOf(-1 * score));
            holder.difficultyTextView.setText(difficulty);
            holder.levelTextView.setText(String.valueOf(level));
            holder.placeTextView.setText(String.valueOf(highScoreItemCount));
        }

        @Override
        public int getItemCount() {return mCursor.getCount();}


        public void swapCursor(Cursor cursor){
            if(mCursor != null){
                mCursor.close();
            }
            mCursor = cursor;
            if(cursor != null){
                this.notifyDataSetChanged();
            }
        }


        class HighScoreHolder extends RecyclerView.ViewHolder{

            TextView placeTextView;
            TextView difficultyTextView;
            TextView levelTextView;
            TextView nameTextView;
            TextView scoreTextView;

            HighScoreHolder(View itemView) {
                super(itemView);
                placeTextView = (TextView) itemView.findViewById(R.id.high_score_place_text_view);
                difficultyTextView = (TextView) itemView.findViewById(R.id.high_score_difficulty_text_view);
                levelTextView = (TextView) itemView.findViewById(R.id.high_score_level_text_view);
                nameTextView = (TextView) itemView.findViewById(R.id.high_score_name_text_view);
                scoreTextView = (TextView) itemView.findViewById(R.id.high_score_score_text_view);
            }
        }
    }
}
