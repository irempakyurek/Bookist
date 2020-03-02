package com.example.bookist.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import uk.co.senab.photoview.PhotoViewAttacher;

import android.os.Bundle;

import com.example.bookist.R;
import com.example.bookist.databinding.ActivityAboutBinding;

public class AboutActivity extends AppCompatActivity {

    ActivityAboutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_about);

        this.populateTextViewContent();
        PhotoViewAttacher photoView = new PhotoViewAttacher(this.binding.ivDiagram);
    }

    private void populateTextViewContent() {
        String[] contentArray = getResources().getStringArray(R.array.about_content_array);
        StringBuilder stringBuilder = new StringBuilder();
        for (String item : contentArray) {
            stringBuilder.append("\n" + item);
        }
        this.binding.tvContent.setText(stringBuilder.toString());
    }

}
