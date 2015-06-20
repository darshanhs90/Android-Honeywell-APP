package com.honeywell.slidingmenu;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.tech.mahindra.R;

public class HomeFragment extends Fragment implements OnClickListener {

	ImageButton ibnEnable,ibnDisable;
	String url;
	int today;
	Activity mActivity;
	Intent mServiceIntent;
	public HomeFragment(Activity mActivity) {   
		this.mActivity = mActivity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_home, container,
				false);
		ibnEnable=(ImageButton) rootView.findViewById(R.id.ibnEnable);
		ibnDisable=(ImageButton) rootView.findViewById(R.id.ibnDisable);
		ibnEnable.setOnClickListener(this);
		ibnDisable.setOnClickListener(this);
		mServiceIntent = new Intent(getActivity(), EarPhoneService.class);
		return rootView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		System.out.println(v.getId());
		switch (v.getId()) {
		case R.id.ibnEnable:
			Toast.makeText(getActivity().getApplicationContext(),"Smart Ear Phone Enabled",Toast.LENGTH_SHORT).show();
			((MainActivity)getActivity()).startRecording();
			mServiceIntent.setData(Uri.parse("1"));
			getActivity().startService(mServiceIntent);
			break;
		case R.id.ibnDisable:
			Toast.makeText(getActivity().getApplicationContext(),"Smart Ear Phone Disabled",Toast.LENGTH_SHORT).show();
			((MainActivity)getActivity()).recorder.stop();
			
			mServiceIntent.setData(Uri.parse("0"));
			getActivity().startService(mServiceIntent);
			break;
		default:
			break;
		}

	}
	
	
	
	
}
