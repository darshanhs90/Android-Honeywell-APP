package com.honeywell.slidingmenu;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.honeywell.adapter.NavDrawerListAdapter;
import com.honeywell.model.NavDrawerItem;
import com.tech.mahindra.R;

public class MainActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title     
    private CharSequence mDrawerTitle;
 
    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    private static final int RECORDER_SAMPLERATE = 44100;
    private AudioManager audio;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    public static AudioRecord recorder = null;
    private Thread recordingThread = null;
    private boolean isRecording = false;
    int BufferElements2Rec = 1024;
    int BytesPerElement = 2;
    int today;
    boolean flag=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTitle = mDrawerTitle = getTitle();
        // load slide menu items   
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //if(audio.isMusicActive() && audio.getMode()!=AudioManager.MODE_IN_CALL)
        
        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons
                .getResourceId(0, -1)));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
                .getResourceId(1, -1)));

        // Communities, Will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons
                .getResourceId(2, -1)));
        // Pages
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
                .getResourceId(3, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons
                .getResourceId(4, -1)));
        
        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, // nav menu toggle icon
                R.string.app_name, // nav drawer open - description for
                // accessibility
                R.string.app_name // nav drawer close - description for
                // accessibility
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
//        if(flag==true)
//            startRecording();
//        else
//            recorder.stop();  
    }

    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
    ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
        case R.id.action_settings:
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /* *
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
        case 0:
            fragment = new HomeFragment(this);
            break;
        case 1:
            fragment = new PostureFragment(this);
            break;

        case 2:
            fragment = new LibraryFragment(this);
            break;
        case 4:
            fragment = new AboutFragment();
            break;
        case 3:
            fragment = new NLPFragment(this);
            break;
//      case 5:
//          fragment = new ListViewDemoFragment();
//          break;
//      case 4:
//          fragment = new ConnectWithFriends();
//          break;
//      case 6:
//          fragment = new BadgesFragment();
//          break;
        default:
            break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
            .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
    
    public void startRecording() {

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
//      for (int rate : mSampleRates) {
//          for (short audioFormat : new short[] {AudioFormat.ENCODING_DEFAULT,
//                  AudioFormat.ENCODING_PCM_16BIT }) {
//              for (short channelConfig : new short[] {
//                      AudioFormat.CHANNEL_IN_MONO}) {
                    try {
                        int rate = 8000;
                        int channelConfig = AudioFormat.CHANNEL_IN_MONO;
                        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
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
//              }
//          }
//      }
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
                {   audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
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
        public void onBackPressed() {
            // TODO Auto-generated method stub
            super.onBackPressed();
            //stopRecording();
        }
        @Override
        protected void onPause() {
            // TODO Auto-generated method stub
            super.onPause();
            //stopRecording();
        }
        @Override
        protected void onResume() {
            // TODO Auto-generated method stub
            super.onResume();
            //startRecording();
        }
        @Override
        protected void onDestroy() {
            // TODO Auto-generated method stub
            super.onDestroy();
            stopRecording();
        }
    
    
}
