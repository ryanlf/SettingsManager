package edu.rosehulman.settingsmanager;

import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

public class EditProfile extends Activity {
	private Profile mProfile;
	private SeekBar mSystemVolumeControl;
	private AudioManager mAudioManager;
	private Button mSaveButton;
	private Button mCancelButton;
	private EditText mNameView;
	private int requestCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);
		Intent intent = this.getIntent();
		mProfile = (Profile) intent.getSerializableExtra(Profiles.KEY_EDIT_PROFILE);
		mAudioManager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
		requestCode = intent.getIntExtra(Profiles.KEY_REQUEST_CODE, 1);
		//Identify controls
		mSystemVolumeControl = (SeekBar) findViewById(R.id.system_volume_control);
		mNameView = (EditText) findViewById(R.id.editText1);
		
		if (requestCode == Profiles.REQUEST_CODE_EDIT_PROFILE) intializeEditProfile();
		else intializeNewProfile();
		
		//Initialize buttons
		mSaveButton = (Button) findViewById(R.id.save_button);
		mCancelButton = (Button) findViewById(R.id.cancel_button);
		mSaveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				save();
			}
		});
		mCancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cancel();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.edit_profile, menu);
		return true;
	}
	private void save(){
		mProfile.setVolume(mSystemVolumeControl.getProgress());
		mProfile.setName(mNameView.getText().toString());
		Intent result = new Intent();
		result.putExtra(Profiles.KEY_EDIT_PROFILE, mProfile);
		this.setResult(RESULT_OK,result);
		this.finish();
	}
	private void cancel() {
		this.setResult(RESULT_CANCELED);
		this.finish();
	}
	private void intializeNewProfile(){
		mSystemVolumeControl.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM));
		mSystemVolumeControl.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM));
		mNameView.setActivated(false);
	}
	private void intializeEditProfile(){
		mSystemVolumeControl.setProgress(mProfile.getSystemVolume());
		mSystemVolumeControl.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM));
		mNameView.setText(mProfile.toString());
		mNameView.setActivated(false);
	}

}
