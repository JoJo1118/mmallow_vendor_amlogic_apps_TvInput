package com.droidlogic.tvinput.settings;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.util.Log;
import android.media.tv.TvInputInfo;

import com.droidlogic.app.tv.DroidLogicTvUtils;
import com.droidlogic.tvclient.TvClient;
import com.droidlogic.tvinput.R;
import android.amlogic.Tv;

public class TvSettingsActivity extends Activity implements OnClickListener, OnFocusChangeListener {
    private static final String TAG = "MainActivity";

    private Tv tv = TvClient.getTvInstance();

    private ContentFragment fragmentImage;
    private ContentFragment fragmentSound;
    private ContentFragment fragmentChannel;
    private ContentFragment fragmentSettings;
    public RelativeLayout mOptionLayout = null;

    private ContentFragment currentFragment;
    private SettingsManager mSettingsManager;
    private OptionUiManager mOptionUiManager;

    private ImageButton tabPicture;
    private ImageButton tabSound;
    private ImageButton tabChannel;
    private ImageButton tabSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_main);

        mSettingsManager = new SettingsManager(this);
        mSettingsManager.setInputId(getIntent().getStringExtra(TvInputInfo.EXTRA_INPUT_ID));

        tabPicture= (ImageButton) findViewById(R.id.button_picture);
        tabSound= (ImageButton) findViewById(R.id.button_sound);
        tabChannel= (ImageButton) findViewById(R.id.button_channel);
        tabSettings= (ImageButton) findViewById(R.id.button_settings);
        tabPicture.setOnClickListener(this);
        tabPicture.setOnFocusChangeListener(this);
        tabSound.setOnClickListener(this);
        tabSound.setOnFocusChangeListener(this);
        tabChannel.setOnClickListener(this);
        tabChannel.setOnFocusChangeListener(this);
        tabSettings.setOnClickListener(this);
        tabSettings.setOnFocusChangeListener(this);

        if (savedInstanceState == null)
            setDefaultFragment();

        mOptionUiManager = new OptionUiManager(this);
    }

    private void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        currentFragment = new ContentFragment(R.xml.list_picture, tabPicture);
        transaction.replace(R.id.settings_list, currentFragment);
        transaction.commit();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "@@@@@@@@@ focus=" + getCurrentFocus());
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
       /* FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        switch (v.getId()) {
            case R.id.button_picture:
                if (fragmentImage == null) {
                    fragmentImage = new ContentFragment(R.xml.list_picture);
                }
                transaction.replace(R.id.settings_list, fragmentImage);
                break;
            case R.id.button_sound:
                if (fragmentSound == null) {
                    fragmentSound = new ContentFragment(R.xml.list_sound);
                }
                transaction.replace(R.id.settings_list, fragmentSound);
                break;
            case R.id.button_channel:
                if (fragmentChannel== null) {
                    fragmentChannel= new ContentFragment(R.xml.list_channel);
                }
                transaction.replace(R.id.settings_list, fragmentChannel);
                break;
            case R.id.button_settings:
                if (fragmentSettings== null) {
                    fragmentSettings= new ContentFragment(R.xml.list_settings);
                }
                transaction.replace(R.id.settings_list, fragmentSettings);
                break;
        }
        // transaction.addToBackStack();
        transaction.commit();*/
    }

    @Override
    public void onFocusChange (View v, boolean hasFocus) {
        if (hasFocus) {
            RelativeLayout main_view = (RelativeLayout)findViewById(R.id.main);
            if (mOptionLayout != null)
                main_view.removeView(mOptionLayout);

            FragmentManager fm = getFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();

            switch (v.getId()) {
            case R.id.button_picture:
                currentFragment = new ContentFragment(R.xml.list_picture, v);
                transaction.replace(R.id.settings_list, currentFragment);
                break;
            case R.id.button_sound:
                currentFragment = new ContentFragment(R.xml.list_sound, v);
                transaction.replace(R.id.settings_list, currentFragment);
                break;
            case R.id.button_channel:
                currentFragment = new ContentFragment(R.xml.list_channel, v);
                transaction.replace(R.id.settings_list, currentFragment);
                break;
            case R.id.button_settings:
                currentFragment = new ContentFragment(R.xml.list_settings, v);
                transaction.replace(R.id.settings_list, currentFragment);
                break;
            }
            // transaction.addToBackStack();
            transaction.commit();
        }
    }

    public ContentFragment getCurrentFragment () {
        return currentFragment;
    }

    public SettingsManager getSettingsManager () {
        return mSettingsManager;
    }

    public OptionUiManager getOptionUiManager () {
        return mOptionUiManager;
    }

    public void finish()
    {
        // TODO Auto-generated method stub
        setResult(mOptionUiManager.getFinishResult());
        super.finish();
    }

    public void onStop()
    {
        // TODO Auto-generated method stub
        if (mOptionUiManager.isSearching()) {
            tv.DtvStopScan();
        }
        super.onStop();
    }
}