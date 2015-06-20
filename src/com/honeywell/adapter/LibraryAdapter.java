package com.honeywell.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class LibraryAdapter extends BaseAdapter {
	private Context mContext;
	int count;
	int mRand=0;

	public LibraryAdapter(Context c, int i,int rand) {
		mContext = c;
		this.count = i;
		mRand = rand;
	}

	public Integer[] mThumbIds = {

	};

	// Constructor
	public LibraryAdapter(Context c) {
		mContext = c;
	}

	@Override
	public int getCount() {
		return count;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ImageView imageView = new ImageView(mContext);
		if(position>19&&position<30)
		{
			imageView.setBackgroundColor(Color.TRANSPARENT);
		}
		else if(position%mRand == 0)
		{
			imageView.setBackgroundColor(Color.BLACK);
		}
		else
		{
			imageView.setBackgroundColor(Color.GREEN);
		}
		
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView.setLayoutParams(new GridView.LayoutParams(70, 70));
		return imageView;
	}

}
