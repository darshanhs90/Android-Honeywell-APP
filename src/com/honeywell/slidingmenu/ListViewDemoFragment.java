//package com.honeywell.slidingmenu;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.app.ListFragment;
//import android.content.res.Resources;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import com.tech.mahindra.R;
//
//public class ListViewDemoFragment extends ListFragment {
//private List<ListViewItem> mItems;        // ListView items list
//
//@Override
//public void onCreate(Bundle savedInstanceState) {
//	super.onCreate(savedInstanceState);
//
//	// initialize the items list
//	mItems = new ArrayList<ListViewItem>();
//	Resources resources = getResources();
//
//	mItems.add(new ListViewItem(resources.getDrawable(R.drawable.flex), getString(R.string.flex), getString(R.string.flex_str)));
//	mItems.add(new ListViewItem(resources.getDrawable(R.drawable.surge), getString(R.string.surge), getString(R.string.surge_str)));
//	mItems.add(new ListViewItem(resources.getDrawable(R.drawable.pebble), getString(R.string.pebble), getString(R.string.pebble_str)));
//	mItems.add(new ListViewItem(resources.getDrawable(R.drawable.myo), getString(R.string.myo), getString(R.string.myo_str)));
//
//	// initialize and set the list adapter
//	setListAdapter(new ListViewDemoAdapter(getActivity(), mItems));
//}
//
//@Override
//public void onViewCreated(View view, Bundle savedInstanceState) {
//	super.onViewCreated(view, savedInstanceState);
//	// remove the dividers from the ListView of the ListFragment
//	getListView().setDivider(null);
//}
//
//@Override
//public void onListItemClick(ListView l, View v, int position, long id) {
//	// retrieve theListView item
//	ListViewItem item = mItems.get(position);
//
//	Toast.makeText(getActivity(), item.title+" Added", Toast.LENGTH_SHORT).show();
//}
//}