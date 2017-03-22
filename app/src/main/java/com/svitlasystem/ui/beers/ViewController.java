package com.svitlasystem.ui.beers;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.svitlasystem.R;


public class ViewController extends RecyclerView.ViewHolder {

    private TextView mIdTextView;
    private TextView mNameTextView;
    private TextView mTypeTextView;

    public ViewController(View itemView) {
        super(itemView);
        mIdTextView = (TextView) itemView.findViewById(R.id.id);
        mNameTextView = (TextView) itemView.findViewById(R.id.name);
        mTypeTextView = (TextView) itemView.findViewById(R.id.type);
    }

    public void bindModel(Cursor cursor) {
        mIdTextView.setText(String.valueOf(cursor.getInt(0)));
        mNameTextView.setText(cursor.getString(1));
        mTypeTextView.setText(cursor.getString(2));
    }
}
