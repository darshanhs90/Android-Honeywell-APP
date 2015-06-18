package com.tech.mahindra.slidingmenu;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tech.mahindra.R;

public class HomeFragment extends Fragment implements OnClickListener {

	TextView mScore, mDaily, mWeekly, mMonthly;
	ProgressBar mProgressBar;
	ProgressDialog mDialog;
	String url;
	int today;
	JSONParserTask mTask;
	Activity mActivity;

	public HomeFragment(Activity mActivity) {   
		this.mActivity = mActivity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_home, container,
				false);
		final TextView mTextView = (TextView) rootView.findViewById(R.id.title);
		final TextView mSlogan = (TextView) rootView.findViewById(R.id.slogan);
		final ImageView mImageView = (ImageView) rootView.findViewById(R.id.bg);

		mDialog = new ProgressDialog(mActivity);

		mProgressBar = (ProgressBar) rootView
				.findViewById(R.id.circularProgressbar);

		mScore = (TextView) rootView.findViewById(R.id.score);
		mDaily = (TextView) rootView.findViewById(R.id.daily);
		mWeekly = (TextView) rootView.findViewById(R.id.weekly);
		mMonthly = (TextView) rootView.findViewById(R.id.monthly);

		mDaily.setOnClickListener(this);
		mWeekly.setOnClickListener(this);
		mMonthly.setOnClickListener(this);

		Thread mThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				try {
					Thread.sleep(1);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mActivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						mImageView.setVisibility(View.GONE);  
						mTextView.setVisibility(View.GONE);
						mSlogan.setVisibility(View.GONE);
						//mTask.execute("http://172.20.10.14:4431/daily.ch");

					}
				});

			}
		});
		mThread.start();

		mTask = new JSONParserTask();

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

				Log.i("anup_check", "" + con.getResponseCode());

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
				mDaily.setSelected(true);
				mMonthly.setSelected(false);
				mWeekly.setSelected(false);
				double check = Double.parseDouble(data.get(0));
				int val = (int) Math.ceil(check);
				new Progress().execute(data.get(0));
				today = val;
				mScore.setText("Score: " + val);
			} else {
				mDialog.dismiss();
				Toast.makeText(mActivity, "Today's data not availiable",
						Toast.LENGTH_LONG).show();
			}

		}

	}

	public ArrayList<String> parseJsonNews(String result) throws JSONException {
		ArrayList<String> mScore = new ArrayList<String>();
		JSONObject mResponseobject = new JSONObject(result);
		mScore.add(mResponseobject.getString("score"));

		return mScore;

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.daily:
			new Progress().execute("" + today);
			mDaily.setSelected(true);
			mMonthly.setSelected(false);
			mWeekly.setSelected(false);
			mScore.setText("Score: " + today);
			break;
		case R.id.weekly:
			mDaily.setSelected(false);
			mMonthly.setSelected(false);
			mWeekly.setSelected(true);
			new Progress().execute("80");
			mScore.setText("Score: " + "80");
			break;
		case R.id.monthly:
			mDaily.setSelected(false);
			mMonthly.setSelected(true);
			mWeekly.setSelected(false);
			new Progress().execute("60");
			mScore.setText("Score: " + "60");
			break;

		default:
			break;
		}

	}

	public class Progress extends AsyncTask<String, String, Void> {

		@Override
		protected Void doInBackground(String... params) {

			if (mProgressBar.getProgress() > Float.parseFloat(params[0])) {
				while (mProgressBar.getProgress() > Float.parseFloat(params[0])) {
					publishProgress("1");
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} else {
				while (mProgressBar.getProgress() < Float.parseFloat(params[0])) {
					publishProgress("-1");
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			if (Integer.parseInt(values[0]) > 0) {
				mProgressBar.incrementProgressBy(-1);
			} else {
				mProgressBar.incrementProgressBy(1);
			}

		}
	}
}
