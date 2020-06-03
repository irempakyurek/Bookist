package com.example.bookist.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookist.R;
import com.example.bookist.model.Question;
import com.example.bookist.model.realm.po.RealmBook;
import com.example.bookist.mvp.GoogleBookMVP;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

public class QuestionsActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView question, qCount, timer;
    private Button option1, option2, option3;
    private List<Question> questionsList;
    private int questionNumber;
    private CountDownTimer countDown;
    private int score;
    private RealmResults<RealmBook> realmResults;
    private FirebaseFirestore firestore;
    private int bookID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        question = findViewById(R.id.question);
        qCount = findViewById(R.id.question_num);
        timer = findViewById(R.id.countdown);

        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);

        option1.setOnClickListener(this);
        option2.setOnClickListener(this);
        option3.setOnClickListener(this);

        bookID = getIntent().getIntExtra("book_id", 1);
        firestore = FirebaseFirestore.getInstance();
        getQuestionsList();

        score = 0;

    }


    private void getQuestionsList() {
        questionsList = new ArrayList<>();

        firestore.collection("Bookist").document("Book"+ String.valueOf(bookID)).collection("SET1")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){
                    QuerySnapshot questions = task.getResult();

                    for(QueryDocumentSnapshot doc : questions){
                        questionsList.add(new Question(doc.getString("QUESTION"),
                                doc.getString("A"),
                                doc.getString("B"),
                                doc.getString("C"),
                                Integer.valueOf(doc.getString("ANSWER"))));
                    }

                    setQuestion();
                } else {
                    Toast.makeText(QuestionsActivity.this, task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

/*        questionsList.add(new Question("Question1", "A", "B", "C", 2));
        questionsList.add(new Question("Question2", "B", "B", "D", 2));
        questionsList.add(new Question("Question3", "D", "A", "C", 2));
        questionsList.add(new Question("Question4", "C", "B", "A", 2));*/


    }

    private void setQuestion() {

        timer.setText(String.valueOf(10));

        question.setText(questionsList.get(0).getQuestion());
        option1.setText(questionsList.get(0).getOptionA());
        option2.setText(questionsList.get(0).getOptionB());
        option3.setText(questionsList.get(0).getOptionC());

        qCount.setText(String.valueOf(1) + "/" + String.valueOf(questionsList.size()));
        
        startTimer();

        questionNumber = 0;
    }

    private void startTimer() {

        countDown = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(millisUntilFinished < 15000){
                    timer.setText(String.valueOf(millisUntilFinished / 1000));
                }

            }

            @Override
            public void onFinish() {
                changeQuestion();
            }
        };
        countDown.start();

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {

        int selectedOption = 0;
        switch (v.getId()){
            case R.id.option1:
                selectedOption = 1;
                break;
            case R.id.option2:
                selectedOption = 2;
                break;
            case R.id.option3:
                selectedOption = 3;
                break;
            default:
        }

        if(countDown != null) {
            countDown.cancel();
        }
        checkAnswer(selectedOption, v);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void checkAnswer(int selectedOption, View view){

        if(selectedOption == questionsList.get(questionNumber).getCorrectAnswer()){
            //Right answer
            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            score++;
        }else {
            //Wrong answer
            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.RED));

            switch (questionsList.get(questionNumber).getCorrectAnswer()){
                case 1:
                    option1.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 2:
                    option2.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 3:
                    option3.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
            }
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeQuestion();
            }
        }, 2000);

    }

    private void changeQuestion(){

        if(questionNumber < questionsList.size() - 1){

            questionNumber++;

            playAnim(question,0,0);
            playAnim(option1,0,1);
            playAnim(option2,0,2);
            playAnim(option3,0,3);

            qCount.setText(String.valueOf(questionNumber + 1) + "/" + String.valueOf(questionsList.size()));

            timer.setText(String.valueOf(10));

            startTimer();
        }else{
            // Go to score activity (there is no more question)
            Intent intent = new Intent(QuestionsActivity.this, ScoreActivity.class);
            intent.putExtra("SCORE", String.valueOf(score) + "/" + String.valueOf(questionsList.size()));
            startActivity(intent);
            QuestionsActivity.this.finish();
        }
    }

    private void playAnim(View view, final int value, int viewNum){
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100).setInterpolator(new DecelerateInterpolator())
        .setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onAnimationEnd(Animator animation) {
                if(value == 0){

                    switch (viewNum){
                        case 0:
                            ((TextView)view).setText(questionsList.get(questionNumber).getQuestion());
                            break;
                        case 1:
                            ((Button)view).setText(questionsList.get(questionNumber).getOptionA());
                            break;
                        case 2:
                            ((Button)view).setText(questionsList.get(questionNumber).getOptionB());
                            break;
                        case 3:
                            ((Button)view).setText(questionsList.get(questionNumber).getOptionC());
                            break;
                    }
                    playAnim(view,1,viewNum);
                }

                if(viewNum != 0){
                    ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFDB58")));
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        countDown.cancel();
    }
}
