package com.example.bookist.view.binding;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LayoutManagerBinding {

    @BindingAdapter({"bind:layout_manager"})
    public static void setLayoutManager(RecyclerView recyclerView, String layout){
        if ("linear".equalsIgnoreCase(layout)){
            setLayoutManager(recyclerView, layout, 0);
        }
    }

    @BindingAdapter({"bind:layout_manager", "bind:columns"})
    public static void setLayoutManager(RecyclerView recyclerView, String layout, int columns){
        if ("linear".equalsIgnoreCase(layout)){
            recyclerView.setLayoutManager(
                    new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        } else if ("grid".equalsIgnoreCase(layout)){
            recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), columns));
        }
    }

}
