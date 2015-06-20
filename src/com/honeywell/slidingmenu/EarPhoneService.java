package com.honeywell.slidingmenu;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;
import android.os.IBinder;
import android.util.Log;

public class EarPhoneService extends Service {

	
	private static final int RECORDER_SAMPLERATE = 44100;
	private AudioManager audio;
	private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
	private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
	private AudioRecord recorder = null;
	private Thread recordingThread = null;
	private boolean isRecording = false;
	int BufferElements2Rec = 1024;
	int BytesPerElement = 2;



	protected void onHandleIntent(Intent workIntent) {
		// Gets data from the incoming Intent
		audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		String dataString = workIntent.getDataString();
		if(dataString.contentEquals("1"))
		{

			//Toast.makeText(getApplicationContext(), "Press the home button to run the appllication in Background",Toast.LENGTH_LONG).show();
			
			//if(audio.isMusicActive() && audio.getMode()!=AudioManager.MODE_IN_CALL)
			startRecording();

		}
		else{
			recorder.stop();
		}
		// Do work here, based on the contents of dataString
	}

	private void startRecording() {

		recorder = findAudioRecord();

		recorder.startRecording(); 
		isRecording = true;
		recordingThread = new Thread(new Runnable() {
			public void run() {
				try {
					writeAudioDataToFile();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, "AudioRecorder Thread");
		recordingThread.start();
	}
	//8000, 11025, 22050, 44100
	private static int[] mSampleRates = new int[] { 8000 };

	public AudioRecord findAudioRecord() { 
		for (int rate : mSampleRates) {
			for (short audioFormat : new short[] {
					AudioFormat.ENCODING_PCM_8BIT,
					AudioFormat.ENCODING_PCM_16BIT }) {
				for (short channelConfig : new short[] {
						AudioFormat.CHANNEL_IN_MONO,
						AudioFormat.CHANNEL_IN_STEREO }) {
					try {

						int bufferSize = AudioRecord.getMinBufferSize(rate,
								channelConfig, audioFormat);

						if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
							// check if we can instantiate and have a success
							AudioRecord recorder = new AudioRecord(
									AudioSource.DEFAULT, rate, channelConfig,
									audioFormat, bufferSize);

							if (recorder.getState() == AudioRecord.STATE_INITIALIZED)
								return recorder;
						}
					} catch (Exception e) {

					}
				}
			}
		}
		return null;
	}

	private void writeAudioDataToFile() throws Exception {
		// Write the output audio in byte

		short sData[] = new short[BufferElements2Rec];
		int prior_val=4000;
		while (isRecording) {

			recorder.read(sData, 0, BufferElements2Rec);

			int max = calculate(41000, sData);//41000
			//if max is in the range of prior value
			//less than 1400,increase once
			//1400-1800
			//1800-2200
			//>2200
			//if new_val falls in same range as prior value
			//no change
			//Thread.sleep(2000,0);
			int MAX_VAL=audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			Log.d(Integer.valueOf(MAX_VAL*3/4).toString()+"prior",Integer.valueOf(MAX_VAL*3/4).toString());
			Log.i("asd","|"+max+"|"+prior_val);
			if((max<1800 && prior_val>max)||((audio.getStreamVolume(AudioManager.STREAM_MUSIC)>MAX_VAL/2)&& (audio.getStreamVolume(AudioManager.STREAM_MUSIC)<(3*MAX_VAL/4)))){
				//decrease volume by one 
				Log.i("Less than 1800,lower volume", "" + max);
				Log.i((Integer.valueOf(audio.getStreamVolume(AudioManager.STREAM_MUSIC))).toString(),(Integer.valueOf(audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC))).toString());
				if(audio.getStreamVolume(AudioManager.STREAM_MUSIC)>MAX_VAL/2)
				{
					while(audio.getStreamVolume(AudioManager.STREAM_MUSIC)>(MAX_VAL/4)){
						audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
						//Log.d(max+"less1800",audio.getStreamVolume(AudioManager.STREAM_MUSIC)+"|||");
					}
					Log.d(max+"less1800if",audio.getStreamVolume(AudioManager.STREAM_MUSIC)+"|||");

				}
				else{
					//Log.d("dddd","dddd");
					audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
							AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
					Log.d(max+"less1800else",audio.getStreamVolume(AudioManager.STREAM_MUSIC)+"|||");

				}

				Thread.sleep(1500);
				prior_val=0;
			}
			else if((max>1800 && max<2200 && (prior_val<1800 || prior_val>2200))||(audio.getStreamVolume(AudioManager.STREAM_MUSIC)<MAX_VAL/2 && audio.getStreamVolume(AudioManager.STREAM_MUSIC)>(3*MAX_VAL/4))){
				//increase volume by one
				Log.i("More than 1800 and less than 2200,raise volume", "" + max);
				Log.i((Integer.valueOf(audio.getStreamVolume(AudioManager.STREAM_MUSIC))).toString(),(Integer.valueOf(audio.getStreamVolume(AudioManager.STREAM_MUSIC))).toString());
				if(audio.getStreamVolume(AudioManager.STREAM_MUSIC)<MAX_VAL/2 && audio.getStreamVolume(AudioManager.STREAM_MUSIC)>(3*MAX_VAL/4))
				{	audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
						10, AudioManager.FLAG_SHOW_UI);
				Log.d(max+"less2200if",audio.getStreamVolume(AudioManager.STREAM_MUSIC)+"|||");

				}else
				{
					audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
							AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
					Log.d(max+"less2200else",audio.getStreamVolume(AudioManager.STREAM_MUSIC)+"|||");

				}
				Thread.sleep(1500);
				prior_val=2200;
			}
			else if((prior_val<2200 && max>2200)||((audio.getStreamVolume(AudioManager.STREAM_MUSIC)<(3*MAX_VAL/4))&&(audio.getStreamVolume(AudioManager.STREAM_MUSIC)>(MAX_VAL/2)))){ 
				//increase volume by two
				Log.i("More than 2200,raise volume", "" + max);
				Log.i((Integer.valueOf(audio.getStreamVolume(AudioManager.STREAM_MUSIC))).toString(),(Integer.valueOf(audio.getStreamVolume(AudioManager.STREAM_MUSIC))).toString());
				if(audio.getStreamVolume(AudioManager.STREAM_MUSIC)<(3*MAX_VAL/4))
				{
					while(audio.getStreamVolume(AudioManager.STREAM_MUSIC)<(3*MAX_VAL/4)){
						audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
								AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
					}
					Log.d(max+"less2500if",audio.getStreamVolume(AudioManager.STREAM_MUSIC)+"|||");
				}
				else{
					audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
							AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
					Log.d(max+"less2500else",audio.getStreamVolume(AudioManager.STREAM_MUSIC)+"|||");

				}
				Thread.sleep(1500);
				prior_val=2300;
			}
			else if(max>3500){
				while(audio.getStreamVolume(AudioManager.STREAM_MUSIC)>(MAX_VAL/5)){
					{	
						audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
								AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
					}

				}


				/*if (max > 2000) {
				Log.i("Check", "" + max);
				//Log.d("Val","hj");
				audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
						AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
				Thread.sleep(3000);
			}
			else{
				Log.i("Lower", "" + max);
				Thread.sleep(3000);
				audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
						AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
			}*/
			}}}
	private void stopRecording() {
		// stops the recording activity
		if (null != recorder) {
			isRecording = false;
			recorder.stop();
			recorder.release();
			recorder = null;
			recordingThread = null;
		}
	}

	public static int calculate(int sampleRate, short[] audioData) {

		int numSamples = audioData.length;
		int numCrossing = 0;
		for (int p = 0; p < numSamples - 1; p++) {
			if ((audioData[p] > 0 && audioData[p + 1] <= 0)
					|| (audioData[p] < 0 && audioData[p + 1] >= 0)) {
				numCrossing++;
			}
		}

		float numSecondsRecorded = (float) numSamples / (float) sampleRate;
		float numCycles = numCrossing / 2;
		float frequency = numCycles / numSecondsRecorded;
		return (int) frequency;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}