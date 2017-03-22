package com.svitlasystem.ui.beers;


import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.svitlasystem.R;

public class BeerAdapter extends RecyclerView.Adapter<ViewController> {

    private Cursor mCursor;

    public void setCursor(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public ViewController onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewController(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.beer_row,
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(ViewController holder, int position) {
        mCursor.moveToPosition(position);
        holder.bindModel(mCursor);
    }

    @Override
    public int getItemCount() {
        return mCursor != null ? mCursor.getCount() : 0;
    }
}
