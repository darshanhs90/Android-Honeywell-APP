package com.honeywell.slidingmenu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;

import com.honeywell.adapter.ExpandableListAdapter;
import com.imageshack.client.ImageShackClient;
import com.imageshack.listener.ResponseListener;
import com.imageshack.model.AuthModel;
import com.imageshack.model.ImageShackModel;
import com.imageshack.model.UploadModel;
import com.tech.mahindra.R;

public class LibraryFragment extends Fragment {
	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;
	String passingUrl="";
	 String destination;
	 File destinationFile;
	private static final int REQUEST_IMAGE = 100;  
	Activity activity;
	ProgressDialog mProgressBar;
	private static final String LOADING = "Loading...";
	//private TextView terminal;
	private final String API_KEY = "68DPQRWX45e67d1d80b3c72b2ecf7f76b8fa42a4";

	// Values for email and password at the time of the login attempt.
	private String mEmail="1rn08ec033@gmail.com";
	private String mPassword="Hackathon123";
	//private TextView authTokenTextView;
	//private TextView apiKeyTextView;

	private String apiKey;
	private String authToken;

	private Button getDataButton;
	private Button browseButton;
	private WebView webView;
	private Uri targetUri;
	View rootView;
	//private Spinner apiSelector;

	private ImageShackClient isClient;
	public LibraryFragment(Activity mActivity) {

		this.activity = mActivity;
		mProgressBar = new ProgressDialog(mActivity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		 rootView = inflater.inflate(R.layout.fragment_community,
				container, false);
		browseButton=(Button) rootView.findViewById(R.id.browseButton);
		getDataButton=(Button) rootView.findViewById(R.id.getDataButton);
		browseButton.setOnClickListener(browseImagesListener);
		getDataButton.setOnClickListener(getDataButtonListener);
		ImageShackClient client = new ImageShackClient(API_KEY);
		String name =   "newone"+new Date().toString();
        destinationFile = new File(Environment.getExternalStorageDirectory(), name + ".jpg");

		
		Button takeImagebutton = (Button) rootView.findViewById(R.id.takepic);
		takeImagebutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Starting image picker activity
				 Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destinationFile));
	                startActivityForResult(intent, REQUEST_IMAGE);	
			
			}
		});
		
		client.login(mEmail, mPassword, new ResponseListener() {

			public void onResponse(ImageShackModel model) {

				AuthModel auth = (AuthModel) model;
				

				if (auth.isSuccess()) {

					authToken = auth.getAuthToken();

					apiKey = API_KEY;

					

					
					isClient = new ImageShackClient(apiKey, authToken);
				}
			}
			});
		
		
		
		return rootView;
	}
	
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		//super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == activity.RESULT_OK && requestCode != REQUEST_IMAGE ) {
			targetUri = data.getData();
			//targetUri.getPath()
			Log.d("asd",data.getData()+"///"+targetUri.getPath());
			destination= getRealPathFromURI(data.getData());
			//Toast.makeText(activity.getApplicationContext(),data.getData().toString() , Toast.LENGTH_SHORT).show();

		}
		if( requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK ){
            try {
                FileInputStream in = new FileInputStream(destinationFile);
                Log.d("asd",destinationFile.getAbsolutePath());
                destination=destinationFile.getAbsolutePath();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
	 }

	}

	

	public OnClickListener getDataButtonListener = new OnClickListener() {

		@SuppressLint("NewApi")
		@Override
		public void onClick(View v) {

			uploadImage();
			
			
				
		}

	};
	
	public OnClickListener browseImagesListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			Intent intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

			startActivityForResult(intent, 0);

		}

	};
	public static String getFinalURL(String url) throws IOException {
	    HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
	    con.setInstanceFollowRedirects(false);
	    con.connect();
	    con.getInputStream();

	    if (con.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM || con.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
	        String redirectUrl = con.getHeaderField("Location");
	        return getFinalURL(redirectUrl);
	    }
	    return url;
	}
	
	private void uploadImage() {

		//terminal.setText(LOADING);

		String[] tags = { "random", "tag", "random tag" };
		String title = "work station";
		String album = "Test album";
		Boolean commentsDisabled = null, isPublic = null;
		 //destination = getRealPathFromURI(targetUri);
		final ProgressDialog pdialog=new ProgressDialog(activity);
		pdialog.setCancelable(true);
		pdialog.setMessage("Loading ....");
		pdialog.show();
		try {
			isClient.uploadImage(destination, tags, album, title, commentsDisabled,
					isPublic, new ResponseListener() {

				@Override
				public void onResponse(ImageShackModel model) {

					UploadModel upload = (UploadModel) model;
					String str=upload.toString().substring(145);
					str=upload.toString().substring(156);
					String arr[]=str.split(",");
					Log.d("asd",arr[0]);
					passingUrl=arr[0];
					Log.d("passingurl",passingUrl);
					getDataButton.setEnabled(true);
					StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
					StrictMode.setThreadPolicy(policy);  
				
					String fetchedUrl="";
					try {
						Log.d("asd",passingUrl);
						fetchedUrl = getFinalURL("https://www.google.com/searchbyimage?site=search&sa=X&image_url=http://"+passingUrl);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Log.d("FetchedURL is:" ,fetchedUrl);
				    webView = (WebView) rootView.findViewById(R.id.webView1);
					webView.getSettings().setJavaScriptEnabled(true);
					pdialog.hide();
					webView.loadUrl(fetchedUrl);
					Log.d("asd",fetchedUrl);
				}

			});
		} catch (FileNotFoundException e) {
			//terminal.setText("File not found!");
			getDataButton.setEnabled(true);
		}

	}

	

	@SuppressWarnings("deprecation")
	public String getRealPathFromURI(Uri contentUri) {
		final String[] proj = { MediaStore.Images.Media.DATA };
		final Cursor cursor = activity.managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	private String formatObjectString(String model) {
		StringBuilder formatted = new StringBuilder();
		int offset = 0;

		for (int i = 0; i < model.length(); i++) {
			if (model.charAt(i) == '[') {
				if (model.charAt(i + 1) == ']') {
					formatted.append("[").append("]");
					i++;
				} else {
					offset++;
					formatted.append("[").append("\n");
					addPadding(formatted, offset);
				}
			} else if (model.charAt(i) == ']') {
				offset--;
				formatted.append("\n");
				addPadding(formatted, offset);
				formatted.append("]");
			} else if (model.charAt(i) == ',') {
				formatted.append(",").append("\n");
				addPadding(formatted, offset);
				i++;
			} else {
				formatted.append(model.charAt(i));
			}
		}

		return formatted.toString();
	}

	private void addPadding(StringBuilder sb, int offset) {
		for (int i = 0; i < offset; i++) {
			sb.append("\t");
		}
	}
}
