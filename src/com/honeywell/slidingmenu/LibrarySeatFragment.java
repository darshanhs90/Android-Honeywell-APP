package com.honeywell.slidingmenu;

import java.util.Random;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.honeywell.adapter.LibraryAdapter;
import com.tech.mahindra.R;

public class LibrarySeatFragment extends Fragment {
	Context context;
	Random mRandom;
	int val;

	public LibrarySeatFragment(Context mContext, int pos) {
		this.context = mContext;
		val = pos;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.library_seat, container,
				false);
		TextView mTextView = (TextView) rootView.findViewById(R.id.label);
		if (val % 2 == 0) {
			mTextView.setText("Quiet Study");
		} else {
			mTextView.setText("Lobby");

		}
		mRandom = new Random();
		int randomNum = mRandom.nextInt((5 - 2) + 1) + 2;
		LibraryAdapter mAdapter = new LibraryAdapter(context, 50, randomNum);

		GridView grid = (GridView) rootView.findViewById(R.id.gridview);
		grid.setAdapter(mAdapter);

		return rootView;
	}
}
