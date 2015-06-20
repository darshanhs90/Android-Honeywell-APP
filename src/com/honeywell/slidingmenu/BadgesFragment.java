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
//public class BadgesFragment extends ListFragment {
//private List<ListViewItem> mItems;        // ListView items list
//
//@Override
//public void onCreate(Bundle savedInstanceState) {
//	super.onCreate(savedInstanceState);
//
//	// initialize the items list
//	mItems = new ArrayList<ListViewItem>();
//	Resources resources = getResources();
//	mItems.add(new ListViewItem(resources.getDrawable(R.drawable.amateur), getString(R.string.amateur), getString(R.string.amateur_scr)));
//	
//	mItems.add(new ListViewItem(resources.getDrawable(R.drawable.onestar), getString(R.string.onestar), getString(R.string.onestar_scr)));
//	
//	mItems.add(new ListViewItem(resources.getDrawable(R.drawable.twostar), getString(R.string.twostar), getString(R.string.twostar_scr)));
//	
//	mItems.add(new ListViewItem(resources.getDrawable(R.drawable.threestar), getString(R.string.threestar), getString(R.string.threestar_scr)));
//	
//	mItems.add(new ListViewItem(resources.getDrawable(R.drawable.fourstar), getString(R.string.fourstar), getString(R.string.fourstar_scr)));
//	
//	mItems.add(new ListViewItem(resources.getDrawable(R.drawable.bestposture), getString(R.string.bestposture), getString(R.string.bestposture_scr)));
//	
//	mItems.add(new ListViewItem(resources.getDrawable(R.drawable.fitnessfreak), getString(R.string.fitnessfreak), getString(R.string.fitnessfreak_scr)));
//	
//	
//	mItems.add(new ListViewItem(resources.getDrawable(R.drawable.constantperformer), getString(R.string.constantperformer), getString(R.string.constantperformer_scr)));
//	
//	mItems.add(new ListViewItem(resources.getDrawable(R.drawable.socialprofile), getString(R.string.socialprofile), getString(R.string.socialnetwork_scr)));
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
//	Toast.makeText(getActivity(), item.title, Toast.LENGTH_SHORT).show();
//}
//}