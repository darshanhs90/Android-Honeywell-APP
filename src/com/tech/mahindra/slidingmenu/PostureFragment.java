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
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.tech.mahindra.R;

public class PostureFragment extends Fragment {
	ImageView mLight1, mLight2, mLight3, mLight4, mLight5, mlight6;
	Activity mActivity;
	ProgressDialog mDialog;
	JSONParserTask mTask;

	public PostureFragment(Activity mActivity) {
		this.mActivity = mActivity;
		mDialog = new ProgressDialog(mActivity);
		mTask = new JSONParserTask();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_find_people,
				container, false);
		ImageView myView = (ImageView) rootView.findViewById(R.id.chair);

		mLight1 = (ImageView) rootView.findViewById(R.id.light1);
		mLight2 = (ImageView) rootView.findViewById(R.id.light2);
		mLight3 = (ImageView) rootView.findViewById(R.id.light3);
		mLight4 = (ImageView) rootView.findViewById(R.id.light4);
		mLight5 = (ImageView) rootView.findViewById(R.id.light5);
		mlight6 = (ImageView) rootView.findViewById(R.id.light6);

		mTask.execute("https://data.sparkfun.com/output/jqE4oYRz1ACyKKgApvdp.json");
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
		ArrayList<Integer> mValues = new ArrayList<Integer>();
		for (int i = 0; i < mcheck.length; i++) {
			mValues.add(Integer.parseInt(mcheck[i] + ""));

			Log.i("check",""+ mValues.get(i));
			if (mValues.get(i) == 0) {
				switch (i) {
				case 0:
					mLight1.setBackgroundColor(Color.RED);
					break;
				case 1:
					mLight2.setBackgroundColor(Color.RED);
					break;
				case 2:
					mLight5.setBackgroundColor(Color.RED);
					break;
				case 3:
					mlight6.setBackgroundColor(Color.RED);
					break;
				case 4:
					mLight4.setBackgroundColor(Color.RED);
					break;
				case 5:
					mLight3.setBackgroundColor(Color.RED);
					break;

				default:
					break;
				}
			}
		}

	}

	public ArrayList<String> parseJsonNews(String result) throws JSONException {
		ArrayList<String> mScore = new ArrayList<String>();
		
		JSONArray mResponseobject = new JSONArray(result);
		Log.d("asd",mResponseobject.get(0).toString());
		
		JSONObject j=(JSONObject) mResponseobject.get(0);
		Log.d("asd pressure1",j.get("pressure1")+"");
		Log.d("asd pressure2",j.get("pressure2")+"");
		Log.d("asd pressure3",j.get("pressure3")+"");
		Log.d("asd pressure4",j.get("pressure4")+"");
		Log.d("asd pressure5",j.get("pressure5")+"");
		Log.d("asd pressure6",j.get("pressure6")+"");
		int pressure1=0,pressure2=0,pressure3=0,pressure4=0,pressure5=0,pressure6=0;
		String pressure=j.get("pressure1").toString();
		if(pressure.contentEquals("0001")){
			pressure1=1;
		}
		pressure=j.get("pressure2").toString();
		if(pressure.contentEquals("0001")){
			pressure2=1;
		}
		pressure=j.get("pressure3").toString();
		if(pressure.contentEquals("0001")){
			pressure3=1;
		}
		pressure=j.get("pressure4").toString();
		if(pressure.contentEquals("0001")){
			pressure4=1;
		}
		pressure=j.get("pressure5").toString();
		if(pressure.contentEquals("0001")){
			pressure5=1;
		}
		pressure=j.get("pressure6").toString();
		if(pressure.contentEquals("0001")){
			pressure6=1;
		}
		String strNew=pressure6+""+pressure5+""+pressure4+""+pressure2+""+pressure3+""+pressure1;
		//String strNew=pressure6+""+pressure5+""+pressure4+""+pressure2+""+pressure3+""+pressure1;
		mScore.add(strNew);
		Log.d("asd",strNew);
		//pressure5+pressure4+pressure6+pressure3+pressure1+pressure2+"";
		
		return mScore;
      
	}

}
