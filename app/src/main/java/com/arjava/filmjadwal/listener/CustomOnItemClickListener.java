package com.arjava.filmjadwal.listener;

import android.view.View;

/*
 * Created by arjava on 12/15/17.
 */

public class CustomOnItemClickListener implements View.OnClickListener {

    private int position;
    private OnItemClickCallback onItemClickCallback;

    @Override
    public void onClick(View view) {
        onItemClickCallback.onItemClicked(view,position);
    }

    public CustomOnItemClickListener(int position, OnItemClickCallback onItemClickCallback) {
        this.position = position;
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(View view, int position);
    }
}
