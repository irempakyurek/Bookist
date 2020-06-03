package com.example.bookist.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bookist.R;
import com.example.bookist.view.fragment.MyCollectionFragment;

public class ScoreActivity extends AppCompatActivity {

    private TextView score;
    private Button done;
    private MyCollectionFragment myCollectionFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_score);

        this.myCollectionFragment = MyCollectionFragment.getViewInstance();

        score = findViewById(R.id.sa_score);
        done = findViewById(R.id.sa_done);

        String score_str = getIntent().getStringExtra("SCORE");
        score.setText(score_str);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myCollectionFragment = new MyCollectionFragment();

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_list, myCollectionFragment, "myCollection")
                        .commit();
               // Intent intent = new Intent(ScoreActivity.this, MainActivity.class);
                //startActivity(intent)
                ScoreActivity.this.finish();
            }
        });
    }
}
