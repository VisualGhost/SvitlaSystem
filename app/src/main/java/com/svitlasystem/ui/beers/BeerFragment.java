package com.svitlasystem.ui.beers;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.svitlasystem.R;
import com.svitlasystem.content_provider.ProviderContract;

public class BeerFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    private BeerAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.beers_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new BeerAdapter();
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), ProviderContract.BEER_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor cursor) {
        mAdapter.setCursor(cursor);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        mAdapter.setCursor(null);
    }

}
