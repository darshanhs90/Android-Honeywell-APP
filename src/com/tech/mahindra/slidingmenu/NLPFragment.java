package com.tech.mahindra.slidingmenu;

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
	JSONParserTask mTask;
	int code;
	static int imgCounter;
	public NLPFragment(Activity mActivity) {
		this.mActivity = mActivity;
		mDialog = new ProgressDialog(mActivity);
		mTask = new JSONParserTask();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		 rootView = inflater.inflate(R.layout.fragment_nlp,
				container, false);
		 myView = (ImageView) rootView.findViewById(R.id.nextImage);
		 ibnCheck=(ImageButton) rootView.findViewById(R.id.iBnCheck);
		 ibnCheck.setOnClickListener(this);
		 code=1;
		imgCounter=0;
		arr=new int[3];
		arr[0]=R.drawable.fist;
		arr[1]=R.drawable.wavein;
		arr[2]=R.drawable.rest;

			//	mTask.execute("https://data.sparkfun.com/output/XGXApXxa3JUp4v5WMNQY.json");
		
	
		
		//mTask.execute("https://data.sparkfun.com/output/XGXApXxa3JUp4v5WMNQY.json");
		return rootView;
	}

	class JSONParserTask extends AsyncTask<String, Void, ArrayList<String>> {

		@Override
		protected void onPreExecute() {
			mDialog.setCancelable(false);
			mDialog.setMessage("Fetching data...");
			mDialog.show();

		}

		@Override
		protected ArrayList<String> doInBackground(String... params) {
			String jsonString = null;
			ArrayList<String> mData = null;
			try {

				URL obj = new URL(params[0]);
				HttpURLConnection con = (HttpURLConnection) obj
						.openConnection();

				con.setRequestMethod("GET");
				con.setReadTimeout(15 * 1000);
				con.connect();

				Log.i("check", "" + con.getResponseCode());

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(con.getInputStream(), "utf-8"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				jsonString = sb.toString();

				if (true) {
					try {
						mData = parseJsonNews(jsonString);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					}
				} else {
					mData = null;
				}

			} catch (Exception e) {
				e.printStackTrace();

			} finally {

			}
			return mData;
		}

		@Override
		protected void onPostExecute(final ArrayList<String> data) {
			if (data != null) {

				mDialog.dismiss();
				setlights(data.get(0));
			} else {
				mDialog.dismiss();
				Toast.makeText(mActivity, "Today's data not availiable",
						Toast.LENGTH_LONG).show();
			}

		}

	} 

	void setlights(String value) {
		char[] mcheck = value.toCharArray();
	
				  
		if(Integer.parseInt(value)==code){
			code++;
			Log.d("asd",""+code);
			if(code>3)
				code=1;  
			 myView.setImageResource(arr[code-1]);
			myView.postInvalidate();
			myView.refreshDrawableState();
			Toast.makeText(rootView.getContext(),"Perform the displayed Gesture",Toast.LENGTH_LONG).show();
		}
		else{
			Toast.makeText(rootView.getContext(),"MyO'stretch Incorrect",Toast.LENGTH_LONG).show();
		}
	}


	public ArrayList<String> parseJsonNews(String result) throws JSONException {
		ArrayList<String> mScore = new ArrayList<String>();

		JSONArray mResponseobject = new JSONArray(result);
		Log.d("asd",mResponseobject.get(0).toString());

		JSONObject j=(JSONObject) mResponseobject.get(0);

		Log.d("asd gesture",j.get("gesture")+"");
		String pressure=j.get("gesture").toString();

		String strNew=pressure;
		//String strNew=pressure6+""+pressure5+""+pressure4+""+pressure2+""+pressure3+""+pressure1;
		mScore.add(strNew);
		Log.d("asd",strNew);
		//pressure5+pressure4+pressure6+pressure3+pressure1+pressure2+"";

		return mScore;

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		mTask = new JSONParserTask();
		mTask.execute("https://data.sparkfun.com/output/XGXApXxa3JUp4v5WMNQY.json");
		
	}

}
