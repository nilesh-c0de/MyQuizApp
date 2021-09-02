package com.example.myquiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myquiz.mqUtils.AddQuestion;
import com.example.myquiz.mqUtils.StoreResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

    DatabaseReference database, ref, refToResults;
    RadioButton rb1, rb2, rb3;
    RadioGroup rg;
    TextView tvScore, tvQuestionNumber, tvQuestion, tvCountDown;
    Button btnConfirmNext;
    int questionCounter, totalQuestions;
    ColorStateList textColorDefaultRb;
    private boolean isAnswered;
    private int score;
    List<AddQuestion> list = new ArrayList<AddQuestion>();
    public static final String EXTRA_SCORE = "result";
    private static final long COUNTDOWN_IN_MILLIS = 30000;
    private ColorStateList textColorDefaultCd;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private long backPressedTime;
    private int current;
    String loadSubject;
    String loadusername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        showStartDialog();

        Intent getSubjectName = getIntent();
        String s = getSubjectName.getStringExtra(StartQuiz.EXTRA_SUBJECT);
        String u = getSubjectName.getStringExtra(StartQuiz.EXTRA_SENDUSERNAME);
        loadSubject = s;
        loadusername = u;

        database = FirebaseDatabase.getInstance().getReference().child("subjects").child(s);
        refToResults = FirebaseDatabase.getInstance().getReference().child("results");
        rg = (RadioGroup) findViewById(R.id.radioGroup);
        rb1 = (RadioButton) findViewById(R.id.radioButton1);
        rb2 = (RadioButton) findViewById(R.id.radioButton2);
        rb3 = (RadioButton) findViewById(R.id.radioButton3);
        tvScore = (TextView) findViewById(R.id.textView3);
        tvQuestionNumber = (TextView) findViewById(R.id.textView4);
        tvQuestion = (TextView) findViewById(R.id.textView6);
        tvCountDown = (TextView) findViewById(R.id.textView5);
        btnConfirmNext = (Button) findViewById(R.id.button3);

        textColorDefaultRb = rb1.getTextColors();
        textColorDefaultCd = tvCountDown.getTextColors();

        // 1.This will get total questions.
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                totalQuestions = (int) dataSnapshot.getChildrenCount();
                doSomething(); // This will call setQuestions() method
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*
        list.add(new AddQuestion("Answer is A","A","B","C","A"));
        list.add(new AddQuestion("Answer is B","A","B","C","B"));
        list.add(new AddQuestion("Answer is C","A","B","C","C"));
        list.add(new AddQuestion("Answer is C","A","B","C","C"));
        list.add(new AddQuestion("Answer is B","A","B","C","B"));
        */

        btnConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isAnswered)
                {
                    if(rb1.isChecked() || rb2.isChecked() || rb3.isChecked())
                    {
                        checkAnswer();
                    }
                    else
                    {
                        Toast toast1 = Toast.makeText(getApplicationContext(),"Please Select an Answer!",Toast.LENGTH_SHORT);
                        toast1.setGravity(Gravity.CENTER_HORIZONTAL, 0, 300);
                        toast1.show();
                    }
                }
                else {
                    showNextQuestion();
                }
            }
        });
    }
    private void showStartDialog() {
        new AlertDialog.Builder(this)
                .setTitle("My Awesome Quiz!")
                .setMessage("Best of luck!!!")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    private void doSomething() {
        for(int i = 0;i<totalQuestions;i++) {
            setQuestions(i+1);
        }
    }

    // 2.This will retrive questions and add question to list.
    // 3.then i want to call showNextQueston means after retriving all question.

    private void setQuestions(final int currentQuestion) {

        ref = database.child(Integer.toString(currentQuestion));
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AddQuestion questionData = dataSnapshot.getValue(AddQuestion.class);
                list.add(questionData);

                if(currentQuestion == totalQuestions)
                {
                    showNextQuestion();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkAnswer() {

        isAnswered = true;

        if(countDownTimer != null) {
            countDownTimer.cancel();
        }

        RadioButton rbSelected = findViewById(rg.getCheckedRadioButtonId());

        Toast toast2 = Toast.makeText(getApplicationContext(),"SelectedAnswer: "+rbSelected.getText().toString()+"\nCorrectAnswer: "+list.get(current).getCorrect(),Toast.LENGTH_SHORT);
        toast2.setGravity(Gravity.CENTER_HORIZONTAL, 0, 300);
        toast2.show();

        if(rbSelected.getText().toString().equals(list.get(current).getCorrect()))
        {
            score++;
            tvScore.setText("Score: "+score);
        }
        if (questionCounter < totalQuestions) {
            btnConfirmNext.setText("Next");
        } else {
            btnConfirmNext.setText("Finish");
        }
    }

    private void showNextQuestion() {
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rg.clearCheck();

        if(questionCounter < list.size())
        {
            current = questionCounter;
            tvQuestion.setText(list.get(current).getQuestion());
            rb1.setText(list.get(current).getOption1());
            rb2.setText(list.get(current).getOption2());
            rb3.setText(list.get(current).getOption3());

            questionCounter++;
            tvQuestionNumber.setText("Question: "+(current+1)+"/"+list.size());
            isAnswered = false;
            btnConfirmNext.setText("Confirm");

            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();
        }
        else
        {
            //Toast.makeText(getApplicationContext(),"Finish",Toast.LENGTH_SHORT).show();
            finishQuiz();
        }
    }

    private void finishQuiz() {
        saveResults();
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    String getDate()
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }
    String getTime()
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }
    private void saveResults() {

        final StoreResult sr = new StoreResult(Integer.toString(score), loadSubject, getDate(), getTime(), "out of "+totalQuestions);
        refToResults.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                refToResults.child(loadusername).child(loadSubject+"_"+getDate()+"_"+getTime()).setValue(sr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void startCountDown() {

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                if(isAnswered == false) {
                    //checkAnswer();
                    if(rb1.isChecked() || rb2.isChecked() || rb3.isChecked())
                    {
                        checkAnswer();
                    }
                    else {
                        Toast toast3 = Toast.makeText(getApplicationContext(), "CorrectAnswer: " + list.get(current).getCorrect(), Toast.LENGTH_SHORT);
                        toast3.setGravity(Gravity.CENTER_HORIZONTAL, 0, 300);
                        toast3.show();
                        isAnswered = true;
                        if (questionCounter < totalQuestions) {
                            btnConfirmNext.setText("Next");
                        } else {
                            btnConfirmNext.setText("Finish");
                        }
                    }
                }
            }
        }.start();
    }

    private void updateCountDownText() {

        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        tvCountDown.setText(timeFormatted);

        if (timeLeftInMillis < 10000) {
            tvCountDown.setTextColor(Color.RED);
        } else {
            tvCountDown.setTextColor(textColorDefaultCd);
        }
    }

    @Override
    public void onBackPressed() {

        // keep it empty or
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finishQuiz();
        } else {
            Toast toast4 = Toast.makeText(this, "Press back again to finish!", Toast.LENGTH_SHORT);
            toast4.setGravity(Gravity.CENTER_HORIZONTAL, 0, 300);
            toast4.show();
        }

        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
