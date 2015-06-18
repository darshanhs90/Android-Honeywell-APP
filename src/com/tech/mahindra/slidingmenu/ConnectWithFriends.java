package com.tech.mahindra.slidingmenu;

import java.util.ArrayList;
import java.util.List;

import android.app.ListFragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.tech.mahindra.R;

public class ConnectWithFriends extends ListFragment {
private List<ListViewItem> mItems;        // ListView items list

@Override
public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	// initialize the items list
	mItems = new ArrayList<ListViewItem>();
	Resources resources = getResources();

	mItems.add(new ListViewItem(resources.getDrawable(R.drawable.cartman), getString(R.string.cartman), getString(R.string.cart_scr)));
	mItems.add(new ListViewItem(resources.getDrawable(R.drawable.spock), getString(R.string.spock), getString(R.string.spock_scr)));
	mItems.add(new ListViewItem(resources.getDrawable(R.drawable.vader), getString(R.string.vader), getString(R.string.vader_scr)));

	// initialize and set the list adapter
	setListAdapter(new ListViewDemoAdapter(getActivity(), mItems));
}

@Override
public void onViewCreated(View view, Bundle savedInstanceState) {
	super.onViewCreated(view, savedInstanceState);
	// remove the dividers from the ListView of the ListFragment
	getListView().setDivider(null);
}

@Override
public void onListItemClick(ListView l, View v, int position, long id) {
	// retrieve theListView item
	ListViewItem item = mItems.get(position);

	Toast.makeText(getActivity(), item.title+" is invited for the Posture challenge", Toast.LENGTH_SHORT).show();
}
}