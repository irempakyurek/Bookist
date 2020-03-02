package com.example.bookist.view.binding;

import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import androidx.databinding.BindingAdapter;

public class ImageBinding {

    @BindingAdapter({"android:src"})
    public static void loadImage(ImageView imageView, String url){
        Picasso.with(imageView.getContext())
                .load(url)
                .into(imageView);
    }

}
