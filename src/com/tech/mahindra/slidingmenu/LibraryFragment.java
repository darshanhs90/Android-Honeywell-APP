package com.tech.mahindra.slidingmenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.tech.mahindra.R;
import com.tech.mahindra.adapter.ExpandableListAdapter;

public class LibraryFragment extends Fragment {
	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;
	Activity activity;
	ProgressDialog mProgressBar;

	public LibraryFragment(Activity mActivity) {

		this.activity = mActivity;
		mProgressBar = new ProgressDialog(mActivity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_community,
				container, false);

		expListView = (ExpandableListView) rootView
				.findViewById(R.id.expandableListView);

		// preparing list data
		prepareListData();

		listAdapter = new ExpandableListAdapter(activity, listDataHeader,
				listDataChild);

		// setting list adapter
		expListView.setAdapter(listAdapter);

		expListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				mProgressBar.setCancelable(false);
				mProgressBar.setMessage("Fetching data...");
				mProgressBar.show();
				final Fragment fragment = new LibrarySeatFragment(activity,childPosition);

				Thread mThread = new Thread(new Runnable() {

					@Override
					public void run() {

						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								mProgressBar.dismiss();
								if (fragment != null) {
									FragmentManager fragmentManager = getFragmentManager();
									fragmentManager
											.beginTransaction()
											.replace(R.id.frame_container,
													fragment).commit();

									// update selected item and title, then
									// close the drawer

								} else {
									// error in creating fragment
									Log.e("MainActivity",
											"Error in creating fragment");
								}
							}
						});

					}
				});
				mThread.start();

				return false;
			}
		});

		return rootView;
	}

	private void prepareListData() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// Adding child data
		listDataHeader.add("First Floor");
		listDataHeader.add("Second Floor");
		listDataHeader.add("Third Floor");

		// Adding child data
		List<String> firstfloor = new ArrayList<String>();
		firstfloor.add("Quiet Study");
		firstfloor.add("Lobby");

		List<String> secondfloor = new ArrayList<String>();
		secondfloor.add("Quiet Study");
		secondfloor.add("Lobby");

		List<String> thirdfloor = new ArrayList<String>();
		thirdfloor.add("Quiet Study");
		thirdfloor.add("Lobby");

		listDataChild.put(listDataHeader.get(0), firstfloor); // Header, Child
																// data
		listDataChild.put(listDataHeader.get(1), secondfloor);
		listDataChild.put(listDataHeader.get(2), thirdfloor);
	}
}
