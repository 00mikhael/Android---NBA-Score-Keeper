package com.example.gravity.nbaplayoffs;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {

    private int mTime = 0;
    private int mHomeScoreValue = 0;
    private int mAwayScoreValue = 0;
    private long mTotalCountDownTime;

    private boolean mStarted = false;
    private boolean mPaused = false;
    private boolean mCancelled = false;
    private boolean mFinished = false;
    private boolean mSet = false;

    private TextView  mHomeScore;
    private TextView mAwayScore;
    private TextView mTimeText;
    private TextView mResultText;
    private CountDownTimer1 mCountDownTimer1;
    private AlertDialog dialog;


    private Button mStartButton;
    private Button mPauseButton;
    private Button mEndButton;
    private Button mSetButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mTimeText = (TextView) findViewById(R.id.time_text);
        mResultText = (TextView) findViewById(R.id.result_text);
        mHomeScore = (TextView) findViewById(R.id.home_score);
        mAwayScore = (TextView) findViewById(R.id.away_score);
        Button mOneHomeButton = (Button) findViewById(R.id.one_point_home);
        mOneHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OneForHome();
            }
        });
        Button mTwoHomeButton = (Button) findViewById(R.id.two_point_home);
        mTwoHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwoForHome();
            }
        });

        Button mThreeHomeButton = (Button) findViewById(R.id.three_point_home);
        mThreeHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreeForHome();
            }
        });

        Button mOneAwayButton = (Button) findViewById(R.id.one_point_away);
        mOneAwayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OneForAway();
            }
        });
        Button mTwoAwayButton = (Button) findViewById(R.id.two_point_away);
        mTwoAwayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwoForAway();

            }
        });
        Button mThreeAwayButton = (Button) findViewById(R.id.three_point_away);
        mThreeAwayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreeForAway();
            }
        });

        mSetButton = (Button) findViewById(R.id.reset);
        mSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimer();
            }
        });

        mStartButton = (Button) findViewById(R.id.start);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSet == true) {
                    mTotalCountDownTime = 60 * mTime * 1000;
                    mSet = false;
                    mCancelled = false;
                    mPaused = false;
                    mFinished = false;
                    mStarted = true;
                    Reset();
                    mResultText.setText("");
                    mEndButton.setVisibility(Button.VISIBLE);
                    mPauseButton.setVisibility(Button.VISIBLE);
                    mStartButton.setVisibility(Button.INVISIBLE);
                    mSetButton.setVisibility(Button.INVISIBLE);
                    startTimer();
                }
                else {
                    Toast.makeText(GameActivity.this, "Set Time", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mPauseButton = (Button) findViewById(R.id.pause);
        mPauseButton.setVisibility(Button.GONE);
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPaused == false) {
                    mCountDownTimer1.pause();
                    mPaused = true;
                    mPauseButton.setText(R.string.resume);
                }
                else {
                    mCountDownTimer1.resume();
                    mPaused = false;
                    mPauseButton.setText(R.string.pause);
                }
            }
        });

        mEndButton  = (Button) findViewById(R.id.end);
        mEndButton.setVisibility(Button.GONE);
        mEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountDownTimer1.cancel();
                mCancelled = true;
                mPauseButton.setVisibility(Button.GONE);
                mEndButton.setVisibility(Button.GONE);
                mStartButton.setVisibility(Button.VISIBLE);
                mSetButton.setVisibility(Button.VISIBLE);
                mTimeText.setText(R.string._00_00);
                DisplayResult();
            }
        });
    }
    public void setTimer() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(GameActivity.this);
        final View mAlertView = getLayoutInflater().inflate(R.layout.timer_layout, null);
        final View mAlertTitle = getLayoutInflater().inflate(R.layout.dialog_title, null);
        final EditText mEditTimer = (EditText) mAlertView.findViewById(R.id.edit_timer);
        Button mEnterButton = (Button) mAlertView.findViewById(R.id.enter_timer);
        Button mCancelButton = (Button) mAlertView.findViewById(R.id.cancel_timer);
        mBuilder.setTitle(R.string.timer);
        mBuilder.setCustomTitle(mAlertTitle);
        mBuilder.setCancelable(false);
        mEnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mEditTimer.getText().toString().isEmpty()) {
                    try {
                        int time = Integer.parseInt(mEditTimer.getText().toString());
                        if ((time > 0) && (time <= 30)) {
                            mTime = time;
                            if (mTime < 10) {
                                mTimeText.setText("0"+mTime+":00");
                            }
                            else {
                                mTimeText.setText(""+mTime+":00");
                            }
                            mSet = true;
                            dialog.dismiss();
                            mSetButton.setText(R.string.reset);
                            Toast.makeText(GameActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            mEditTimer.setError("Choose time between 1 to 30");
                        }
                    }
                    catch (RuntimeException e) {
                        mEditTimer.setError("Invalid Time");
                    }
                }
                else{
                    mEditTimer.setError("Time Empty");
                }
            }
        });
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                Toast.makeText(GameActivity.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
            }
        });
        mBuilder.setView(mAlertView);
        dialog = mBuilder.create();
        dialog.show();
        }



    public void startTimer() {
        mCountDownTimer1 = new CountDownTimer1(mTotalCountDownTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                mTimeText.setText(String.format("%02d", seconds / 60) + ":" + String.format("%02d", seconds % 60));
            }

            @Override
            public void onFinish() {
                mTimeText.setText(R.string.time_up);
                mStartButton.setVisibility(Button.VISIBLE);
                mEndButton.setVisibility(Button.GONE);
                mPauseButton.setVisibility(Button.GONE);
                mSetButton.setVisibility(Button.VISIBLE);
                mFinished = true;
                DisplayResult();
            }
        }.start();
    }


    public void OneForHome() {
        if ((mStarted == true) && (mPaused == false) && (mCancelled == false) && (mFinished == false)) {
            mHomeScoreValue = mHomeScoreValue + 1;
            mHomeScore.setText("" + mHomeScoreValue);
        }
    }
    public void TwoForHome() {
        if ((mStarted == true) && (mPaused == false) && (mCancelled == false)&&(mFinished == false)) {
            mHomeScoreValue = mHomeScoreValue + 2;
            mHomeScore.setText("" + mHomeScoreValue);
        }
    }
    public void ThreeForHome() {
        if ((mStarted == true) && (mPaused == false) && (mCancelled == false)&& (mFinished == false)) {
            mHomeScoreValue = mHomeScoreValue + 3;
            mHomeScore.setText("" + mHomeScoreValue);
        }
    }
    public void OneForAway() {
        if ((mStarted == true) && (mPaused == false) && (mCancelled == false)&& (mFinished == false)) {
            mAwayScoreValue = mAwayScoreValue + 1;
            mAwayScore.setText("" + mAwayScoreValue);
        }
    }
    public void TwoForAway() {
        if ((mStarted == true) && (mPaused == false) && (mCancelled == false)&& (mFinished == false)) {
            mAwayScoreValue = mAwayScoreValue + 2;
            mAwayScore.setText("" + mAwayScoreValue);
        }
    }
    public void ThreeForAway() {
        if ((mStarted == true) && (mPaused == false) && (mCancelled == false)&& (mFinished == false)) {
            mAwayScoreValue = mAwayScoreValue + 3;
            mAwayScore.setText("" + mAwayScoreValue);
        }
    }
    public void Reset() {
        mAwayScoreValue = 0;
        mHomeScoreValue = 0;
        mHomeScore.setText("" + mHomeScoreValue);
        mAwayScore.setText("" + mAwayScoreValue);
    }

    public void DisplayResult() {
        if (mHomeScoreValue > mAwayScoreValue) {
            mResultText.setText("Home Team Win!!\nHome Score: " + mHomeScoreValue +
                "\nAway Score: " + mAwayScoreValue + "\nDifference: " + (mHomeScoreValue - mAwayScoreValue));
        }
        else if (mHomeScoreValue == mAwayScoreValue) {
            mResultText.setText("Its a Draw!!\nHome Score: " + mHomeScoreValue +
                    "\nAway Score: " + mAwayScoreValue + "\nDifference: " + (mHomeScoreValue - mAwayScoreValue));
        }
        else {
            mResultText.setText("Away Team Win!!\nHome Score: " + mHomeScoreValue +
                    "\nAway Score: " + mAwayScoreValue + "\nDifference: " + (mAwayScoreValue - mHomeScoreValue));
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
