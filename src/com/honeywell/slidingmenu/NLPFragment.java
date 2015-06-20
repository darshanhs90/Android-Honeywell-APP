package com.honeywell.slidingmenu;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.tech.mahindra.R;

public class NLPFragment extends Fragment implements OnClickListener{
	ImageView mLight1, mLight2, mLight3, mLight4, mLight5, mlight6 , myView;
	Activity mActivity;
	ImageButton ibnCheck;
	View rootView;
	Button b;
	int arr[];
	ProgressDialog mDialog;
	int code;
	static int imgCounter;
	public NLPFragment(Activity mActivity) {
		this.mActivity = mActivity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		 rootView = inflater.inflate(R.layout.fragment_nlp,
				container, false);
		
		return rootView;
	}

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
