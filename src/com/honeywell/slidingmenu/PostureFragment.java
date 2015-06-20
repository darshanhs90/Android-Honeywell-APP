package com.honeywell.slidingmenu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tech.mahindra.R;

public class PostureFragment extends Fragment  implements OnClickListener{
	Activity mActivity;
	ProgressDialog mDialog;
private final int RESPONSE_OK = 200;
	
	private final int IMAGE_PICKER_REQUEST = 1;
	
	private TextView picNameText;
	private EditText langCodeField;
	private EditText apiKeyFiled;
	
	private String apiKey;
	private String langCode;
	private String fileName;
	 File destination;
	private static final int REQUEST_IMAGE = 100;  
	public PostureFragment(Activity mActivity) {
		this.mActivity = mActivity;
	
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_find_people,
				container, false);
		mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		String name =   "newone"+new Date().toString();
        destination = new File(Environment.getExternalStorageDirectory(), name + ".jpg");

		picNameText = (TextView) rootView.findViewById(R.id.imageName);

		final Button pickButton = (Button) rootView.findViewById(R.id.picImagebutton);
		pickButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Starting image picker activity
				startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI), IMAGE_PICKER_REQUEST);
			}
		});
		final Button takeImagebutton = (Button) rootView.findViewById(R.id.takeImagebutton);
		takeImagebutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Starting image picker activity
				 Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destination));
	                startActivityForResult(intent, REQUEST_IMAGE);	
			
			}
		});
		
		
		final Button convertButton = (Button) rootView.findViewById(R.id.convert);
		convertButton.setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		apiKey = "FtBQuD7ENH";
		langCode = "en";
		
		// Checking are all fields set
		if (fileName != null && !apiKey.equals("") && !langCode.equals("")) {
			final ProgressDialog dialog = ProgressDialog.show( mActivity, "Loading ...", "Converting to text.", true, false);
			final Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					final OCRServiceAPI apiClient = new OCRServiceAPI(apiKey);
					apiClient.convertToText(langCode, fileName);
					
					// Doing UI related code in UI thread
					mActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							dialog.dismiss();
							
							// Showing response dialog
							final AlertDialog.Builder alert = new AlertDialog.Builder(mActivity);
							alert.setMessage(apiClient.getResponseText());
							alert.setPositiveButton(
								"OK",
								new DialogInterface.OnClickListener() {
									public void onClick( DialogInterface dialog, int id) {
									}
								});
							
							// Setting dialog title related from response code
							if (apiClient.getResponseCode() == RESPONSE_OK) {
								alert.setTitle("Success");
							} else {
								alert.setTitle("Faild");
							}
							
							alert.show();
						}
					});
				}
			});
			thread.start();
		} else {
			Toast.makeText(mActivity.getApplicationContext(), "All data are required.", Toast.LENGTH_SHORT).show();
		}
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == IMAGE_PICKER_REQUEST && resultCode == mActivity.RESULT_OK) {
			fileName = getRealPathFromURI(data.getData());
			//picNameText.setText("Selected: en" + getStringNameFromRealPath(fileName));
		}
		
		 if( requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK ){
	            try {
	                FileInputStream in = new FileInputStream(destination);
	                fileName=destination.getAbsolutePath();
	            } catch (FileNotFoundException e) {
	                e.printStackTrace();
	            }
		 }

	}

	/*
	 * Returns image real path.
	 */
	private String getRealPathFromURI(final Uri contentUri) {
		final String[] proj = { MediaStore.Images.Media.DATA };
		final Cursor cursor = mActivity.managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		
		return cursor.getString(column_index);
	}

	/*
	 * Cuts selected file name from real path to show in screen.
	 */
	private String getStringNameFromRealPath(final String bucketName) {
		return bucketName.lastIndexOf('/') > 0 ? bucketName.substring(bucketName.lastIndexOf('/') + 1) : bucketName;
	}
	
}
